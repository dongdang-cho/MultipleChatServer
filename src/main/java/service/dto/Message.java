package service.dto;

import java.util.List;

public class Message {
    private String type;
    private String status;
    private String sender;
    private String message;
    private List<String> receiver;

    public Message() {}

    public Message(String type, String status, String sender, String message, List<String> receiver) {
        this.type = type;
        this.status = status;
        this.sender = sender;
        this.message = message;
        this.receiver = receiver;
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

    @Override
    public String toString() {
        return "Message{" +
                "type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", sender='" + sender + '\'' +
                ", message='" + message + '\'' +
                ", receiver=" + receiver +
                '}';
    }
}
