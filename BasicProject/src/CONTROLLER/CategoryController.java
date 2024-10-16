package CONTROLLER;

import java.util.List;

import SERVICE.CategoryService;
import UTIL.Command;
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
	List<CategoryVo> catevo = CategoryService.getCategoryList;
			
	
	return Command.CATEGORY_LIST;
}
	//카테고리 추가
public Command categoryInsert() {
	
	return Command.CATEGORY_INSERT;
		
}
	//카테고리 수정
public Command categoryUpdate() {
		
	return Command.CATEGORY_UPDATE;
	
}
	//카테고리 삭제
public Command categoryDelete() {
	
	return Command.CATEGORY_DELETE;
	
}
}
