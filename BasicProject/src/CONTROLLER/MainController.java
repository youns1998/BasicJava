package CONTROLLER;

import java.util.HashMap;
import java.util.Map;

import UTIL.Command;
import UTIL.ScanUtil;
import VO.UsersVo;

public class MainController {
	// 세션 데이터를 저장할 Map 객체
	public static Map<String, Object> sessionMap = new HashMap<>();   

    // 색상 상수
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BROWN = "\033[38;5;136m";

	// 각각의 컨트롤러 객체 선언
	private UsersController usersController;
	private CategoryController categoryController;
	private CommentController commentController;
	private FavoriteController favoriteController;
	private HistoryController historyController;
	private PostController postController;
	
	// MainController 생성자 - 각 컨트롤러의 인스턴스 초기화
	public MainController() {
		usersController = UsersController.getInstance();
		categoryController = CategoryController.getInstance();
		commentController = CommentController.getInstance();
		favoriteController = FavoriteController.getInstance();
		historyController = HistoryController.getInstance();
		postController = PostController.getInstance();
	}
	
	// 프로그램 실행의 시작점
	public static void main(String[] args) {
		new MainController().start(); // 메인 컨트롤러 시작
	}
	
	// 프로그램의 주 실행 루프 - 사용자의 입력에 따라 각각의 기능 실행
	public void start() {
		Command cmd = Command.HOME; // 초기 상태는 HOME

		// 무한 루프 - 사용자의 입력에 따라 계속해서 명령을 처리
		while(true) {
			switch(cmd) {
				case HOME: cmd = home(); break; // HOME 화면 처리
				case LOGIN: cmd = usersController.login(); break; // 로그인
				case JOIN: cmd = usersController.join(); break; // 회원가입
				case MYPAGE: cmd = usersController.myPage(); break; // 마이페이지
				case USER_LIST: cmd =usersController.userList(); break; // 사용자 목록
				case ADMIN_USER: cmd = usersController.userSelect(); break; // 관리자용 사용자 선택
				case ADMIN_USERDETAIL: cmd = usersController.userdetail(); break; // 관리자용 사용자 상세
				case USER_UPDATE: cmd =usersController.userUpdate(); break; // 관리자용 사용자 수정
				case USER_DELETE: cmd = usersController.userDelete(); break; // 관리자용 사용자 삭제
				case USER_SELF: cmd = usersController.userSelf(); break; // 사용자 자신의 정보
				case USER_SELFUPDATE: cmd =usersController.userSelfUpdate(); break; // 사용자 자신의 정보 수정
				case USER_SELFDELETE: cmd = usersController.userSelfDelete(); break; // 사용자 자신의 계정 삭제
				case S_ID: cmd = usersController.findUserId(); break; // 사용자 ID 찾기
				case S_PW: cmd = usersController.findUserPass(); break; // 사용자 비밀번호 찾기
				
				// 로그인 후 사용자 홈 화면
				case USER_HOME: cmd = userHome(); break; 
				case ADMIN_HOME: cmd = admin_home(); break; // 관리자 홈 화면

				// 게시글 관리 관련 명령 처리
				case POST_DELETE: cmd = postController.postDelete(); break; // 게시글 삭제
				case POST_INSERT: cmd = postController.postInsert(); break; // 게시글 추가
				case POST_LIST: cmd = postController.postList(); break; // 게시글 목록
				case POST_UPDATE: cmd = postController.postUpdate(); break; // 게시글 수정
				case POST_DETAIL:
				    Integer currentPostId = (Integer) MainController.sessionMap.get("currentPostId");
				    if (currentPostId != null) {
				        cmd = postController.detailPost(currentPostId); // 이미 보고 있는 게시글 ID로 이동
				    } else {
				        cmd = postController.detailPost(); // 처음 접근 시 글 번호를 입력받음
				    }
				    break;
				case POST_SELF: cmd = postController.userPost(); break; // 내가 쓴 게시글 보기
				case POST_ADMIN: cmd = postController.adminPost(); break; // 관리자가 회원이 쓴 게시글 보기
				
				// 댓글 관리 관련 명령 처리
				case COMMENT_SELF: cmd = commentController.CommentList(); break; // 내가 쓴 댓글 보기
				case COMMENT_ADMIN: cmd = commentController.adminCommentList(); break; // 관리자가 댓글 관리
					
				// 카테고리 관련 명령 처리
				case CATEGORY_LIST: cmd = categoryController.categoryList(); break; // 카테고리 목록
				case CATEGORY_INSERT: cmd = categoryController.categoryInsert(); break; // 카테고리 추가
				case CATEGORY_UPDATE: cmd = categoryController.categoryUpdate(); break; // 카테고리 수정
				case CATEGORY_DELETE: cmd = categoryController.categoryDelete(); break; // 카테고리 삭제

				// 관심 상품 보기
				case FAVORITE_LIST: cmd = favoriteController.viewFavorites(); break; // 찜한 상품 목록 보기
				case FAVORITE_DELETE: cmd = favoriteController.displayMenu(); break; // 찜한 상품 삭제 메뉴
				case USER_FAVORITE: cmd = favoriteController.adminviewFavorites(); break; // 관리자가 회원 찜 목록 보기
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
				case HISTORY_LIST: cmd = historyController.viewTransactionHistory(); break; // 거래 기록 보기
				
				// 프로그램 종료
				case END:
					System.out.println("프로그램이 종료되었습니다.");
					return; // 종료
				default:
					// 잘못된 선택일 경우 홈 화면으로 돌아감
					System.out.println("잘못된 선택입니다. 홈 화면으로 돌아갑니다.");
					cmd = Command.HOME;
			}
		}
	}

