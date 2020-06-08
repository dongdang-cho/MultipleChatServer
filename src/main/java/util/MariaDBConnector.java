package util;

import service.bl.MultiChatServerService;

import java.sql.*;
import java.util.Properties;

public class MariaDBConnector {

    static String driver = "";
    static String dbServerAddr =  "";
    static String dbName = "";
    static String user = "";
    static String pw = "";

    //DB 구동
    public static void dbLoading(Properties profile) {
        driver = profile.getProperty("driver");
        dbServerAddr = profile.getProperty("dbServerAddr");
        dbName =  profile.getProperty("dbName");
        user =  profile.getProperty("user");
        pw =  profile.getProperty("pw");
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    //DB 연결
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbServerAddr + dbName,user, pw);
    }
    // DB 종료
    public static void dbClose(Connection con, Statement st, ResultSet rs) {
        try {
            if(rs != null) {
                rs.close();	rs=null;
            }
            if(st != null) {
                st.close();	st=null;
            }
            if(con != null) {
                con.close();	con=null;
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }

}
