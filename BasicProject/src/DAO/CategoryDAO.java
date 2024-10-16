package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import UTIL.DBUtil;
import VO.CategoryVo;
import VO.UsersVo;

public class CategoryDAO {
	private Connection con = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	
	private static CategoryDAO instance;

	private CategoryDAO() {}

	public static CategoryDAO getInstance() {
		if (instance == null)
			instance = new CategoryDAO();
		return instance;
	}
	
	private void disConnect() {
		if(rs != null) try { rs.close(); } catch(Exception e) {}
		if(ps != null) try { ps.close(); } catch(Exception e) {}
		if(con != null) try { con.close(); } catch(Exception e) {}
	}
	
//카테고리 목록 조회
	public List<CategoryVo> getCategoryList(){
		List<CategoryVo> cateList = null;
String sql = "SELECT * FROM CATEGORY";
		
		try {
			con = DBUtil.getConnection();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			cateList = new ArrayList<CategoryVo>();
			while(rs.next()) {
				CategoryVo catevo = new CategoryVo();
				catevo.setCategory_id((rs.getInt("CATEGORY_ID")));
				catevo.setCategory_name((rs.getString("CATEGORY_NAME")));
				cateList.add(catevo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnect();
		}
		return cateList;
	}
//카테고리 추가
	
	public int InsertCategory(CategoryVo cate) {
		int cnt= 0;
		String sql = "INSERT INTO CATEGORY (CATEGORY_ID, CATEGORY_NAME) VLAUSE (?,?)";
		try {
			con = DBUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, cate.getCategory_id());      
			ps.setString(2, cate.getCategory_name()); 
			cnt = ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnect();
		}
		return cnt;
	}
	
//카테고리 수정
	public int UpdateCategory(CategoryVo cate) {
		int cnt= 0;
		String sql = "UPDATE CATEGORY SET CATEGORY_NAME = ? WHERE CATEGORY_ID = ?";
		
		try {
			con = DBUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, cate.getCategory_id());      
			ps.setString(2, cate.getCategory_name()); 
			cnt = ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnect();
		}
		return cnt;
	}
	
	
//카테고리 삭제
	
	public int DeleteCategory(CategoryVo cate) {
		int cnt= 0;
		String sql = "DELETE FROM CATEGORY WHERE CATEGORY_ID = ?"; 
		
		try {
			con = DBUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, cate.getCategory_id());      
			ps.setString(2, cate.getCategory_name()); 
			cnt = ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnect();
		}
		return cnt;
	}
}











