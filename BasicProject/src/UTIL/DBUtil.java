package UTIL;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
public class DBUtil {
    private static final String DB_URL = "jdbc:oracle:thin:@192.168.142.37:1588/FREEPDB1";
    private static final String DB_USER = "PEANUT";
    private static final String DB_PASSWORD = "java";

    static {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("드라이버 로딩 실패");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            System.out.println("DB 연결 실패");
            e.printStackTrace();
        }
        return conn;
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("DB 연결 닫기 실패");
                e.printStackTrace();
            }
        }
    }
}