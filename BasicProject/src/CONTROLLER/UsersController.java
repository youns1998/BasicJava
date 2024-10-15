package CONTROLLER;
import java.util.HashMap;
import java.util.Map;
import UTIL.*;
import VO.*;
import CONTROLLER.*;
import SERVICE.UsersService;

public class UsersController {
	private UsersService userService;

	private static UsersController instance;

	private UsersController() {
		userService = UsersService.getInstance();
	}

	public static UsersController getInstance() {
		if (instance == null)
			instance = new UsersController();
		return instance;
	}
	public Command join(){
		System.out.println("=========== 회원가입 =============");
		String userId = ScanUtil.nextLine("아이디 >> ");
		String password = ScanUtil.nextLine("비밀번호 >> ");
		String userName = ScanUtil.nextLine("이름 >> ");
		String email = ScanUtil.nextLine("이메일 >> ");
		String phone_number = ScanUtil.nextLine("전화번호 >> ");
		String address = ScanUtil.nextLine("주소 >> ");

		//아이디 중복 확인 생략
		//비밀번호 확인 생략
		//정규표현식(유효성 검사) 생략
		
		UsersVo userVo = new UsersVo();
		userVo.setUser_id(userId);
		userVo.setUser_pass(password);
		userVo.setUsername(userName);
		userVo.setAddress(address);
		userVo.setEmail(email);
		userVo.setPhone_number(phone_number);
		
		
		int result = userService.addUser(userVo);
		
		if(0 < result){
			System.out.println("회원가입 성공");
		}else{
			System.out.println("회원가입 실패");
		}
		
		return Command.HOME;
	}
	
	public Command login(){
		System.out.println("============== 로그인 ===============");
		String userId = ScanUtil.nextLine("아이디 >> ");
		String password = ScanUtil.nextLine("비밀번호 >> ");
		
		UsersVo userVo = new UsersVo();
		userVo.setUser_id(userId);
		userVo.setUser_pass(password);
		
		UsersVo loginUserVo = userService.getUser(userVo);
		
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
		UsersVo loginUser = (UsersVo) MainController.sessionMap.get("loginUser");
	    if (loginUser == null) {
	        System.out.println("로그인이 필요합니다.");
	        return Command.LOGIN;
	    }

	    // 마이페이지 출력
	    System.out.println("============== MY PAGE ====================");
	    System.out.println("아이디: " + loginUser.getUser_id());
	    System.out.println("이름: " + loginUser.getUsername());
	    System.out.println("이메일: " + loginUser.getEmail());
	    System.out.println("전화번호: " + loginUser.getPhone_number());

		return Command.USER_HOME;
	}

}