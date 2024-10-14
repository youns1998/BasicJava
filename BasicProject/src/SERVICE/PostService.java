package SERVICE;

import java.util.List;

import VO.PostVo;                                                             
import DAO.PostDAO;



public class PostService {
	private PostDAO dao;
	
	// 게시글 전체 목록을 가져와 List에 저장하여 반환하는 메서드
		public List<PostVo> getPostList(){
			return dao.getPostList();
	}
	// 추가할 게시글 정보가 저장된 VO객체를 매개변수로 받아서 추가하는 메서드
		public int insertPost(PostVo postVo) {
			return dao.insertPost(postVo);
		}
		
		// 게시글 번호를 매개변수로 받아서 해당 게시글의 내용을 가져와 VO에 저장하여 반환하는 메서드
		public PostVo getPost(int PostNo) {
			return dao.getPost(PostNo);
		}
		
		// 수정할 게시글 정보가 저장된 VO객체를 매개변수로 받아서 수정하는 메서드
		public int updatePost(PostVo postVo) {
			return dao.updatePost(postVo);
		}
		
		
		// 게시글 번호를 매개변수로 받아서 해당 게시글 정보를 삭제하는 메서드
		public int deletePost(int postNo) {
			return dao.deletePost(postNo);
		}
	
	
	
}