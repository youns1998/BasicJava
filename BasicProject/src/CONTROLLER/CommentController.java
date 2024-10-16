package CONTROLLER;

import java.time.LocalDateTime;
import java.util.List;
import SERVICE.*;
import UTIL.*;
import VO.*;

public class CommentController {
    private static CommentController instance;

    private CommentController() {}

    public static CommentController getInstance() {
        if (instance == null)
            instance = new CommentController();
        return instance;
    }

    // 댓글 작성 메서드
    public Command insertComment(int postId) {
        String commentText = ScanUtil.nextLine("댓글을 입력하세요: ");
        
        // 세션에서 로그인한 사용자 정보 가져오기
        UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser");

        if (loginUserVo == null) {
            System.out.println("로그인이 필요합니다.");
            return Command.USER_HOME; // 로그인이 안 된 상태라면 홈으로 돌아가기
        }

        CommentsVo comment = new CommentsVo();
        comment.setPost_id(postId);
        comment.setUser_id(loginUserVo.getUser_id()); // 로그인한 사용자의 ID 사용
        comment.setContent(commentText);
        comment.setCreated_at(LocalDateTime.now()); // 댓글 작성 시간 설정

        CommentsService commentService = CommentsService.getInstance();
        int result = commentService.addComment(comment);

        if (result > 0) {
            System.out.println("댓글이 성공적으로 추가되었습니다.");
        } else {
            System.out.println("댓글 추가에 실패했습니다.");
        }
        return Command.POST_LIST;
    }


    // 댓글 조회 메서드
    public Command viewComments(int postId) {
        List<CommentsVo> comments = CommentsService.getInstance().getComments(postId);

        if (comments.isEmpty()) {
            System.out.println("댓글이 없습니다.");
        } else {
            System.out.println("==== 댓글 목록 ====");
            for (CommentsVo comment : comments) {
                System.out.println("댓글 번호: " + comment.getComment_id() +
                                   ", 작성자: " + comment.getUser_id() +
                                   ", 내용: " + comment.getContent() +
                                   ", 작성 시간: " + comment.getCreated_at());
            }
        }
        return Command.POST_LIST;
    }

    // 댓글 수정 메서드
    public Command updateComment(int postId) {
        int commentId = ScanUtil.nextInt("수정할 댓글 번호를 입력하세요: ");
        ScanUtil.nextLine(); // 버퍼 정리

        CommentsVo comment = CommentsService.getInstance().getComment(commentId);
        if (comment == null || comment.getPost_id() != postId) {
            System.out.println("해당 댓글을 찾을 수 없습니다.");
            return Command.POST_LIST;
        }

        // 세션에서 로그인한 사용자 정보 가져오기
        UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser");
        if (loginUserVo == null || !comment.getUser_id().equals(loginUserVo.getUser_id())) {
            System.out.println("다른 사용자의 댓글은 수정할 수 없습니다.");
            return Command.POST_LIST;
        }

        String newCommentText = ScanUtil.nextLine("새 댓글 내용을 입력하세요: ");
        comment.setContent(newCommentText);

        int result = CommentsService.getInstance().updateComment(comment);
        if (result > 0) {
            System.out.println("댓글이 성공적으로 수정되었습니다.");
        } else {
            System.out.println("댓글 수정에 실패했습니다.");
        }
        return Command.POST_LIST;
    }

    // 댓글 삭제 메서드
    public Command deleteComment(int postId) {
        int commentId = ScanUtil.nextInt("삭제할 댓글 번호를 입력하세요: ");
        CommentsVo comment = CommentsService.getInstance().getComment(commentId);

        if (comment == null || comment.getPost_id() != postId) {
            System.out.println("해당 댓글을 찾을 수 없습니다.");
            return Command.POST_LIST;
        }

        // 세션에서 로그인한 사용자 정보 가져오기
        UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser");
        if (loginUserVo == null || !comment.getUser_id().equals(loginUserVo.getUser_id())) {
            System.out.println("다른 사용자의 댓글은 삭제할 수 없습니다.");
            return Command.POST_LIST;
        }

        int result = CommentsService.getInstance().deleteComment(commentId);
        if (result > 0) {
            System.out.println("댓글이 성공적으로 삭제되었습니다.");
        } else {
            System.out.println("댓글 삭제에 실패했습니다.");
        }
        return Command.POST_LIST;
    }

}
