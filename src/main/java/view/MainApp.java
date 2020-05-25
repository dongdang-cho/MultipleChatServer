package view;


import service.bl.MultiChatServerService;

public class MainApp {
    public static void main(String[] args) {
        new MultiChatServerService().service();
    }
}
