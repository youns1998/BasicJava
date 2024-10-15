package CONTROLLER;

import java.util.ArrayList;
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
	
	public Command postList() {
		System.out.println("전체 게시물 ");
		System.out.println("=====================================================================");
		PostService postService = PostService.getInstance(); 
		
		UsersVo loginUserVo = (UsersVo)MainController.sessionMap.get("loginUser");
		List<PostVo> posts = postService.getPostList(); 
		
		if (posts == null || posts.isEmpty()) {
	        System.out.println("작성된 게시물이 없습니다");
	    } else {
	        // 공지사항과 일반 게시물 분리
	        List<PostVo> notices = new ArrayList<>();
	        List<PostVo> regularPosts = new ArrayList<>();

	        for (PostVo post : posts) {
	            if (post.getisNotice()) { // 공지사항 여부에 따라 분리
	            	notices.add(post);
	            } else {
	                regularPosts.add(post);
	            }
	        }

	        // 공지사항 출력
	        for (PostVo post : notices) {
	            System.out.println("공 지 사 항" +
	                ",  제목:" + post.getTitle() + 
	                ",  작성자: 관리자"
	                // 댓글 달린 갯수도 표시 해줘야함
	            );
	        }

	        // 일반 게시물 출력
	        for (PostVo post : regularPosts) {
	            System.out.println(
	                "게시물 번호:" + post.getPost_id() + 
	                ",  제목:" + post.getTitle() + 
	                ",  가격:" + post.getPrice() + 
	                ",  분류:" + post.getCategory_id() +
	                ",  작성자:" + post.getUser_id() + 
	                ",  상태:" + post.getCondition()
	                // 댓글 달린 갯수도 표시 해줘야함
	            );
	        }
	    }
	    
		 System.out.println("=====================================================================");
		 if(loginUserVo.getRole()!=0 ) {
			 int input = ScanUtil.nextInt("1.공지 작성 2.글 삭제 3.수정 4.상세보기 0.관리자 화면으로 >> ");

				switch (input) {
				case 1:
					return Command.POST_INSERT;
				case 2:
					return Command.POST_DELETE;
				case 3:
					return Command.POST_UPDATE;
				case 4:
					return Command.POST_UPDATE;
				case 0:
					return Command.USER_HOME;
					
				}	 
		 }
		 else {
		int input = ScanUtil.nextInt("1.판매글 작성 2.내 글 삭제 3.내 글 수정 4.상세보기 0.내 화면으로 >> ");

		switch (input) {
		case 1:
			return Command.POST_INSERT;
		case 2:
			return Command.POST_DELETE;
		case 3:
			return Command.POST_UPDATE;
		case 4:
			return Command.POST_UPDATE;
		case 0:
			return Command.USER_HOME;
			
		}
		 }
		 return Command.USER_HOME;
	}
	public Command postInsert() {
		PostService postService = PostService.getInstance();
		PostVo post = new PostVo();
		UsersVo loginUserVo = (UsersVo)MainController.sessionMap.get("loginUser");
		if(loginUserVo.getRole() != 0) {
			String Title = ScanUtil.nextLine("공지 제목>>");
			String content = ScanUtil.nextLine("공지 내용 입력 >> ");	
			post.setTitle(Title);
			post.setContent(content);
			post.setUser_id("관리자");
			
		}else {
		System.out.println("글 쓰기");
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
	
	
	public Command postUpdate() {
		System.out.println("수정할 것을 선택하세요");
		int input = ScanUtil.nextInt("1.제목 2.내용 3.찜(Y/N)  0.게시판 목록으로 >> ");

		switch (input) {
		case 1:
			return Command.POST_DELETE;
		case 2:
			return Command.POST_INSERT;
		case 3:
			return Command.POST_LIST;
		case 4:
			return Command.POST_UPDATE;
		case 0:
			return Command.USER_HOME;
			
		}
		return Command.USER_HOME;
		
	}
	public Command postDelete() {
		System.out.println("내 글 중 삭제할 게시물 번호를 입력하세요");
		int input = ScanUtil.nextInt();

		switch (input) {
		case 1:
			return Command.POST_DELETE;
		case 2:
			return Command.POST_INSERT;
		case 3:
			return Command.POST_LIST;
		case 4:
			return Command.POST_UPDATE;
		case 0:
			return Command.USER_HOME;
			
		}
		return Command.USER_HOME;
		
	}
}