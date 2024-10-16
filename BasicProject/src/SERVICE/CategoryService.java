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
	
		public int insertCategory(CategoryVo cate) {
			return dao.InsertCategory(cate);
		}
		
		public List<CategoryVo> getCategoryList() {
			return dao.getCategoryList();
		}
		
		public CategoryVo getCategorySelect(int category_Id) {
		     return dao.getCategory(category_Id);
	    }
		 
		public int UpdateCategory(CategoryVo cate) {
			return dao.UpdateCategory(cate);
		}
		
		public int DeleteCategory(CategoryVo cate) {
			return dao.DeleteCategory(cate);
		}

		public boolean isCategoryIdExists(int newid) {
			return dao.isCategoryIdExists(newid);
		}
}