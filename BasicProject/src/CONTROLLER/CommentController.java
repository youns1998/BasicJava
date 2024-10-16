package CONTROLLER;

import java.time.LocalDateTime;
import java.util.List;
import SERVICE.*;
import UTIL.*;
import VO.*;

public class CommentController {
    private static CommentController instance;

    private Command returnToPostDetail(int postId) {
        PostController postController = PostController.getInstance();
        postController.detailPost(postId);  // 현재 보던 글의 postId로 상세 보기를 호출
        return Command.POST_DETAIL;
    }

    private CommentController() {}

    public static CommentController getInstance() {
        if (instance == null)
            instance = new CommentController();
        return instance;
    }

    // 댓글 작성 메서드
    
    public Command insertComment(int postId) {
        String commentText = ScanUtil.nextLine("댓글을 입력하세요: ");
        UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser");

        if (loginUserVo == null) {
            System.out.println("로그인이 필요합니다.");
            return Command.USER_HOME;
        }

        CommentsVo comment = new CommentsVo();
        comment.setPost_id(postId);
        comment.setUser_id(loginUserVo.getUser_id());
        comment.setContent(commentText);
        comment.setCreated_at(LocalDateTime.now());

        int result = CommentsService.getInstance().addComment(comment);

        if (result > 0) {
            System.out.println("댓글이 성공적으로 추가되었습니다.");
        } else {
            System.out.println("댓글 추가에 실패했습니다.");
        }
        
        return returnToPostDetail(postId); // 댓글 추가 후 상세 보기로 돌아가기
    }

    public Command updateComment(int postId) {
        int commentId = ScanUtil.nextInt("수정할 댓글 번호를 입력하세요: ");
        ScanUtil.nextLine();

        CommentsVo comment = CommentsService.getInstance().getComment(commentId);
        if (comment == null || comment.getPost_id() != postId) {
            System.out.println("해당 댓글을 찾을 수 없습니다.");
            return returnToPostDetail(postId);
        }

        UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser");
        if (loginUserVo == null || !comment.getUser_id().equals(loginUserVo.getUser_id())) {
            System.out.println("다른 사용자의 댓글은 수정할 수 없습니다.");
            return returnToPostDetail(postId);
        }

        String newCommentText = ScanUtil.nextLine("새 댓글 내용을 입력하세요: ");
        comment.setContent(newCommentText);

        int result = CommentsService.getInstance().updateComment(comment);
        System.out.println(result > 0 ? "댓글이 성공적으로 수정되었습니다." : "댓글 수정에 실패했습니다.");

        return returnToPostDetail(postId); // 댓글 수정 후 상세 보기로 돌아가기
    }

    public Command deleteComment(int postId) {
        int commentId = ScanUtil.nextInt("삭제할 댓글 번호를 입력하세요: ");
        CommentsVo comment = CommentsService.getInstance().getComment(commentId);

        if (comment == null || comment.getPost_id() != postId) {
            System.out.println("해당 댓글을 찾을 수 없습니다.");
            return returnToPostDetail(postId);
        }

        UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser");
        if (loginUserVo == null || !comment.getUser_id().equals(loginUserVo.getUser_id())) {
            System.out.println("다른 사용자의 댓글은 삭제할 수 없습니다.");
            return returnToPostDetail(postId);
        }

        int result = CommentsService.getInstance().deleteComment(commentId);
        System.out.println(result > 0 ? "댓글이 성공적으로 삭제되었습니다." : "댓글 삭제에 실패했습니다.");

        return returnToPostDetail(postId); // 댓글 삭제 후 상세 보기로 돌아가기
    }

}
