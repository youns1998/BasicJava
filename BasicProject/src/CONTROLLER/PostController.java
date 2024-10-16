package CONTROLLER;

import java.util.ArrayList;
import java.util.List;

import SERVICE.CommentsService;
import SERVICE.PostService;
import SERVICE.UsersService;
import UTIL.Command;
import UTIL.ScanUtil;
import VO.PostVo;
import VO.UsersVo;


////작성글
//case POST_DELETE: cmd = postController
//case POST_INSERT:
//case POST_LIST:
//case POST_UPDATE:

public class PostController {
	
	private static final int TITLE_MAX_LEN = 10;
	private static final int AUTHOR_MAX_LEN = 5;
	private static final int STATUS_MAX_LEN = 5;
	private static PostController instance;
    private CommentController commentController = CommentController.getInstance(); // CommentController 인스턴스 생성

	private PostController() {

	}

	public static PostController getInstance() {
		if (instance == null)
			instance = new PostController();
		return instance;
	}
	//상세 게시글 보기
	public Command detailPost() {
		System.out.println("=====================================================================");
		PostService postService = PostService.getInstance(); 
		
		 int choice = ScanUtil.nextInt("보고싶은 글 번호를 입력하세요: ");
		    PostVo selectedPost = postService.getPost(choice);

		    if (selectedPost == null) {
		        System.out.println("해당 게시글이 존재하지 않습니다.");
		        return Command.USER_HOME;
		    }
		    // 선택한 게시글의 상세정보 출력
		    displayPostDetails(selectedPost);
		    return commentMenu(selectedPost.getPost_id());
	}
	
	
	
	
	
	// 댓글 메뉴 메서드
	private Command commentMenu(int postId) {
	    System.out.println("1. 댓글 달기 2. 댓글 보기 3. 댓글 수정 4. 댓글 삭제 5. 찜하기 0. 전체 게시물 보러가기");
	    int choice = ScanUtil.nextInt();

	    switch (choice) {
        case 1: return commentController.insertComment(postId); // 댓글 달기
        case 2: return commentController.viewComments(postId); // 댓글 보기
        case 3: return commentController.updateComment(postId); // 댓글 수정
        case 4: return commentController.deleteComment(postId); // 댓글 삭제
        case 5: return Command.FAVORITE_INSERT; // 찜하기
        case 0: return Command.POST_LIST; // 전체 게시물 보기
        default: 
            System.out.println("잘못된 선택입니다. 다시 시도하세요.");
            return commentMenu(postId); // 선택이 잘못되면 다시 메뉴 호출
    }
	}
	
	
	
	
	
	private Command viewComments(int postId) {
	    // List<CommentVo> comments = commentService.getComments(postId);
	    return Command.POST_LIST; 
	}
	private Command updateComment(int postId) {
	    return Command.POST_LIST; 
	}
	private Command deleteComment(int postId) {
	    return Command.POST_LIST; 
	}
	
	//게시물 상세보기
	 private void displayPostDetails(PostVo post) {
		  CommentsService commentsService = CommentsService.getInstance();
		  int commentCount = commentsService.getCommentCount(post.getPost_id());
		  System.out.println("◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆");
		  System.out.print("작성자:" + post.getUser_id() +"  \t");
		  System.out.print("제목:" + post.getTitle()+" \t");
		  System.out.print("가격:" + post.getPrice()+" \t");
		  System.out.println("상태:" + post.getCondition()     );
		  System.out.println("＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊");
		  System.out.println("내용:" + post.getContent()+" \n");
		  System.out.println("＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊");
		  System.out.println("작성 시간:"+post.getCreated_at());
		  System.out.println("수정 시간:");
		  System.out.println("＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊");
		  System.out.print("댓글 : 	" + commentCount + "					찜한 사람 수: \n" );
		  //여기에 댓글 개수 + 찜한 사람의 수 그리고 작성시간과 수정시간을 넣어야함
		  System.out.println("◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆");
	}
	//전체 게시글 보기
	// ANSI 색상 코드 설정

	

	
	// 아스키 아트 박스 출력 함수 (마지막 닫는 라인을 출력하지 않음)
	 private void printAsciiArtBox(String content, boolean isLast) {
	     int width = 80; // 가로 너비 설정 (박스 내부 포함하여 고정)
	     String borderLine = "+" + "-".repeat(width - 2) + "+"; // 박스의 상단과 하단 라인
	     System.out.println(borderLine);
	     System.out.printf("| %-"+ (width - 3) +"s\n", content); // 박스 내용 출력 (마지막 | 생략)
	     if (!isLast) { // 마지막 박스가 아닌 경우에만 닫는 선을 출력
	         System.out.println(borderLine);
	     }
	 }

