package service.dto;

public class UserInfoDTO {
    private String ipAdr;
    private int port;
    private String id;
    private String name;

    public UserInfoDTO() {
    }

    public UserInfoDTO(String ipAdr, int port, String id, String name) {
        this.ipAdr = ipAdr;
        this.port = port;
        this.id = id;
        this.name = name;
    }

    public String getIpAdr() {
        return ipAdr;
    }

    public void setIpAdr(String ipAdr) {
        this.ipAdr = ipAdr;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
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

    @Override
    public String toString() {
        return "UserInfoDTO{" +
                "ipAdr='" + ipAdr + '\'' +
                ", port=" + port +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}

