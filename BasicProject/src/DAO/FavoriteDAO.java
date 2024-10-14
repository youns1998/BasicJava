package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import UTIL.DBUtil;
import VO.FavoriteVo;

ㄴ

public class FavoriteDAO {
//관심 상품 추가
	public void addFavorite(FavoriteVo favorite) {
        String sql = "INSERT INTO FAVORITE (USER_ID, POST_ID) VALUES (?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, favorite.getUser_id());
            pstmt.setInt(2, favorite.getPost_id());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
// 사용자의 관심 상품 목록 조회
	public List<FavoriteVo> getFavoritesByUser(String userId) {
        List<FavoriteVo> favorites = new ArrayList<>();
        String sql = "SELECT * FROM FAVORITE WHERE USER_ID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                FavoriteVo favorite = new FavoriteVo();
                favorite.setUser_id(rs.getString("USER_ID"));
                favorite.setPost_id(rs.getInt("POST_ID"));
                favorites.add(favorite);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return favorites;
    }
// 관심 상품 삭제
	public void deleteFavorite(String userId, int postId) {
        String sql = "DELETE FROM FAVORITE WHERE USER_ID = ? AND POST_ID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            pstmt.setInt(2, postId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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
