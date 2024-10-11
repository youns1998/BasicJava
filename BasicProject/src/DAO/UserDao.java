package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import VO.UserVO;
import kr.or.ddit.util.DBUtil;


public class UserDaoImpl {
	private Connection con = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	
	//싱글톤 패턴 ---------------------------------------------------
	private static UserDaoImpl dao;
	private UserDaoImpl(){}
	public static UserDaoImpl getInstance(){
		if(dao == null){
			dao = new UserDaoImpl();
		}
		return dao;
	}
	// ---------------------------------------------------------------
	
	private void disConnect() {
		if(rs != null) try { rs.close(); } catch(Exception e) {}
		if(ps != null) try { ps.close(); } catch(Exception e) {}
		if(con != null) try { con.close(); } catch(Exception e) {}
	}
	
	public int insertUser(UserVO userVo){
		int cnt = 0;
		
		String sql = "INSERT INTO TB_JDBC_USER (USER_ID, USER_PASS, USER_NAME) VALUES (?, ?, ?)";
		
		try {
			con = DBUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, userVo.getUser_id());
			ps.setString(2, userVo.getUser_pass());
			ps.setString(3, userVo.getUser_name());
			
			cnt = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnect();
		}
		
		return cnt;
	}
	
	public UserVO getUser(UserVO userVo) {
		UserVO getUserVo = null;
		
		String sql = "SELECT USER_ID, USER_PASS, USER_NAME FROM TB_JDBC_USER "
				+ " WHERE USER_ID = ? AND USER_PASS = ?";
		try {
			con = DBUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, userVo.getUser_id());
			ps.setString(2, userVo.getUser_pass());
			rs = ps.executeQuery();
			if(rs.next()) {
				getUserVo = new UserVO();
				getUserVo.setUser_id(rs.getString("USER_ID"));
				getUserVo.setUser_pass(rs.getString("USER_PASS"));
				getUserVo.setUser_name(rs.getString("USER_NAME"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnect();
		}
		
		
		return getUserVo;
	}
	
}








