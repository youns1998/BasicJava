package UTIL;

public enum Command {
	
	HOME, 			// 첫화면
	USER_HOME,		// 로그인 성공 후 게시판 첫 화면
	ADMIN_HOME,		// 관리자 메인화면 
	ADMIN_USER,		// 관리자 회원관리화면 
	ADMIN_POST,		// 관리자 게시판관리화면
	END,			// 종료
	
	// 회원 관련
	ADMIN_USERDETAIL, // 회원관리 화면
	UESR_LIST,		// 회원 리스트 화면 
	LOGIN,			// 로그인
	JOIN,   		// 회원가입
	MYPAGE, 		// 내 정보 수정
	S_ID,			// ID 찾기
	S_PW,			// PW 찾기
	
	// 카테고리 관련
	CATEGORY_LIST,		// 카테고리
	CATEGORY_INSERT, 	// 카테고리 추가
	CATEGORY_UPDATE, 	// 카테고리 수정
	CATEGORY_DELETE, 	// 카테고리 삭제
	
	// 관심(찜) 물품 관련
	FAVORITE_LIST,		// 관심 물품
	FAVORITE_INSERT,	// 관심 물품 추가
	FAVORITE_DELETE,    // 관심 물품 삭제
	
	// 거래 기록 관련
	HISTORY_LIST,		// 거래 기록
	
	// 전체 글 
	POST_LIST,		// 전체 글 보기
	POST_DETAIL,	// 상세보기
	POST_INSERT,	// 글 추가
	POST_UPDATE,	// 글 수정
	POST_DELETE,	// 글 삭제

	// 댓글 
	COMMENT_INSERT,	// 댓글 추가
	COMMENT_UPDATE,	// 댓글 수정
	COMMENT_DELETE	// 댓글 삭제
	
}

