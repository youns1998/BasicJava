package CONTROLLER;

import java.util.List;
import java.util.Scanner;

import SERVICE.*;
import UTIL.Command;
import UTIL.PasswordUtil;
import UTIL.ScanUtil;
import VO.UsersVo;

public class UsersController {
	private UsersService userService; // 사용자 서비스 인스턴스
	private static UsersController instance; // 싱글톤 패턴 적용

	private UsersController() { // 생성자
		userService = UsersService.getInstance(); // UsersService의 싱글톤 인스턴스 가져오기
	}

	public static UsersController getInstance() { // 싱글톤 인스턴스 반환
		if (instance == null)
			instance = new UsersController();
		return instance;
	}

	// 전체 회원 출력 (관리자용)
	public Command userList() {
		List<UsersVo> users = userService.getPostList(); // 전체 회원 목록 가져오기
		if (users.isEmpty()) {
			System.out.println("등록된 유저가 없습니다.");
			return Command.USER_HOME; // 회원 목록이 비었을 경우 홈으로 이동
		}

		return Command.ADMIN_USERDETAIL; // 회원 목록이 있을 경우 관리자 회원 상세보기로 이동
	}

	// 관리자의 회원 관리 화면
	public Command userdetail() {
		System.out.println();
		System.out.println("==============================전체 유저 리스트==============================");
		List<UsersVo> users = userService.getPostList(); // 회원 목록 가져오기
		for (UsersVo user : users) {
			if(user.getUser_ban()==null) {
	            System.out.println("ID: " + user.getUser_id() + "\t\t이름: " + user.getUsername());
	        	}else {
	        	System.out.println("ID: " + user.getUser_id() +"\t\t제재당한 사용자입니다(사유: " + user.getUser_ban()+")");
	        	}
		}	
		System.out.println("=========================================================================");
		int input = ScanUtil.nextInt("1.회원 상세보기 2.회원 수정 3.회원 탈퇴 4.회원의 찜 목록 조회 5.회원의 게시물 조회 6.회원의 댓글 조회 0.뒤로가기\n메뉴 선택 >> ");
		switch (input) {
		case 1:
			return Command.ADMIN_USER; // 회원 상세보기
		case 2:
			return Command.USER_UPDATE; // 회원 수정
		case 3:
			return Command.USER_DELETE; // 회원 삭제
		case 4:
			return Command.USER_FAVORITE; // 회원의 찜 목록 조회
		case 5:
			return Command.POST_ADMIN; // 회원의 게시물 조회
		case 6:
			return Command.COMMENT_ADMIN; // 회원의 댓글 조회
		case 0:
			return Command.USER_HOME; // 뒤로가기
		}

		return Command.USER_HOME; // 기본적으로 홈으로 이동
	}

	// 내 정보 관리 - 사용자용
	public Command userSelf() {
		int choice = ScanUtil.nextInt("1. 개인 정보 수정 2.내가 쓴 글 보기 3.내가 쓴 댓글 보기 4.회원 탈퇴 0.돌아가기\n메뉴 선택 >> ");
		switch (choice) {
		case 1:
			return Command.USER_SELFUPDATE; // 개인 정보 수정
		case 2:
			return Command.POST_SELF; // 내가 쓴 글 보기
		case 3:
			return Command.COMMENT_SELF; // 내가 쓴 댓글 보기
		case 4:
			return Command.USER_SELFDELETE; // 회원 탈퇴
		}
		return Command.USER_HOME; // 기본적으로 홈으로 이동
	}

	// 개인정보 수정 - 사용자용
	public Command userSelfUpdate() {
		UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser"); // 로그인한 사용자 정보 가져오기
		String choice = loginUserVo.getUser_id(); // 사용자 ID 가져오기
		UsersService userService = UsersService.getInstance(); // 사용자 서비스 인스턴스 가져오기
		UsersVo uservo = userService.getUserSelect(choice); // 사용자 정보 가져오기
		userService.updateUser(uservo); // 사용자 정보 업데이트
		System.out.println("회원 수정이 끝났습니다");

		return Command.USER_HOME; // 수정 후 홈으로 이동
	}