	// 덜 진한 빨간색 설정
	 private static final String ANSI_LIGHT_RED = "\033[38;5;203m"; // 덜한 빨간색
	 private static final String ANSI_BOLD = "\033[1m";
	 private static final String ANSI_RESET = "\033[0m";

	 public Command postList() {
	     int width = 80;
	     System.out.println("+" + "=".repeat(width - 2) + "+"); // 상단 경계선
	     
	     PostService postService = PostService.getInstance();
	     UsersService usersService = UsersService.getInstance();
	     UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser");
	     List<PostVo> posts = postService.getPostList();

	     if (posts == null || posts.isEmpty()) {
	         System.out.println("작성된 게시물이 없습니다");
	     } else {
	         List<PostVo> adminPosts = new ArrayList<>();
	         List<PostVo> userPosts = new ArrayList<>();

	         for (PostVo post : posts) {
	             UsersVo user = usersService.getUserById(post.getUser_id());
	             if (user != null && user.getRole() == 1) {
	                 adminPosts.add(post);
	             } else {
	                 userPosts.add(post);
	             }
	         }

	         // 공지사항 출력 (눈에 띄게)
	         for (PostVo post : adminPosts) {
	             String content = "# 공지사항: " + post.getTitle() + " #";
	             System.out.println(ANSI_LIGHT_RED + ANSI_BOLD + content + ANSI_RESET);
	         }

	         for (int i = 0; i < userPosts.size(); i++) {
	             PostVo post = userPosts.get(i);
	             String title = padRight(truncate(post.getTitle(), 15), 20);
	             String author = padRight(truncate(post.getUser_id(), 5), 6);
	             String status = padRight(truncate(post.getCondition(), 10), 10);
	             String content = String.format(
	                 "%-2d | 제목: %-20s | 가격: %-5s | 작성자: %-6s | 상태: %-10s", 
	                 post.getPost_id(),
	                 title, 
	                 post.getPrice(),
	                 author,
	                 status
	             );
	             printAsciiArtBox(content, i == userPosts.size() - 1);
	         }
	     }
	     
	     System.out.println("+" + "=".repeat(width - 2) + "+"); // 하단 경계선

	     if (loginUserVo.getRole() != 0) {
	         int input = ScanUtil.nextInt("1.공지 작성 2.글 삭제 3.수정 4.상세보기 0.관리자 화면으로 >> ");
	         switch (input) {
	             case 1:
	                 return Command.POST_INSERT;
	             case 2:
	                 return Command.POST_DELETE;
	             case 3:
	                 return Command.POST_UPDATE;
	             case 4:
	                 return Command.POST_DETAIL;
	             case 0:
	                 return Command.USER_HOME;
	         }
	     } else {
	         int input = ScanUtil.nextInt("1.판매 글 작성 2. 게시물 삭제 3. 게시물 수정 4.상세 보기 0.내 화면으로 >> ");
	         switch (input) {
	             case 1:
	                 return Command.POST_INSERT;
	             case 2:
	                 return Command.POST_DELETE;
	             case 3:
	                 return Command.POST_UPDATE;
	             case 4:
	                 return Command.POST_DETAIL;
	             case 0:
	                 return Command.USER_HOME;
	         }
	     }
	     return Command.USER_HOME;
	 }

	 // 문자열 길이를 제한하고, 초과하면 ... 추가
	 private String truncate(String text, int maxLength) {
	     return text.length() > maxLength ? text.substring(0, maxLength - 3) + "..." : text;
	 }

