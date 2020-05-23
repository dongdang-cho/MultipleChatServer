package service.dao;

import service.dto.UserInfoDTO;
import util.MariaDBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MultiChatDAO {

    public UserInfoDTO login(UserInfoDTO user) {
        String id = user.getId();
        String pwd = user.getPw();
        String query = "select id, name from account where id=? and pwd=(select sha2(?,512));";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        UserInfoDTO dto = null;

        try {
            con = MariaDBConnector.getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1,id);
            ps.setString(2,pwd);
            rs = ps.executeQuery();
            if(rs.next()) {
                dto = new UserInfoDTO();
                dto.setId(rs.getString("id"));
                dto.setName(rs.getString("name"));
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            MariaDBConnector.dbClose(con, ps, rs);
        }
        return dto;
    }

    public boolean isAvailableJoin(String id) {
        String query = "select id from account where id=?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = MariaDBConnector.getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1,id);
            rs = ps.executeQuery();
            if(rs.next()) {
                return false;
            }
        }catch (SQLException e) {
            e.printStackTrace();
            return false;
        }catch(Exception e) {
            e.printStackTrace();
            return false;
        }finally {
            MariaDBConnector.dbClose(con, ps, rs);
        }
        return true;
    }

    public boolean join(UserInfoDTO user) {
        String query = "insert into account values(?,?,(SELECT SHA2(?, 512)));";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = MariaDBConnector.getConnection();

            ps = con.prepareStatement(query);
            ps.setString(1,user.getId());
            ps.setString(2,user.getName());
            ps.setString(3,user.getPw());
            ps.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
            return false;
        }catch(Exception e) {
            e.printStackTrace();
            return false;
        }finally {
            MariaDBConnector.dbClose(con, ps, rs);
        }
        return true;

    }
}
