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
			//거래 글
			//댓글

			//카테고리
			//관심물품
			//거래내역

			
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