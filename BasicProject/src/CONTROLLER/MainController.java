package CONTROLLER;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import CONTROLLER.*;
import SERVICE.UsersService;
import UTIL.*;
import VO.UsersVo;



public class MainController {

<<<<<<< HEAD
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
=======
    public static Map<String, Object> sessionMap = new HashMap<>(); // 사용자 세션 데이터 저장
>>>>>>> branch 'main' of https://github.com/youns1998/BasicJava

    // 컨트롤러 인스턴스 변수 선언
    private UsersController usersController;
    private CategoryController categoryController;
    private CommentController commentController;
    private FavoriteController favoriteController;
    private HistoryController historyController;
    private PostController postController;
    
    // 생성자에서 컨트롤러 인스턴스 초기화
    public MainController() {
        usersController = UsersController.getInstance();
        categoryController = CategoryController.getInstance();
        commentController = CommentController.getInstance();
        favoriteController = FavoriteController.getInstance();
        historyController = HistoryController.getInstance();
        postController = PostController.getInstance();
    }
    
    // 애플리케이션 시작 메서드
    public static void main(String[] args) {
        new MainController().start(); // 메인 컨트롤러 시작
    }
    
    // 애플리케이션 메인 루프
    public void start() {
        Command cmd = Command.HOME; // 초기 명령어는 HOME

        while(true) {
            switch(cmd) {
                case HOME: // 초기 화면
                    cmd = home();
                    break;
                case LOGIN: // 로그인 기능
                    cmd = usersController.login();
                    break;
                case JOIN: // 회원가입 기능
                    cmd = usersController.join();
                    break;
                case MYPAGE: // 마이페이지 기능
                    cmd = usersController.myPage();
                    break;
                case UESR_LIST: // 관리자용 회원 목록 조회
                    cmd = usersController.userList();
                    break;
                case ADMIN_USER: // 관리자용 회원 상세 조회
                    cmd = usersController.userSelect();
                    break;
                case ADMIN_USERDETAIL: // 관리자용 상세 회원 관리 메뉴
                    cmd = usersController.userdetail();
                    break;
                case USER_UPDATE: // 사용자 정보 수정 기능
                    cmd = usersController.userUpdate();
                    break;
                case USER_DELETE: // 사용자 계정 삭제 기능
                    cmd = usersController.userDelete();
                    break;
                case S_ID: // 아이디 찾기 기능
                    cmd = usersController.findUserId();
                    break;
                case S_PW: // 비밀번호 찾기 기능
                    cmd = usersController.findUserPass();
                    break;
                case USER_HOME: // 사용자 홈 화면
                    cmd = userHome();
                    break;
                case ADMIN_HOME: // 관리자 홈 화면
                    cmd = admin_home();
                    break;
                case POST_DELETE: // 게시글 삭제
                    cmd = postController.postDelete();
                    break;
                case POST_INSERT: // 게시글 추가
                    cmd = postController.postInsert();
                    break;
                case POST_LIST: // 게시글 목록
                    cmd = postController.postList();
                    break;
                case POST_UPDATE: // 게시글 수정
                    cmd = postController.postUpdate();
                    break;
                case POST_DETAIL: // 게시글 상세보기
                    Integer currentPostId = (Integer) MainController.sessionMap.get("currentPostId");
                    if (currentPostId != null) {
                        cmd = postController.detailPost(currentPostId); // 이미 보고 있는 게시글로 이동
                    } else {
                        cmd = postController.detailPost(); // 글 번호 입력받아 게시글 보기
                    }
                    break;
                case CATEGORY_LIST: // 카테고리 목록 보기
                    cmd = categoryController.categoryList();
                    break;
                case CATEGORY_INSERT: // 카테고리 추가
                    cmd = categoryController.categoryInsert();
                    break;
                case CATEGORY_UPDATE: // 카테고리 수정
                    cmd = categoryController.categoryUpdate();
                    break;
                case CATEGORY_DELETE: // 카테고리 삭제
                    cmd = categoryController.categoryDelete();
                    break;
                case FAVORITE_LIST: // 관심 상품 목록 보기
                    cmd = favoriteController.displayMenu();
                    break;
                case FAVORITE_INSERT: // 관심 상품 등록
                    Integer postId = (Integer) MainController.sessionMap.get("currentPostId");
                    if (postId != null) {
                        cmd = favoriteController.addFavorite(postId); // 현재 보고 있는 게시물 ID 사용
                    } else {
                        System.out.println("현재 선택된 게시물이 없습니다.");
                        cmd = Command.POST_LIST; // 기본 게시물 목록으로 돌아가기
                    }
                    break;
                case END: // 프로그램 종료
                    System.out.println("프로그램이 종료되었습니다.");
                    return;
                default: // 잘못된 명령 처리
                    System.out.println("잘못된 선택입니다. 홈 화면으로 돌아갑니다.");
                    cmd = Command.HOME;
            }
        }
    }

<<<<<<< HEAD
//				 관심 물품 보기
				case FAVORITE_LIST: cmd = favoriteController.viewFavorites(); break;
				case FAVORITE_DELETE: cmd = favoriteController.displayMenu(); break;
				case USER_FAVORITE: cmd = favoriteController.viewFavorites(); break;
				case FAVORITE_INSERT:
				    Integer postId = (Integer) MainController.sessionMap.get("currentPostId");
				    if (postId != null) {
				        cmd = favoriteController.addFavorite(postId); // 현재 게시물의 ID를 전달
				    } else {
				        System.out.println("현재 선택된 게시물이 없습니다.");
				        cmd = Command.POST_LIST; // 기본 게시물 목록으로 돌아가기
				    }
				    break;
=======
    // 초기 화면 출력 및 명령 선택
    private Command home() {
        System.out.println("##############################################");
        System.out.println("#     땅 콩 마 켓 에 오 신 것 을 환 영 합 니 다     # ");
        System.out.println("##############################################");
>>>>>>> branch 'main' of https://github.com/youns1998/BasicJava

        System.out.println("1.로그인  2.회원가입  3.ID찾기  4.비밀번호찾기 0.나가기 ");
        System.out.println("----------------------------------------------");
        System.out.println();
        int input = ScanUtil.nextInt("메뉴 선택 >>");
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
    
    // 일반 사용자 홈 화면
    public Command userHome() {
        UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser");
        if (loginUserVo == null) {
            return Command.HOME;
        }
        if (loginUserVo.getRole() != 0) {
            return Command.ADMIN_HOME;
        }
        System.out.println(loginUserVo.getUsername() + "님 반가워요");
        System.out.println("아래 메뉴에서 작업할 번호를 선택하세요.");
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("1.중고장터보기\t2.내정보보기\t3.관심상품보기\t4.나의거래내역\t0.로그아웃");
        System.out.println("-----------------------------------------------------------------------");
        int input = ScanUtil.nextInt("메뉴 선택 >> ");
        
        switch (input) {
            case 1: return Command.POST_LIST;
            case 2: return Command.MYPAGE;
            case 3: return Command.FAVORITE_LIST;
            case 0:
                MainController.sessionMap.clear(); // 로그아웃: 세션 데이터 삭제
                return Command.HOME;
        }
        return Command.USER_HOME;
    }

    // 관리자 홈 화면
    public Command admin_home() {
        UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser");
        if (loginUserVo == null) {
            return Command.HOME;
        }
        System.out.println(loginUserVo.getUsername() + "님은 관리자 메뉴를 이용할 수 있습니다");
        System.out.println("아래 메뉴에서 작업할 번호를 선택하세요.");
        System.out.println("-------------------------------------------------------------");
        System.out.println("1.회원정보관리\t2.중고장터관리\t3.댓글관리\t4.카테고리관리\t0.로그아웃");
        System.out.println("-------------------------------------------------------------");
        int input = ScanUtil.nextInt("메뉴 선택 >> ");
        
        switch (input) {
            case 1: return Command.UESR_LIST;
            case 2: return Command.POST_LIST;
            case 4: return Command.CATEGORY_LIST;
            case 0:
                MainController.sessionMap.clear(); // 로그아웃: 세션 데이터 삭제
                return Command.HOME;
        }
        return Command.USER_HOME;
    }
}
