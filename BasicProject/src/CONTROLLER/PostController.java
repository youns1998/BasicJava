package CONTROLLER;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import SERVICE.*;

import UTIL.ColorUtil;
import UTIL.Command;
import UTIL.ScanUtil;
import VO.CategoryVo;
import VO.CommentsVo;
import VO.PostVo;
import VO.UsersVo;

public class PostController {

	// 색상 및 서식 지정 상수
	private static final String ANSI_LIGHT_RED = "\033[38;5;203m"; // 덜한 빨간색
	private static final String ANSI_BOLD = "\033[1m"; // 볼드체
	private static final String ANSI_RESET = "\033[0m"; // 리셋
	NumberFormat formatter = NumberFormat.getInstance(); // 숫자 포맷팅
	DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // 날짜 포맷팅
	
	private static PostController instance; // 싱글톤 인스턴스
	private CommentController commentController = CommentController.getInstance(); // 댓글 컨트롤러 인스턴스
	private FavoriteController favoriteController = FavoriteController.getInstance(); // 찜 컨트롤러 인스턴스
	private HistoryController historyController = HistoryController.getInstance(); // 거래 내역 컨트롤러 인스턴스

	// 게시물 목록으로 돌아가는 메서드
	private Command returnToPostList() {
		PostController postController = PostController.getInstance(); // 싱글톤 방식으로 인스턴스 가져오기
		return postController.postList(); // 게시물 목록으로 이동
	}

	// 생성자 - 외부에서 직접 호출할 수 없도록 private 선언
	private PostController() {
	}

	// 싱글톤 인스턴스 생성 메서드
	public static PostController getInstance() {
		if (instance == null) // 인스턴스가 없을 경우 새로 생성
			instance = new PostController();
		return instance; // 이미 있을 경우 해당 인스턴스 반환
	}

	// 상세 게시글 보기 메서드 - 글 번호를 입력받아 상세 보기 실행
	public Command detailPost() {
		int postId = ScanUtil.nextInt("보고싶은 글 번호를 입력하세요: "); // 글 번호 입력받기
		return detailPost(postId); // 해당 글 번호로 상세 보기 호출
	}

	// 글 번호로 상세 게시글 보기 메서드
	public Command detailPost(int postId) {
		PostService postService = PostService.getInstance(); // 게시물 서비스 인스턴스 가져오기
		CommentsService commentsService = CommentsService.getInstance(); // 댓글 서비스 인스턴스 가져오기

		PostVo selectedPost = postService.getPost(postId); // 입력받은 글 번호로 게시물 정보 가져오기

		if (selectedPost == null) { // 게시물이 없을 경우 메시지 출력
			System.out.println("해당 게시글이 존재하지 않습니다.");
			return Command.USER_HOME; // 사용자 홈으로 이동
		}

		displayPostDetails(selectedPost); // 게시물 상세 정보 출력

		// 댓글 목록 출력
		List<CommentsVo> comments = commentsService.getComments(selectedPost.getPost_id()); // 댓글 목록 가져오기

		if (comments.isEmpty()) { // 댓글이 없을 경우
			System.out.println("댓글이 없습니다.");
		} else { // 댓글이 있을 경우
			System.out.println("+:::::::::::::::::::::::::::::::::: 댓글 목록 :::::::::::::::::::::::::::::::::::+");

			for (CommentsVo comment : comments) {
				String createdAt = comment.getFormattedCreatedAt(); // 댓글 작성 시간
				System.out.println("+------------------------------------------------------------------------------+");
				System.out.printf("| %-3s | 작성자: %-5s | 내용: %-10s | 작성 시간: %-8s \n", comment.getComment_id(),
						comment.getUser_id(), comment.getContent(), createdAt);
				System.out.println("+------------------------------------------------------------------------------+");
			}
		}

		MainController.sessionMap.put("currentPostId", postId); // 현재 보고 있는 게시물 ID 세션에 저장
		return commentMenu(postId); // 댓글 메뉴로 이동
	}

