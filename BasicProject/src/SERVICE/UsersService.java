package SERVICE;

import java.util.List;

import DAO.UsersDao;
import VO.UsersVo;


public class UsersService{
	private UsersDao dao;
	
	
	
	private static UsersService instance;

	private UsersService() {
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
		
		 public UsersVo getUserSelect(int userId) {
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
		
}