	// 회원 탈퇴 - 사용자용
	public Command userSelfDelete() {
		UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser"); // 로그인한 사용자 정보 가져오기
		String choice = loginUserVo.getUser_id(); // 사용자 ID 가져오기
		UsersService userService = UsersService.getInstance(); // 사용자 서비스 인스턴스 가져오기
		UsersVo user = userService.getUserSelect(choice); // 사용자 정보 가져오기

		if (user.getUser_id().equals(loginUserVo.getUser_id()) || loginUserVo.getRole() != 0) { // 본인 또는 관리자인지 확인
			System.out.println(".　　　_＿＿＿_\r\n"
					+ "　　／　　　　＼　\r\n"
					+ "　／　  _ノ 　ヽ_＼\r\n"
					+ " ／   　（●）（●）＼\r\n"
					+ " |　    ///（_人_）///| 참내..\r\n"
					+ " ＼   　　　 　　    ／\r\n"
					+ "／　 　　　 　    　＼\r\n"
					+ "");
			int choice1 = ScanUtil.nextInt("진짜루?? \n 탈퇴하시려면 1 입력 \n 돌아가시려면 아무숫자 입력 \n 선택 >> ");

			if (choice1 == 1) { // 탈퇴 확인
				userService.deleteUser(user); // 사용자 삭제
				System.out.println(".　　。+。☆゜*。゜。\r\n"
						+ "　。＊゜゜+☆＊+゜。*。\r\n"
						+ "　＠。゜*゜。+。☆＊＠゜\r\n"
						+ "　゜+。☆゜。*。＠。+*゜\r\n"
						+ "　＼゜*。゜。*゜*。+／\r\n"
						+ "　　 ＼*゜+ ∧,,∧ .／\r\n"
						+ "　　　　＼ (^ω^＊)　그 동안 Peanut Market을\r\n"
						+ "　　　　　◎⊂　)　아끼고 찾아 주셔서\r\n"
						+ "　　　　　△し-J　감사 합니다 ﻿ʚ◡̈ɞ\r\n"
						+ "");
			} else { // 탈퇴 취소
				System.out.println("좋은 생각입니다 더 좋은 서비스로 보답하겠습니다");
				return Command.USER_HOME; // 홈으로 돌아가기
			}
		}
		return Command.HOME; // 기본적으로 홈으로 이동
	}

	// 회원 수정 - 관리자용
	public Command userUpdate() {
		UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser"); // 로그인한 사용자 정보 가져오기
		String choice = ScanUtil.nextLine("수정할 회원 ID를 입력하세요: "); // 수정할 회원 ID 입력받기
		UsersService userService = UsersService.getInstance(); // 사용자 서비스 인스턴스 가져오기
		UsersVo uservo = userService.getUserSelect(choice); // 회원 정보 가져오기

		if (uservo == null) { // 해당 회원이 없는 경우
			System.out.println("해당 회원을 찾을 수 없습니다.");
			System.out.println("▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄\r\n"
					+ "████▌▄▌▄▐▐▌█████\r\n"
					+ "████▌▄▌▄▐▐▌▀████\r\n"
					+ "▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀\r\n"
					+ "");
			return Command.USER_LIST; // 회원 목록으로 돌아감
		}

		// 사용자 권한 확인 (본인 또는 관리자)
		if (uservo.getUser_id().equals(loginUserVo.getUser_id()) || loginUserVo.getRole() != 0) {
			userService.updateUser(uservo); // 회원 정보 수정
			System.out.println("수정 끝났습니다");
		}

		return Command.ADMIN_USERDETAIL; // 수정 후 관리자 회원 상세보기로 이동
	}

	// 회원 삭제 - 관리자용
	public Command userDelete() {
		UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser"); // 로그인한 사용자 정보 가져오기
		String choice = ScanUtil.nextLine("삭제할 회원 ID를 입력하세요: "); // 삭제할 회원 ID 입력받기
		UsersService userService = UsersService.getInstance(); // 사용자 서비스 인스턴스 가져오기
		UsersVo user = userService.getUserSelect(choice); // 삭제할 회원 정보 가져오기

		if (user == null) { // 회원이 없는 경우
			System.out.println("해당 유저를 찾을 수 없습니다.");
			System.out.println("▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄\r\n"
					+ "████▌▄▌▄▐▐▌█████\r\n"
					+ "████▌▄▌▄▐▐▌▀████\r\n"
					+ "▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀\r\n"
					+ "");
			return Command.ADMIN_USERDETAIL; // 관리자 회원 상세보기로 돌아감
		}

		// 사용자 권한 확인 (본인 또는 관리자)
		if (user.getUser_id().equals(loginUserVo.getUser_id()) || loginUserVo.getRole() != 0) {
			userService.deleteUser(user); // 회원 삭제
			System.out.println("선택한 회원이 삭제 되었습니다");
		}

		return Command.ADMIN_USERDETAIL; // 삭제 후 관리자 회원 상세보기로 돌아감
	}

