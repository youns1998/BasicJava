package SERVICE;

import DAO.UserDao;
import VO.UserVO;

public class UserService{
	private UserDao dao;
	
	// 싱글톤 ----------------------------
	private static UserService service;
	
	private UserService() {
		dao = UserDao.getInstance();
	}
	
	public static UserService getInstance() {
		if(service==null) {
			service = new UserService();
		}
		return service;
	}
	//----------------------------------------------
	
	public int insertUser(UserVO userVo) {
		return dao.insertUser(userVo);
	}
	
	public UserVO getUser(UserVO userVo) {
		return dao.getUser(userVo);
	}
}
