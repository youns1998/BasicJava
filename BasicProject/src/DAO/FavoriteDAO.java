package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import CONTROLLER.MainController;
import UTIL.DBUtil;
import VO.FavoriteVo;
import VO.PostVo;
import VO.UsersVo;



public class FavoriteDAO {
   private Connection con = null;
   private PreparedStatement ps = null;
   private ResultSet rs = null;
 
   public FavoriteDAO() {
   
   }

   private void disConnect() {
      if(rs != null) try { rs.close(); } catch(Exception e) {}
      if(ps != null) try { ps.close(); } catch(Exception e) {}
      if(con != null) try { con.close(); } catch(Exception e) {}
   }
   
   
   
   
   //관심 상품 추가
   public int addFavorite(FavoriteVo favorite) {
	    int cnt = 0;
	    String sql = "INSERT INTO FAVORITE (USER_ID, POST_ID, POST_TITLE) VALUES (?, ?, ?)";
	    
	    try (Connection con = DBUtil.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {
	         
	        ps.setString(1, favorite.getUser_id());
	        ps.setInt(2, favorite.getPost_id());
	        ps.setString(3, favorite.getPost_title());
	        
	        cnt = ps.executeUpdate();
	        
	    } catch (SQLException e) {
	        e.printStackTrace(); // 예외 발생 시 로그 출력
	        // 동일한 상품이 있는 경우 처리 로직 추가
	    }
	    
	    return cnt;
	}

// 사용자의 관심 상품 목록 조회
	public List<FavoriteVo> getFavoritesByUser() {
		 UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser");
		 if (loginUserVo == null) {
		        return new ArrayList<>(); // 또는 null 반환
		    }
		 
		 List<FavoriteVo> favorites = new ArrayList<>();
        String sql = "SELECT f.USER_ID, f.POST_ID, p.TITLE, p.USER_ID AS AUTHOR "
                	+ "FROM FAVORITE f "
                	+ "JOIN POST p ON f.POST_ID = p.POST_ID "
                	+ "WHERE f.USER_ID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, loginUserVo.getUser_id());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                FavoriteVo favorite = new FavoriteVo();
                favorite.setUser_id(rs.getString("USER_ID"));
                favorite.setPost_id(rs.getInt("POST_ID"));
                favorite.setPost_title(rs.getString("TITLE")); // 게시글 제목 설정
                favorite.setAuthor(rs.getString("AUTHOR")); // 작성자 설정 (USER_ID)
                favorites.add(favorite);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return favorites;
    }
// 관심 상품 삭제
	   public boolean deleteFavorite(String userId, int postId) {
	        String sql = "DELETE FROM FAVORITE WHERE USER_ID = ? AND POST_ID = ?";
	        try (Connection conn = DBUtil.getConnection();
	                PreparedStatement pstmt = conn.prepareStatement(sql)) {
	               pstmt.setString(1, userId);
	               pstmt.setInt(2, postId);
	               int rowsAffected = pstmt.executeUpdate();
	               return rowsAffected > 0; // 삭제 성공 여부 반환
	           } catch (SQLException e) {
	               e.printStackTrace();
	               return false; // 예외 발생 시 false 반환
	           }
	    }
// 특정 사용자가 특정 게시글을 즐겨찾기 했는지 확인
	  public boolean isFavorite(String userId, int postId) {
	        String sql = "SELECT COUNT(*) FROM FAVORITE WHERE USER_ID = ? AND POST_ID = ?";
	        try (Connection conn = DBUtil.getConnection();
	             PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            pstmt.setString(1, userId);
	            pstmt.setInt(2, postId);
	            ResultSet rs = pstmt.executeQuery();
	            if (rs.next()) {
	                return rs.getInt(1) > 0;
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return false;
	    }
}
