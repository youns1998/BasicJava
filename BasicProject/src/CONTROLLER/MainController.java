package CONTROLLER;
import CONTROLLER.*;
import UTIL.*;


public class MainController {
	
	private UsersController usersController;
	private CategoryController categoryController;
	private CommentController commentController;
	private FavoriteController favoriteController;
	private HistoryController historyController;
	private PostController postController;
	
	public MainController() {
		usersController = UsersController.getInstance();
		categoryController = CategoryController.getInstance();
		commentController = CommentController.getInstance();
		favoriteController = FavoriteController.getInstance();
		historyController = HistoryController.getInstance();
		postController = PostController.getInstance();
	}
	public static void main(String[] args) {
		new MainController().start();
	}
	
	public void start() {
		Command cmd = Command.HOME;
		
		while(true) {
			
			switch(cmd) {
			case HOME: cmd = home(); break;
			
			case LOGIN: cmd = usersController.login(); break;
			case JOIN: cmd = usersController.join(); break;
			case MYPAGE: cmd = usersController.myPage(); break;
			//로그인 후
			case USER_HOME: cmd = usersController.userHome(); break;
			
			//작성글
			case POST_DELETE: cmd = postController.postDelete();break;
			case POST_INSERT: cmd = postController.postInsert();break;
			case POST_LIST: cmd = postController.postList();break;
			case POST_UPDATE: cmd = postController.postUpdate();break;
				
			//댓글
			case COMMENT_DELETE:
			case COMMENT_INSERT:
			case COMMENT_LIST:
			case COMMENT_UPDATE:
				
				
			//카테고리
			case CATEGORY_LIST:
			
			
			
			//관심물품
			case FAVORITE_LIST:
			
			//거래 기록
			case HISTORY_LIST:
			
			
			
			
			
			
			case END:
				System.out.println("프로그램이 종료되었습니다. ");
				return;
			default : cmd = Command.HOME;
			}
			
		}
	}
	
	private Command home() {
		System.out.println("-----------------------------------");
		System.out.println("         중 고 나 라          ");
		System.out.println("-----------------------------------");
		System.out.println("\t1. 로그인\t2. 회원가입\t0. 프로그램 종료");
		int input = ScanUtil.nextInt("메뉴 선택 >>");
		switch (input) {
		case 1: return Command.LOGIN;
		case 2: return Command.JOIN;
		case 0: return Command.END;
		}
		return Command.HOME;

	}
}