	// 댓글 메뉴 메서드
	private Command commentMenu(int postId) {
		UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser"); // 로그인한 사용자 정보 가져오기
		PostService postService = PostService.getInstance(); // 게시물 서비스 인스턴스
		PostVo post = postService.getPost(postId); // 게시물 정보 가져오기

		System.out.println("1. 댓글 달기 2. 댓글 수정 3. 댓글 삭제 4. 찜하기 0. 전체 게시물 보러가기");

		if (post.getUser_id().equals(loginUserVo.getUser_id())) { // 게시글 작성자인 경우
			System.out.println("5. 판매글 수정  6. 판매 상태 변경"); // 수정, 상태 변경 옵션 추가
		}

		System.out.print("메뉴 선택 >> ");
		int choice = ScanUtil.nextInt(); // 사용자 선택 입력받기

		switch (choice) {
		case 1:
			return commentController.insertComment(postId); // 댓글 달기
		case 2:
			return commentController.updateComment(postId); // 댓글 수정
		case 3:
			return commentController.deleteComment(postId); // 댓글 삭제
		case 4:
			return favoriteController.addFavorite(postId); // 찜하기
		case 5:
			return postUpdate(postId); // 게시글 수정
		case 6:
			return changePostCondition(postId); // 게시글 상태 수정
		case 0:
			return returnToPostList(); // 게시글 목록으로 이동
		default:
			System.out.println("잘못된 선택입니다. 다시 시도하세요."); // 잘못된 입력 처리
			return commentMenu(postId); // 다시 댓글 메뉴로 이동
		}
	}

	// 댓글 관리 기능들 (view, update, delete)
	private Command viewComments(int postId) {
		return Command.POST_LIST;
	}

	private Command updateComment(int postId) {
		return Command.POST_LIST;
	}

	private Command deleteComment(int postId) {
		return Command.POST_LIST;
	}