	// 회원 상세보기
	public Command userSelect() {
		String userId = ScanUtil.nextLine("조회할 회원 ID를 입력하세요: "); // 조회할 회원 ID 입력받기
		UsersVo user = userService.getUserSelect(userId); // 회원 정보 가져오기
		if (user != null) { // 회원이 존재할 경우
			System.out.println("ID : " + user.getUser_id());
			System.out.println("이름 : " + user.getUsername());
			System.out.println("주소 : " + user.getAddress());
			System.out.println("전화번호 : " + user.getPhone_number());
			System.out.println("이메일 : " + user.getEmail());
			System.out.println("제재사유 : "+user.getUser_ban());
			System.out.println();
		} else { // 회원이 존재하지 않을 경우
			System.out.println("해당 회원을 찾을 수 없습니다.");
			System.out.println("▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄\r\n"
					+ "████▌▄▌▄▐▐▌█████\r\n"
					+ "████▌▄▌▄▐▐▌▀████\r\n"
					+ "▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀\r\n"
					+ "");
			System.out.println();
		}
		return Command.ADMIN_USERDETAIL; // 회원 조회 후 관리자 회원 상세보기로 돌아감
	}

	// 아이디 중복 확인 메서드
	private boolean isUserIdDuplicated(String userId) {
		return userService.getUserSelect(userId) != null; // 해당 아이디가 이미 존재하는지 확인
	}

	// 아이디 및 비밀번호 형식 검증 메서드
	private boolean validateUserId(String userId) {
		return userId.matches("^[a-zA-Z0-9]{4,12}$"); // 아이디 형식이 유효한지 확인 (영문자 및 숫자, 4~12자)
	}

	// 비밀번호 형식 검증 메서드
	private boolean validatePassword(String password) {
		return password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$"); // 비밀번호 형식이 유효한지 확인 (영문자 및 숫자, 8~20자)
	}

	// 회원가입
	public Command join() {
		System.out.println("==================== 회원가입 ======================");
		System.out.println("아이디는 영문자와 숫자로 이루어져야 하며, 길이는 4~12자여야 합니다.");

		// 아이디 입력 및 유효성 검사
		String userId;
		do {
			userId = ScanUtil.nextLine("아이디 >> ");
			if (!validateUserId(userId)) {
				System.out.println("아이디 형식이 올바르지 않습니다. 다시 입력해 주세요.");
				System.out.println("▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄\r\n"
						+ "████▌▄▌▄▐▐▌█████\r\n"
						+ "████▌▄▌▄▐▐▌▀████\r\n"
						+ "▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀\r\n"
						+ "");
				continue;
			}
			if (isUserIdDuplicated(userId)) {
				System.out.println("이미 사용 중인 아이디입니다. 다른 아이디를 선택하세요.");
			}
		} while (!validateUserId(userId) || isUserIdDuplicated(userId));

		// 비밀번호 입력 및 유효성 검사
		System.out.println("비밀번호는 영문자와 숫자로 이루어져야 하며, 길이는 8~20자여야 합니다.");
		String password, passwordConfirm = null;
		do {
			password = ScanUtil.nextLine("비밀번호 >> ");
			if (!validatePassword(password)) {
				System.out.println("비밀번호 형식이 올바르지 않습니다. 다시 입력해 주세요.");
				System.out.println("▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄\r\n"
						+ "████▌▄▌▄▐▐▌█████\r\n"
						+ "████▌▄▌▄▐▐▌▀████\r\n"
						+ "▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀\r\n"
						+ "");
				continue;
			}
			passwordConfirm = ScanUtil.nextLine("비밀번호 확인 >> ");
			if (!password.equals(passwordConfirm)) {
				System.out.println("비밀번호가 일치하지 않습니다. 다시 입력해 주세요.");
				System.out.println("▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄\r\n"
						+ "████▌▄▌▄▐▐▌█████\r\n"
						+ "████▌▄▌▄▐▐▌▀████\r\n"
						+ "▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀\r\n"
						+ "");
			}
		} while (!validatePassword(password) || !password.equals(passwordConfirm));

		// 이메일 입력 및 인증 처리
		String email = ScanUtil.nextLine("이메일 >> ");
		VerificationController verificationController = VerificationController.getInstance(); // 이메일 인증 컨트롤러
		verificationController.sendVerificationCode(email); // 이메일 인증 코드 발송

		// 인증 코드 입력 및 확인
		while (true) {
			String code = ScanUtil.nextLine("인증 코드 >> ");
			if (verificationController.verifyCode(email, code)) {
				System.out.println("이메일 인증에 성공했습니다.");
				break;
			} else {
				System.out.println("잘못된 인증 코드입니다. 다시 입력하세요.");
				System.out.println("▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄\r\n"
						+ "████▌▄▌▄▐▐▌█████\r\n"
						+ "████▌▄▌▄▐▐▌▀████\r\n"
						+ "▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀\r\n"
						+ "");
			}
		}

		// 나머지 회원가입 정보 입력
		String userName = ScanUtil.nextLine("이름 >> ");
		String phoneNumber = ScanUtil.nextLine("전화번호 >> ");
		String address = ScanUtil.nextLine("주소 >> ");

		// 사용자 객체 생성 및 회원가입 처리
		UsersVo userVo = new UsersVo(userId, password, userName, address, email, phoneNumber);
		int result = userService.addUser(userVo); // 사용자 추가

		// 회원가입 성공 여부 출력
		System.out.println(result > 0 ? "회원가입 성공!!" : "회원가입 실패!!");
		return Command.HOME; // 홈으로 이동
	}

