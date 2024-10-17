package CONTROLLER;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import CONTROLLER.*;
import SERVICE.UsersService;
import UTIL.*;
import VO.UsersVo;



public class MainController {
	public static Map<String, Object> sessionMap = new HashMap<>();   
    public static final String ANSI_BROWN = "\033[38;5;136m";
    public static final String ANSI_RESET = "\u001B[0m";

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
				case UESR_LIST: cmd =usersController.userList(); break;
				case ADMIN_USER: cmd = usersController.userSelect(); break;
				case ADMIN_USERDETAIL: cmd = usersController.userdetail(); break;
				case USER_UPDATE: cmd =usersController.userUpdate(); break; //관리자용
				case USER_DELETE: cmd = usersController.userDelete(); break; //관리자용
				case USER_SELF: cmd = usersController.userSelf(); break;
				case USER_SELFUPDATE: cmd =usersController.userSelfUpdate(); break; //사용자용
				case USER_SELFDELETE: cmd = usersController.userSelfDelete(); break; //사용자용
				case S_ID: cmd = usersController.findUserId(); break;
				case S_PW: cmd = usersController.findUserPass(); break;
				// 로그인 후
				case USER_HOME: cmd = userHome(); break;
				case ADMIN_HOME: cmd = admin_home(); break;
				
				// 게시글 관리
				case POST_DELETE: cmd = postController.postDelete(); break;
				case POST_INSERT: cmd = postController.postInsert(); break;
				case POST_LIST: cmd = postController.postList(); break;
				case POST_UPDATE: cmd = postController.postUpdate(); break;
				case POST_DETAIL:
				    Integer currentPostId = (Integer) MainController.sessionMap.get("currentPostId");
				    if (currentPostId != null) {
				        cmd = postController.detailPost(currentPostId); // 이미 보고 있는 게시글 ID로 이동
				    } else {
				        cmd = postController.detailPost(); // 처음 접근 시 글 번호를 입력받아야 함
				    }
				    break;
				case POST_SELF: cmd = postController.userPost(); break; //내가 쓴 게시글 보기
				case POST_ADMIN: cmd = postController.adminPost(); break; //관리자가 회원이 쓴 게시글 보기
				
//				// 댓글 관리
//				case COMMENT_DELETE: cmd = commentController.commentDelete(); break;
				case COMMENT_SELF: cmd = commentController.CommentList(); break;	//내가 쓴 댓글 보기
				case COMMENT_ADMIN: cmd =commentController.adminCommentList(); break;	//관리자가 회원이 쓴 댓글 보기
//				case COMMENT_UPDATE: cmd = commentController.commentUpdate(); break;
//				// 카테고리 보기 
				case CATEGORY_LIST: cmd = categoryController.categoryList(); break;
				case CATEGORY_INSERT: cmd = categoryController.categoryInsert(); break;
				case CATEGORY_UPDATE: cmd = categoryController.categoryUpdate(); break;
				case CATEGORY_DELETE: cmd = categoryController.categoryDelete(); break;

//				 관심 물품 보기
				case FAVORITE_LIST: cmd = favoriteController.viewFavorites(); break;
				case FAVORITE_DELETE: cmd = favoriteController.displayMenu(); break;
				case USER_FAVORITE: cmd = favoriteController.adminviewFavorites(); break;
				case FAVORITE_INSERT:
				    Integer postId = (Integer) MainController.sessionMap.get("currentPostId");
				    if (postId != null) {
				        cmd = favoriteController.addFavorite(postId); // 현재 게시물의 ID를 전달
				    } else {
				        System.out.println("현재 선택된 게시물이 없습니다.");
				        cmd = Command.POST_LIST; // 기본 게시물 목록으로 돌아가기
				    }
				    break;

				// 거래 기록 보기
				case HISTORY_LIST: cmd = historyController.viewTransactionHistory(); break;
				
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
		 
		System.out.println(ANSI_BROWN+"            ■■■■■■■■    ■       ■  ");
        System.out.println("            ■       ■   ■■     ■■  ");
        System.out.println("            ■■■■■■■■    ■  ■ ■  ■  ");
        System.out.println("            ■           ■   ■   ■  ");
        System.out.println("            ■   eanut   ■       ■ arket" +ANSI_RESET);

