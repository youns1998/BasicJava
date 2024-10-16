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
String sql = "SELECT * FROM CATEGORY ORDER BY CATEGORY_ID";
		
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
//카테고리 선택 조회
public CategoryVo getCategory(int category_id) {
	CategoryVo cate = null;
	String sql = "SELECT * FROM CATEGORY WHERE CATEGORY_ID = ?";
	try {
		con=DBUtil.getConnection();
		ps=con.prepareStatement(sql);
		ps.setInt(1, category_id);
		rs=ps.executeQuery();
		
		 if (rs.next()) {
			 	cate = new CategoryVo();
				cate.setCategory_id(rs.getInt("CATEGORY_ID"));
				cate.setCategory_name(rs.getString("CATEGORY_NAME"));
		 }
		 } catch (Exception e) {
		e.printStackTrace();
	}	finally {
		disConnect();
	}
	return cate;
}
//카테고리 추가
	
	public int InsertCategory(CategoryVo cate) {
		int cnt= 0;
		String sql = "INSERT INTO CATEGORY (CATEGORY_ID, CATEGORY_NAME) VALUES (?,?) ";
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
			ps.setString(1, cate.getCategory_name()); 
			ps.setInt(2, cate.getCategory_id());      
			cnt = ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnect();
		}
		return cnt;
	}

	//카테고리에 같은 내용이있으면 false반환 없으면 true반환
	public boolean isCategoryIdExists(int categoryId) {
	    String sql = "SELECT COUNT(*) FROM CATEGORY WHERE CATEGORY_ID = ?";
	    try (Connection con = DBUtil.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setInt(1, categoryId);
	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) {
	            return rs.getInt(1) > 0; // 존재하면 true, 아니면 false
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false; 
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











