package DAO;
import java.sql.Timestamp;
import java.util.List;

import VO.UsersVo;
public class UserDaoTest {

	 public static void main(String[] args) {
	        UsersDao usersDao = new UsersDao();

	        // 사용자 추가 테스트
	        UsersVo newUser = new UsersVo();
	        newUser.setUser_id("testUser");
	        newUser.setEmail("test@example.com");
	        newUser.setUsername("TestUser");
	        newUser.setPhone_number("123-456-7890");
	        newUser.setAddress("Test Address");
	        newUser.setCreated_at(Timestamp.valueOf("2023-10-14 10:00:00").toLocalDateTime());
	        newUser.setUser_pass("password");

	        int addResult = usersDao.addUser(newUser);
	        System.out.println(" 사용자 추가");
	        System.out.println("Add User Result: " + addResult);

	        // 모든 사용자 조회 테스트
	        List<UsersVo> userList = usersDao.getUserList();
	        System.out.println("모든 사용자 조회");
	        System.out.println("User List:");
	        for (UsersVo user : userList) {
	            System.out.println(user.getUser_id() + " - " + user.getUsername());
	        }

	        // 사용자 상세 조회 테스트
	        UsersVo retrievedUser = usersDao.getUser_id("testUser");
	        System.out.println("상세 사용자 조회");
	        if (retrievedUser != null) {
	            System.out.println("Retrieved User: " + retrievedUser.getUsername());
	        } else {
	            System.out.println("User not found.");
	        }

	        // 사용자 정보 수정 테스트
	        newUser.setPhone_number("987-654-3210");
	        System.out.println("사용자 정보 수정");
	        int updateResult = usersDao.updateUser(newUser);
	        System.out.println("Update User Result: " + updateResult);

	        // 사용자 삭제 테스트
	        int deleteResult = usersDao.deleteUser("testUser");
	        System.out.println("사용자 삭제");
	        System.out.println("Delete User Result: " + deleteResult);
	    }
	}