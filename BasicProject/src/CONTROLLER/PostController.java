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

	private static final String ANSI_LIGHT_RED = "\033[38;5;203m"; // 덜한 빨간색
	private static final String ANSI_BOLD = "\033[1m";
	private static final String ANSI_RESET = "\033[0m";
	NumberFormat formatter = NumberFormat.getInstance();
	DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private static PostController instance;
	private CommentController commentController = CommentController.getInstance();
	private FavoriteController favoriteController = FavoriteController.getInstance();
	private HistoryController historyController = HistoryController.getInstance();

	
	private Command returnToPostList() {
		PostController postController = PostController.getInstance();
		return postController.postList();
	}

	private PostController() {
	}

	public static PostController getInstance() {
		if (instance == null)
			instance = new PostController();
		return instance;
	}

	// 상세 게시글 보기
	public Command detailPost() {
		int postId = ScanUtil.nextInt("보고싶은 글 번호를 입력하세요: ");
		return detailPost(postId);
	}

	public Command detailPost(int postId) {
		PostService postService = PostService.getInstance();
		CommentsService commentsService = CommentsService.getInstance();

		PostVo selectedPost = postService.getPost(postId);

		if (selectedPost == null) {
			System.out.println("해당 게시글이 존재하지 않습니다.");
			return Command.USER_HOME;
		}

		displayPostDetails(selectedPost);

		// 댓글 출력 부분
		List<CommentsVo> comments = commentsService.getComments(selectedPost.getPost_id());

		if (comments.isEmpty()) {
			System.out.println("댓글이 없습니다.");
		} else {
			System.out.println("+:::::::::::::::::::::::::::::::::: 댓글 목록 :::::::::::::::::::::::::::::::::::+");

			for (CommentsVo comment : comments) {
				String createdAt = comment.getFormattedCreatedAt(); // 작성 시간에서 분까지만 추출
				System.out.println("+------------------------------------------------------------------------------+");
				System.out.printf("| %-3s | 작성자: %-5s | 내용: %-10s | 작성 시간: %-8s \n", comment.getComment_id(),
						comment.getUser_id(), comment.getContent(), createdAt);
				System.out.println("+------------------------------------------------------------------------------+");

			}
		}
 
		MainController.sessionMap.put("currentPostId", postId);
		return commentMenu(postId);
	}

	// 댓글 메뉴 메서드
	private Command commentMenu(int postId) {
		UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser");
		PostService postService = PostService.getInstance();
		PostVo post = postService.getPost(postId);

		System.out.println("1. 댓글 달기 2. 댓글 수정 3. 댓글 삭제 4. 찜하기 0. 전체 게시물 보러가기");


		if (post.getUser_id().equals(loginUserVo.getUser_id())) {
			System.out.println("5. 판매글 수정  6. 판매 상태 변경");
		}

		System.out.print("메뉴 선택 >> ");
		int choice = ScanUtil.nextInt();


		switch (choice) {
		case 1:
			return commentController.insertComment(postId);
		case 2:
			return commentController.updateComment(postId);
		case 3:
			return commentController.deleteComment(postId);
		case 4:
			return favoriteController.addFavorite(postId);
		case 5:
			return postUpdate(postId); // 게시물 ID를 자동으로 전달하여 수정 화면으로 이동
		case 6:
			return postDelete(postId);
		case 0:
			return returnToPostList();
		default:
			System.out.println("잘못된 선택입니다. 다시 시도하세요.");
			return commentMenu(postId);
		}

	}


	// 댓글, 수정, 삭제 기능을 유지
	private Command viewComments(int postId) {
		return Command.POST_LIST;
	}

	private Command updateComment(int postId) {
		return Command.POST_LIST;
	}

	private Command deleteComment(int postId) {
		return Command.POST_LIST;
	}

	// 게시물 상세보기
	private void displayPostDetails(PostVo post) {
		CommentsService commentsService = CommentsService.getInstance();
		FavoriteService favoriteService = FavoriteService.getInstance();
		UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser");

		PostService postService = PostService.getInstance();
		int commentCount = commentsService.getCommentCount(post.getPost_id());
		boolean isFavorite = favoriteService.isFavoriteExists(loginUserVo.getUser_id(), post.getPost_id());

		CategoryService categoryService = CategoryService.getInstance();
		String categoryName = categoryService.getCategoryNameById(post.getCategory_id()); // 카테고리 이름 가져오기

		String borderLine = "+==============================================================================+";
		System.out.println(borderLine);

		// 직접 출력으로 변경
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
		System.out.printf("| 제목: %-20s 가격: %-20s 상태: %-10s \n| 작성자: %-50s  %s\n", post.getTitle(),
				formatter.format(post.getPrice()) + "원", condition, post.getUser_id(), isFavorite ? "♡ 찜한 상품" : " ");
		System.out.println(borderLine);

		// 내용 출력
		System.out.printf("| 내용: %-72s \n", post.getContent());
		System.out.printf("| %-78s \n", "");
		System.out.println(borderLine);

		LocalDateTime createdAt = post.getCreated_at();
		LocalDateTime updatedAt = post.getUpdated_at();

		// 작성 시간 및 수정 시간 출력
		System.out.printf("| 작성 시간: %-40s        카테고리: %s \n", createdAt.format(formatter1), categoryName);
		System.out.printf("| 수정 시간: %-61s \n", updatedAt.format(formatter1));
		System.out.println(borderLine);

		// 댓글 수와 찜한 사람 수 출력

		System.out.printf("| 댓글 수: %-48d 찜한 사람 : %s \n ", commentCount,
				favoriteService.countFavoritesForPost(post.getPost_id()));
		System.out.println(borderLine);
	}

	

	// 내가 쓴 게시물 보기
	public Command userPost() {
		PostService postService = PostService.getInstance();
		UsersService usersService = UsersService.getInstance();
		UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser");
		List<PostVo> posts = postService.getPost(loginUserVo.getUser_id());
		if (loginUserVo.getRole() != 0) {
			return Command.POST_ADMIN;
		}
		if (posts.isEmpty()) {
			System.out.println("작성된 게시물이 없습니다.");
		} else {
			for (PostVo post : posts) {
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
		return Command.MYPAGE;
	}

	// 관리자가 회원별 쓴 게시물 보기
	public Command adminPost() {
		PostService postService = PostService.getInstance();
		UsersService usersService = UsersService.getInstance();
		String userId = ScanUtil.nextLine("게시물 리스트를 조회할 회원 ID >> ");
		  UsersVo user = usersService.getUserSelect(userId);
		 if(user==null) {						
	        	System.out.println("등록된 회원이 아닙니다");
	        	return Command.USER_LIST;
		 }
		List<PostVo> posts = postService.getPost(userId);
		if (posts.isEmpty()) {
			System.out.println("작성된 게시물이 없습니다.");
		} else {
			for (PostVo post : posts) {
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
		return Command.ADMIN_USERDETAIL;
	}

	// 게시물 목록 출력############################################################################
	public Command postList() {
	    int width = 80;
	    int pageSize = 10;  // 페이지당 게시물 수 (공지사항 제외)
	    int currentPage = 1;  // 현재 페이지
	    PostService postService = PostService.getInstance();
	    UsersService usersService = UsersService.getInstance();
	    UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser");
	    List<PostVo> posts = postService.getPostList();
	    
	    if (posts == null || posts.isEmpty()) {
	        System.out.println("작성된 게시물이 없습니다");
	        return Command.USER_HOME;
	    }

	    // 공지사항 필터링 (작성자 ID가 "1"인 게시물)
	    List<PostVo> noticePosts = new ArrayList<>();
	    List<PostVo> generalPosts = new ArrayList<>();
	    for (PostVo post : posts) {
	        if (post.getUser_id().equals("1")) {
	            noticePosts.add(post);  // 공지사항으로 분류
	        } else {
	            generalPosts.add(post);  // 일반 게시물로 분류
	        }
	    }

	    int totalGeneralPosts = generalPosts.size();
	    int totalPages = (int) Math.ceil((double) totalGeneralPosts / pageSize);  // 공지사항은 제외한 게시물 수로 페이지 계산

	    while (true) {
	        // 공지사항은 항상 출력
	        System.out.println("+" + "=".repeat(width - 2) + "+");  // 공지사항 테두리 빨간색
	        for (PostVo post : noticePosts) {
	            // 공지사항 내용 출력 (상태, 가격 생략)
	            String content = String.format("%-2d | %10s###########공지사항 : %-50s%s  ", 
	                post.getPost_id(),
	                ANSI_BOLD + ANSI_LIGHT_RED,  // 빨간색 볼드체 시작	               
	                post.getTitle(),
	                ANSI_RESET,  // 색상과 볼드체 초기화
	                post.getUser_id()
	            );
	            System.out.println(ANSI_BOLD+ANSI_LIGHT_RED + "+" + "-".repeat(width - 2) + "+" + ANSI_RESET);  // 공지사항 내부 테두리 빨간색
	            System.out.printf(ANSI_BOLD+ANSI_LIGHT_RED + "| %-" + (width - 3) + "s\n" + ANSI_RESET, content);  // 공지사항 내용 출력
	            System.out.println(ANSI_BOLD+ANSI_LIGHT_RED + "+" + "-".repeat(width - 2) + "+" + ANSI_RESET);  // 공지사항 하단 테두리 빨간색
	        }

	        // 현재 페이지에 맞는 일반 게시물 출력
	        int start = (currentPage - 1) * pageSize;
	        int end = Math.min(start + pageSize, totalGeneralPosts);
	        List<PostVo> pagePosts = generalPosts.subList(start, end);

	        // 일반 게시물 출력
	        for (PostVo post : pagePosts) {
	            String statusColor = getStatusColor(post.getCondition());
	            String content = String.format("%-2d | 제목: %-20s | 가격: %-5s | 작성자: %-6s | 상태: %-10s", 
	                post.getPost_id(),
	                post.getTitle(),
	                formatter.format(post.getPrice()) + "원",
	                post.getUser_id(),
	                statusColor + getStatus(post.getCondition()) + ColorUtil.RESET
	            );
	            printAsciiArtBox(content, false);  // 일반 게시물 출력
	        }

	        System.out.println("+" + "=".repeat(width - 2) + "+");

	        // 페이지 이동 옵션 출력
	        System.out.print("페이지: ");
	        for (int i = 1; i <= totalPages; i++) {
	            if (i == currentPage) {
	                System.out.print("\033[1m" + i + "\033[0m "); // 현재 페이지는 볼드로 표시
	            } else {
	                System.out.print(i + " ");
	            }
	        }
	        System.out.println();
	        System.out.println();
	        // 페이지 이동 메뉴
	        int input = ScanUtil.nextInt("1.이전 페이지 2.다음 페이지 3.판매 글 작성 4.상세 보기 5.검색 0.내 화면으로 \n 메뉴 선택 >> ");
	        switch (input) {
	            case 1:
	                if (currentPage > 1) {
	                    currentPage--;
	                } else {
	                    System.out.println("첫 페이지입니다.");
	                }
	                break;
	            case 2:
	                if (currentPage < totalPages) {
	                    currentPage++;
	                } else {
	                    System.out.println("마지막 페이지입니다.");
	                }
	                break;
	            case 3:
	                return Command.POST_INSERT;
	            case 4:
	                MainController.sessionMap.remove("currentPostId");
	                return Command.POST_DETAIL;
	            case 5:
	                return postSearch(); // 검색 기능 추가
	            case 0:
	                return Command.USER_HOME;
	            default:
	                System.out.println("잘못된 선택입니다. 다시 시도하세요.");
	        }
	    }
	}



	// 게시물 상태 문자열 반환
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

	// 게시물 상태에 따라 색상 반환
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

	// 아스키 아트 출력 함수
	private void printAsciiArtBox(String content, boolean isLast) {
		int width = 80;
		String borderLine = "+" + "-".repeat(width - 2) + "+";
		System.out.println(borderLine);
		System.out.printf("| %-" + (width - 3) + "s\n", content);
		if (!isLast) {
			System.out.println(borderLine);
		}
	}


	// 게시글 추가 메서드
	public Command postInsert() {
		PostService postService = PostService.getInstance();
		PostVo post = new PostVo();
		UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser");
		
		if (loginUserVo.getRole() != 0) { 												// 관리자의 공지 추가
			System.out.println("<<공지사항 쓰기>>");
			String title = ScanUtil.nextLine("제목 >> ");
			String content = ScanUtil.nextLine("내용 >> ");
			post.setTitle(title);
			post.setContent(content);
			post.setUser_id(loginUserVo.getUser_id());
		} else { 																	// 사용자의 게시글 추가
			String Title = ScanUtil.nextLine("글 제목 >> ");
			int price = ScanUtil.nextInt("가격 >> ");
			CategoryService cateservice = CategoryService.getInstance();
			List<CategoryVo> catevo = cateservice.getCategoryList();
			for (CategoryVo category : catevo) {
				System.out.println("분류번호: " + category.getCategory_id() + ", 카테고리: " + category.getCategory_name());
			}
			int category = ScanUtil.nextInt("카테고리 >> ");
			String content = ScanUtil.nextLine("글 내용 입력 >> ");
			post.setTitle(Title);
			post.setPrice(price);
			post.setCategory_id(category);
			post.setContent(content);
			post.setCondition(1);

			post.setUser_id(loginUserVo.getUser_id());
		}
		int result = postService.insertPost(post);
		if (result > 0) {
			System.out.println("게시글이 성공적으로 등록되었습니다.");
		} else {
			System.out.println("▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄\r\n" + "████▌▄▌▄▐▐▌█████\r\n" + "████▌▄▌▄▐▐▌▀████\r\n"
					+ "▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀\r\n" + "");
		}
		return Command.POST_LIST;
	}

	// 게시글 수정 메서드
	// 기존 postUpdate: 사용자가 글 번호를 입력하는 방식
	public Command postUpdate() {

		UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser");
		int choice = ScanUtil.nextInt("수정할 내 글 번호를 입력하세요: ");
		PostService postService = PostService.getInstance();
		PostVo post = postService.getPost(choice);
		if (post == null) {
			System.out.println("해당 게시물을 찾을 수 없습니다.");
			return Command.POST_LIST;
		}
		if (post.getUser_id().equals(loginUserVo.getUser_id()) || loginUserVo.getRole() != 0) {
			postService.updatePostSelect(post);
		} else {
			System.out.println("다른 사용자의 글은 수정할 수 없습니다.");
		}
		return Command.POST_LIST;
	}

	// 새로운 postUpdate: 이미 글 번호를 알고 있는 경우 (글의 상세보기에 들어가 있는 경우)
	public Command postUpdate(int postId) {
		UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser");
		PostService postService = PostService.getInstance();
		PostVo post = postService.getPost(postId);

		if (post.getUser_id().equals(loginUserVo.getUser_id()) || loginUserVo.getRole() != 0) {
			postService.updatePostSelect(post);
		} else {
			System.out.println("다른 사용자의 글은 수정할 수 없습니다.");
		}
		return Command.POST_LIST;
	}

	// 게시글 삭제 메서드: 이미 글 번호를 알고 있는 경우 (글의 상세보기에 들어가 있는 경우) 
	public Command postDelete(int postId) {
		UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser");
		PostService postService = PostService.getInstance();
		PostVo post = postService.getPost(postId);

		if (post.getUser_id().equals(loginUserVo.getUser_id()) || loginUserVo.getRole() != 0) {
			postService.deletePost(post.getPost_id());
		} else {
			System.out.println("다른 사용자의 글은 삭제할 수 없습니다.");
		}
		return Command.POST_LIST;
	}

	// 게시글 삭제 메서드 
	public Command postDelete() {
		UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser");
		int choice = ScanUtil.nextInt("삭제할 글 번호를 입력하세요: ");
		PostService postService = PostService.getInstance();
		PostVo post = postService.getPost(choice);
		if (post == null) {
			System.out.println("해당 게시물을 찾을 수 없습니다.");
			return Command.POST_LIST;
		}
		
		if (post.getUser_id().equals(loginUserVo.getUser_id()) || loginUserVo.getRole() != 0) {
			postService.deletePost(post.getPost_id());
		} else {
			 
			System.out.println("다른 사용자의 글은 삭제할 수 없습니다.");
		}
		return Command.POST_LIST;
	}  

	public void updatePostCondition(int postId, String newCondition, String sellerId) {
	    PostService postService = PostService.getInstance();  // 싱글톤 방식 사용
	    String buyerId = null;  // 구매자 ID를 입력받음
	    HistoryService historyService = HistoryService.getInstance(); // 싱글톤 인스턴스 사용

	    // 문자열 상태를 숫자로 변환
	    int conditionCode;
	    switch (newCondition) {
	        case "1":
	            conditionCode = 1; // 판매중
	            break;
	        case "2":
	            conditionCode = 2; // 예약중
	            // 구매자 ID를 입력받아 예약 처리
	            buyerId = ScanUtil.nextLine("구매자 ID를 입력하세요: ");
	            if (buyerId == null || buyerId.isEmpty()) {
	                System.out.println("유효하지 않은 구매자 ID입니다.");
	                return; // 구매자 ID가 없으면 중단
	            }
	            break;
	        case "3":
	            conditionCode = 3; // 거래 완료
	            
	            // 거래 완료인 경우, 이전에 설정된 구매자 ID를 가져오기
	            buyerId = historyService.getBuyerIdFromTransaction(postId);
	            
	            // 예약 중이었던 기록이 없는 경우
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


	private Command changePostCondition(int postId) {
	    PostService postService = PostService.getInstance();
	    UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser");
	    PostVo post = postService.getPost(postId);

	    if (post == null || !post.getUser_id().equals(loginUserVo.getUser_id())) {
	        System.out.println("해당 게시물을 수정할 수 없습니다.");
	        return Command.POST_LIST;
	    }

	    System.out.println("1. 판매중으로 변경 2. 예약중으로 변경 3. 거래 완료로 변경");
	    int choice = ScanUtil.nextInt("선택 >> ");

	    switch (choice) {
	        case 1:
	            postService.updatePostCondition(postId, 1, null, post.getUser_id(), null);  // 상태: 판매중 (숫자 1)
	            System.out.println("상태가 '판매중'으로 변경되었습니다.");
	            break;

	        case 2:
	            String buyerId = ScanUtil.nextLine("예약할 구매자 ID를 입력하세요: ");
	            postService.updatePostCondition(postId, 2, buyerId, post.getUser_id(), 1);  // 상태: 예약중 (숫자 2)
	            System.out.println("상태가 '예약중'으로 변경되었습니다.");
	            break;

	        case 3:
	            postService.updatePostCondition(postId, 3, null, post.getUser_id(), null);  // 상태: 거래 완료 (숫자 3)
	            System.out.println("상태가 '거래 완료'로 변경되었습니다.");
	            break;

	        default:
	            System.out.println("잘못된 선택입니다.");
	            break;
	    }

	    return Command.POST_LIST;
	}



	// 게시글 검색 메서드
	public Command postSearch() {
		System.out.println("검색 방법을 선택하세요:");
		System.out.println("1. 키워드로 검색");
		System.out.println("2. 카테고리로 검색");

		int choice = ScanUtil.nextInt("선택 >> ");

		PostService postService = PostService.getInstance();

		if (choice == 1) {
			String keyword = ScanUtil.nextLine("검색할 키워드를 입력하세요: ");
			List<PostVo> results = postService.searchPosts(keyword, null); // 카테고리는 null로 설정

			if (results.isEmpty()) {
				System.out.println("검색 결과가 없습니다.");
			} else {
				for (PostVo post : results) {
					System.out.printf("게시물 번호: %d | 제목: %s | 가격: %d | 상태: %d\n", post.getPost_id(), post.getTitle(),
							post.getPrice(), post.getCondition());
				}
			}
		} else if (choice == 2) {
			// 카테고리 목록 출력
			CategoryService categoryService = CategoryService.getInstance();
			List<CategoryVo> categories = categoryService.getCategoryList();
			System.out.println("카테고리 목록:");
			for (CategoryVo category : categories) {
				System.out.printf("분류번호: %d, 이름: %s\n", category.getCategory_id(), category.getCategory_name());
			}

			int categoryId = ScanUtil.nextInt("검색할 분류번호를 입력하세요: ");
			
			
			List<PostVo> results = postService.searchPosts(null, categoryId); // 키워드는 null로 설정

			if (results.isEmpty()) {
				System.out.println("검색 결과가 없습니다.");
			} else {
				for (PostVo post : results) {
	                String categoryName = categoryService.getCategoryNameById(post.getCategory_id());
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
					System.out.printf("게시물 번호: %d | 제목: %s | 가격: %d | 상태: %s\n", post.getPost_id(), post.getTitle(),
							post.getPrice(), condition);
				}

			}
		} else {
			System.out.println("잘못된 선택입니다.");
		}

		return Command.POST_LIST; // 검색 후 게시물 목록으로 돌아감
	}
	
	
	
	
}




