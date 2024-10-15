package CONTROLLER;
import java.util.HashMap;
import java.util.Map;
import CONTROLLER.*;
import UTIL.*;
import VO.UsersVo;


public class MainController {
	
	public static Map<String, Object> sessionMap = new HashMap<>();   

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
				case ADMIN_HOME: cmd = admin_home(); break;
				case LOGIN: cmd = usersController.login(); break;
				case JOIN: cmd = usersController.join(); break;
				case MYPAGE: cmd = usersController.myPage(); break;
				
				// 로그인 후
				case USER_HOME: cmd = userHome(); break;
				
//				// 게시글 관리
//				case POST_DELETE: cmd = postController.postDelete(); break;
//				case POST_INSERT: cmd = postController.postInsert(); break;
				case POST_LIST: cmd = postController.postList(); break;
//				case POST_UPDATE: cmd = postController.postUpdate(); break;
//				
//				// 댓글 관리
//				case COMMENT_DELETE: cmd = commentController.commentDelete(); break;
//				case COMMENT_INSERT: cmd = commentController.commentInsert(); break;
//				case COMMENT_LIST: cmd = commentController.commentList(); break;
//				case COMMENT_UPDATE: cmd = commentController.commentUpdate(); break;
//				
//				// 카테고리 보기
//				case CATEGORY_LIST: cmd = categoryController.categoryList(); break;
//				
//				 관심 물품 보기
//				case FAVORITE_LIST: cmd = favoriteController.favoriteList(); break;
//				
//				// 거래 기록 보기
//				case HISTORY_LIST: cmd = historyController.historyList(); break;
				
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
		System.out.println("########################################");
		System.out.println("# 땅 콩 마 켓 에 오 신 것 을 환 영 합 니 다   # ");
		System.out.println("########################################");

		System.out.println("1.로그인  2.회원가입  3.ID찾기  4.비밀번호찾기 0.나가기 ");
		int input = ScanUtil.nextInt("메뉴 선택 >>");
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
		if(loginUserVo.getRole()==1)
			return Command.ADMIN_HOME;
		System.out.println();
		System.out.print(loginUserVo.getUsername() + "님 반가워요");
		System.out.println("\t 메인 페이지입니다");
		System.out.println("아래 메뉴에서 작업할 번호를 선택하세요.");
		System.out.println("-----------------------------------------------------------------------");
		System.out.println("1.게시판보기\t2.내정보보기\t3.찜한상품보기\t4.거래내역\t0.로그아웃");
		System.out.println("-----------------------------------------------------------------------");
		int input = ScanUtil.nextInt("메뉴 선택 >> ");
		
		switch (input) {
			case 1: return Command.POST_LIST;
			case 2: return Command.MYPAGE;
			case 0:
				// ProjectMain의 정적변수인 sessionMap에 저장된 모든 자료를 삭제한다.
				MainController.sessionMap.clear();;
				return Command.HOME;
		}
		return Command.USER_HOME;
		
	}
	public Command admin_home() {
		UsersVo loginUserVo = (UsersVo)MainController.sessionMap.get("loginUser");
		if(loginUserVo==null) {
			return Command.HOME;
		}
		System.out.println("관리자 페이지");
		System.out.println(loginUserVo.getUsername() + "님은 관리자 메뉴를 이용할 수 있습니다");
		System.out.println("아래 메뉴에서 작업할 번호를 선택하세요.");
		System.out.println("-------------------------------------------------------------");
		System.out.println("1.회원정보관리\t2.게시판 관리\t3.댓글관리\t.4카테고리관리\t0.로그아웃");
		System.out.println("-------------------------------------------------------------");
		int input = ScanUtil.nextInt("메뉴 선택 >> ");
		
		switch (input) {
			case 1: return Command.POST_LIST;
			case 2: return Command.MYPAGE;
//			case 3: return Command.MYPAGE;
//			case 3: return Command.MYPAGE;
			case 0:
				// ProjectMain의 정적변수인 sessionMap에 저장된 모든 자료를 삭제한다.
				MainController.sessionMap.clear();;
				return Command.HOME;
		}
		return Command.USER_HOME;
		
	}
}