	// 홈 화면 출력 및 입력 처리 메서드
	private Command home() {
		// 프로그램 로고 및 선택 메뉴 출력
		System.out.println(ANSI_BROWN+"            ■■■■■■■■    ■       ■  ");
        System.out.println("            ■       ■   ■■     ■■  ");
        System.out.println("            ■■■■■■■■    ■  ■ ■  ■  ");
        System.out.println("            ■           ■   ■   ■  ");
        System.out.println("            ■   eanut   ■       ■ arket" +ANSI_RESET);

	    System.out.println("------------------------------------------------");
	    System.out.println("  1.로그인  2.회원가입  3.ID찾기  4.비밀번호찾기  0.나가기 ");
	    System.out.println("------------------------------------------------");
	    System.out.println();
	    
	    // 사용자 입력 처리
	    int input = ScanUtil.nextInt("선택 >> ");
	    System.out.println();
	    
	    // 입력된 값에 따라 명령 반환
	    switch (input) {
	        case 1:  return Command.LOGIN ;
	        case 2: return Command.JOIN;
	        case 3: return Command.S_ID;
	        case 4: return Command.S_PW;
	        case 0: return Command.END;
	        default:
	            System.out.println("잘못된 입력입니다. 다시 선택해 주세요.");
	            return Command.HOME;
	    }
	}

	// 사용자 홈 화면 메서드
	public Command userHome() {
		// 세션에서 로그인한 사용자 정보 가져오기
		UsersVo loginUserVo = (UsersVo)MainController.sessionMap.get("loginUser");
		
		// 로그인되지 않았으면 홈으로 돌아가기
		if(loginUserVo == null) {
			return Command.HOME;
		}
		
		// 관리자일 경우 관리자 홈으로 이동
		if(loginUserVo.getRole() != 0)
			return Command.ADMIN_HOME;
		
		// 일반 사용자일 경우 사용자 환영 메시지 출력 및 메뉴 표시
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
		
		// 사용자 입력 처리
		int input = ScanUtil.nextInt("메뉴 선택 >> ");
		
		// 입력된 값에 따라 명령 반환
		switch (input) {
			case 1: return Command.POST_LIST;
			case 2: return Command.MYPAGE;
			case 3: return Command.FAVORITE_LIST;
			case 4: return Command.HISTORY_LIST;
			case 0:
				// 로그아웃 시 세션 데이터 초기화
				MainController.sessionMap.clear();
				return Command.HOME;
		}
		return Command.USER_HOME;
	}

	// 관리자 홈 화면 메서드
	public Command admin_home() {
		// 세션에서 로그인한 사용자 정보 가져오기
		UsersVo loginUserVo = (UsersVo)MainController.sessionMap.get("loginUser");
		
		// 로그인되지 않았으면 홈으로 돌아가기
		if(loginUserVo == null) {
			return Command.HOME;
		}
		
		// 관리자 환영 메시지 출력 및 메뉴 표시
		System.out.println(loginUserVo.getUsername() + " 님이 관리자로 로그인 하셨습니다");
		System.out.println("▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄\r\n"
				+ "█░░░░░░░░▀█▄▀▄▀██████░▀█▄▀▄▀██████░\r\n"
				+ "░░░░░░░░░░░▀█▄█▄███▀░░░ ▀██▄█▄███▀░\r\n"
				+ "");
		System.out.println("-------------------------------------------------------------");
		System.out.println("1.회원정보관리\t2.중고장터관리\t3.댓글관리\t4.카테고리관리\t0.로그아웃");
		System.out.println("-------------------------------------------------------------");
		
		// 사용자 입력 처리
		int input = ScanUtil.nextInt("메뉴 선택 >> ");
		
		// 입력된 값에 따라 명령 반환
		switch (input) {
			case 1: return Command.USER_LIST;
			case 2: return Command.POST_LIST;
			case 3: return Command.COMMENT_ADMIN;			// 관리자가 모든 댓글 조회
			case 4: return Command.CATEGORY_LIST;
			case 0:
				// 로그아웃 시 세션 데이터 초기화
				MainController.sessionMap.clear();;
				return Command.HOME;
		}
		return Command.USER_HOME;
	}
}
