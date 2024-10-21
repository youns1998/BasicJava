package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import UTIL.DBUtil;
import VO.CommentsVo;

public class CommentDAO {
    private static CommentDAO instance;  // 싱글톤 패턴을 위한 인스턴스 변수

    // 기본 생성자 - 외부에서 인스턴스를 생성하지 못하도록 private 설정
    private CommentDAO() {}

    // 싱글톤 패턴을 적용하여 단일 인스턴스 반환
    public static CommentDAO getInstance() {
        if (instance == null)
            instance = new CommentDAO();  // 인스턴스가 없을 경우 생성
        return instance;  // 이미 존재하는 인스턴스를 반환
    }
    
    // 댓글 추가 메서드
    public int insertComment(CommentsVo comment) {
        String sql = "INSERT INTO comments (comment_id, post_id, user_id, content, created_at) VALUES (comment_seq.NEXTVAL, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, comment.getPost_id());  // 게시글 ID 바인딩
            pstmt.setString(2, comment.getUser_id());  // 사용자 ID 바인딩
            pstmt.setString(3, comment.getContent());  // 댓글 내용 바인딩
            pstmt.setTimestamp(4, Timestamp.valueOf(comment.getCreated_at()));  // 작성 시간 바인딩
            return pstmt.executeUpdate();  // 쿼리 실행 후 영향받은 행 수 반환
        } catch (SQLException e) {
            e.printStackTrace();  // 예외 발생 시 오류 메시지 출력
        }
        return 0;  // 실패 시 0 반환
    }

    // 특정 게시물의 댓글 수 조회 메서드
    public int getCommentCount(int postId) {
        String sql = "SELECT COUNT(*) FROM comments WHERE post_id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, postId);  // 게시글 ID 바인딩
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);  // 댓글 수 반환
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  // 예외 발생 시 오류 메시지 출력
        }
        return 0;  // 댓글이 없을 경우 0 반환
    }

    // 특정 게시글의 댓글 목록 조회 메서드
    public List<CommentsVo> selectCommentsByPostId(int postId) {
        String sql = "SELECT * FROM comments WHERE post_id = ?";
        List<CommentsVo> commentsList = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, postId);  // 게시글 ID 바인딩
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    CommentsVo comment = new CommentsVo();
                    comment.setComment_id(rs.getInt("comment_id"));  // 댓글 ID 설정
                    comment.setPost_id(rs.getInt("post_id"));  // 게시글 ID 설정
                    comment.setUser_id(rs.getString("user_id"));  // 사용자 ID 설정
                    comment.setContent(rs.getString("content"));  // 댓글 내용 설정
                    comment.setCreated_at(rs.getTimestamp("created_at").toLocalDateTime());  // 작성 시간 설정
                    commentsList.add(comment);  // 댓글 리스트에 추가
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  // 예외 발생 시 오류 메시지 출력
        }
        return commentsList;  // 댓글 리스트 반환
    }

    // 특정 사용자가 작성한 댓글 목록 조회 메서드
    public List<CommentsVo> userCommentsList(String userId) {
        String sql = "SELECT * FROM comments WHERE USER_ID = ?";
        List<CommentsVo> commentsList = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);  // 사용자 ID 바인딩
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    CommentsVo comment = new CommentsVo();
                    comment.setPost_id(rs.getInt("post_id"));  // 게시글 ID 설정
                    comment.setContent(rs.getString("content"));  // 댓글 내용 설정
                    comment.setCreated_at(rs.getTimestamp("created_at").toLocalDateTime());  // 작성 시간 설정
                    commentsList.add(comment);  // 댓글 리스트에 추가
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  // 예외 발생 시 오류 메시지 출력
        }
        return commentsList;  // 댓글 리스트 반환
    }

    // 댓글 ID로 특정 댓글 조회 메서드
    public CommentsVo selectCommentById(int commentId) {
        String sql = "SELECT * FROM comments WHERE comment_id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, commentId);  // 댓글 ID 바인딩
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    CommentsVo comment = new CommentsVo();
                    comment.setComment_id(rs.getInt("comment_id"));  // 댓글 ID 설정
                    comment.setPost_id(rs.getInt("post_id"));  // 게시글 ID 설정
                    comment.setUser_id(rs.getString("user_id"));  // 사용자 ID 설정
                    comment.setContent(rs.getString("content"));  // 댓글 내용 설정
                    comment.setCreated_at(rs.getTimestamp("created_at").toLocalDateTime());  // 작성 시간 설정
                    return comment;  // 조회된 댓글 반환
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  // 예외 발생 시 오류 메시지 출력
        }
        return null;  // 댓글이 없을 경우 null 반환
    }

    // 댓글 수정 메서드
    public int updateComment(CommentsVo comment) {
        String sql = "UPDATE comments SET content = ? WHERE comment_id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, comment.getContent());  // 수정된 댓글 내용 바인딩
            pstmt.setInt(2, comment.getComment_id());  // 댓글 ID 바인딩
            return pstmt.executeUpdate();  // 쿼리 실행 후 영향받은 행 수 반환
        } catch (SQLException e) {
            e.printStackTrace();  // 예외 발생 시 오류 메시지 출력
        }
        return 0;  // 실패 시 0 반환
    }

    // 댓글 삭제 메서드
    public int deleteComment(int commentId) {
        String sql = "DELETE FROM comments WHERE comment_id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, commentId);  // 댓글 ID 바인딩
            return pstmt.executeUpdate();  // 쿼리 실행 후 영향받은 행 수 반환
        } catch (SQLException e) {
            e.printStackTrace();  // 예외 발생 시 오류 메시지 출력
        }
        return 0;  // 실패 시 0 반환
    }
    
    //게시물이 삭제되면 댓글도 삭제되는 메서드 
    public void deleteCommentPost(int postId) {
    	System.out.println("댓글 삭제 요청: 게시글 ID = " + postId);
        String sql = "DELETE FROM comments WHERE post_id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, postId);  // 댓글 ID 바인딩
            int rowsAffected = pstmt.executeUpdate();  // 쿼리 실행 후 영향받은 행 수 반환
            System.out.println(rowsAffected + " 댓글이 삭제되었습니다.");  // 삭제된 댓글 수 확인
        } catch (SQLException e) {
            e.printStackTrace();  // 예외 발생 시 오류 메시지 출력
        }
    }
}
