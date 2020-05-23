package service.dto;

public class UserInfoDTO {
    private String type;
    private String id;
    private String name;
    private String pw;
    private int status;

    public UserInfoDTO() {
    }

    public UserInfoDTO(String id, String pw) {
        this.id = id;
        this.pw = pw;
    }

    public UserInfoDTO(String id, String name, String pw) {
        this.id = id;
        this.name = name;
        this.pw = pw;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UserInfoDTO{" +
                "type='" + type + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", pw='" + pw + '\'' +
                ", status=" + status +
                '}';
    }
}

