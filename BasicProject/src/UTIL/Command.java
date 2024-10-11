package UTIL;

public enum Command {
	
	HOME, 			// 첫화면
	USER_HOME,		// 로그인 성공 후 화면
	END,			// 종료
	
	// 회원 관련
	LOGIN,			// 로그인
	JOIN,   		// 회원가입
	MYPAGE, 		// 내 정보 수정
	
	// 게시판 관련
	BOARD_LIST,		// 게시판 리스트
	BOARD_VIEW,		// 게시글 상세보기
	BOARD_INSERT,	// 게시글 추가
	BOARD_UPDATE,	// 게시글 수정
	BOARD_DELETE	// 게시글 삭제
	
}