	// 게시물 상세보기 출력 메서드
	private void displayPostDetails(PostVo post) {
		CommentsService commentsService = CommentsService.getInstance(); // 댓글 서비스 인스턴스
		FavoriteService favoriteService = FavoriteService.getInstance(); // 찜 서비스 인스턴스
		UsersService userService = UsersService.getInstance();	
		PostService postService = PostService.getInstance(); // 게시물 서비스 인스턴스
		CategoryService categoryService = CategoryService.getInstance(); // 카테고리 서비스 인스턴스

		UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser"); // 로그인한 사용자 정보
		
		int commentCount = commentsService.getCommentCount(post.getPost_id()); // 댓글 개수 가져오기
		boolean isFavorite = favoriteService.isFavoriteExists(loginUserVo.getUser_id(), post.getPost_id()); // 찜 여부 확인
		String categoryName = categoryService.getCategoryNameById(post.getCategory_id()); // 카테고리 이름 가져오기
		
		// 게시물 상세 정보 출력
		String borderLine = "+==============================================================================+";
		System.out.println(borderLine);
		
		// 게시물 상태를 조건에 맞게 출력
		String condition;
		switch (post.getCondition()) {
		case 1:
			condition = "판매중";
			break;
		case 2:
			condition = "예약중";
			break;
		case 3:
			condition = "거래완료";
			break;
		default:
			condition = "알 수 없음"; // 기본값 설정
		}
		if(post.getRole()!=0) {
			// 제목, 작성자 출력
			System.out.printf("| 제목: %-20s \n| 작성자: %-50s  \n", post.getTitle(), post.getUser_id());
			System.out.println(borderLine);

			// 내용 출력
			System.out.printf("| 내용: %-72s \n", post.getContent());
			System.out.printf("| %-78s \n", "");
			System.out.println(borderLine);

			LocalDateTime createdAt = post.getCreated_at(); // 작성 시간
			LocalDateTime updatedAt = post.getUpdated_at(); // 수정 시간

			// 작성 및 수정 시간 출력
			System.out.printf("| 작성 시간: %-40s \n", createdAt.format(formatter1));
			System.out.printf("| 수정 시간: %-61s \n", updatedAt.format(formatter1));
			System.out.println(borderLine);

			// 댓글 수와 찜한 사람 수 출력
			System.out.printf("| 댓글 수: %-48d \n ", commentCount);
			System.out.println(borderLine);
			
		}else {
		// 제목, 가격, 상태, 작성자 출력
		System.out.printf("| 제목: %-20s 가격: %-20s 상태: %-10s \n| 작성자: %-50s  %s\n", post.getTitle(),
				formatter.format(post.getPrice()) + "원", condition, post.getUser_id(), isFavorite ? "♡ 찜한 상품" : " ");
		System.out.println(borderLine);

		// 내용 출력
		System.out.printf("| 내용: %-72s \n", post.getContent());
		System.out.printf("| %-78s \n", "");
		System.out.println(borderLine);

		LocalDateTime createdAt = post.getCreated_at(); // 작성 시간
		LocalDateTime updatedAt = post.getUpdated_at(); // 수정 시간

		// 작성 및 수정 시간 출력
		System.out.printf("| 작성 시간: %-40s        카테고리: %s \n", createdAt.format(formatter1), categoryName);
		System.out.printf("| 수정 시간: %-61s \n", updatedAt.format(formatter1));
		System.out.println(borderLine);

		// 댓글 수와 찜한 사람 수 출력
		System.out.printf("| 댓글 수: %-48d 찜한 사람 : %s \n ", commentCount,
				favoriteService.countFavoritesForPost(post.getPost_id()));
		System.out.println(borderLine);
		}
	}
	// 내가 쓴 게시물 보기 메서드
	public Command userPost() {
		PostService postService = PostService.getInstance(); // 게시물 서비스 인스턴스
		UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser"); // 로그인한 사용자 정보
		List<PostVo> posts = postService.getPost(loginUserVo.getUser_id()); // 사용자가 작성한 게시물 리스트

		if (loginUserVo.getRole() != 0) { // 관리자가 아니면
			return Command.POST_ADMIN; // 관리자용 게시물 관리 화면으로 이동
		}

		if (posts.isEmpty()) { // 게시물이 없을 경우
			System.out.println("작성된 게시물이 없습니다.");
		} else { // 게시물이 있을 경우
			for (PostVo post : posts) {
				// 게시물 정보 출력
				System.out.println("게시물 번호: " + post.getPost_id());
				System.out.println("제목: " + post.getTitle());
				System.out.println("내용: " + post.getContent());
				System.out.println("가격: " + formatter.format(post.getPrice()) + "원");
				System.out.println("작성일: " + post.getCreated_at());
				System.out.println("수정일: " + post.getUpdated_at());
				System.out.println("현재 상태: " + post.getCondition());
				System.out.println("------------------------------");
			}
		}
		return Command.MYPAGE; // 마이페이지로 돌아감
	}

	// 관리자가 회원별 작성한 게시물 보기 메서드
	public Command adminPost() {
		PostService postService = PostService.getInstance(); // 게시물 서비스 인스턴스
		UsersService usersService = UsersService.getInstance(); // 사용자 서비스 인스턴스
		String userId = ScanUtil.nextLine("게시물 리스트를 조회할 회원 ID >> "); // 회원 ID 입력받기
		UsersVo user = usersService.getUserSelect(userId); // 해당 회원 정보 가져오기
		 // 입력한 사용자 ID가 없을 경우
        if (user == null) {
            int choice = ScanUtil.nextInt("등록된 회원이 아닙니다 \n1. 다시 조회 0.뒤로 가기 >>");
            if(choice==1) {return Command.POST_ADMIN;} // 게시글 조회 창으로 다시 감}
            else {return Command.USER_LIST;} // 게시글 조회 창으로 다시 감
        }
		
		List<PostVo> posts = postService.getPost(userId); // 해당 회원이 작성한 게시물 목록 가져오기
		if (posts.isEmpty()) { // 게시물이 없을 경우
			System.out.println("작성된 게시물이 없습니다.");
		} else { // 게시물이 있을 경우
			for (PostVo post : posts) {
				// 게시물 정보 출력
				System.out.println("게시물 번호: " + post.getPost_id());
				System.out.println("제목: " + post.getTitle());
				System.out.println("내용: " + post.getContent());
				System.out.println("가격: " + formatter.format(post.getPrice()) + "원");
				System.out.println("작성일: " + post.getCreated_at());
				System.out.println("수정일: " + post.getUpdated_at());
				System.out.println("현재 상태: " + post.getCondition());
				System.out.println("------------------------------");
			}
		}
		return Command.ADMIN_USERDETAIL; // 관리자의 사용자 상세보기로 돌아감
	}

