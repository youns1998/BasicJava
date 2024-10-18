package SERVICE;

import java.util.List;

import DAO.CategoryDAO;
import VO.CategoryVo;

public class CategoryService {
	private CategoryDAO dao;

	private static CategoryService instance;

	private CategoryService() {
		this.dao = CategoryDAO.getInstance();

	}

	public static CategoryService getInstance() {
		if (instance == null)
			instance = new CategoryService();
		return instance;
	}

	public int insertCategory(CategoryVo cate) {
		return dao.InsertCategory(cate);
	}

	public List<CategoryVo> getCategoryList() {
		return dao.getCategoryList();
	}

	public CategoryVo getCategorySelect(int category_Id) {
		return dao.getCategory(category_Id);
	}

	public int UpdateCategory(CategoryVo cate, int newid) {

		return dao.UpdateCategory(cate, newid);
	}

	public int DeleteCategory(CategoryVo cate) {
		return dao.DeleteCategory(cate);
	}

	public boolean isCategoryIdExists(int newid) {
		return dao.isCategoryIdExists(newid);
	}

	public String getCategoryNameById(int categoryId) {
		// 카테고리 목록을 가져오고, 해당 ID에 맞는 카테고리 이름을 찾습니다.
		List<CategoryVo> categories = getCategoryList();
		for (CategoryVo category : categories) {
			if (category.getCategory_id() == categoryId) {
				return category.getCategory_name();
			}
		}
		return "미지정"; // 해당 ID의 카테고리가 없으면 '미지정'으로 반환
	}
}