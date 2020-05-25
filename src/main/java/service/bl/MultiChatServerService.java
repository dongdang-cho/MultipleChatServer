package service.bl;

import com.google.gson.Gson;
import service.dao.MultiChatDAO;
import service.bl.ServerReceiverThread;
import service.dto.Message;
import service.dto.UserInfoDTO;
import util.MetaDataLoader;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiChatServerService {
    private HashMap<String, ServerReceiverThread> clientMap;
    private MultiChatDAO multiChatDAO;

    private final int GOOD = 200; //통신 양호
    private final int GOOD_ALLOW = 201; //통신 양호-의미 긍정
    private final int GOOD_DENY = 202; //통신 양호-의미 부정

    private int port;

    public MultiChatServerService() {
        clientMap = new HashMap<String, ServerReceiverThread>();
        multiChatDAO = new MultiChatDAO();
        this.port = MetaDataLoader.getPort();
    }
    /////////////////////////////////////////////////////////////////////
   /*
   구동할 서비스
    */
    public void service() {
        try {
            ServerSocket servSock = new ServerSocket(port);
            //Lan에서 사용 가능
            //wan사용시 공유기에서 포트포워딩 설정할 것.
            while(true) {
                System.out.println("클라이언트의 접속을 기다리고 있습니다...");
                Socket socket = servSock.accept(); //접속 대기 및 수락

                //입력 스트림 생성 및 입력
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String temp = br.readLine();

                //출력 스트림 생성
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                //입력 데이터 객체화
                UserInfoDTO user = readUserInfo(temp);

                //로그인인지 회원가입인지 확인 및 처리
                if(user.getType().equals("join")) {
                    joinSerivce(user,bw);
                    continue;
                }else if(user.getType().equals("login")) {
                    user = loginService(user,bw);
                    if(user==null) continue;
                    //로그인, 가입 요청이 아닐 때
                }else {
                    errorHandling("처리할 수 없는 type이 감지되어 처리하지 않습니다.");
                    continue;
                }

                visitChat(socket,user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /////////////////////////////////////////////////////////////////////
    /*
        단일 서비스 메소드
     */
    //회원 가입 작업과 절차
    public void joinSerivce(UserInfoDTO user,BufferedWriter bw) {
        UserInfoDTO sendUserInfo = null;
        /*
                    join시
                    - 회원가입 가능한지 판단후 회원가입 절차 진행
                    - 결과 보내기.
                     */
        sendUserInfo = join(user);
        sendReply(sendUserInfo,bw);

        if(sendUserInfo.getStatus()==GOOD_ALLOW) {
            messageHandling(user.getId()," 가입 승인!");
        }
        //있는 ID일 때
        else if(sendUserInfo.getStatus()==GOOD_DENY) {
            errorHandling(user.getId(),"가입 미승인!");
        }
        //정의된 코드에 없을 때
        else {
            errorHandling("등록되지 않은 코드입니다.");
        }

    }

    //로그인 작업과 절차
    public UserInfoDTO loginService(UserInfoDTO user, BufferedWriter bw) {
        UserInfoDTO sendUserInfo = null;
          /*
                    login시
                    - 로그인 절차 진행
                    - 입장 가능한 지 확인.
                    - 결과 보내기.
                     */
        sendUserInfo  = login(user);
        UserInfoDTO rtn = null;
        //없는 ID, 비밀번호 틀렸을 때
        if(sendUserInfo.getStatus() == GOOD_DENY) {
            sendReply(sendUserInfo,bw);
            messageHandling(user.getId(), "로그인 미승인!");

        }
        else {
            //방문할 수 있는지 판단.
            if (isVisit(sendUserInfo.getId())) {
                sendReply(sendUserInfo, bw);
                messageHandling(user.getId(), "로그인 승인!");
                rtn = sendUserInfo;
            }
            //이미 같은 ID로 방문자가 있을 때
            else {
                sendUserInfo.setStatus(GOOD_DENY);
                sendReply(sendUserInfo, bw);
                errorHandling(sendUserInfo.getId(), "로그인 미승인!");
            }
        }
        return rtn;
    }

    //채팅방 입장
    public void visitChat(Socket socket,UserInfoDTO user) {

        ServerReceiverThread receiverThread = new ServerReceiverThread(socket,clientMap,user);
        receiverThread.start();
        //접속자 추가
        clientMap.put(user.getId(),receiverThread);

        //시스템 출력
        messageHandling(user.getName(),"입장!");
        List<UserInfoDTO> visitorList = new ArrayList<UserInfoDTO>();
        StringBuilder visitors = new StringBuilder("[");
        for(String key : clientMap.keySet()) {
            UserInfoDTO temp = clientMap.get(key).getUserInfo();
            visitors.append(temp.getName()+",");
            visitorList.add(temp);
        }
        visitors.replace(visitors.length()-1,visitors.length(),"]");
        messageHandling("현재 접속자는 " +visitors.toString());

        Message msg = new Message();
        msg.setType("system-visit");
        msg.setStatus("200");
        msg.setSender("");
        msg.setMessage("현재 접속자는 " + visitors.toString());
        msg.setVisitor(visitorList);
        //접속 내용 전파
        for (Map.Entry<String, ServerReceiverThread> entry : clientMap.entrySet()) {
            try {
                entry.getValue().writeInfo("["+user.getName()+"]"+" 님이 입장하셨습니다.");
                entry.getValue().writeInfo(msg);
            } catch (Exception e) {
                entry.getValue().close();
                clientMap.remove(entry.getKey());
            }
        }
    }



    //////////////////////////////////////////////////////////////////////////
    /*
    부속 메소드
     */

    //회원가입 작업
    private UserInfoDTO join(UserInfoDTO user) {
        /*
        이미 존재하는 아이디라면, 202 코드,
        아이디 삽입에 성공했다면, 201 코드
         */
        UserInfoDTO rtn = new UserInfoDTO();
        rtn.setType("join");
        if(!multiChatDAO.isAvailableJoin(user.getId())) rtn.setStatus(GOOD_DENY);
        if(multiChatDAO.join(user)) rtn.setStatus(GOOD_ALLOW);
        else rtn.setStatus(GOOD_DENY);

        return rtn;

    }

    //로그인 작업
    private UserInfoDTO login(UserInfoDTO user) {
        /*
        로그인 성공시
        -> 201 코드
        로그인 실패시(비밀번호가 틀렸을 때, 아이디가 존재하지 않을떄)
        -> 202 코드
         */
        UserInfoDTO rtn = multiChatDAO.login(user);
        if(rtn==null) {
            rtn = new UserInfoDTO();
            rtn.setStatus(GOOD_DENY);
        }
        else rtn.setStatus(GOOD_ALLOW);
        rtn.setType("login");
        return rtn;
    }

    //채팅방 참여 유무
    private boolean isVisit(String id) {
        /*
        채팅방 참여 가능한가
        -> 가능시 true;
        -> 불가능시 false
         */
        return !clientMap.containsKey(id);
    }

    //회원 정보 객체로 저장
    private UserInfoDTO readUserInfo(String userInfoJson) {
        return new Gson().fromJson(userInfoJson,UserInfoDTO.class);
    }

    //송신
    private void sendReply(UserInfoDTO infoDTO,BufferedWriter bw) {
        String message = new Gson().toJson(infoDTO);
        try {
            bw.write(message+"\n");
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //메세지 처리
    public void messageHandling(String message) {
        System.out.println(message);
    }
    public void messageHandling(String user, String message) {
        System.out.println("["+user+"] 님 " + message);
    }
    public void errorHandling(String message) {
        System.err.println(message);
    }
    public void errorHandling(String user, String message) {
        System.err.println("["+user+"] 님 " + message);
    }
}


//shift 두번
//ctrl+n 클래스 찾기

//alt+Shift+F10 실행
//ctrl+Shift+F12 코드만 보기

//ALT+ENTER 언어 인젝션
//alt+insert 소스 생성

//ctrl+y 라인 삭제
