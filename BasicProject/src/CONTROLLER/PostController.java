package CONTROLLER;

import UTIL.Command;
import UTIL.ScanUtil;


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
		System.out.println("글 리스트 출력");
		int input = ScanUtil.nextInt("1 삭제 2 삽입 3 출력 4 업데이트 0 돌아가기>> ");

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
	public Command postInsert() {
		System.out.println("글 리스트 출력");
		int input = ScanUtil.nextInt("1 삭제 2 삽입 3 출력 4 업데이트 0 돌아가기>> ");

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
	public Command postUpdate() {
		System.out.println("글 리스트 출력");
		int input = ScanUtil.nextInt("1 삭제 2 삽입 3 출력 4 업데이트 0 돌아가기>> ");

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
		System.out.println("글 리스트 출력");
		int input = ScanUtil.nextInt("1 삭제 2 삽입 3 출력 4 업데이트 0 돌아가기>> ");

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