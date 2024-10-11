package CONTROLLER;

import java.util.List;

import SERVICE.BoardService;
import UTIL.Command;
import UTIL.ScanUtil;
import VO.BoardVO;
import VO.UserVO;

public class BoardController {
	private BoardService boardService;
	
	private static BoardController controller;
	
	private BoardController() {
		boardService = BoardService.getInstance();
	}
	
	public static BoardController getInstance() {
		if(controller==null) controller = new BoardController();
		return controller;
	}
	
	public Command boardList() {
		List<BoardVO> boardList = boardService.getBoardList();
		
		System.out.println("=================================");
		System.out.println("번호\t제목\t작성자\t작성일");
		System.out.println("---------------------------------");
		for(BoardVO board : boardList){
			System.out.println(board.getBoard_no() + "\t"
					+ board.getTitle() + "\t"
					+ board.getUser_name() + "\t"
					+ board.getReg_date());
		}
		System.out.println("=================================");
		System.out.println("1. 게시글 상세보기\t2. 새글 등록\t0.처음으로...");
		int input = ScanUtil.nextInt("입력 >> ");
		
		switch (input) {
		case 1:
			return Command.BOARD_VIEW;
		case 2:
			return Command.BOARD_INSERT;
		case 0:
			return Command.USER_HOME;
			
		}
		
		return Command.BOARD_LIST;
	}
	
	public Command boardInsertForm() {
		System.out.print("제목 >> ");
		String title = ScanUtil.nextLine();
		
//		System.out.print("내용 >> ");
//		String content = ScanUtil.nextLine();
		String content = ScanUtil.nextLine("내용 >> ");
		
		BoardVO boardVo = new BoardVO();
		boardVo.setTitle(title);
		boardVo.setContent(content);
		
		// 로그인 한 회원의 ID를 구해서 작성자 ID로 사용한다.
		UserVO loginUser = (UserVO)MainController.sessionMap.get("loginUser");
//		boardVo.setUserId(ProjectMain.sessionLoginUser.getUserId());
		boardVo.setUser_id(loginUser.getUser_id());
		
		int result = boardService.insertBoard(boardVo);
		
		if(0 < result){
			System.out.println("게시글 등록 성공");
		}else{
			System.out.println("게시글 등록 실패");
		}
		
		return Command.BOARD_LIST;
	}
	
	public Command boardView() {
		System.out.print("게시글 번호 입력 >> ");
		int boardNo = ScanUtil.nextInt();
		
		BoardVO boardVo = boardService.getBoard(boardNo);
		if(boardVo==null) {
			System.out.println(boardNo + "번 게시글은 존재하지 않습니다.");
			return Command.BOARD_LIST;
		}
		System.out.println(boardNo + "번 게시글 상세내용");
		System.out.println("--------------------------------------");
		System.out.println("번호\t: " + boardVo.getBoard_no());
		System.out.println("작성자\t: " + boardVo.getUser_name());
		System.out.println("작성일\t: " + boardVo.getReg_date());
		System.out.println("제목\t: " + boardVo.getTitle());
		System.out.println("내용\t: " + boardVo.getContent());
		System.out.println("--------------------------------------");
		System.out.println("1. 수정\t2. 삭제\t0. 목록");
		System.out.print("입력 >> ");
		
		int input = ScanUtil.nextInt();
		
		switch (input) {
			case 1: MainController.sessionMap.put("updateBoardNo", boardNo);
					return Command.BOARD_UPDATE;
			case 2: MainController.sessionMap.put("deleteBoardNo", boardNo);
					return Command.BOARD_DELETE;
			case 0: return Command.BOARD_LIST;
		}
	
		return Command.BOARD_VIEW;
	}
	
	public Command boardUpdate() {
		if(!MainController.sessionMap.containsKey("updateBoardNo")) {
			System.out.println("수정할 게시글 번호가 없습니다.");
			return Command.BOARD_LIST;
		}
		int boardNo = (int)MainController.sessionMap.get("updateBoardNo");
		
		System.out.println("수정할 내용을 입력하세요...");
		System.out.print("새로운 제목 >> ");
		String newTitle = ScanUtil.nextLine();
		
		String newContent = ScanUtil.nextLine("새로운 내용 >> ");
		
		// 새롭게 입력한 수정할 자료를 VO객체에 저장한다.
		BoardVO boardVo = new BoardVO();
		boardVo.setBoard_no(boardNo);
		boardVo.setTitle(newTitle);
		boardVo.setContent(newContent);
		
		int result = boardService.updateBoard(boardVo);
		
		if(0 < result){
			System.out.println("게시글 수정 성공");
		}else{
			System.out.println("게시글 수정 실패");
		}
		
		return Command.BOARD_LIST;
	}
	
	
	public Command boardDelete() {
		if(!MainController.sessionMap.containsKey("deleteBoardNo")) {
			System.out.println("삭제할 게시글 번호가 없습니다.");
			return Command.BOARD_LIST;
		}
		int boardNo = (int)MainController.sessionMap.get("deleteBoardNo");
		
		int result = boardService.deleteBoard(boardNo);
		
		if(0 < result){
			System.out.println("게시글 삭제 성공");
		}else{
			System.out.println("게시글 삭제 실패");
		}
		
		return Command.BOARD_LIST;
	}
}
