package CONTROLLER;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import SERVICE.*;
import UTIL.*;
import VO.*;

public class CommentController {
    private static CommentController instance;

    // 게시물 상세보기로 돌아가는 메서드 (postId를 인자로 받음)
    private Command returnToPostDetail(int postId) {
        PostController postController = PostController.getInstance(); // PostController 인스턴스 가져오기
        postController.detailPost(postId);  // 현재 보던 글의 postId로 상세 보기를 호출
        return Command.USER_HOME;

    }

    // 싱글톤 패턴을 적용한 생성자
    private CommentController() {}

    // 싱글톤 인스턴스를 반환하는 메서드
    public static CommentController getInstance() {
        if (instance == null)
            instance = new CommentController();
        return instance;
    }

    // 사용자의 내가 쓴 댓글 목록을 보는 메서드
    public Command CommentList() {
        CommentsService commentservice = CommentsService.getInstance(); // CommentsService 인스턴스 가져오기
        UsersService usersService = UsersService.getInstance(); // UsersService 인스턴스 가져오기
        UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser"); // 로그인한 사용자 정보 가져오기
        List<CommentsVo> comvo = commentservice.getComment(loginUserVo.getUser_id()); // 로그인한 사용자가 쓴 댓글 목록 가져오기
        
        // 관리자가 로그인했을 경우 관리자용 댓글 관리 화면으로 이동
        if (loginUserVo.getRole() != 0) {
            return Command.COMMENT_ADMIN;
        }

        // 작성된 댓글이 없는 경우
        if (comvo.isEmpty()) {
            System.out.println("작성된 댓글이 없습니다.");
        } else {
        	 // 댓글 목록을 게시물 번호(post_id)로 정렬
            comvo.sort(Comparator.comparing(CommentsVo::getPost_id));
            
            // 댓글이 있을 경우, 댓글 리스트 출력
            for (CommentsVo cvo : comvo) {
                System.out.print("게시물 번호: " + cvo.getPost_id() + "\t");
                System.out.println("댓글 내용: " + cvo.getContent());
                System.out.println();
                System.out.println("작성일: " + cvo.getCreated_at());
                System.out.println("------------------------------");
            }
        }
        return Command.USER_SELF; // 사용자 화면으로 돌아감
    }

    // 관리자가 회원 ID로 댓글을 조회하는 메서드
    public Command adminCommentList() {
        CommentsService commentservice = CommentsService.getInstance(); // CommentsService 인스턴스 가져오기
        UsersService usersService = UsersService.getInstance(); // UsersService 인스턴스 가져오기
        UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser"); // 로그인한 사용자 정보 가져오기
        
        // 전체 사용자 목록 출력
        System.out.println();
        System.out.println("==============================전체 유저 리스트==============================");
        List<UsersVo> users = usersService.getPostList(); // 전체 사용자 목록 불러오기
        for (UsersVo user : users)
            System.out.println("ID: " + user.getUser_id());
        System.out.println("=========================================================================");
        
        // 댓글 리스트를 조회할 사용자 ID 입력받기
        String userId = ScanUtil.nextLine("댓글 리스트를 조회할 회원 ID>>");
        UsersVo user = usersService.getUserSelect(userId); // 해당 사용자 정보 가져오기
        
        // 입력한 사용자 ID가 없을 경우
        if (user == null) {
            int choice = ScanUtil.nextInt("등록된 회원이 아닙니다 \n1. 다시 조회 0.뒤로 가기 >>");
            if(choice==1) {return Command.COMMENT_ADMIN;} // 댓글 조회 창으로 다시 감}
            else {return Command.USER_LIST;} // 댓글 조회 창으로 다시 감
        }

        // 해당 사용자의 댓글 목록 가져오기
        List<CommentsVo> comvo = commentservice.getComment(userId);
        
        // 댓글이 없을 경우
        if (comvo.isEmpty()) {
            System.out.println("작성된 댓글이 없습니다.");
        } else {
        	
            comvo.sort(Comparator.comparing(CommentsVo::getPost_id));

            // 댓글이 있을 경우, 댓글 리스트 출력
            for (CommentsVo cvo : comvo) {
                System.out.print("게시물 번호: " + cvo.getPost_id() + "\t");
                System.out.println("댓글 내용: " + cvo.getContent());
                System.out.println();
                System.out.println("작성일: " + cvo.getCreated_at());
                System.out.println("------------------------------");
            }
        }
        return Command.USER_LIST; // 관리자의 유저리스트 화면으로 돌아감
    }

