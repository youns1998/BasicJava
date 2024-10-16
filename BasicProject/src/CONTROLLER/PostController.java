package CONTROLLER;

import java.util.List;

import SERVICE.PostService;
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
	private static PostController instance;
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
	        case 1: return insertComment(postId); 
	        case 2: return viewComments(postId); 
	        case 3: return updateComment(postId); 
	        case 4: return deleteComment(postId); 
	        case 5: return Command.FAVORITE_INSERT;
	        case 0: return Command.POST_LIST;
	        default: System.out.println("잘못된 선택입니다. 다시 시도하세요.");
	            return commentMenu(postId); 
	    }
	}
	private Command insertComment(int postId) {
	    String comment = ScanUtil.nextLine("댓글을 입력하세요: ");
	 //  CommentService.addComment(postId, comment);
	    System.out.println("댓글이 추가되었습니다.");
	    return Command.POST_LIST; 
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
		  System.out.println("◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆");
		  System.out.print("작성자:" + post.getUser_id() +"  \t");
		  System.out.print("제목:" + post.getTitle()+" \t");
		  System.out.print("가격:" + post.getPrice()+" \t");
		  System.out.println("상태:" + post.getCondition()     );
		  System.out.println("＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊");
		  System.out.println("내용:" + post.getContent()+" \n");
		  System.out.println("＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊");
		  System.out.println("작성 시간:");
		  System.out.println("수정 시간:");
		  System.out.println("＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊");
		  System.out.print("댓글 : 						찜한 사람 수: \n" );
		  //여기에 댓글 개수 + 찜한 사람의 수 그리고 작성시간과 수정시간을 넣어야함
		  System.out.println("◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆");
	}
	//전체 게시글 보기
	public Command postList() {
		System.out.println("============================ 전체 게시물 ================================");
		PostService postService = PostService.getInstance(); 
		UsersVo loginUserVo = (UsersVo)MainController.sessionMap.get("loginUser");
		List<PostVo> posts = postService.getPostList(); 
		if (posts == null || posts.isEmpty()) {	//게시글 배열이 없으면
	        System.out.println("작성된 게시물이 없습니다");
	    } else {								//게시글 배열이 있다면
	        for (PostVo post : posts) {
	        	 System.out.println(
	 	                "게시물 번호:" + post.getPost_id() + 
	 	                ",  제목:" + post.getTitle() + 
	 	                ",  가격:" + post.getPrice() + 
	 	                ",  분류:" + post.getCategory_id() +
	 	                ",  작성자:" + post.getUser_id() + 
	 	                ",  상태:" + post.getCondition());
	 	                // 댓글 달린 갯수도 표시 해줘야함
	        }
	    }
		 System.out.println("======================================================================");
		 if(loginUserVo.getRole()!=0 ) {	//관리자가 보는 게시글 페이지
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
		 }
		 else {								//사용자가 보는 게시글 페이지
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



