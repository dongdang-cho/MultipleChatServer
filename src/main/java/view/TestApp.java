package view;

import service.dao.MultiChatDAO;
import service.dto.UserInfoDTO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestApp {

    public static void main(String[] args) {
//        MultiChatDAO dao = new MultiChatDAO();
//        System.out.println(dao.login(new UserInfoDTO("sample","sample","sample")));
//        System.out.println("sample : "+dao.isAvailableJoin("sample"));
//        System.out.println("test : "+dao.isAvailableJoin("test"));
        String str = " @test1 @test2  안녕하 @test3 세요";
        Pattern pattern = Pattern.compile("(@\\S+) ");
        Matcher matcher = pattern.matcher(str);
        while(matcher.find()) {
            System.out.println(matcher.group().substring(1));
            str = str.replace(matcher.group(),"");
        }
        str = str.replaceAll("(\\s+)","");
        System.out.println(str);


    }
}