	// 게시물 목록 출력 메서드 (공지사항 포함)
	public Command postList() {
	    int width = 80; // 출력 폭 설정
	    int pageSize = 10; // 페이지당 게시물 수 (공지사항 제외)
	    int currentPage = 1; // 현재 페이지 설정
	    PostService postService = PostService.getInstance(); // 게시물 서비스 인스턴스
	    List<PostVo> posts = postService.getPostList(); // 모든 게시물 가져오기

	    if (posts == null || posts.isEmpty()) { // 게시물이 없을 경우
	        System.out.println("작성된 게시물이 없습니다");
	        return Command.USER_HOME; // 사용자 홈으로 이동
	    }

	    // 공지사항 필터링 (작성자 ID가 "1"인 게시물은 공지사항으로 간주)
	    List<PostVo> noticePosts = new ArrayList<>();
	    List<PostVo> generalPosts = new ArrayList<>();
	    for (PostVo post : posts) {
	        if (post.getUser_id().equals("1")) {
	            noticePosts.add(post); // 공지사항에 추가
	        } else {
	            generalPosts.add(post); // 일반 게시물에 추가
	        }
	    }

	    int totalGeneralPosts = generalPosts.size(); // 전체 일반 게시물 수
	    int totalPages = (int) Math.ceil((double) totalGeneralPosts / pageSize); // 페이지 수 계산

	    while (true) {
	        // 공지사항은 항상 출력
	        System.out.println("+" + "=".repeat(width - 2) + "+"); // 테두리 출력
	        for (PostVo post : noticePosts) {
	            // 공지사항은 제목만 빨간색 볼드체로 출력 (상태, 가격 생략)
	            String content = String.format("%-2d | %10s#공지사항 : %-50s%s  ", 
	                post.getPost_id(),
	                ANSI_BOLD + ANSI_LIGHT_RED, // 공지사항을 빨간색 볼드체로 출력
	                post.getTitle(),
	                ANSI_RESET // 색상 및 볼드체 초기화
	            );
	            System.out.println(ANSI_BOLD+ANSI_LIGHT_RED + "+" + "-".repeat(width - 2) + "+" + ANSI_RESET); // 공지사항 테두리 빨간색
	            System.out.printf(ANSI_BOLD+ANSI_LIGHT_RED + "| %-" + (width - 3) + "s\n" + ANSI_RESET, content); // 공지사항 내용
	            System.out.println(ANSI_BOLD+ANSI_LIGHT_RED + "+" + "-".repeat(width - 2) + "+" + ANSI_RESET); // 테두리 닫기
	        }

	        // 현재 페이지에 맞는 일반 게시물 출력
	        int start = (currentPage - 1) * pageSize; // 시작 인덱스 계산
	        int end = Math.min(start + pageSize, totalGeneralPosts); // 끝 인덱스 계산
	        List<PostVo> pagePosts = generalPosts.subList(start, end); // 해당 페이지에 해당하는 게시물 가져오기

	        // 일반 게시물 출력
	        for (PostVo post : pagePosts) {
	            String statusColor = getStatusColor(post.getCondition()); // 상태에 맞는 색상 지정
	            String content = String.format("%-2d |  제목: %-10s  %-10s    |  작성자: %-6s    |  조회: %-10s\n|    |  가격: %-5s", 
	                post.getPost_id(),
	                post.getTitle(),
	                statusColor + getStatus(post.getCondition()) + ColorUtil.RESET,
	                post.getUser_id(),
	                post.getView_count(),
	                formatter.format(post.getPrice()) + "원"
	            );
	            printAsciiArtBox(content, false); // 게시물 출력
	        }

	        System.out.println("+" + "=".repeat(width - 2) + "+"); // 테두리 출력

	        // 페이지 이동 옵션 출력
	        System.out.print("페이지: ");
	        for (int i = 1; i <= totalPages; i++) {
	            if (i == currentPage) {
	                System.out.print("\033[1m" + i + "\033[0m "); // 현재 페이지는 볼드체로 표시
	            } else {
	                System.out.print(i + " ");
	            }
	        }
	        System.out.println();

	        // 페이지 이동 메뉴 출력
	        int input = ScanUtil.nextInt("1.이전 페이지 2.다음 페이지 3.판매 글 작성 4.상세 보기 5.검색 0.내 화면으로 \n 메뉴 선택 >> ");
	        switch (input) {
	            case 1: // 이전 페이지로 이동
	                if (currentPage > 1) {
	                    currentPage--;
	                } else {
	                    System.out.println("첫 페이지입니다.");
	                }
	                break;
	            case 2: // 다음 페이지로 이동
	                if (currentPage < totalPages) {
	                    currentPage++;
	                } else {
	                    System.out.println("마지막 페이지입니다.");
	                }
	                break;
	            case 3: // 게시글 작성
	                return Command.POST_INSERT;
	            case 4: // 상세보기
	                MainController.sessionMap.remove("currentPostId");
	                return Command.POST_DETAIL;
	            case 5: // 검색 기능
	                return postSearch(); // 검색 기능 추가
	            case 0: // 사용자 홈으로 돌아가기
	                return Command.USER_HOME;
	            default:
	                System.out.println("잘못된 선택입니다. 다시 시도하세요.");
	        }
	    }
	}