	// 비밀번호 찾기
	
	
	// 비밀번호 찾기 - 비밀번호만 업데이트
	public Command findUserPass() {
	    String userId = ScanUtil.nextLine("비밀번호를 찾을 계정의 아이디를 입력하세요 >> ");
	    String email = ScanUtil.nextLine("등록된 이메일 주소를 입력해 주세요 >> ");
	    boolean istrue = userService.iDisMatch(userId, email); // 등록된 이메일과 아이디가 일치하는지 확인
	    int count = 3; // 인증 코드 입력 기회

	    if (istrue && (count > 0)) {
	        VerificationController verificationController = VerificationController.getInstance(); // 이메일 인증 컨트롤러
	        verificationController.sendVerificationCode(email); // 이메일 인증 코드 발송

	        while (true) {
	            String code = ScanUtil.nextLine("인증 코드 >> ");
	            if (verificationController.verifyCode(email, code)) {
	                System.out.println("이메일 인증에 성공했습니다.");

	                // 새 비밀번호 설정
	                String newPassword, newPasswordConfirm;
	                do {
	                    newPassword = ScanUtil.nextLine("새로운 비밀번호를 입력하세요 >> ");
	                    newPasswordConfirm = ScanUtil.nextLine("새로운 비밀번호를 다시 입력하세요 >> ");

	                    if (!validatePassword(newPassword)) {
	                        System.out.println("비밀번호 형식이 올바르지 않습니다. 다시 입력해 주세요.");
	                    } else if (!newPassword.equals(newPasswordConfirm)) {
	                        System.out.println("비밀번호가 일치하지 않습니다. 다시 입력해 주세요.");
	                    }
	                } while (!validatePassword(newPassword) || !newPassword.equals(newPasswordConfirm));

	                // 비밀번호 해싱 후 업데이트
	                String hashedPassword = PasswordUtil.hashPassword(newPassword);
	                int result = userService.updatePassword(userId, hashedPassword);
	                if (result > 0) {
	                    System.out.println("비밀번호가 성공적으로 변경되었습니다.");
	                } else {
	                    System.out.println("비밀번호 변경에 실패했습니다.");
	                }
	                return Command.USER_HOME; // 수정 후 홈으로 이동
	            } else {
	                count--;
	                System.out.printf("잘못된 인증 코드입니다. 다시 입력하세요. 남은기회 " + count + "회\n");
	            }

	            if (count <= 0) {
	                System.out.println("인증 기회를 모두 소진했습니다.");
	                String retry = ScanUtil.nextLine("다시 인증하시겠습니까? (y/n) >> ");
	                if (retry.equalsIgnoreCase("y")) {
	                    count = 3; // 기회 초기화
	                    verificationController.sendVerificationCode(email); // 인증 코드 재발송
	                } else {
	                    return Command.HOME; // 홈으로 이동
	                }
	            }
	        }
	    } else {
	        System.out.println("등록된 이메일이 아닙니다.");
	        return Command.HOME; // 홈으로 이동
	    }
	}