	    System.out.println("------------------------------------------------");
	    System.out.println("  1.로그인  2.회원가입  3.ID찾기  4.비밀번호찾기  0.나가기 ");
	    System.out.println("------------------------------------------------");
	    System.out.println();
	    int input = ScanUtil.nextInt("선택 >> ");
	    System.out.println();
	    switch (input) {
	        case 1: return Command.LOGIN;
	        case 2: return Command.JOIN;
	        case 3: return Command.S_ID;
	        case 4: return Command.S_PW;
	        case 0: return Command.END;
	        default:
	            System.out.println("잘못된 입력입니다. 다시 선택해 주세요.");
	            return Command.HOME;
	    }
	}

		
	
	
	public Command userHome() {
		UsersVo loginUserVo = (UsersVo)MainController.sessionMap.get("loginUser");
		if(loginUserVo==null) {
			return Command.HOME;
		}
		if(loginUserVo.getRole()!=0)
			return Command.ADMIN_HOME;
		System.out.println();
		System.out.println(loginUserVo.getUsername() + "님 반가워요 ");
		System.out.println("╭◜◝ ͡ ◜◝╮    몽실   ╭◜◝ ͡ ◜◝╮\r\n"
				+ " ( •ㅅ•    ) 몽실몽실 (   •ㅅ•  )\r\n"
				+ " ╰◟◞ ͜ ╭◜◝ ͡ ◜◝╮몽실몽실 ͜ ◟◞╯\r\n"
				+ "  몽몽실(  •ㅅ•   ) 몽실\r\n"
				+ " 몽실몽 ╰◟◞ ◟◞╯몽실몽실");
		System.out.println("-----------------------------------------------------------------------");
		System.out.println("1.PM 장터 보기\t2.내 정보 보기\t3.찜한 상품 보기\t4.나의 거래 내역\t0.로그 아웃");
		System.out.println("-----------------------------------------------------------------------");
		int input = ScanUtil.nextInt("선택 >> ");
		
		switch (input) {
			case 1: return Command.POST_LIST;
			case 2: return Command.MYPAGE;
			case 3: return Command.FAVORITE_LIST;
			case 4: return Command.HISTORY_LIST;
			case 0:
				// ProjectMain의 정적변수인 sessionMap에 저장된 모든 자료를 삭제한다.
				MainController.sessionMap.clear();
				return Command.HOME;
		}
		return Command.USER_HOME;
		
	}
	public Command admin_home() {
		UsersVo loginUserVo = (UsersVo)MainController.sessionMap.get("loginUser");
		if(loginUserVo==null) {
			return Command.HOME;
		}
		System.out.println(loginUserVo.getUsername() + " 님이 관리자로 로그인 하셨습니다");
		System.out.println("╭◜◝ ͡ ◜◝╮    몽실   ╭◜◝ ͡ ◜◝╮\r\n"
				+ " ( •ㅅ•    ) 몽실몽실 (   •ㅅ•  )\r\n"
				+ " ╰◟◞ ͜ ╭◜◝ ͡ ◜◝╮몽실몽실 ͜ ◟◞╯\r\n"
				+ "  몽몽실(  •ㅅ•   ) 몽실\r\n"
				+ " 몽실몽 ╰◟◞ ◟◞╯몽실몽실");
		System.out.println("-------------------------------------------------------------");
		System.out.println("1.회원정보관리\t2.중고장터관리\t3.댓글관리\t4.카테고리관리\t0.로그아웃");
		System.out.println("-------------------------------------------------------------");
		int input = ScanUtil.nextInt("메뉴 선택 >> ");
		
		switch (input) {
			case 1: return Command.UESR_LIST;
			case 2: return Command.POST_LIST;
//			case 3: return Command.MYPAGE;
			case 4: return Command.CATEGORY_LIST;
//			case 3: return Command.MYPAGE;
			case 0:
				// ProjectMain의 정적변수인 sessionMap에 저장된 모든 자료를 삭제한다.
				MainController.sessionMap.clear();;
				return Command.HOME;
		}
		return Command.USER_HOME;
		
	}
}
