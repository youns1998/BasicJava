package CONTROLLER;

import java.util.List;

import SERVICE.PostService;
import UTIL.Command;
import UTIL.ScanUtil;
import VO.PostVo;


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
		List<PostVo> posts = postService.getPostList(); 
		if(posts==null) {
			System.out.println("작성된 게시물이 없습니다");
		}
		 for (PostVo post : posts) {
		        System.out.println("ID: " + post.getPost_id() + ", 제목: " + post.getTitle() + ", 작성자: " + post.getUsername());
		    }
		 System.out.println("=====================================================================");
		int input = ScanUtil.nextInt("1.글 작성 2.글 삭제 3.수정 0.내화면으로 >> ");

		switch (input) {
		case 1:
			return Command.POST_INSERT;
		case 2:
			return Command.POST_DELETE;
		case 4:
			return Command.POST_UPDATE;
		case 0:
			return Command.USER_HOME;
			
		}
		return Command.USER_HOME;

	}
	public Command postInsert() {
		System.out.println("글 쓰기");
		int input = ScanUtil.nextInt("1.삭제 2.수정 0.게시판 목록으로 >> ");

		switch (input) {
		case 1:
			return Command.POST_DELETE;
		case 3:
			return Command.POST_UPDATE;
		case 0:
			return Command.POST_LIST;
			
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