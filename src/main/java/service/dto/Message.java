package service.dto;

import java.util.List;

public class Message {
    private String type;
    private String status;
    private String sender;
    private String message;
    private List<String> receiver;
    private List<UserInfoDTO> visitor;
    private String exit;

    public Message() {
    }

    public Message(String type, String status, String sender, String message, List<String> receiver, List<UserInfoDTO> visitor, String exit) {
        this.type = type;
        this.status = status;
        this.sender = sender;
        this.message = message;
        this.receiver = receiver;
        this.visitor = visitor;
        this.exit = exit;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getReceiver() {
        return receiver;
    }

    public void setReceiver(List<String> receiver) {
        this.receiver = receiver;
    }

    public List<UserInfoDTO> getVisitor() {
        return visitor;
    }

    public void setVisitor(List<UserInfoDTO> visitor) {
        this.visitor = visitor;
    }

    public String getExit() {
        return exit;
    }

    public void setExit(String exit) {
        this.exit = exit;
    }

    @Override
    public String toString() {
        return "Message{" +
                "type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", sender='" + sender + '\'' +
                ", message='" + message + '\'' +
                ", receiver=" + receiver +
                ", visitor=" + visitor +
                ", exit='" + exit + '\'' +
                '}';
    }
}
