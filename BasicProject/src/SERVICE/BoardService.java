package SERVICE;

import java.util.List;

import VO.BoardVO;
import kr.or.ddit.dao.BoardDaoImpl;



public class BoardService{
	private BoardDaoImpl dao;
	
	//싱글톤 패턴 ------------------------------------------
	private static BoardService service;
	
	private BoardService(){
		dao = BoardDaoImpl.getInstance();
	}
	
	public static BoardService getInstance(){
		if(service == null){
			service = new BoardService();
		}
		return service;
	}
	//--------------------------------------------------------
	
	
	// 게시글 전체 목록을 가져와 List에 저장하여 반환하는 메서드
	public List<BoardVO> getBoardList(){
		return dao.getBoardList();
	}
	
	// 추가할 게시글 정보가 저장된 VO객체를 매개변수로 받아서 추가하는 메서드
	public int insertBoard(BoardVO boardVo) {
		return dao.insertBoard(boardVo);
	}
	
	// 게시글 번호를 매개변수로 받아서 해당 게시글의 내용을 가져와 VO에 저장하여 반환하는 메서드
	public BoardVO getBoard(int boardNo) {
		return dao.getBoard(boardNo);
	}
	
	// 수정할 게시글 정보가 저장된 VO객체를 매개변수로 받아서 수정하는 메서드
	public int updateBoard(BoardVO boardVo) {
		return dao.updateBoard(boardVo);
	}
	
	
	// 게시글 번호를 매개변수로 받아서 해당 게시글 정보를 삭제하는 메서드
	public int deleteBoard(int boardNo) {
		return dao.deleteBoard(boardNo);
	}
	
}
