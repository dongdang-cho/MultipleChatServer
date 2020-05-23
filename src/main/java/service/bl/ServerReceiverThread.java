package main.java.service.bl;

import com.google.gson.Gson;
import service.dto.Message;
import service.dto.UserInfoDTO;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ServerReceiverThread extends Thread{
    private Socket sock;
    private HashMap<String, ServerReceiverThread> clientMap;
    private BufferedWriter bw;
    private BufferedReader br;
    private UserInfoDTO userInfo;

    public ServerReceiverThread(Socket sock, HashMap<String, ServerReceiverThread> clientMap,UserInfoDTO userInfo) {
        this.sock = sock;
        this.userInfo = userInfo;
        this.clientMap = clientMap;

        try {
            bw = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
            br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(String message) throws IOException {
        if(message==null) return;
        bw.write(message+"\n");
        bw.flush();
    }
    public void writeInfo(String message) throws IOException {
        if(message==null) return;
        Message msg = new Message();
        msg.setType("system");
        msg.setSender("");
        msg.setStatus("200");
        msg.setReceiver(null);
        msg.setMessage(message);
        bw.write(new Gson().toJson(msg)+"\n");
        bw.flush();
    }
    public void close() {
        try {
            br.close();
            bw.close();
            sock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String read() throws IOException {
        return br.readLine();
    }


    @Override
    public void run() {
        super.run();
        String message = null;

        try {
            while (true) {
                message = read();
                if(message==null) {
                    System.out.println("["+userInfo.getName()+"] 님이 퇴장하였습니다.");
                    if(sock != null) {
                        close();
                        clientMap.remove(userInfo.getId());
                        String m = "["+userInfo.getName()+"] 님이 퇴장하였습니다.";
                        Iterator<Map.Entry<String, ServerReceiverThread>> iterator = clientMap.entrySet().iterator();

                        while(iterator.hasNext()) {
                            Map.Entry<String,ServerReceiverThread> entry = iterator.next();
                            try {
                                entry.getValue().writeInfo(m);
                            } catch (Exception e) {
                                entry.getValue().close();
                                iterator.remove();
                            }
                        }
                    }
                    break;
                }

                System.out.println("수신 : " + message);
                Message msg = new Gson().fromJson(message,Message.class);

                for (Map.Entry<String, ServerReceiverThread> entry : clientMap.entrySet()) {
                    try {
                        entry.getValue().write(message);
                    } catch (Exception e) {
                        entry.getValue().close();
                        clientMap.remove(entry.getKey());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(sock != null) {
                close();
                clientMap.remove(userInfo.getId());
            }
        }

    }
}
