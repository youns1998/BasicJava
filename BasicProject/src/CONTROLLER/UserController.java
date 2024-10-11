package kr.or.ddit.controller;

import kr.or.ddit.service.UserServiceImpl;
import kr.or.ddit.util.Command;
import kr.or.ddit.util.ScanUtil;
import kr.or.ddit.vo.UserVO;

public class UserController {
	private UserServiceImpl userService;
	
	private static UserController controller;
	
	private UserController() {
		userService = UserServiceImpl.getInstance();
	}
	
	public static UserController getInstance() {
		if(controller==null) controller = new UserController();
		return controller;
	}
	
	public Command join(){
		System.out.println("=========== 회원가입 =============");
		System.out.print("아이디 >> ");
		String userId = ScanUtil.nextLine();
		System.out.print("비밀번호 >> ");
		String password = ScanUtil.nextLine();
		System.out.print("이름 >> ");
		String userName = ScanUtil.nextLine();
		
		//아이디 중복 확인 생략
		//비밀번호 확인 생략
		//정규표현식(유효성 검사) 생략
		
		UserVO userVo = new UserVO();
		userVo.setUser_id(userId);
		userVo.setUser_pass(password);
		userVo.setUser_name(userName);
		
		
		int result = userService.insertUser(userVo);
		
		if(0 < result){
			System.out.println("회원가입 성공");
		}else{
			System.out.println("회원가입 실패");
		}
		
		return Command.HOME;
	}
	
	public Command login(){
		System.out.println("============== 로그인 ===============");
		System.out.print("아이디 >> ");
		String userId = ScanUtil.nextLine();
		System.out.print("비밀번호 >> ");
		String password = ScanUtil.nextLine();
		
		UserVO userVo = new UserVO();
		userVo.setUser_id(userId);
		userVo.setUser_pass(password);
		
		UserVO loginUserVo = userService.getUser(userVo);
		
		if(loginUserVo == null){
			System.out.println("아이디 혹은 비밀번호를 잘못 입력하셨습니다.");
		}else{
			System.out.println("로그인 성공입니다...");
			// ProjectMain 클래스의 정적변수인 sessionMap에 로그인 정보를 저장한다.
//			ProjectMain.sessionLoginUser = loginUser;
			MainController.sessionMap.put("loginUser", loginUserVo);
			
			return Command.USER_HOME;
		}
		return Command.LOGIN;
	}
	
	public Command myPage() {
		System.out.println("============== MY PAGE ====================");
		
		return Command.USER_HOME;
	}

}
