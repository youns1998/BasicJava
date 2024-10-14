package DAO;

import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import UTIL.DBUtil;
import VO.UsersVo;


public class UsersDao {
	private Connection con = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	
	private void disConnect() {
		if(rs != null) try { rs.close(); } catch(Exception e) {}
		if(ps != null) try { ps.close(); } catch(Exception e) {}
		if(con != null) try { con.close(); } catch(Exception e) {}
	}
	
	
	//사용자 추가
	public int addUser(UsersVo user) {
		int cnt = 0;
		String sql = "INSERT INTO USERS (USER_ID, EMAIL, USER_NAME, PHONE_NUMBER, ADDRESS, CREATED_AT, USER_PASS, ) "
				+ " VALUES (?, ? ,?, ?, ?, ?, ?)";
		
		try {
			con = DBUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, user.getUser_id());      // USER_ID
			ps.setString(2, user.getEmail());        // EMAIL
			ps.setString(3, user.getUsername());     // USER_NAME
			ps.setString(4, user.getPhone_number()); // PHONE_NUMBER
			ps.setString(5, user.getAddress());      // ADDRESS
			ps.setTimestamp(6, Timestamp.valueOf(user.getCreated_at())); // CREATED_AT
			ps.setString(7, user.getUser_pass());    // USER_PASS
			
			cnt = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnect();
		}
		
		return cnt;
	}
	

	//모든 사용자 조회
	public List<UsersVo> getPostList(){
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
	
	// 사용자 상세조회
	public int search(String user_id) {
	int cnt = 0;
	
	String sql = "SELECT FROM USERS "
			+ " WHERE USER_ID = ? ";
	
	try {
		con = DBUtil.getConnection();
		ps = con.prepareStatement(sql);
		ps.setString(1, user_id);
		
		rs = ps.executeQuery();
		
	} catch (SQLException e) {
		e.printStackTrace();
	} finally {
		disConnect();
	}
	
	return cnt;
}

<<<<<<< HEAD
	//사용자 정보 수정
	public int updateUser(UsersVo user) {
		UsersVo getUserVo = null;
=======
	// 사용자 정보 수정
	public int updateUser(UsersVo user) {
		int cnt = 0;
>>>>>>> branch 'main' of https://github.com/youns1998/BasicJava

	    String sql = "UPDATE USERS SET USER_PASS = ?,EMAIL = ? , USERNAME = ?, PHONE_NUMBER = ?, ADDRESS = ?, WHERE USER_ID = ?"; // 공백 추가

	    try {
	        con = DBUtil.getConnection();
	        ps = con.prepareStatement(sql);
	        ps.setString(1, user.getUser_pass()); 
	        ps.setString(2, user.getEmail()); 
	        ps.setString(3, user.getUsername());
	        ps.setString(4, user.getPhone_number());
	        ps.setString(5, user.getAddress());
	        ps.setString(6, user.getUser_id());
	        
	        ps = con.prepareStatement(sql);
			
<<<<<<< HEAD
			ps.setString(1, user.getUser_pass());
			ps.setString(2, user.getUser_id());
			
			rs =  ps.executeUpdate();
			if(rs.next()) {
				getUserVo = new UsersVo();
				getUserVo.setUser_id(rs.getString("USER_ID"));
				getUserVo.setUser_pass(rs.getString("USER_PASS"));
				getUserVo.setUsername(rs.getString("USER_NAME"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnect();
		}


		return ;
		}
=======
>>>>>>> branch 'main' of https://github.com/youns1998/BasicJava
	
			
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
	
}