	// 게시물 상태 반환
	private String getStatus(int condition) {
		switch (condition) {
			case 1:
				return "판매중";
			case 2:
				return "예약중";
			case 3:
				return "거래 완료";
			default:
				return "알 수 없음";
		}
	}

	// 게시물 상태에 따라 색상 지정
	private String getStatusColor(int condition) {
		switch (condition) {
			case 1:
				return ColorUtil.RED;  // 판매중은 빨간색
			case 2:
				return ColorUtil.GREEN;  // 예약중은 초록색
			case 3:
				return ColorUtil.GRAY;  // 거래 완료는 회색
			default:
				return ColorUtil.RESET;  // 기본 색상
		}
	}

	// 아스키 아트로 출력하는 메서드
	private void printAsciiArtBox(String content, boolean isLast) {
		int width = 80; // 너비 설정
		String borderLine = "+" + "-".repeat(width - 2) + "+"; // 테두리 설정
		System.out.println(borderLine); // 테두리 출력
		System.out.printf("| %-" + (width - 3) + "s\n", content); // 게시물 내용 출력
		if (!isLast) { // 마지막이 아닌 경우
			System.out.println(borderLine); // 테두리 출력
		}
	}

	// 게시글 추가 메서드
	public Command postInsert() {
		PostService postService = PostService.getInstance(); // 게시물 서비스 인스턴스
		PostVo post = new PostVo(); // 새 게시물 객체 생성
		UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser"); // 로그인 사용자 정보

		if (loginUserVo.getRole() != 0) { // 관리자가 공지사항 추가
			System.out.println("<<공지사항 쓰기>>");
			String title = ScanUtil.nextLine("제목 >> ");
			String content = ScanUtil.nextLine("내용 >> ");
			post.setTitle(title); // 제목 설정
			post.setContent(content); // 내용 설정
			post.setUser_id(loginUserVo.getUser_id()); // 작성자 ID 설정
			post.setRole(loginUserVo.getRole());
		} else { // 일반 사용자의 게시글 추가
			String Title = ScanUtil.nextLine("글 제목 >> ");
			int price = ScanUtil.nextInt("가격 >> ");
			CategoryService cateservice = CategoryService.getInstance(); // 카테고리 서비스 인스턴스
			List<CategoryVo> catevo = cateservice.getCategoryList(); // 카테고리 목록 가져오기
			for (CategoryVo category : catevo) {
				// 카테고리 목록 출력
				System.out.println("분류번호: " + category.getCategory_id() + ", 카테고리: " + category.getCategory_name());
			}
			int category = ScanUtil.nextInt("카테고리 >> "); // 카테고리 선택
			String content = ScanUtil.nextLine("글 내용 입력 >> "); // 게시물 내용 입력
			post.setTitle(Title); // 제목 설정
			post.setPrice(price); // 가격 설정
			post.setCategory_id(category); // 카테고리 설정
			post.setContent(content); // 내용 설정
			post.setCondition(1); // 상태 설정
			post.setRole(loginUserVo.getRole());
			post.setUser_id(loginUserVo.getUser_id()); // 작성자 ID 설정
		}
		int result = postService.insertPost(post); // 게시글 추가
		if (result > 0) { // 성공적으로 등록된 경우
			System.out.println("게시글이 성공적으로 등록되었습니다.");
		} else { // 실패한 경우
			System.out.println("▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄\r\n" + "████▌▄▌▄▐▐▌█████\r\n" + "████▌▄▌▄▐▐▌▀████\r\n"
					+ "▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀\r\n" + "");
		}
		return Command.POST_LIST; // 게시물 목록으로 이동
	}

