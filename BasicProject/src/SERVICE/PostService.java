package SERVICE;

import java.util.List;

import DAO.PostDao;
import VO.PostVo;

public class PostService {
    private PostDao dao;
    private HistoryService historyService; // HistoryService 추가

    // 싱글톤 패턴 --------------------------------------------------
    private static PostService service;

    PostService() {
        dao = PostDao.getInstance();
        historyService = HistoryService.getInstance(); // HistoryService 싱글톤 초기화
    }

    public static PostService getInstance() {
        if (service == null) {
            service = new PostService();
        }
        return service;
    }
    // -----------------------------------------------------------

 // 게시물 상태 업데이트
    public void updatePostCondition(int postId, int conditionCode, String buyerId, String sellerId, Integer commentId) {
        // 상태 코드 유효성 검사
        if (conditionCode < 1 || conditionCode > 3) {
            throw new IllegalArgumentException("잘못된 상태: " + conditionCode);
        }

        // DAO 메서드 호출 (숫자 상태로 전달)
        dao.updatePostCondition(postId, conditionCode, buyerId, sellerId, commentId);

        // 거래 상태가 "예약중" 또는 "거래 완료"인 경우 거래 내역 추가 또는 업데이트
        HistoryService historyService = HistoryService.getInstance();  // 싱글톤 사용

        if (conditionCode == 2) {  // 예약중(2)
            historyService.processTransaction(buyerId, sellerId, postId);
            System.out.println("거래 상태가 '예약중'으로 업데이트되었습니다.");
        } else if (conditionCode == 3) {  // 거래완료(3)
            historyService.updateTransactionToComplete(buyerId, sellerId, postId);  // 거래 완료로 상태 업데이트
            System.out.println("거래 상태가 '거래 완료'로 업데이트되었습니다.");
        }
    }


    // 게시글 전체 목록을 가져와 List에 저장하여 반환하는 메서드
    public List<PostVo> getPostList() {
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

    // 게시물 번호를 받아서 해당 게시글을 가져오는 메서드
    public PostVo getPost(int post_id) {
        if (post_id == 0) {
            System.out.println("게시글 ID가 없습니다.");
            return null;
        }
        return dao.getPost(post_id);
    }

    // 회원 ID를 받아서 해당 게시글을 가져오는 메서드
    public PostVo getPostSelect(String userid) {
        if (userid == null) {
            System.out.println("등록된 회원이 없습니다.");
            return null;
        }
        return dao.getPostuser(userid);
    }

    // 회원 ID를 받아서 해당 ID가 쓴 게시물을 다 불러오는 서비스
    public List<PostVo> getPost(String userid) {
        if (userid == null) {
            System.out.println("쓴 게시글이 없습니다.");
            return null;
        }
        return dao.getAllPosts(userid);
    }

    // 수정할 게시글 정보가 저장된 VO객체를 매개변수로 받아서 전체 수정하는 메서드
    public int updatePost(PostVo postVo) {
        if (postVo == null) {
            System.out.println("수정할 게시글 정보가 없습니다.");
            return 0;
        }
        return dao.updatePost(postVo);
    }

    // 선택 수정
    public void updatePostSelect(PostVo postvo) {
        if (postvo == null) {
            System.out.println("수정할 게시글 정보가 없습니다.");
            return;
        }
        dao.updatePostSelect(postvo);
    }

    // 게시글 번호를 매개변수로 받아서 해당 게시글 정보를 삭제하는 메서드
    public int deletePost(int post_id) {
        if (post_id == 0) {
            System.out.println("게시글 번호가 없습니다.");
            return 0;
        }
        return dao.deletePost(post_id);
    }

    // 검색기능 메서드
    public List<PostVo> searchPosts(String keyword, Integer categoryId) {
	    PostDao postDao = PostDao.getInstance();
	    return postDao.searchPosts(keyword, categoryId);
	}
}
		

