package service.bl;

import com.google.gson.Gson;
import service.dto.UserInfoDTO;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class MultiChatServerService {
    private HashMap<String, ServerReceiverThread> clientMap;
    public MultiChatServerService() {
        clientMap = new HashMap<String, ServerReceiverThread>();

        try {
            ServerSocket servSock = new ServerSocket(6469);
            //Lan에서 사용 가능
            //wan사용시 공유기에서 포트포워딩 설정할 것.
            while(true) {
                System.out.println("클라이언트의 접속을 기다리고 있습니다...");
                Socket socket = servSock.accept(); //접속 대기 및 수락

                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String temp = br.readLine();
                UserInfoDTO user = readUserInfo(temp);
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                String message = null;
                if(clientMap.get(user.getName())==null)  {
                    message = "{\"status\":200}"+"\n";
                    bw.write(message);
                    bw.flush();
                }
                else {
                    message ="{\"status\":404}"+"\n";
                    bw.write(message);
                    bw.flush();
                    continue;
                }

                ServerReceiverThread receiverThread = new ServerReceiverThread(socket,clientMap,user);
                receiverThread.start();
                clientMap.put(user.getName(),receiverThread);
                System.out.println("["+user.getName()+"]"+" 님이 입장하셨습니다.");
                System.out.println("현재 접속자는 "+clientMap.keySet());
                for (Map.Entry<String, ServerReceiverThread> entry : clientMap.entrySet()) {
                    try {
                        entry.getValue().writeInfo("["+user.getName()+"]"+" 님이 입장하셨습니다.");
                        bw.flush();
                        entry.getValue().writeInfo("현재 접속자는 "+clientMap.keySet());
                        bw.flush();
                    } catch (Exception e) {
                        entry.getValue().close();
                        clientMap.remove(entry.getKey());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private UserInfoDTO readUserInfo(String userInfoJson) {
        return new Gson().fromJson(userInfoJson,UserInfoDTO.class);
    }

}


//shift 두번
//ctrl+n 클래스 찾기

//alt+Shift+F10 실행
//ctrl+Shift+F12 코드만 보기

//ALT+ENTER 언어 인젝션
//alt+insert 소스 생성

//ctrl+y 라인 삭제