	// 게시글 수정 메서드
	// 기존 방식: 사용자가 직접 글 번호를 입력
	public Command postUpdate() {
		UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser"); // 로그인 사용자 정보
		int choice = ScanUtil.nextInt("수정할 내 글 번호를 입력하세요: "); // 수정할 글 번호 입력받기
		PostService postService = PostService.getInstance(); // 게시물 서비스 인스턴스
		PostVo post = postService.getPost(choice); // 해당 게시물 가져오기
		if (post == null) { // 게시물이 없을 경우
			System.out.println("해당 게시물을 찾을 수 없습니다.");
			return Command.POST_LIST; // 게시물 목록으로 돌아감
		}
		if (post.getUser_id().equals(loginUserVo.getUser_id()) || loginUserVo.getRole() != 0) { // 본인 글일 경우
			postService.updatePostSelect(post); // 게시물 수정 처리
		} else { // 본인 글이 아닐 경우
			System.out.println("다른 사용자의 글은 수정할 수 없습니다.");
		}
		return Command.POST_LIST; // 게시물 목록으로 돌아감
	}

	// 새로운 방식: 이미 글 번호를 알고 있는 경우 (글 상세보기 상태)
	public Command postUpdate(int postId) {
		UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser"); // 로그인 사용자 정보
		PostService postService = PostService.getInstance(); // 게시물 서비스 인스턴스
		PostVo post = postService.getPost(postId); // 게시물 가져오기

		if (post.getUser_id().equals(loginUserVo.getUser_id()) || loginUserVo.getRole() != 0) { // 본인 글일 경우
			postService.updatePostSelect(post); // 게시물 수정 처리
		} else { // 본인 글이 아닐 경우
			System.out.println("다른 사용자의 글은 수정할 수 없습니다.");
		}
		return Command.POST_LIST; // 게시물 목록으로 돌아감
	}

