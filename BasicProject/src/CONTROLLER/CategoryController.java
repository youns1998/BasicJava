package CONTROLLER;

import java.util.List;

import SERVICE.CategoryService;
import UTIL.Command;
import UTIL.ScanUtil;
import VO.CategoryVo;

public class CategoryController {
	private static CategoryController instance;

	// CategoryController의 싱글톤 패턴을 위한 생성자
	private CategoryController() {

	}

	// CategoryController의 싱글톤 인스턴스를 반환하는 메서드
	public static CategoryController getInstance() {
		if (instance == null)
			instance = new CategoryController();
		return instance;
	}

// 전체 카테고리 목록을 출력하는 메서드
public Command categoryList() {
		CategoryService cateservice = CategoryService.getInstance(); // 카테고리 서비스 인스턴스 가져오기
		List<CategoryVo> catevo = cateservice.getCategoryList(); // 카테고리 목록 불러오기
		
		// 카테고리 리스트를 출력
		for (CategoryVo category : catevo) {
			System.out.println("분류번호: " + category.getCategory_id() + ", 카테고리: " + category.getCategory_name());
		}
		System.out.println("======================================================================");
		
		// 사용자로부터 입력을 받아 다음 액션을 결정
		int choice = ScanUtil.nextInt("1.카테고리 추가 2.카테고리 수정 3.카테고리 삭제 0.돌아가기\n실행할 번호를 입력하세요. >> ");
		
		// 선택에 따라 명령어 반환
		if (choice == 1)
			return Command.CATEGORY_INSERT; // 카테고리 추가
		if (choice == 2)
			return Command.CATEGORY_UPDATE; // 카테고리 수정
		if (choice == 3)
			return Command.CATEGORY_DELETE; // 카테고리 삭제
		return Command.USER_HOME; // 기본적으로 홈으로 돌아감
	}

// 카테고리 상세 정보를 보는 메서드 (기본적으로 카테고리 리스트로 이동)
public Command categoryDetail() {
		return Command.CATEGORY_LIST;
	}

// 카테고리 추가 메서드
public Command categoryInsert() {
		CategoryService cateservice = CategoryService.getInstance(); // 카테고리 서비스 인스턴스 가져오기
		CategoryVo cate = new CategoryVo(); // 새 카테고리 객체 생성
		System.out.println("카테고리 추가 화면");
		
		// 사용자로부터 카테고리 ID와 이름 입력받기
		int id = ScanUtil.nextInt("카테고리 ID 입력 >>");
		String name = ScanUtil.nextLine("카테고리 이름 입력 >>");
		
		// 카테고리 객체에 ID와 이름 설정
		cate.setCategory_id(id);
		cate.setCategory_name(name);
		
		// 카테고리 추가 서비스 호출
		int result = cateservice.insertCategory(cate);
		
		// 결과에 따른 메시지 출력
		if (result > 0) {
			System.out.println("카테고리 등록 성공.");
		} else {
			// 카테고리 등록 실패 시 출력
			System.out.println("▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄\r\n"
					+ "████▌▄▌▄▐▐▌█████\r\n"
					+ "████▌▄▌▄▐▐▌▀████\r\n"
					+ "▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀\r\n"
					+ "");
		}
		return Command.CATEGORY_LIST; // 카테고리 리스트로 돌아감
	}

// 카테고리 수정 메서드
public Command categoryUpdate() {
		CategoryService cateservice = CategoryService.getInstance(); // 카테고리 서비스 인스턴스 가져오기
		System.out.println("카테고리 수정 화면");
		
		// 사용자로부터 수정할 카테고리의 ID 입력받기
		int id = ScanUtil.nextInt("수정 할 분류번호 입력 >>");
		CategoryVo cate = cateservice.getCategorySelect(id); // 입력한 ID에 해당하는 카테고리 가져오기
		
		// 해당 ID의 카테고리가 존재하지 않을 경우
		if (cate == null) {
			System.out.println("해당 분류번호는 없습니다. 다시 선택하세요");
			return Command.CATEGORY_UPDATE;
		}
		
		// 현재 카테고리 정보 출력
		System.out.println("현재 카테고리 ID : " + cate.getCategory_id());
		int newid = ScanUtil.nextInt("새로운 분류번호 입력 >>");
		
		// 새로 입력한 ID가 기존 ID와 같을 경우
		if (newid == cate.getCategory_id()) {
			System.out.println("입력한 분류번호가 현재 카테고리 번호와 같습니다. 다시 선택하세요");
			return Command.CATEGORY_UPDATE;
		}
		
		// 새로 입력한 ID가 이미 다른 카테고리에서 사용 중인 ID일 경우
		if (cateservice.isCategoryIdExists(newid)) {
			System.out.println("이미 다른 카테고리에서 사용 중인 분류번호입니다. 다시 선택하세요.");
			return Command.CATEGORY_UPDATE;
		}
		
		// 사용자로부터 새로운 카테고리 이름 입력받기
		String newname = ScanUtil.nextLine("새로운 카테고리 이름 입력 >>");
		cate.setCategory_name(newname); // 카테고리 이름 업데이트
		
		// 카테고리 업데이트 서비스 호출
		int result = cateservice.UpdateCategory(cate, newid);
		
		// 결과에 따른 메시지 출력
		if (result > 0) {
			System.out.println("카테고리 수정 성공.");
			System.out.println();
		} else {
			// 카테고리 수정 실패 시 출력
			System.out.println("▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄\r\n"
					+ "████▌▄▌▄▐▐▌█████\r\n"
					+ "████▌▄▌▄▐▐▌▀████\r\n"
					+ "▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀\r\n"
					+ "");
		}
		return Command.CATEGORY_LIST; // 카테고리 리스트로 돌아감
	}

	// 카테고리 삭제 메서드
public Command categoryDelete() {
		CategoryService cateservice = CategoryService.getInstance(); // 카테고리 서비스 인스턴스 가져오기
		System.out.println("카테고리 수정 화면");
		
		// 사용자로부터 삭제할 카테고리의 ID 입력받기
		int id = ScanUtil.nextInt("삭제 할 분류번호 입력 >>");
		CategoryVo cate = cateservice.getCategorySelect(id); // 입력한 ID에 해당하는 카테고리 가져오기
		
		// 해당 ID의 카테고리가 존재하지 않을 경우
		if (cate == null) {
			System.out.println("해당 분류번호는 없습니다. 다시 선택하세요");
			return Command.CATEGORY_UPDATE;
		}
		
		// 카테고리 삭제 서비스 호출
		int result = cateservice.DeleteCategory(cate);
		
		// 결과에 따른 메시지 출력
		if (result > 0) {
			System.out.println("카테고리 삭제 성공.");
			System.out.println();
		} else {
			// 카테고리 삭제 실패 시 출력
			System.out.println("▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄\r\n"
					+ "████▌▄▌▄▐▐▌█████\r\n"
					+ "████▌▄▌▄▐▐▌▀████\r\n"
					+ "▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀\r\n"
					+ "");
		}
		return Command.CATEGORY_LIST; // 카테고리 리스트로 돌아감
	}
}