	// 아이디 찾기
	public Command findUserId() {
		String email = ScanUtil.nextLine("찾고싶은 계정의 이메일 주소를 입력해 주세요 >> ");
		boolean istrue = userService.EmailisMatch(email); // 등록된 이메일인지 확인
		int count = 3; // 인증 코드 입력 기회

		if (istrue && (count > 0)) {
			VerificationController verificationController = VerificationController.getInstance(); // 이메일 인증 컨트롤러
			verificationController.sendVerificationCode(email); // 이메일 인증 코드 발송

			while (true) {
				String code = ScanUtil.nextLine("인증 코드 >> ");
				if (verificationController.verifyCode(email, code)) {
					System.out.println("이메일 인증에 성공했습니다.");

					UsersVo user = userService.findUserId(email); // 아이디 찾기
					if (user != null) {
						System.out.println("찾은 아이디: " + user.getUser_id());
					} else {
						System.out.println("해당 정보로 아이디를 찾을 수 없습니다.");
					}

					break;
				} else {
					count--;
					System.out.printf("잘못된 인증 코드입니다. 다시 입력하세요. 남은기회 " + count + "회\n");
				}

				if (count <= 0) {
					System.out.println("▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄\r\n"
							+ "████▌▄▌▄▐▐▌█████\r\n"
							+ "████▌▄▌▄▐▐▌▀████\r\n"
							+ "▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀\r\n"
							+ "");
					String retry = ScanUtil.nextLine("다시 인증하시겠습니까? (y/n) >> ");
					if (retry.equalsIgnoreCase("y")) {
						count = 3; // 기회 초기화
						verificationController.sendVerificationCode(email); // 인증 코드 재발송
					} else {
						return Command.HOME; // 홈으로 이동
					}
				}
			}
		}

		else {
			System.out.println("등록된 이메일이 아닙니다.");
			return Command.HOME; // 홈으로 이동
		}
		return Command.HOME; // 기본적으로 홈으로 이동
	}

	// 로그인
	public Command login() {
		Scanner scanner = new Scanner(System.in);
		System.out.println(" __        ______     _______  __  .__   __. \r\n"
				+ "|  |      /  __  \\   /  _____||  | |  \\ |  | \r\n"
				+ "|  |     |  |  |  | |  |  __  |  | |   \\|  | \r\n"
				+ "|  |     |  |  |  | |  | |_ | |  | |  . `  | \r\n"
				+ "|  `----.|  `--'  | |  |__| | |  | |  |\\   | \r\n"
				+ "|_______| \\______/   \\______| |__| |__| \\__| \r\n"
				+ "                                             \r\n"
				+ "");

		// ID 입력 박스
		System.out.println("┌────────────────────────────┐");
		System.out.println("│       ID를 입력하세요         │");
		System.out.print("│         >> ");
		String userId = scanner.nextLine();
		System.out.println("└────────────────────────────┘"); // 박스 닫기

		// PW 입력 박스
		System.out.println("┌────────────────────────────┐");
		System.out.println("│       PW를 입력하세요         │");
		System.out.print("│         >> ");
		String password = scanner.nextLine();
		System.out.println("└────────────────────────────┘");

		UsersVo loginUserVo = userService.getUser(new UsersVo(userId, password)); // 로그인 정보 확인
		if (loginUserVo == null) { // 로그인 실패 시
			System.out.println("ID 혹은 PW를 잘못 입력하셨습니다.");
			return Command.LOGIN; // 다시 로그인
		}
		if(loginUserVo.getUser_ban()!=null) {
			System.out.println();
			System.out.println("당신은 관리자에 의해 제재 되었습니다 \n사유 : " + loginUserVo.getUser_ban());
			System.out.println();
			return Command.HOME; // 다시 로그인
		}
		MainController.sessionMap.put("loginUser", loginUserVo); // 세션에 로그인 사용자 정보 저장
		System.out.println();
		System.out.println(loginUserVo.getRole() != 0 ? "#관리자 로그인 성공!!# " : "#일반 사용자 로그인 성공!!#");
		return Command.USER_HOME; // 로그인 후 홈으로 이동
	}

	// 내 정보 (마이페이지)
	public Command myPage() {
		UsersVo loginUser = (UsersVo) MainController.sessionMap.get("loginUser"); // 로그인한 사용자 정보 가져오기
		if (loginUser == null) { // 로그인되지 않은 경우
			System.out.println("로그인이 필요합니다.");
			return Command.LOGIN; // 로그인 페이지로 이동
		}

		// 인터페이스 출력
		System.out.println("+==============================+");
		System.out.println("|           MY PAGE            |");
		System.out.println("+------------------------------+");

		System.out.printf("   %-8s         %-15s \n", "ID", loginUser.getUser_id());
		System.out.println("|-----------|------------------|");
		System.out.printf("   %-8s         %-15s \n", "이름", loginUser.getUsername());
		System.out.println("|-----------|------------------|");
		System.out.printf("   %-8s         %-15s \n", "E-MAIL", loginUser.getEmail());
		System.out.println("|-----------|------------------|");
		System.out.printf("   %-8s         %-15s \n", "전화번호", loginUser.getPhone_number());
		System.out.println("|-----------|------------------|");
		System.out.printf("   %-8s         %-15s \n", "주소", loginUser.getAddress());

		System.out.println("+==============================+");

		return Command.USER_SELF; // 마이페이지 후 사용자 정보 관리로 이동
	}

}
