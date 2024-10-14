package CONTROLLER;

import UTIL.*;

import CONTROLLER.UsersController;

public class MainController {
	
	private UsersController usersController;

	
	public MainController() {
		usersController = UsersController.getInstance();
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
			
			case END:
				System.out.println("프로그램이 종료되었습니다. ");
				return;
			default : cmd = Command.HOME;
			}
			
		}
	}
	
	private Command home() {
		System.out.println("-----------------------------------");
		System.out.println("         중 고 나 라          ");
		System.out.println("-----------------------------------");
		System.out.println("\t1. 로그인\t2. 회원가입\t0. 프로그램 종료");
		int input = ScanUtil.nextInt("메뉴 선택 >>");
		switch (input) {
		case 1: return Command.LOGIN;
		case 2: return Command.JOIN;
		case 0: return Command.END;
		}
		return Command.HOME;

	}
}