	// 게시글 삭제 메서드: 이미 글 번호를 알고 있는 경우 (글 상세보기 상태)
	public Command postDelete(int postId) {
		UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser"); // 로그인 사용자 정보
		PostService postService = PostService.getInstance(); // 게시물 서비스 인스턴스
		PostVo post = postService.getPost(postId); // 게시물 정보 가져오기

		if (post.getUser_id().equals(loginUserVo.getUser_id()) || loginUserVo.getRole() != 0) { // 본인 글일 경우
			postService.deletePost(post.getPost_id()); // 게시물 삭제
		} else { // 본인 글이 아닐 경우
			System.out.println("다른 사용자의 글은 삭제할 수 없습니다.");
		}
		return Command.POST_LIST; // 게시물 목록으로 돌아감
	}

	// 게시글 삭제 메서드 (삭제할 글 번호를 직접 입력)
	public Command postDelete() {
		UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser"); // 로그인 사용자 정보
		int choice = ScanUtil.nextInt("삭제할 글 번호를 입력하세요: "); // 삭제할 글 번호 입력받기
		PostService postService = PostService.getInstance(); // 게시물 서비스 인스턴스
		PostVo post = postService.getPost(choice); // 게시물 정보 가져오기
		if (post == null) { // 게시물이 없을 경우
			System.out.println("해당 게시물을 찾을 수 없습니다.");
			return Command.POST_LIST; // 게시물 목록으로 돌아감
		}

		if (post.getUser_id().equals(loginUserVo.getUser_id()) || loginUserVo.getRole() != 0) { // 본인 글일 경우
			postService.deletePost(post.getPost_id()); // 게시물 삭제
		} else { // 본인 글이 아닐 경우
			System.out.println("다른 사용자의 글은 삭제할 수 없습니다.");
		}
		return Command.POST_LIST; // 게시물 목록으로 돌아감
	}

	// 게시물 상태 업데이트 메서드
	public void updatePostCondition(int postId, String newCondition, String sellerId) {
	    PostService postService = PostService.getInstance(); // 게시물 서비스 인스턴스
	    String buyerId = null; // 구매자 ID를 저장할 변수
	    HistoryService historyService = HistoryService.getInstance(); // 거래 내역 서비스 인스턴스

	    // 상태 변환 처리 (문자열 상태를 숫자로 변환)
	    int conditionCode;
	    switch (newCondition) {
	        case "1":
	            conditionCode = 1; // 판매중 상태
	            break;
	        case "2":
	            conditionCode = 2; // 예약중 상태
	            // 구매자 ID 입력받기
	            buyerId = ScanUtil.nextLine("구매자 ID를 입력하세요: ");
	            if (buyerId == null || buyerId.isEmpty()) {
	                System.out.println("유효하지 않은 구매자 ID입니다.");
	                return;
	            }
	            break;
	        case "3":
	            conditionCode = 3; // 거래 완료 상태
	            // 예약된 구매자 정보 가져오기
	            buyerId = historyService.getBuyerIdFromTransaction(postId);
	            if (buyerId == null || buyerId.isEmpty()) {
	                buyerId = ScanUtil.nextLine("거래 완료를 위한 구매자 ID를 입력하세요: ");
	                if (buyerId == null || buyerId.isEmpty()) {
	                    System.out.println("유효하지 않은 구매자 ID입니다.");
	                    return;
	                }
	            }
	            break;
	        default:
	            throw new IllegalArgumentException("잘못된 상태: " + newCondition);
	    }

	    // 상태 업데이트 및 거래 내역 처리
	    postService.updatePostCondition(postId, conditionCode, buyerId, sellerId, null);
	    System.out.println("게시물 상태가 업데이트되었습니다.");
	}

