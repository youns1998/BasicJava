package SERVICE;

import java.util.List;

import DAO.CommentDAO;
import VO.CommentsVo;

public class CommentsService {
    private static CommentsService instance;
    private CommentDAO commentDAO = CommentDAO.getInstance();

    private CommentsService() {
    }

    public static CommentsService getInstance() {
        if (instance == null)
            instance = new CommentsService();
        return instance;
    }

    public int addComment(CommentsVo comment) {
        return commentDAO.insertComment(comment);
    }

    public List<CommentsVo> getComments(int postId) {
        return commentDAO.selectCommentsByPostId(postId);
    }

    public CommentsVo getComment(int commentId) {
        return commentDAO.selectCommentById(commentId);
    }

    public int updateComment(CommentsVo comment) {
        return commentDAO.updateComment(comment);
    }

    public int deleteComment(int commentId) {
        return commentDAO.deleteComment(commentId);
    }
    public int getCommentCount(int postId) {
        return commentDAO.getCommentCount(postId);
    }
}