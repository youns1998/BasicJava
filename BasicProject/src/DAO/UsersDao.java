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
import VO.UsersVo;


public class UsersDao {
	private Connection con = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	
	private static UsersDao instance;

	private UsersDao() {

	}

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
	
	//사용자 추가
	public int addUser(UsersVo user) {
		int cnt = 0;

		String sql = "INSERT INTO USERS (USER_ID, EMAIL, USERNAME, PHONE_NUMBER, ADDRESS, CREATED_AT, USER_PASS ) "
				+ " VALUES (?, ? ,?, ?, ?, ?, ?)";
		
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
	
	//사용자 상세 조회
	public UsersVo getUser(UsersVo userVo) {
		UsersVo getUserVo = null;
		
		String sql = "SELECT USER_ID, EMAIL, USERNAME, PHONE_NUMBER, ADDRESS, CREATED_AT, USER_PASS FROM USERS "
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
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnect();
		}
		
		
		return getUserVo;
	}

	//모든 사용자 조회
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
	
	// 사용자 정보 수정
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
	//사용자 삭제
	public int deleteUser(String user_id) {
		int cnt = 0;
		String sql = "DELETE FROM USERS "
				+ " WHERE USER_ID = ? ";
		try {
			con = DBUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, user_id);
			
			cnt = ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnect();
		}
		return cnt;
	}
	
//	회원ID의 갯수를 반환하는 메서드
	public int getUserCount(String user_id) {
		int count = 0;		//반환 값이 저장될 변수
		 try {
	         con = DBUtil.getConnection();
	         String sql = "select count(*) cnt from users "
	               + "where user_id = ?";
	         ps = con.prepareStatement(sql);
	         ps.setString(1, user_id);
	         
	         rs = ps.executeQuery();
	         
	         if(rs.next()) {
	            count = rs.getInt("cnt");
	         }
	      } catch (SQLException e) {
	         e.printStackTrace();
	      } finally {
	    	  disConnect();
	      }
		return count;
	}
}
