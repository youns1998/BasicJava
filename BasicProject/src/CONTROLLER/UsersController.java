package CONTROLLER;
import java.util.ArrayList;
import java.util.List;

import SERVICE.UsersService;
import UTIL.Command;
import UTIL.ScanUtil;
import VO.UsersVo;

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
	//전체 회원 출력
	public Command userlist() {
		UsersService userservice = UsersService.getInstance(); 
		List<UsersVo> uservo = userservice.getPostList();
		if(uservo.isEmpty()) {
			System.out.println("등록된 유저가 없습니다");
			return Command.USER_HOME;
		}
		System.out.println("전체 유저 리스트");
		for (UsersVo user : uservo) {
	        System.out.println("ID: " + user.getUser_id() + ", 이름: " + user.getUsername() 
	        + ", 주소: " + user.getAddress() +  "  전화번호: " + user.getPhone_number() + "  이메일: " +
	        		user.getEmail());
	        
	    }
		return Command.USER_HOME;
	}
	//회원가입
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
			System.out.println("회원가입 성공!!");
		}else{
			System.out.println("회원가입 실패!!");
		}
		
		return Command.HOME;
	}
	
	//로그인
	public Command login(){
		System.out.println("============== 로그인 ===============");
		String userId = ScanUtil.nextLine("ID를 입력하세요 >> ");
		String password = ScanUtil.nextLine("PW를 입력하세요 >> ");
		
		UsersVo userVo = new UsersVo();
		userVo.setUser_id(userId);
		userVo.setUser_pass(password);
		
		UsersVo loginUserVo = userService.getUser(userVo);
		
		 if (loginUserVo == null) {
		        System.out.println("ID 혹은 PW를 잘못 입력하셨습니다.");
		        return Command.LOGIN; // 로그인 실패 시 LOGIN으로 돌아감
		    } else {
		        int role = loginUserVo.getRole(); // 역할 가져오기

		        if (role != 0 ) { // ADMIN 역할 확인
		            System.out.println("관리자 로그인 성공!!");
		            // 관리자 로그인 성공 시 추가 로직을 여기에 작성
		        } else {
		            System.out.println("일반 사용자 로그인 성공!!");
		            // 일반 사용자 로그인 성공 시 추가 로직을 여기에 작성
		        }

		        // ProjectMain 클래스의 정적변수인 sessionMap에 로그인 정보를 저장한다.
		        MainController.sessionMap.put("loginUser", loginUserVo);
		        
		        return Command.USER_HOME; // 로그인 성공 시 USER_HOME으로 이동
		    }
		}
	

	//내 정보
	public Command myPage() {
		System.out.println("============== MY PAGE ====================");
		UsersVo loginUser = (UsersVo) MainController.sessionMap.get("loginUser");
	    if (loginUser == null) {
	        System.out.println("로그인이 필요합니다.");
	        return Command.LOGIN;
	    }

	    // 마이페이지 출력
	    System.out.println("ID: " + loginUser.getUser_id());
	    System.out.println("이름: " + loginUser.getUsername());
	    System.out.println("E-MAIL: " + loginUser.getEmail());
	    System.out.println("전화번호: " + loginUser.getPhone_number());
	    if(loginUser.getRole()!=0)
	    System.out.println("관리자 : " + loginUser.getRole());
	    
		return Command.USER_HOME;
	}

}