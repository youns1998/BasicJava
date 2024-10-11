package CONTROLLER;

import java.util.HashMap;
import java.util.Map;

import UTIL.Command;
import UTIL.ScanUtil;
import VO.UserVO;


public class MainController {
	//public static UserVO sessionLoginUser;   // 로그인 한 회원 정보가 저장될 변수(로그아웃되면 null이 저장됨)
	
	// 전체 프로젝트에서 사용해야할 자료를 저장할 Map 객체 (key 값은 데이터를 구별할 수 있는 문자열, value 값은 저장할 데이터)
	// 예) 로그인한 회원 정보 
	public static Map<String, Object> sessionMap = new HashMap<>();   
	
	private UserController userController;
	private BoardController boardController;
	
	public MainController() {
		userController = UserController.getInstance();
		boardController = BoardController.getInstance();
	}

	public static void main(String[] args) {
		new MainController().start();
	}
	
	public void start() {
		Command cmd = Command.HOME;
		
		while(true){
			switch (cmd) {
				case HOME: cmd = home(); break;
				case USER_HOME: cmd = userHome(); break;
				
				case LOGIN: cmd = userController.login(); break;
				case JOIN: cmd = userController.join(); break;
				case MYPAGE: cmd = userController.myPage(); break;
				
				// 상세조회, 등록, 수정, 삭제.
				case BOARD_LIST: cmd = boardController.boardList(); break;
				case BOARD_INSERT: cmd = boardController.boardInsertForm(); break;
				case BOARD_VIEW: cmd = boardController.boardView(); break;
				case BOARD_UPDATE: cmd = boardController.boardUpdate(); break;
				case BOARD_DELETE: cmd = boardController.boardDelete(); break;
				
				case END: 
						System.out.println("프로그램이 종료되었습니다.");
						return;
				default : cmd = Command.HOME;
			}
		}
	}
	
	private Command home() {
		System.out.println("-------------------------------------------------------------");
		System.out.println("\t1. 로그인\t2. 회원가입\t0. 프로그램 종료");
		System.out.println("-------------------------------------------------------------");
		System.out.print("메뉴 선택 >> ");
		
		int input = ScanUtil.nextInt();
		
		switch (input) {
			case 1: return Command.LOGIN;
			case 2: return Command.JOIN;
			case 0: return Command.END;
		}
		return Command.HOME;
	}
	
	private Command userHome() {
		UserVO loginUserVo = (UserVO)MainController.sessionMap.get("loginUser");
		if(loginUserVo==null) {
			return Command.HOME;
		}
		System.out.println();
		System.out.println(loginUserVo.getUser_name() + "님 반가워요");
		System.out.println("아래 메뉴에서 작업할 번호를 선택하세요.");
		System.out.println();
		System.out.println("-------------------------------------------------------------");
		System.out.println("\t1. 내 정보 수정\t2. 게시글 목록 보기\t0. 로그아웃");
		System.out.println("-------------------------------------------------------------");
		int input = ScanUtil.nextInt("메뉴 선택 >> ");
		
		switch (input) {
			case 1: return Command.MYPAGE;
			case 2: return Command.BOARD_LIST;
			case 0:
				// ProjectMain의 정적변수인 sessionMap에 저장된 모든 자료를 삭제한다.
				MainController.sessionMap.clear();;
				return Command.HOME;
		}
		return Command.USER_HOME;
	}
	
	

}