	// 게시물 상태 변경 메뉴 메서드
	private Command changePostCondition(int postId) {
	    PostService postService = PostService.getInstance(); // 게시물 서비스 인스턴스
	    UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser"); // 로그인 사용자 정보
	    PostVo post = postService.getPost(postId); // 게시물 정보 가져오기

	    if (post == null || !post.getUser_id().equals(loginUserVo.getUser_id())) { // 본인 글이 아닌 경우
	        System.out.println("해당 게시물을 수정할 수 없습니다.");
	        return Command.POST_LIST; // 게시물 목록으로 돌아감
	    }

	    // 상태 변경 메뉴 출력
	    System.out.println("1. 판매중으로 변경 2. 예약중으로 변경 3. 거래 완료로 변경");
	    int choice = ScanUtil.nextInt("선택 >> ");

	    switch (choice) {
	        case 1:
	            postService.updatePostCondition(postId, 1, null, post.getUser_id(), null);  // 판매중으로 상태 변경
	            System.out.println("상태가 '판매중'으로 변경되었습니다.");
	            break;
	        case 2:
	            String buyerId = ScanUtil.nextLine("예약할 구매자 ID를 입력하세요: ");
	            postService.updatePostCondition(postId, 2, buyerId, post.getUser_id(), 1);  // 예약중으로 상태 변경
	            System.out.println("상태가 '예약중'으로 변경되었습니다.");
	            break;
	        case 3:
	            postService.updatePostCondition(postId, 3, null, post.getUser_id(), null);  // 거래 완료로 상태 변경
	            System.out.println("상태가 '거래 완료'로 변경되었습니다.");
	            break;
	        default:
	            System.out.println("잘못된 선택입니다.");
	            break;
	    }

	    return Command.POST_LIST; // 게시물 목록으로 돌아감
	}

	// 게시물 검색 메서드
	public Command postSearch() {
		System.out.println("검색 방법을 선택하세요:");
		System.out.println("1. 키워드로 검색");
		System.out.println("2. 카테고리로 검색");

		int choice = ScanUtil.nextInt("선택 >> "); // 검색 옵션 선택

		PostService postService = PostService.getInstance(); // 게시물 서비스 인스턴스

		if (choice == 1) { // 키워드로 검색
			String keyword = ScanUtil.nextLine("검색할 키워드를 입력하세요: ");
			List<PostVo> results = postService.searchPosts(keyword, null); // 키워드로 검색

			if (results.isEmpty()) { // 검색 결과가 없을 경우
				System.out.println("검색 결과가 없습니다.");
			} else { // 검색 결과가 있을 경우
				for (PostVo post : results) {
					// 검색 결과 출력
					System.out.printf("게시물 번호: %d | 제목: %s | 가격: %d | 상태: %d\n", post.getPost_id(), post.getTitle(),
							post.getPrice(), post.getCondition());
				}
			}
		} else if (choice == 2) { // 카테고리로 검색
			CategoryService categoryService = CategoryService.getInstance(); // 카테고리 서비스 인스턴스
			List<CategoryVo> categories = categoryService.getCategoryList(); // 카테고리 목록 가져오기
			System.out.println("카테고리 목록:");
			for (CategoryVo category : categories) { // 카테고리 출력
				System.out.printf("분류번호: %d, 이름: %s\n", category.getCategory_id(), category.getCategory_name());
			}

			int categoryId = ScanUtil.nextInt("검색할 분류번호를 입력하세요: "); // 검색할 카테고리 선택

			List<PostVo> results = postService.searchPosts(null, categoryId); // 카테고리로 검색

			if (results.isEmpty()) { // 검색 결과가 없을 경우
				System.out.println("검색 결과가 없습니다.");
			} else { // 검색 결과가 있을 경우
				for (PostVo post : results) {
					String condition;
					switch (post.getCondition()) {
					case 1:
						condition = "판매중";
						break;
					case 2:
						condition = "예약중";
						break;
					case 3:
						condition = "거래완료";
						break;
					default:
						condition = "알 수 없음"; // 기본값 설정
					}
					// 검색 결과 출력
					System.out.printf("게시물 번호: %d | 제목: %s | 가격: %d | 상태: %s\n", post.getPost_id(), post.getTitle(),
							post.getPrice(), condition);
				}
			}
		} else {
			System.out.println("잘못된 선택입니다."); // 잘못된 선택 처리
		}

		return Command.POST_LIST; // 검색 후 게시물 목록으로 돌아감
	}
}
