package DAO;

import java.time.LocalDateTime;
import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import UTIL.DBUtil;
import UTIL.ScanUtil;
import VO.PostVo;
import VO.UsersVo;


public class UsersDao {
	private Connection con = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	
	private static UsersDao instance;

	private UsersDao() {}

	public static UsersDao getInstance() {
		if (instance == null)
			instance = new UsersDao();
		return instance;
	}
	private void disConnect() {
		if(rs != null) try { rs.close(); } catch(Exception e) {}
		if(ps != null) try { ps.close(); } catch(Exception e) {}
		if(con != null) try { con.close(); } catch(Exception e) {}
	}
	
	//사용자 추가(회원가입)
	public int addUser(UsersVo user) {
		int cnt = 0;
		String sql = "INSERT INTO USERS (USER_ID, EMAIL, USERNAME, PHONE_NUMBER, ADDRESS, CREATED_AT, USER_PASS ) "
				+ " VALUES (?, ? ,?, ?, ?, ?,?)";
		try {
			con = DBUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, user.getUser_id());      // USER_ID
			ps.setString(2, user.getEmail());        // EMAIL
			ps.setString(3, user.getUsername());     // USER_NAME
			ps.setString(4, user.getPhone_number()); // PHONE_NUMBER
			ps.setString(5, user.getAddress());      // ADDRESS
			ps.setTimestamp(6, user.getCreated_at() != null ? Timestamp.valueOf(user.getCreated_at()) : Timestamp.valueOf(LocalDateTime.now()));
			ps.setString(7, user.getUser_pass());    // USER_PASS
			cnt = ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnect();
		}
		return cnt;
	}
	 // 선택 수정 메서드
    public void updateUserSelect(UsersVo uservo) {
    	boolean exit = true;
        while (exit) {
        	System.out.println();
            System.out.println("수정할 항목을 선택하세요 >>");
            System.out.println("1.PW 2.이름 3.번호 4.주소 5.이메일 0.뒤로가기");
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
                    return;
                case 0:
                	break;
                default:
                    System.out.println("잘못된 선택입니다. 다시 시도하세요.");
                    continue;
            }

            // 수정된 내용을 데이터베이스에 반영
            if(choice!=0){try {
                int result = updateUser(uservo); // 게시물 업데이트
                if (result > 0) {
                    System.out.println("회원 정보가 수정되었습니다.\n "
                    		+ "1.더 수정 하기 0.되돌아 가기");
                    int y = ScanUtil.nextInt();
                    if(y==1) {continue;}
                    if(y==0) {break;}	
                    
                } else {
                    System.out.println("회원정보 수정에 실패했습니다.");
                }
            } 
            catch (Exception e) {
                e.printStackTrace();
            }
            }
            exit = false;
        }
    }
	// 로그인 
	public UsersVo getUser(UsersVo userVo) {
		UsersVo getUserVo = null;
		
		String sql = "SELECT USER_ID, EMAIL, USERNAME, PHONE_NUMBER, ADDRESS, CREATED_AT, USER_PASS, ROLE FROM USERS "
				+ " WHERE USER_ID = ? AND USER_PASS = ?";
		try {
			con = DBUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, userVo.getUser_id());
			ps.setString(2, userVo.getUser_pass());
			rs = ps.executeQuery();
			
			if(rs.next()) {
				getUserVo = new UsersVo();
				getUserVo.setUser_id(rs.getString("USER_ID"));
				getUserVo.setUser_pass(rs.getString("USER_PASS"));
				getUserVo.setUsername(rs.getString("USERNAME"));
				getUserVo.setAddress(rs.getString("ADDRESS"));
				getUserVo.setEmail(rs.getString("EMAIL"));
				getUserVo.setPhone_number(rs.getString("PHONE_NUMBER"));
				getUserVo.setRole(rs.getInt("ROLE"));
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnect();
		}
		
		
		return getUserVo;
	}
	
	// 비번 찾기
	public UsersVo findUserPass(String userId, String name, String email) {
		 String sql = "SELECT user_pass FROM USERS WHERE user_id = ? AND username = ? AND email = ? ";
		 
		 try (Connection conn = DBUtil.getConnection();
	             PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            
			 	pstmt.setString(1, userId);
			 	pstmt.setString(2, name);
	            pstmt.setString(3, email);
	            ResultSet rs = pstmt.executeQuery();

	            if (rs.next()) {
	                String userPass = rs.getString("user_pass");
	                return new UsersVo(userId, name, email, userPass); // 반환할 VO 생성
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return null; // 사용자를 찾지 못한 경우
	    }
	
	
	// 아이디 찾기
	 public UsersVo findUserId(String name, String email) {
	        String sql = "SELECT user_id FROM USERS WHERE username = ? AND email = ? ";
	        
	        try (Connection conn = DBUtil.getConnection();
	             PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            pstmt.setString(1, name);
	            pstmt.setString(2, email);
	            ResultSet rs = pstmt.executeQuery();

	            if (rs.next()) {
	                String userId = rs.getString("user_id");
	                return new UsersVo(userId, name, email); // 반환할 VO 생성
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return null; // 사용자를 찾지 못한 경우
	    }
	
	
	
	//사용자 상세보기
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
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	            disConnect();
	        }

	        return user;
	    }
	

	//모든 사용자 조회 - 관리자용 (사용자는 불가)
	public List<UsersVo> getUserList(){
		List<UsersVo> userList = null;
		String sql = "SELECT * FROM USERS";
		
		try {
			con = DBUtil.getConnection();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			userList = new ArrayList<UsersVo>();
			while(rs.next()) {
				UsersVo user = new UsersVo();
				user.setUser_id(rs.getString("USER_ID"));
				user.setEmail(rs.getString("EMAIL"));
				user.setUsername(rs.getString("USERNAME"));
				user.setPhone_number(rs.getString("PHONE_NUMBER"));
				user.setAddress(rs.getString("ADDRESS"));
				user.setRole(rs.getInt("ROLE"));
				user.setCreated_at(rs.getTimestamp("CREATED_AT").toLocalDateTime());
				userList.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnect();
		}
		return userList;
	}
	
	// 내 정보 수정
	public int updateUser(UsersVo user) {
		int cnt = 0;
		String sql = "UPDATE USERS SET USER_PASS = ?, EMAIL = ?, USERNAME = ?, PHONE_NUMBER = ?, ADDRESS = ? WHERE USER_ID = ?";
	    try {
	        con = DBUtil.getConnection();
	        ps = con.prepareStatement(sql);
	        ps.setString(1, user.getUser_pass()); 
	        ps.setString(2, user.getEmail()); 
	        ps.setString(3, user.getUsername());
	        ps.setString(4, user.getPhone_number());
	        ps.setString(5, user.getAddress());
	        ps.setString(6, user.getUser_id());
	        

			cnt = ps.executeUpdate();

	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        disConnect(); // 자원 해제
	    }

	    return cnt;  
	}
	//회원 탈퇴 (회원 삭제)
	public int deleteUser(UsersVo user) {
		int cnt = 0;
		String sql = "DELETE FROM USERS "
				+ " WHERE USER_ID = ? ";
		try {
			con = DBUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, user.getUser_id());
			
			cnt = ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnect();
		}
		return cnt;
	}
}
