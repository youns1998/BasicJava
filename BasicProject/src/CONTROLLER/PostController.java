package CONTROLLER;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import SERVICE.CategoryService;
import SERVICE.CommentsService;
import SERVICE.FavoriteService;
import SERVICE.PostService;
import SERVICE.UsersService;
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
	        System.out.println("5. 판매글 수정  6. 판매글 삭제");
	    }
	    
	    System.out.print("메뉴 선택 >> ");
	    int choice = ScanUtil.nextInt();

	    switch (choice) {
	    case 1: return commentController.insertComment(postId);
	    case 2: return commentController.updateComment(postId);
	    case 3: return commentController.deleteComment(postId);
	    case 4: return favoriteController.addFavorite(postId);
	    case 5: return postUpdate(postId); // 게시물 ID를 자동으로 전달하여 수정 화면으로 이동
	    case 6: return postDelete(postId);
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
	      System.out.printf("| 작성자: %-12s 제목: %-20s 가격: %-10s 상태: %s %s\n", post.getUser_id(), post.getTitle(),
	            formatter.format(post.getPrice())+"원", condition, isFavorite ? "♡ 찜한 상품" : " ");
	      System.out.println(borderLine);

	      // 내용 출력
	      System.out.printf("| 내용: %-72s \n", post.getContent());
	      System.out.printf("| %-78s \n", "");
	      System.out.println(borderLine);
	      
	      LocalDateTime createdAt = post.getCreated_at();
	      LocalDateTime updatedAt = post.getUpdated_at();
	      
	      // 작성 시간 및 수정 시간 출력
	      System.out.printf("| 작성 시간: %-35s        카테고리: %s \n", createdAt.format(formatter1),categoryName);
	      System.out.printf("| 수정 시간: %-61s \n", updatedAt.format(formatter1));
	      System.out.println(borderLine);

	      // 댓글 수와 찜한 사람 수 출력
	      System.out.printf("| 댓글 수: %-10d 찜한 사람 수: %-48s \n", commentCount, "(찜한 사람 수 미정)");
	      System.out.println(borderLine);
	   }

	// 아스키 아트 박스 출력 함수
	private void printAsciiArtBox(String content, boolean isLast) {
		int width = 80;
		String borderLine = "+" + "-".repeat(width - 2) + "+";
		System.out.println(borderLine);
		System.out.printf("| %-" + (width - 3) + "s\n", content);
		if (!isLast) {
			System.out.println(borderLine);
		}
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
		UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser");
		String userId = ScanUtil.nextLine("게시물 리스트를 조회할 회원 ID >> ");
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

	// 게시물 목록 출력
	public Command postList() {
		int width = 80;
		System.out.println("+" + "=".repeat(width - 2) + "+");

		PostService postService = PostService.getInstance();
		UsersService usersService = UsersService.getInstance();
		UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser");
		List<PostVo> posts = postService.getPostList();

		if (posts == null || posts.isEmpty()) {
			System.out.println("작성된 게시물이 없습니다");
		} else {
			List<PostVo> adminPosts = new ArrayList<>();
			List<PostVo> salePosts = new ArrayList<>(); // 상태 1: 판매 중
			List<PostVo> reservedPosts = new ArrayList<>(); // 상태 2: 예약 중
			List<PostVo> completedPosts = new ArrayList<>(); // 상태 3: 거래 완료

			for (PostVo post : posts) {
				UsersVo user = usersService.getUserSelect(post.getUser_id());
				if (user != null && user.getRole() == 1) {
					adminPosts.add(post);
				} else {
					switch (post.getCondition()) {
					case 1:
						salePosts.add(post);
						break;
					case 2:
						reservedPosts.add(post);
						break;
					case 3:
						completedPosts.add(post);
						break;
					}
				}
			}

			// 공지사항 출력
			for (PostVo post : adminPosts) {
				String content = "# 공지사항 : " + post.getTitle() + " #";
				System.out.println(ColorUtil.RED + content + ColorUtil.RESET);
			}

			// 판매 중 게시물 출력
			for (PostVo post : salePosts) {
				String content = String.format("%-2d | 제목: %-20s | 가격: %-5s | 작성자: %-6s | 상태: %-10s", post.getPost_id(),
						post.getTitle(), formatter.format(post.getPrice())+"원", post.getUser_id(), "판매중");
				printAsciiArtBox(content, false);
			}

			// 예약 중 게시물 출력 (초록색)
			for (PostVo post : reservedPosts) {
				String content = String.format("%-2d | 제목: %-20s | 가격: %-5s | 작성자: %-6s | 상태: %-10s", post.getPost_id(),
						post.getTitle(), formatter.format(post.getPrice())+"원", post.getUser_id(), "예약중");
				System.out.print(ColorUtil.GREEN);
				printAsciiArtBox(content, false);
				System.out.print(ColorUtil.RESET);
			}


			// 거래 완료 게시물 출력 (회색)
			for (PostVo post : completedPosts) {
				String content = String.format("%-2d | 제목: %-20s | 가격: %-5s | 작성자: %-6s | 상태: %-10s", post.getPost_id(),
						post.getTitle(), formatter.format(post.getPrice())+"원", post.getUser_id(), "거래 완료");
				System.out.print(ColorUtil.GRAY);
				printAsciiArtBox(content, true);
				System.out.print(ColorUtil.RESET);
			}
		}

		System.out.println("+" + "=".repeat(width - 2) + "+");
		System.out.print("\033[H\033[2J");
		System.out.flush();

		// 메뉴 선택
		if (loginUserVo.getRole() != 0) {
			int input = ScanUtil.nextInt("1.공지 작성 2.글 삭제 3.수정 4.상세보기 5.검색 0.관리자 화면으로 \n 메뉴 선택 >> ");
			switch (input) {
			case 1:	return Command.POST_INSERT;
			case 2:	return Command.POST_DELETE;
			case 3:	return Command.POST_UPDATE;
			case 4:	MainController.sessionMap.remove("currentPostId");	
					return Command.POST_DETAIL;
			case 5:	return postSearch(); // 검색 기능 추가
			case 0:	return Command.USER_HOME;
			}
		} else {
			int input = ScanUtil.nextInt("1.판매 글 작성 2.상세 보기 3.검색 0.내 화면으로 \n 메뉴 선택 >> ");
			switch (input) {
			case 1:	return Command.POST_INSERT;
			case 2: MainController.sessionMap.remove("currentPostId");	
					return Command.POST_DETAIL;
			case 3: return postSearch(); // 검색 기능 추가
			case 0:	return Command.USER_HOME;
			}
		}
		return Command.USER_HOME;
	}

	// 게시글 추가 메서드
	public Command postInsert() {
		PostService postService = PostService.getInstance();
		PostVo post = new PostVo();
		UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser");
		if (loginUserVo.getRole() != 0) { // 관리자의 공지 추가
			System.out.println("공지사항 쓰기");
			String title = ScanUtil.nextLine("제목 >> ");
			String content = ScanUtil.nextLine("내용 >> ");
			post.setTitle(title);
			post.setContent(content);
			post.setUser_id(loginUserVo.getUser_id());
		} else { // 사용자의 게시글 추가
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

	// 새로운 postUpdate: 이미 글 번호를 알고 있는 경우
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
	// 게시글 삭제 메서드: 이미 글 번호를 알고 있는 경우
		public Command postDelete(int postId ) {
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

	public void updatePostCondition(int postId, String newCondition, String buyerId, String sellerId) {
		PostService.updatePostCondition(postId, newCondition, buyerId, sellerId);
		System.out.println("게시물 상태가 업데이트되었습니다.");
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
		                System.out.printf("게시물 번호: %d | 제목: %s | 가격: %d | 상태: %d\n",
		                        post.getPost_id(), post.getTitle(), post.getPrice(), post.getCondition());
		            }
		        }
		    } else if (choice == 2) {
		        // 카테고리 목록 출력
		        CategoryService categoryService = CategoryService.getInstance();
		        List<CategoryVo> categories = categoryService.getCategoryList();
		        
		        System.out.println("카테고리 목록:");
		        for (CategoryVo category : categories) {
		            System.out.printf("ID: %d, 이름: %s\n", category.getCategory_id(), category.getCategory_name());
		        }

		        int categoryId = ScanUtil.nextInt("검색할 카테고리 ID를 입력하세요: ");
		        List<PostVo> results = postService.searchPosts(null, categoryId); // 키워드는 null로 설정

		        if (results.isEmpty()) {
		            System.out.println("검색 결과가 없습니다.");
		        } else {
		            for (PostVo post : results) {
		                System.out.printf("게시물 번호: %d | 제목: %s | 가격: %d | 상태: %d\n",
		                        post.getPost_id(), post.getTitle(), post.getPrice(), post.getCondition());
		            }
		        }
		    } else {
		        System.out.println("잘못된 선택입니다.");
		    }
		    
		    return Command.POST_LIST; // 검색 후 게시물 목록으로 돌아감
	}
}