	 // 한글과 영문 모두 정렬을 맞추기 위해 패딩을 추가하는 함수
	 private String padRight(String text, int length) {
	     int textLength = text.codePoints().map(cp -> Character.isAlphabetic(cp) ? 1 : 2).sum();
	     int padSize = length - textLength;
	     return text + " ".repeat(Math.max(0, padSize));
	 }












	
	//게시글 추가 메서드
	public Command postInsert() {	
		PostService postService = PostService.getInstance();
		PostVo post = new PostVo();
		UsersVo loginUserVo = (UsersVo)MainController.sessionMap.get("loginUser");
		if (loginUserVo.getRole() != 0) {		// 관리자의 공지 추가
	        System.out.println("공지사항 쓰기");
	        String title = ScanUtil.nextLine("제목 >> ");
	        String content = ScanUtil.nextLine("내용 >> ");
	        
	        post.setTitle(title);
	        post.setContent(content);
	        post.setUser_id(loginUserVo.getUser_id()); // 관리자 ID 설정
		}else {						//사용자의 게시글 추가
		String Title = ScanUtil.nextLine("글 제목 >> ");
		int price = ScanUtil.nextInt("가격 >> ");
		
		// 여기에 카테고리 전체리스트 보여줘야 밑에서 고를 수 있음
		// ex) 남성 의류 >> 101 여성 의류 >> 102
		
		int category = ScanUtil.nextInt("카테고리 >> ");
		String content = ScanUtil.nextLine("글 내용 입력 >> ");
			post.setTitle(Title);
		    post.setPrice(price);
		    post.setCategory_id(category);
		    post.setContent(content);
		    post.setCondition("판매중");
		    post.setUser_id(loginUserVo.getUser_id());
		}
		    int result = postService.insertPost(post); // 게시글 추가 메서드 호출
		    if (result > 0) {
		        System.out.println("게시글이 성공적으로 등록되었습니다.");
		    } else {
		        System.out.println("게시글 등록에 실패했습니다.");
		    }
		return Command.USER_HOME;
		}
	
	//게시글 수정 메서드
	public Command postUpdate() {		
		UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser");
	    int choice = ScanUtil.nextInt("수정할 내 글 번호를 입력하세요: ");
	    PostService postService = PostService.getInstance();
	    
	    // 글 상세보기로 들어가기 (선택한 게시물을 가져옴)
	    PostVo post = postService.getPost(choice); // 선택한 게시물 가져오기
	    if (post == null) {
	        System.out.println("해당 게시물을 찾을 수 없습니다.");
	        return Command.POST_LIST; 
	    }
	    // 사용자 권한 확인
	    if (post.getUser_id() != loginUserVo.getUser_id() && loginUserVo.getRole() == 0) {
	        System.out.println("다른 사용자의 글은 수정할 수 없습니다.");
	        return Command.POST_LIST;
	    }
	    postService.updatePostSelect(post); // updatePostMenu를 호출하여 수정 진행

	    return Command.USER_HOME; // 수정 완료 후 사용자 홈으로 돌아감
	}
	
	//게시글 삭제 메서드
	public Command postDelete() {
	    UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser");
	    int choice = ScanUtil.nextInt("삭제할 글 번호를 입력하세요: ");
	    PostService postService = PostService.getInstance();
	    
	    // 글 상세보기로 들어가기 (선택한 게시물을 가져옴)
	    PostVo post = postService.getPost(choice); // 선택한 게시물 가져오기
	    if (post == null) {
	        System.out.println("해당 게시물을 찾을 수 없습니다.");
	        return Command.POST_LIST; // 게시판 목록으로 돌아가기
	    }

	    // 사용자 권한 확인
	    if (post.getUser_id() != loginUserVo.getUser_id() && loginUserVo.getRole() == 0) {
	        System.out.println("다른 사용자의 글은 삭제할 수 없습니다.");
	        return Command.POST_LIST;
	    }

	    return Command.USER_HOME;
	}
}



