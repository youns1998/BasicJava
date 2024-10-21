package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import CONTROLLER.MainController;
import UTIL.Command;
import UTIL.DBUtil;
import UTIL.ScanUtil;
import VO.UsersVo;

public class UsersDao {
    private Connection con = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    
    private static UsersDao instance;

    // 싱글톤 패턴을 사용한 UsersDao 인스턴스 생성
    private UsersDao() {}

    public static UsersDao getInstance() {
        if (instance == null)
            instance = new UsersDao();
        return instance;
    }
    
    // DB 연결 해제 메서드
    private void disConnect() {
        if(rs != null) try { rs.close(); } catch(Exception e) {}
        if(ps != null) try { ps.close(); } catch(Exception e) {}
        if(con != null) try { con.close(); } catch(Exception e) {}
    }
    
    public String returnRole(String userid) {
        String sql = "SELECT ROLE FROM USERS WHERE USER_ID = ?";  // ROLE 필드를 선택하는 SQL 쿼리
        String userrole = null;  // 역할을 저장할 변수
        
        try {
            con = DBUtil.getConnection();  // DB 연결 가져오기
            ps = con.prepareStatement(sql);  // SQL 준비
            ps.setString(1, userid);  // USER_ID 설정
            
            rs = ps.executeQuery();  // 쿼리 실행
            
            if (rs.next()) {  // 결과가 있는지 확인
                int role = rs.getInt("ROLE");  // ROLE 필드 값 가져오기
                userrole = String.valueOf(role);  // int형 ROLE을 String으로 변환
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disConnect();  // DB 연결 해제
        }
        
        return userrole;  // 역할 반환
    }

    // 사용자 추가(회원가입)
    public int addUser(UsersVo user) {
        int cnt = 0;
        String sql = "INSERT INTO USERS (USER_ID, EMAIL, USERNAME, PHONE_NUMBER, ADDRESS, CREATED_AT, USER_PASS ) "
                   + " VALUES (?, ? ,?, ?, ?, ?, ?)";
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, user.getUser_id());      // USER_ID
            ps.setString(2, user.getEmail());        // EMAIL
            ps.setString(3, user.getUsername());     // USERNAME
            ps.setString(4, user.getPhone_number()); // PHONE_NUMBER
            ps.setString(5, user.getAddress());      // ADDRESS
            ps.setTimestamp(6, user.getCreated_at() != null ? Timestamp.valueOf(user.getCreated_at()) : Timestamp.valueOf(LocalDateTime.now())); // 생성 일시
            ps.setString(7, user.getUser_pass());    // USER_PASS
            cnt = ps.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disConnect(); // DB 연결 해제
        }
        return cnt;
    }

    // 관리자의 사용자 정보 선택 수정 메서드
    public void updateUserSelect(UsersVo uservo) {
        boolean exit = true;
        while (exit) {
            System.out.println();
            if(uservo.getRole()!=0) {
            System.out.println("1.PW 2.이름 3.번호 4.주소 5.이메일 6.회원제제 0.뒤로가기");
            }else {
            System.out.println("1.PW 2.이름 3.번호 4.주소 5.이메일  0.뒤로가기");
            }
            System.out.println("수정할 항목을 선택하세요 >>");
            int choice = ScanUtil.nextInt();
            switch (choice) {
                case 1:
                    System.out.print("새로운 PW를 입력하세요: ");
                    String newpw = ScanUtil.nextLine();
                    uservo.setUser_pass(newpw);
                    break;
                case 2:
                    System.out.print("새로운 이름을 입력하세요: ");
                    String newname = ScanUtil.nextLine();
                    uservo.setUsername(newname);
                    break;
                case 3:
                    System.out.print("새로운 번호를 입력하세요: ");
                    String newphone_number = ScanUtil.nextLine();
                    uservo.setPhone_number(newphone_number);
                    break;
                case 4:
                    System.out.print("새로운 주소를 입력하세요: ");
                    String newAdd = ScanUtil.nextLine();
                    uservo.setAddress(newAdd);
                    break;
                case 5:
                    System.out.println("새로운 이메일을 입력하세요.");
                    String newemail = ScanUtil.nextLine();    
                    uservo.setEmail(newemail);
                    break;
                case 6: 
                	System.out.println("제제할 회원의 ID를 입력하세요 >> ");
                	String newId = ScanUtil.nextLine();
                	String banId = ScanUtil.nextLine("제제할 사유를 입력하세요 >> ");
                	uservo.setUser_ban(banId);
                	uservo.setUser_id(newId);
                	MainController.sessionMap.put("loginUser", uservo);
                case 0:
                    exit = false;
                    break;
                default:
                    System.out.println("잘못된 선택입니다. 다시 시도하세요.");
                    continue;
            }

            // 수정된 내용을 DB에 반영
            if (choice != 0) {
                try {
                    int result = updateUser(uservo); // 회원 정보 업데이트
                    if (result > 0) {
                    	 MainController.sessionMap.put("loginUser", uservo);
                        System.out.println("회원 정보가 수정되었습니다.\n 1.더 수정하기 0.되돌아가기");
                        int y = ScanUtil.nextInt();
                        if (y == 1) continue;
                        if (y == 0) exit = false;
                    } else {
                        System.out.println("회원 정보 수정에 실패했습니다.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 로그인 메서드 (사용자 정보 가져오기)
    public UsersVo getUser(UsersVo userVo) {
        UsersVo getUserVo = null;
        String sql = "SELECT USER_ID, EMAIL, USERNAME, PHONE_NUMBER, ADDRESS, CREATED_AT, USER_PASS, ROLE, USER_BAN FROM USERS "
                   + "WHERE USER_ID = ? AND USER_PASS = ?";
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, userVo.getUser_id());
            ps.setString(2, userVo.getUser_pass());
            
            rs = ps.executeQuery();
            
            if (rs.next()) {
                getUserVo = new UsersVo();
                getUserVo.setUser_id(rs.getString("USER_ID"));
                getUserVo.setUser_pass(rs.getString("USER_PASS"));
                getUserVo.setUsername(rs.getString("USERNAME"));
                getUserVo.setAddress(rs.getString("ADDRESS"));
                getUserVo.setEmail(rs.getString("EMAIL"));
                getUserVo.setPhone_number(rs.getString("PHONE_NUMBER"));
                getUserVo.setRole(rs.getInt("ROLE")); // 사용자 역할 정보
                getUserVo.setUser_ban(rs.getString("USER_BAN"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disConnect(); // DB 연결 해제
        }
        return getUserVo;
    }

    // 비밀번호 찾기 메서드
    public UsersVo findUserPass(String userId, String email) {
        String sql = "SELECT user_pass FROM USERS WHERE user_id = ? AND email = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            pstmt.setString(2, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String userPass = rs.getString("user_pass");
                return new UsersVo(userId, email, userPass, true); // 찾은 비밀번호 반환
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // 사용자를 찾지 못한 경우
    }

    // 사용자 ID와 이메일이 일치하는지 확인하는 메서드
    public boolean iDisMatch(String userId, String email) {
        String sql = "SELECT user_id FROM USERS WHERE user_id = ? AND email = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            pstmt.setString(2, email);
            ResultSet rs = pstmt.executeQuery();

            return rs.next(); // 일치하는 결과가 있으면 true 반환
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 아이디 찾기 메서드
    public UsersVo findUserId(String email) {
        String sql = "SELECT user_id FROM USERS WHERE email = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String userId = rs.getString("user_id");
                return new UsersVo(userId, email); // 찾은 아이디 반환
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // 사용자를 찾지 못한 경우
    }

    // 이메일이 존재하는지 확인하는 메서드
    public boolean EmailisMatch(String email) {
        String sql = "SELECT user_id FROM USERS WHERE email = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // 이메일이 존재하면 true 반환
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 사용자 상세 조회 메서드
    public UsersVo getUserSelect(String userId) {
        UsersVo user = null;
        String sql = "SELECT * FROM USERS WHERE USER_ID = ?";
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user = new UsersVo();
                user.setUser_id(rs.getString("USER_ID"));
                user.setUser_pass(rs.getString("USER_PASS"));
                user.setUsername(rs.getString("USERNAME"));
                user.setAddress(rs.getString("ADDRESS"));
                user.setEmail(rs.getString("EMAIL"));
                user.setPhone_number(rs.getString("PHONE_NUMBER"));
                user.setRole(rs.getInt("ROLE"));
                user.setUser_ban(rs.getString("USER_BAN"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disConnect(); // DB 연결 해제
        }
        return user;
    }

    // 모든 사용자 조회 (관리자용)
    public List<UsersVo> getUserList() {
        List<UsersVo> userList = new ArrayList<>();
        String sql = "SELECT * FROM USERS";
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                UsersVo user = new UsersVo();
                user.setUser_id(rs.getString("USER_ID"));
                user.setEmail(rs.getString("EMAIL"));
                user.setUsername(rs.getString("USERNAME"));
                user.setPhone_number(rs.getString("PHONE_NUMBER"));
                user.setAddress(rs.getString("ADDRESS"));
                user.setRole(rs.getInt("ROLE"));
                user.setUser_ban(rs.getString("USER_BAN"));
                user.setCreated_at(rs.getTimestamp("CREATED_AT").toLocalDateTime());
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disConnect(); // DB 연결 해제
        }
        return userList;
    }

    // 사용자 정보 수정 메서드
    public int updateUser(UsersVo user) {
        int cnt = 0;
        String sql = "UPDATE USERS SET USER_PASS = ?, EMAIL = ?, USERNAME = ?, PHONE_NUMBER = ?, ADDRESS = ?, USER_BAN = ? WHERE USER_ID = ?";
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, user.getUser_pass());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getUsername());
            ps.setString(4, user.getPhone_number());
            ps.setString(5, user.getAddress());
            ps.setString(6, user.getUser_ban());
            ps.setString(7, user.getUser_id());           
            cnt = ps.executeUpdate(); // 업데이트 결과 반환
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disConnect(); // DB 연결 해제
        }
        return cnt;
    }

    // 사용자 삭제 (회원 탈퇴)
    public int deleteUser(UsersVo user) {
        int cnt = 0;
        String sql = "DELETE FROM USERS WHERE USER_ID = ?";
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, user.getUser_id());
            cnt = ps.executeUpdate(); // 삭제 결과 반환
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disConnect(); // DB 연결 해제
        }
        return cnt;
    }
}
