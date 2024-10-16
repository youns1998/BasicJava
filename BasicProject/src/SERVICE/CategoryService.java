package SERVICE;


import java.util.List;

import DAO.CategoryDAO;
import VO.CategoryVo;
import VO.UsersVo;

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
	
		public int addUser(CategoryVo cate) {
			return dao.InsertCategory(cate);
		}
		
		public List<UsersVo> getCategoryList() {
			return dao.getCategoryList();
		}
		
		
		public int UpdateCategory(CategoryVo cate) {
			return UpdateCategory(cate);
		}
		
		public int DeleteCategory(CategoryVo cate) {
			return DeleteCategory(cate);
		}
}