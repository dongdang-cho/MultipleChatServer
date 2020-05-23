package view;

import service.dao.MultiChatDAO;
import service.dto.UserInfoDTO;

public class TestApp {


    public static void main(String[] args) {
//        MultiChatDAO dao = new MultiChatDAO();
//        System.out.println(dao.login(new UserInfoDTO("sample","sample","sample")));
//        System.out.println("sample : "+dao.isAvailableJoin("sample"));
//        System.out.println("test : "+dao.isAvailableJoin("test"));
        Test a = new Test();
        a.a();;
    }

}

class Test {
    public void a() {
        TestDTO test = new TestDTO("a");
        System.out.println(test);
        b(test);
        System.out.println(test);
    }
    public void b(TestDTO test) {
        TestDTO testB = new TestDTO("b");
        test = testB;
        System.out.println(test);
    }
}

class TestDTO {
    private String a;

    public TestDTO(String a) {
        this.a = a;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    @Override
    public String toString() {
        return "TestDTO{" +
                "a='" + a + '\'' +
                '}';
    }
}