    // 댓글 작성 메서드
    public Command insertComment(int postId) {
        // 사용자로부터 댓글 내용 입력받기
        String commentText = ScanUtil.nextLine("댓글을 입력하세요: ");
        UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser"); // 로그인한 사용자 정보 가져오기

        // 새로운 댓글 객체 생성 및 데이터 설정
        CommentsVo comment = new CommentsVo();
        comment.setPost_id(postId);
        comment.setUser_id(loginUserVo.getUser_id());
        comment.setContent(commentText);
        comment.setCreated_at(LocalDateTime.now());

        // 댓글 추가 서비스 호출
        int result = CommentsService.getInstance().addComment(comment);
        
        // 결과에 따른 메시지 출력
        if (result > 0) {
            System.out.println("댓글이 성공적으로 추가되었습니다.");
        } else {
            System.out.println("댓글 추가에 실패했습니다.");
        }
        return returnToPostDetail(postId); // 댓글 추가 후 해당 게시물 상세 보기로 돌아감
    }

    // 댓글 수정 메서드
    public Command updateComment(int postId) {
        // 수정할 댓글 번호 입력받기
	 int commentId = ScanUtil.nextInt("수정할 댓글 번호를 입력하세요 >> ");
        // 입력한 댓글 번호에 해당하는 댓글 가져오기
        CommentsVo comment = CommentsService.getInstance().getComment(commentId);
        
        // 해당 댓글이 없거나, 현재 게시물의 댓글이 아닌 경우
        if (comment == null || comment.getPost_id() != postId) {
            System.out.println("해당 댓글을 찾을 수 없습니다.");
            return returnToPostDetail(postId); // 해당 게시물 상세 보기로 돌아감
        }

        // 로그인한 사용자 정보 가져오기
        UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser");
        
        if (loginUserVo.getRole() != 0 || comment.getUser_id().equals(loginUserVo.getUser_id())) {
            String newCommentText = ScanUtil.nextLine("새 댓글 내용을 입력하세요: ");
            comment.setContent(newCommentText);
            
            int result = CommentsService.getInstance().updateComment(comment);
            System.out.println(result > 0 ? "댓글이 성공적으로 수정되었습니다." : "댓글 수정에 실패했습니다.");
        } else {
            System.out.println("다른 사용자의 댓글은 수정할 수 없습니다.");
        }

        return returnToPostDetail(postId); // 댓글 수정 후 해당 게시물 상세 보기로 돌아감
    }

    // 댓글 삭제 메서드
    public Command deleteComment(int postId) {
        // 삭제할 댓글 번호 입력받기
        int commentId = ScanUtil.nextInt("삭제할 댓글 번호를 입력하세요: ");
        
        // 입력한 댓글 번호에 해당하는 댓글 가져오기
        CommentsVo comment = CommentsService.getInstance().getComment(commentId);

        // 해당 댓글이 없거나, 현재 게시물의 댓글이 아닌 경우
        if (comment == null || comment.getPost_id() != postId) {
            System.out.println("해당 댓글을 찾을 수 없습니다.");
            return returnToPostDetail(postId); // 해당 게시물 상세 보기로 돌아감
        }

        // 로그인한 사용자 정보 가져오기
        UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser");
        
        // 로그인한 사용자와 댓글 작성자가 일치하지 않는 경우
        if (loginUserVo == null || !comment.getUser_id().equals(loginUserVo.getUser_id())) {
            System.out.println("다른 사용자의 댓글은 삭제할 수 없습니다.");
            return returnToPostDetail(postId); // 해당 게시물 상세 보기로 돌아감
        }

        // 댓글 삭제 서비스 호출
        int result = CommentsService.getInstance().deleteComment(commentId);
        System.out.println(result > 0 ? "댓글이 성공적으로 삭제되었습니다." : "댓글 삭제에 실패했습니다.");

        return returnToPostDetail(postId); // 댓글 삭제 후 해당 게시물 상세 보기로 돌아감
    }
    
}
