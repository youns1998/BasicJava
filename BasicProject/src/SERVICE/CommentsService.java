package SERVICE;

import java.util.List;

import DAO.CommentDAO;
import VO.CommentsVo;
import VO.PostVo;

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
    public CommentsVo selectCommentById(int commentId) {
        return commentDAO.selectCommentById(commentId);
    }

    public int addComment(CommentsVo comment) {
        return commentDAO.insertComment(comment);
    }
 // 회원 ID를 받아서 해당 ID가 쓴 게시물을 다 불러오는 서비스
	public List<CommentsVo> getComment(String userid) {
        return commentDAO.userCommentsList(userid);
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