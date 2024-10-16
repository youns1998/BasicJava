package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import UTIL.DBUtil;
import VO.CommentsVo;

public class CommentDAO {
	private static CommentDAO instance;

	private CommentDAO() {
	}

	public static CommentDAO getInstance() {
		if (instance == null)
			instance = new CommentDAO();
		return instance;
	}

	public int insertComment(CommentsVo comment) {
	    String sql = "INSERT INTO comments (comment_id, post_id, user_id, content, created_at) VALUES (comment_seq.NEXTVAL, ?, ?, ?, ?)";
	    try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, comment.getPost_id());
	        pstmt.setString(2, comment.getUser_id());
	        pstmt.setString(3, comment.getContent());
	        pstmt.setTimestamp(4, Timestamp.valueOf(comment.getCreated_at()));
	        return pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return 0;
	}
	 public int getCommentCount(int postId) {
	        String sql = "SELECT COUNT(*) FROM comments WHERE post_id = ?";
	        try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            pstmt.setInt(1, postId);
	            try (ResultSet rs = pstmt.executeQuery()) {
	                if (rs.next()) {
	                    return rs.getInt(1);
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return 0;
	    }


	public List<CommentsVo> selectCommentsByPostId(int postId) {
		String sql = "SELECT * FROM comments WHERE post_id = ?";
		List<CommentsVo> commentsList = new ArrayList<>();
		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, postId);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					CommentsVo comment = new CommentsVo();
					comment.setComment_id(rs.getInt("comment_id"));
					comment.setPost_id(rs.getInt("post_id"));
					comment.setUser_id(rs.getString("user_id"));
					comment.setContent(rs.getString("content"));
					comment.setCreated_at(rs.getTimestamp("created_at").toLocalDateTime());
					commentsList.add(comment);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return commentsList;
	}

	public CommentsVo selectCommentById(int commentId) {
		String sql = "SELECT * FROM comments WHERE comment_id = ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, commentId);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					CommentsVo comment = new CommentsVo();
					comment.setComment_id(rs.getInt("comment_id"));
					comment.setPost_id(rs.getInt("post_id"));
					comment.setUser_id(rs.getString("user_id"));
					comment.setContent(rs.getString("content"));
					comment.setCreated_at(rs.getTimestamp("created_at").toLocalDateTime());
					return comment;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public int updateComment(CommentsVo comment) {
		String sql = "UPDATE comments SET content = ? WHERE comment_id = ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, comment.getContent());
			pstmt.setInt(2, comment.getComment_id());
			return pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int deleteComment(int commentId) {
		String sql = "DELETE FROM comments WHERE comment_id = ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, commentId);
			return pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
