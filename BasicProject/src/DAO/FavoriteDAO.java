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

    // 기본 생성자
    public FavoriteDAO() {
    }

    // 리소스를 해제하는 메서드
    private void disConnect() {
        if (rs != null) try { rs.close(); } catch (Exception e) {}
        if (ps != null) try { ps.close(); } catch (Exception e) {}
        if (con != null) try { con.close(); } catch (Exception e) {}
    }

    // 관심 상품 추가 메서드
    public int addFavorite(FavoriteVo favorite) {
        int cnt = 0;
        String sql = "INSERT INTO FAVORITE (USER_ID, POST_ID, POST_TITLE) VALUES (?, ?, ?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, favorite.getUser_id());  // 사용자 ID 설정
            ps.setInt(2, favorite.getPost_id());  // 게시물 ID 설정
            ps.setString(3, favorite.getPost_title());  // 게시물 제목 설정
            cnt = ps.executeUpdate();  // 쿼리 실행 및 업데이트된 행 수 반환
        } catch (SQLException e) {
            e.printStackTrace();  // 예외 발생 시 로그 출력
        }
        return cnt;  // 업데이트된 행 수 반환
    }

    // 사용자의 관심 상품 목록 조회 메서드
    public List<FavoriteVo> getFavoritesByUser() {
        UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser");  // 로그인한 사용자 정보 가져오기
        List<FavoriteVo> favorites = new ArrayList<>();
        String sql = "SELECT f.USER_ID, f.POST_ID, p.TITLE, p.USER_ID AS AUTHOR "
                     + "FROM FAVORITE f "
                     + "JOIN POST p ON f.POST_ID = p.POST_ID "
                     + "WHERE f.USER_ID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, loginUserVo.getUser_id());  // 사용자 ID 바인딩
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                FavoriteVo favorite = new FavoriteVo();
                favorite.setUser_id(rs.getString("USER_ID"));  // 사용자 ID 설정
                favorite.setPost_id(rs.getInt("POST_ID"));  // 게시물 ID 설정
                favorite.setPost_title(rs.getString("TITLE"));  // 게시물 제목 설정
                favorite.setAuthor(rs.getString("AUTHOR"));  // 게시물 작성자 설정
                favorites.add(favorite);  // 리스트에 추가
            }
        } catch (SQLException e) {
            e.printStackTrace();  // 예외 발생 시 로그 출력
        }
        return favorites;  // 관심 상품 리스트 반환
    }

    // 관리자의 사용자 관심 상품 목록 조회 메서드
    public List<FavoriteVo> getFavoritesList(String userid) {
        List<FavoriteVo> favorites = new ArrayList<>();
        String sql = "SELECT f.USER_ID, f.POST_ID, p.TITLE, p.USER_ID AS AUTHOR "
                     + "FROM FAVORITE f "
                     + "JOIN POST p ON f.POST_ID = p.POST_ID "
                     + "WHERE f.USER_ID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userid);  // 사용자 ID 바인딩
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                FavoriteVo favorite = new FavoriteVo();
                favorite.setUser_id(rs.getString("USER_ID"));  // 사용자 ID 설정
                favorite.setPost_id(rs.getInt("POST_ID"));  // 게시물 ID 설정
                favorite.setPost_title(rs.getString("TITLE"));  // 게시물 제목 설정
                favorite.setAuthor(rs.getString("AUTHOR"));  // 게시물 작성자 설정
                favorites.add(favorite);  // 리스트에 추가
            }
        } catch (SQLException e) {
            e.printStackTrace();  // 예외 발생 시 로그 출력
        }
        return favorites;  // 관심 상품 리스트 반환
    }

    // 관심 상품 삭제 메서드
    public boolean deleteFavorite(String userId, int postId) {
        String sql = "DELETE FROM FAVORITE WHERE USER_ID = ? AND POST_ID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);  // 사용자 ID 바인딩
            pstmt.setInt(2, postId);  // 게시물 ID 바인딩
            int rowsAffected = pstmt.executeUpdate();  // 삭제된 행 수 반환
            return rowsAffected > 0;  // 삭제 성공 여부 반환
        } catch (SQLException e) {
            e.printStackTrace();  // 예외 발생 시 로그 출력
            return false;  // 삭제 실패 시 false 반환
        }
    }

    // 특정 사용자가 특정 게시글을 즐겨찾기 했는지 확인하는 메서드
    public boolean isFavorite(String userId, int postId) {
        String sql = "SELECT COUNT(*) FROM FAVORITE WHERE USER_ID = ? AND POST_ID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);  // 사용자 ID 바인딩
            pstmt.setInt(2, postId);  // 게시물 ID 바인딩
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;  // 해당 게시글이 관심 상품에 있는지 여부 반환
            }
        } catch (SQLException e) {
            e.printStackTrace();  // 예외 발생 시 로그 출력
        }
        return false;  // 관심 상품에 없을 경우 false 반환
    }

    // 특정 게시물을 찜한 사람의 수를 확인하는 메서드
    public int countFavoritesForPost(int postId) {
        String sql = "SELECT COUNT(DISTINCT USER_ID) FROM FAVORITE WHERE POST_ID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, postId);  // 게시물 ID 바인딩
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);  // 찜한 사람의 수 반환
            }
        } catch (SQLException e) {
            e.printStackTrace();  // 예외 발생 시 로그 출력
        }
        return 0;  // 찜한 사람이 없을 경우 0 반환
    }
}
