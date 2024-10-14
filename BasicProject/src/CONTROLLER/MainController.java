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
				
				// 로그인 후
				case USER_HOME: cmd = usersController.userHome(); break;
				
				// 게시글 관리
				case POST_DELETE: cmd = postController.postDelete(); break;
				case POST_INSERT: cmd = postController.postInsert(); break;
				case POST_LIST: cmd = postController.postList(); break;
				case POST_UPDATE: cmd = postController.postUpdate(); break;
				
				// 댓글 관리
				case COMMENT_DELETE: cmd = commentController.commentDelete(); break;
				case COMMENT_INSERT: cmd = commentController.commentInsert(); break;
				case COMMENT_LIST: cmd = commentController.commentList(); break;
				case COMMENT_UPDATE: cmd = commentController.commentUpdate(); break;
				
				// 카테고리 보기
				case CATEGORY_LIST: cmd = categoryController.categoryList(); break;
				
				// 관심 물품 보기
				case FAVORITE_LIST: cmd = favoriteController.favoriteList(); break;
				
				// 거래 기록 보기
				case HISTORY_LIST: cmd = historyController.historyList(); break;
				
				case END:
					System.out.println("프로그램이 종료되었습니다.");
					return;
				default:
					System.out.println("잘못된 선택입니다. 홈 화면으로 돌아갑니다.");
					cmd = Command.HOME;
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
			default:
				System.out.println("잘못된 입력입니다. 다시 선택해 주세요.");
				return Command.HOME;
		}
	}
}
