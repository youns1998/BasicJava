package SERVICE;

import java.util.List;

import DAO.UsersDao;
import VO.UsersVo;


public class UsersService{
	private UsersDao dao;
	
	
	
	private static UsersService instance;

	public UsersService() {
		this.dao = UsersDao.getInstance();

	}

	public static UsersService getInstance() {
		if (instance == null)
			instance = new UsersService();
		return instance;
	}
	
		public int addUser(UsersVo user) {
			return dao.addUser(user);
		}
		
		public UsersVo getUser(UsersVo userVo) {
			return dao.getUser(userVo);
		}
		public List<UsersVo> getPostList() {
			return dao.getUserList();
		}
		
		public UsersVo getUserSelect(String userId) {
		        return dao.getUserSelect(userId);
		    }
		
		public int updateUser(UsersVo user) {
			return dao.updateUser(user);
		}
		
		public int deleteUser(String user_id) {
			return dao.deleteUser(user_id);
		}
		
		public int getMemberCount(String user_id) {
			return getMemberCount(user_id);
		}
		
		public UsersVo findUserId(String name, String email) {
		        return dao.findUserId(name, email);
		    }
		
		public UsersVo findUserPass(String userId, String name, String email) {
	        return dao.findUserPass(userId, name, email);
	    }
		 
}