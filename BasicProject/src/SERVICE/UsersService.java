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
		//회원가입
		public int addUser(UsersVo user) {
			return dao.addUser(user);
		}
		//로그인
		public UsersVo getUser(UsersVo userVo) {
			return dao.getUser(userVo);
		}
		//전체 회원 보기
		public List<UsersVo> getPostList() {
			return dao.getUserList();
		}
		//상세보기
		public UsersVo getUserSelect(String userId) {
		        return dao.getUserSelect(userId);
		    }
		//수정
		public void updateUser(UsersVo userVo) {
			 dao.updateUserSelect(userVo);
		}
		//삭제
		public void deleteUser(UsersVo userVo) {
			 dao.deleteUser(userVo);
		}
		
		public int getMemberCount(String user_id) {
			return getMemberCount(user_id);
		}
		
		public UsersVo findUserId(String email) {
		        return dao.findUserId(email);
		    }
		
		
		public UsersVo findUserPass(String userId,  String email) {
	        return dao.findUserPass(userId, email);
	    }
		
		public boolean iDisMatch(String userId, String email) {
	        return dao.iDisMatch(userId, email);
	    }
		
		public boolean EmailisMatch(String email) {
	        return dao.EmailisMatch(email);
	    }
		 
}