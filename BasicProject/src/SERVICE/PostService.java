package SERVICE;

import java.util.List;

import DAO.PostDao;
import VO.PostVo;



public class PostService {
	private PostDao dao;
	

	//싱글톤 패턴 --------------------------------------------------
		private static PostService service;
		
		private PostService() {
			dao = PostDao.getInstance();
		}
		
		public static PostService getInstance() {
			if(service == null) {
				service = new PostService();
			}
			return service;
		}
	//-----------------------------------------------------------
	
	

	// 게시글 전체 목록을 가져와 List에 저장하여 반환하는 메서드
		public List<PostVo> getPostList(){
			 List<PostVo> postList = dao.getAllPosts();
		        if (postList == null) {
		            System.out.println("게시물 목록이 없습니다.");
		        }
		        return postList;
			
	}
	// 추가할 게시글 정보가 저장된 VO객체를 매개변수로 받아서 추가하는 메서드
		public int insertPost(PostVo postVo) {
	        if (postVo == null) {
	            System.out.println("게시글 정보가 없습니다.");
	            return 0;
	        }
	        return dao.insertPost(postVo);
	    }
		
		// 게시물 번호를 매개변수로 받아서 해당 게시글의 내용을 가져와 VO에 저장하여 반환하는 메서드
		public PostVo getPost(int post_id) {
	        if (post_id == 0) {
	            System.out.println("유저 ID가 없습니다.");
	            return null;
	        }
	        return dao.getPost(post_id);
	    }
		
		// 수정할 게시글 정보가 저장된 VO객체를 매개변수로 받아서 수정하는 메서드
		public int updatePost(PostVo postVo) {
	        if (postVo == null) {
	            System.out.println("수정할 게시글 정보가 없습니다.");
	            return 0;
	        }
	        return dao.updatePost(postVo);
	    }
		
		
		// 게시글 번호를 매개변수로 받아서 해당 게시글 정보를 삭제하는 메서드
		public int deletePost(int postNo) {
	        return dao.deletePost(postNo);
	    }
	}