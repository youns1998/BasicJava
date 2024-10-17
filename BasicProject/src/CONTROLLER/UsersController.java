package CONTROLLER;

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

    // 전체 회원 출력 (관리자용)
    public Command userList() {
        List<UsersVo> users = userService.getPostList();
        if (users.isEmpty()) {
            System.out.println("등록된 유저가 없습니다.");
            return Command.USER_HOME;
        }

        
        return Command.ADMIN_USERDETAIL;
    }	
    //회원관리 - 조회할 회원 ID 입력 후 나타나는 화면
    public Command userdetail() {
    	 System.out.println("전체 유저 리스트");
    	 List<UsersVo> users = userService.getPostList();
         for (UsersVo user : users) {
             System.out.println("ID: " + user.getUser_id() + ", 이름: " + user.getUsername() 
             + ", 주소: " + user.getAddress() + ", 전화번호: " + user.getPhone_number() 
             + ", 이메일: " + user.getEmail() + ", 관리자: " + user.getRole());
         }
         System.out.println("======================================================");
    	int input = ScanUtil.nextInt("1.회원 상세보기 2.회원 수정 3.회원 삭제 4.찜 목록 조회 5.쓴 게시물 조회 6.쓴 댓글 조회 0.뒤로가기");
    	switch(input) {
    	case 1: return Command.ADMIN_USER;
    	case 2: return Command.USER_UPDATE;
    	case 3: return Command.USER_DELETE;
    	case 4:
    	case 5:
    	case 6:
    	case 0:
    	}
    	
    	return Command.USER_HOME;
    }
    //회원 수정
    public Command userUpdate() {
    	 UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser");
         String choice = ScanUtil.nextLine("수정할 회원 ID를 입력하세요: ");
         UsersService userService = UsersService.getInstance();
         
         UsersVo uservo = userService.getUserSelect(choice); 
         if (uservo == null) {
             System.out.println("해당 회원을 찾을 수 없습니다.");
             return Command.UESR_LIST; 
         }
         
         // 사용자 권한 확인
         if (uservo.getUser_id().equals(loginUserVo.getUser_id()) || loginUserVo.getRole() != 0) {
             // 수정 진행
             userService.updateUser(uservo);
             System.out.println("선택한 회원이 수정되었습니다");
         } 
    	
    	return Command.ADMIN_USERDETAIL;
    }
    //회원 삭제
    public Command userDelete() {
    	 UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser");
         String choice = ScanUtil.nextLine("삭제할 회원 ID를 입력하세요: ");
         UsersService userService = UsersService.getInstance();
         
         
         UsersVo user = userService.getUserSelect(choice);
         if (user == null) {
             System.out.println("해당 유저를 찾을 수 없습니다.");
             return Command.ADMIN_USERDETAIL;
         }

         // 사용자 권한 확인
         if (user.getUser_id().equals(loginUserVo.getUser_id()) || loginUserVo.getRole() != 0) {
             // 자신이거나 관리자라면 삭제 가능
             userService.deleteUser(user);
             System.out.println("선택한 회원이 삭제 되었습니다");
         } 
    	
    	return Command.ADMIN_USERDETAIL;
    }
    // 회원 상세보기
    public Command userSelect() {
        String userId = ScanUtil.nextLine("조회할 회원 ID를 입력하세요: ");
        UsersVo user = userService.getUserSelect(userId);
        if (user != null) {
            System.out.println("ID: " + user.getUser_id());
            System.out.println("이름: " + user.getUsername());
            System.out.println("주소: " + user.getAddress());
            System.out.println("전화번호: " + user.getPhone_number());
            System.out.println("이메일: " + user.getEmail());
            System.out.println();
        } else {
            System.out.println("해당 회원을 찾을 수 없습니다.");
            System.out.println();
        }
        return Command.ADMIN_USERDETAIL;
    }

    // 아이디 중복 확인 메서드
    private boolean isUserIdDuplicated(String userId) {
        return userService.getUserSelect(userId) != null;
    }

    // 아이디 및 비밀번호 형식 검증 메서드
    private boolean validateUserId(String userId) {
        return userId.matches("^[a-zA-Z0-9]{4,12}$");
    }

 // 비밀번호 형식 검증 메서드
    private boolean validatePassword(String password) {
        return password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$");
    }


    // 회원가입
 // 회원가입 메서드
    public Command join() {
        System.out.println("=========== 회원가입 =============");
        System.out.println("아이디는 영문자와 숫자로 이루어져야 하며, 길이는 4~12자여야 합니다.");

        String userId;
        do {
            userId = ScanUtil.nextLine("아이디 >> ");
            if (!validateUserId(userId)) {
                System.out.println("아이디 형식이 올바르지 않습니다. 다시 입력해 주세요.");
                continue;
            }
            if (isUserIdDuplicated(userId)) {
                System.out.println("이미 사용 중인 아이디입니다. 다른 아이디를 선택하세요.");
            }
        } while (!validateUserId(userId) || isUserIdDuplicated(userId));

        System.out.println("비밀번호는 영문자와 숫자로 이루어져야 하며, 길이는 8~20자여야 합니다.");
        String password, passwordConfirm = null;
        do {
            password = ScanUtil.nextLine("비밀번호 >> ");
            if (!validatePassword(password)) {
                System.out.println("비밀번호 형식이 올바르지 않습니다. 다시 입력해 주세요.");
                continue;
            }
            passwordConfirm = ScanUtil.nextLine("비밀번호 확인 >> ");
            if (!password.equals(passwordConfirm)) {
                System.out.println("비밀번호가 일치하지 않습니다. 다시 입력해 주세요.");
            }
        } while (!validatePassword(password) || !password.equals(passwordConfirm));

        String userName = ScanUtil.nextLine("이름 >> ");
        String email = ScanUtil.nextLine("이메일 >> ");
        String phoneNumber = ScanUtil.nextLine("전화번호 >> ");
        String address = ScanUtil.nextLine("주소 >> ");

        UsersVo userVo = new UsersVo(userId, password, userName, address, email, phoneNumber);
        int result = userService.addUser(userVo);

        System.out.println(result > 0 ? "회원가입 성공!!" : "회원가입 실패!!");
        return Command.HOME;
    }
    
    // 비번 찾기
    public Command findUserPass() {
        String userId = ScanUtil.nextLine("아이디를 입력하세요 : ");
        String name = ScanUtil.nextLine("이름을 입력하세요 : ");
        String email = ScanUtil.nextLine("이메일을 입력하세요 : ");
        
        UsersVo user = userService.findUserPass(userId, name, email );
        
        if (user != null) {
    		System.out.println("찾은 비밀번호: " + user.getUser_pass());
    	} else {
    		System.out.println("해당 정보로 비밀번호를 찾을 수 없습니다.");
    	}		
    	return Command.HOME;
    }
    
    // 아이디 찾기 
    public Command findUserId() {
    	String name = ScanUtil.nextLine("이름을 입력하세요 : ");
    	String email = ScanUtil.nextLine("이메일을 입력하세요 : ");

    	UsersVo user = userService.findUserId(name, email);
    
    	if (user != null) {
    		System.out.println("찾은 아이디: " + user.getUser_id());
    	} else {
    		System.out.println("해당 정보로 아이디를 찾을 수 없습니다.");
    	}		
    	return Command.HOME;
}
    // 로그인
    public Command login() {
        System.out.println("============== 로그인 ===============");

        if (MainController.sessionMap.get("loginUser") != null) {
            System.out.println("이미 로그인된 상태입니다.");
            return Command.USER_HOME;
        }

        String userId = ScanUtil.nextLine("ID를 입력하세요 >> ");
        String password = ScanUtil.nextLine("PW를 입력하세요 >> ");
        UsersVo loginUserVo = userService.getUser(new UsersVo(userId, password));

        if (loginUserVo == null) {
            System.out.println("ID 혹은 PW를 잘못 입력하셨습니다.");
            return Command.LOGIN;
        }

        MainController.sessionMap.put("loginUser", loginUserVo);
        System.out.println(loginUserVo.getRole() != 0 ? "관리자 로그인 성공!! \n" : "일반 사용자 로그인 성공!!\n");
        return Command.USER_HOME;
    }

    // 내 정보 (마이페이지)
    public Command myPage() {
        UsersVo loginUser = (UsersVo) MainController.sessionMap.get("loginUser");
        if (loginUser == null) {
            System.out.println("로그인이 필요합니다.");
            return Command.LOGIN;
        }

        System.out.println("============== MY PAGE ====================");
        System.out.println("ID: " + loginUser.getUser_id());
        System.out.println("이름: " + loginUser.getUsername());
        System.out.println("E-MAIL: " + loginUser.getEmail());
        System.out.println("전화번호: " + loginUser.getPhone_number());
        System.out.println("주소: " + loginUser.getAddress());

        return Command.USER_HOME;
    }

	
}
