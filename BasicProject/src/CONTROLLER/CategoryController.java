package CONTROLLER;

import java.util.List;

import SERVICE.CategoryService;
import UTIL.Command;
import UTIL.ScanUtil;
import VO.CategoryVo;

public class CategoryController {
	private static CategoryController instance;

	private CategoryController() {

	}

	public static CategoryController getInstance() {
		if (instance == null)
			instance = new CategoryController();
		return instance;
	}
	//전체 카테고리 출력
public Command categoryList() {
	CategoryService cateservice = CategoryService.getInstance();
	List<CategoryVo> catevo = cateservice.getCategoryList();
	
	 for (CategoryVo category : catevo) {
	        System.out.println("분류번호: " + category.getCategory_id() + ", 카테고리: " + category.getCategory_name());
	    }
	 System.out.println("======================================================================");
	 int choice = ScanUtil.nextInt("1.카테고리 추가 2.카테고리 수정 3.카테고리 삭제 0.돌아가기\n실행할 번호를 입력하세요. >> ");
	 if(choice==1)
		 return Command.CATEGORY_INSERT;
	 if(choice==2)
		 return Command.CATEGORY_UPDATE;
	 if(choice==3)
		 return Command.CATEGORY_DELETE;
	return Command.USER_HOME;
}
public Command categoryDetail() {
	
	return Command.CATEGORY_LIST;
}
	//카테고리 추가
public Command categoryInsert() {
	CategoryService cateservice = CategoryService.getInstance();
	CategoryVo cate = new CategoryVo();
	System.out.println("카테고리 추가 화면");
	int id = ScanUtil.nextInt("카테고리 ID 입력 >>");
	String name = ScanUtil.nextLine("카테고리 이름 입력 >>");
	
	cate.setCategory_id(id);
	cate.setCategory_name(name);
	int result = cateservice.insertCategory(cate); 
    if (result > 0) {
        System.out.println("카테고리 등록 성공.");
    } else {
    	System.out.println("▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄\r\n"
    			+ "████▌▄▌▄▐▐▌█████\r\n"
    			+ "████▌▄▌▄▐▐▌▀████\r\n"
    			+ "▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀\r\n"
    			+ "");
    }
	return Command.CATEGORY_LIST;
		
}
	//카테고리 수정
public Command categoryUpdate() {
	  CategoryService cateservice = CategoryService.getInstance();
	    System.out.println("카테고리 수정 화면");
	    int id = ScanUtil.nextInt("수정 할 분류번호 입력 >>");
	    CategoryVo cate = cateservice.getCategorySelect(id);
	    
	    if (cate==null) { 
	        System.out.println("해당 분류번호는 없습니다. 다시 선택하세요");
	        return Command.CATEGORY_UPDATE;
	    }
	    System.out.println("현재 카테고리 ID : " + cate.getCategory_id());
	    int newid = ScanUtil.nextInt("새로운 분류번호 입력 >>");
	    
	    if (newid == cate.getCategory_id()) {
	        System.out.println("입력한 분류번호가 현재 카테고리 번호와 같습니다. 다시 선택하세요");
	        return Command.CATEGORY_UPDATE;
	    }
	    System.out.println(cateservice.isCategoryIdExists(newid));
	    if (cateservice.isCategoryIdExists(newid)) {
	        System.out.println("이미 다른 카테고리에서 사용 중인 분류번호입니다. 다시 선택하세요.");
	        return Command.CATEGORY_UPDATE;
	    }
	    
	    String newname = ScanUtil.nextLine("새로운 카테고리 이름 입력 >>");
	    cate.setCategory_name(newname);
	    
	    int result = cateservice.UpdateCategory(cate,newid);
	    if (result > 0) {
	        System.out.println("카테고리 수정 성공.");
	        System.out.println();
	    } else {
	        System.out.println("▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄\r\n"
	        		+ "████▌▄▌▄▐▐▌█████\r\n"
	        		+ "████▌▄▌▄▐▐▌▀████\r\n"
	        		+ "▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀\r\n"
	        		+ "");
	    }
	    return Command.CATEGORY_LIST;
	}

	//카테고리 삭제 
public Command categoryDelete() {
	 CategoryService cateservice = CategoryService.getInstance();
	  System.out.println("카테고리 수정 화면");
	    int id = ScanUtil.nextInt("삭제 할 분류번호 입력 >>");
	    CategoryVo cate = cateservice.getCategorySelect(id);
	    
	    if (cate==null) { 
	        System.out.println("해당 분류번호는 없습니다. 다시 선택하세요");
	        return Command.CATEGORY_UPDATE;
	    }
	    int result = cateservice.DeleteCategory(cate);
	    if (result > 0) {
	        System.out.println("카테고리 삭제 성공.");
	        System.out.println();
	    } else {
	    	System.out.println("▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄\r\n"
	    			+ "████▌▄▌▄▐▐▌█████\r\n"
	    			+ "████▌▄▌▄▐▐▌▀████\r\n"
	    			+ "▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀\r\n"
	    			+ "");
	    }
	return Command.CATEGORY_LIST;
	
}
}
