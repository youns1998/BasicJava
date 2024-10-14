package SERVICE;

import java.util.List;

import DAO.UsersDao;
import VO.UsersVo;


public class UsersService{
	private UsersDao dao;
	
		
		public int addUser(UsersVo user) {
			return dao.addUser(user);
		}
		
		public List<UsersVo> getPostList() {
			return dao.getPostList();
		}
		
		public int search(String user_id) {
			return search(user_id);
		}
		
		public int updateUser(UsersVo user) {
			return updateUser(user);
		}
		
		public int deleteUser(String user_id) {
			return deleteUser(user_id);
		}
		
}