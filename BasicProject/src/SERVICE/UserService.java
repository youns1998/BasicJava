package SERVICE;

import kr.or.ddit.dao.UserDaoImpl;
import kr.or.ddit.vo.UserVO;

public class UserService{
	private UserDaoImpl dao;
	
	// 싱글톤 ----------------------------
	private static UserService service;
	
	private UserService() {
		dao = UserDaoImpl.getInstance();
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
