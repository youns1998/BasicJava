package CONTROLLER;

import java.util.ArrayList;
import java.util.List;

import SERVICE.CategoryService;
import SERVICE.CommentsService;
import SERVICE.FavoriteService;
import SERVICE.PostService;
import SERVICE.UsersService;
import UTIL.Command;
import UTIL.ScanUtil;
import VO.*;

public class PostController {
    
	private static final String ANSI_LIGHT_RED = "\033[38;5;203m"; // 덜한 빨간색
	private static final String ANSI_BOLD = "\033[1m";
	private static final String ANSI_RESET = "\033[0m";

	private static PostController instance;
	private CommentController commentController = CommentController.getInstance();
	private FavoriteController favoriteController = FavoriteController.getInstance();

	private Command returnToPostList() {
		PostController postController = PostController.getInstance();
		return postController.postList();
	}

	private PostController() {}

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
		        System.out.printf("| %-3s | 작성자: %-5s | 내용: %-10s | 작성 시간: %-8s \n", 
		                          comment.getComment_id(), comment.getUser_id(), comment.getContent(), createdAt);
		        System.out.println("+------------------------------------------------------------------------------+");

		    }
		}


		MainController.sessionMap.put("currentPostId", postId);
		return commentMenu(postId);
	}

	// 댓글 메뉴 메서드
	private Command commentMenu(int postId) {
		System.out.println("1. 댓글 달기 2. 댓글 수정 3. 댓글 삭제 4. 찜하기 0. 전체 게시물 보러가기");
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

		int commentCount = commentsService.getCommentCount(post.getPost_id());
		boolean isFavorite = favoriteService.isFavoriteExists(loginUserVo.getUser_id(), post.getPost_id());

		String borderLine = "+==============================================================================+";
		System.out.println(borderLine);

		// 직접 출력으로 변경
		System.out.printf("| 작성자: %-12s 제목: %-20s 가격: %-8s 상태: %-3s %s\n", 
			post.getUser_id(), 
			post.getTitle(), 
			post.getPrice() + "원", 
			post.getCondition(),
			isFavorite ? "♡ 찜한 상품" : " ");
		System.out.println(borderLine);

		// 내용 출력
		System.out.printf("| 내용: %-72s \n", post.getContent());
		System.out.printf("| %-78s \n", "");
		System.out.println(borderLine);

		// 작성 시간 및 수정 시간 출력
		System.out.printf("| 작성 시간: %-61s \n", post.getCreated_at());
		System.out.printf("| 수정 시간: %-61s \n", "(수정 시각 정보 미정)");
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
		if(loginUserVo.getRole()!=0) {
    		return Command.POST_ADMIN;
    	}
		if (posts.isEmpty()) {
	        System.out.println("작성된 게시물이 없습니다.");
	    } else {
	        for (PostVo post : posts) {
	            System.out.println("게시물 번호: " + post.getPost_id());
	            System.out.println("제목: " + post.getTitle());
	            System.out.println("내용: " + post.getContent());
	            System.out.println("가격: " +post.getPrice());
	            System.out.println("작성일: " + post.getCreated_at());
	            System.out.println("수정일: "+post.getUpdated_at());
	            System.out.println("현재 상태: " +post.getCondition());
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
		            System.out.println("가격: " +post.getPrice());
		            System.out.println("작성일: " + post.getCreated_at());
		            System.out.println("수정일: "+post.getUpdated_at());
		            System.out.println("현재 상태: " +post.getCondition());
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
			// 공지사항과 사용자 게시물 목록 출력
			List<PostVo> adminPosts = new ArrayList<>();
			List<PostVo> userPosts = new ArrayList<>();

			for (PostVo post : posts) {
				UsersVo user = usersService.getUserSelect(post.getUser_id());
				if (user != null && user.getRole() == 1) {
					adminPosts.add(post);
				} else {
					userPosts.add(post);
				}
			}

			// 공지사항 출력
			for (PostVo post : adminPosts) {
				String content = "# 공지사항 : " + post.getTitle() + " #";
				System.out.println(ANSI_LIGHT_RED + ANSI_BOLD + content + ANSI_RESET);
			}

			// 사용자 게시물 출력
			for (int i = 0; i < userPosts.size(); i++) {
				PostVo post = userPosts.get(i);
				String content = String.format(
					"%-2d | 제목: %-20s | 가격: %-5s | 작성자: %-6s | 상태: %-10s", 
					post.getPost_id(),
					post.getTitle(), 
					post.getPrice(),
					post.getUser_id(),
					post.getCondition());
				printAsciiArtBox(content, i == userPosts.size() - 1);
			}
		}

		System.out.println("+" + "=".repeat(width - 2) + "+");

		System.out.print("\033[H\033[2J");
		System.out.flush();

		// 메뉴 선택
		if (loginUserVo.getRole() != 0) {
			int input = ScanUtil.nextInt("1.공지 작성 2.글 삭제 3.수정 4.상세보기 0.관리자 화면으로 \n 메뉴 선택 >> ");
			switch (input) {
			case 1:
				return Command.POST_INSERT;
			case 2:
				return Command.POST_DELETE;
			case 3:
				return Command.POST_UPDATE;
			case 4:
				MainController.sessionMap.remove("currentPostId");
				return Command.POST_DETAIL;
			case 0:
				return Command.USER_HOME;
			}
		} else {
			int input = ScanUtil.nextInt("1.판매 글 작성 2. 게시물 삭제 3. 게시물 수정 4.상세 보기 0.내 화면으로 \n 메뉴 선택 >> ");
			switch (input) {
			case 1:
				return Command.POST_INSERT;
			case 2:
				return Command.POST_DELETE;
			case 3:
				return Command.POST_UPDATE;
			case 4:
				MainController.sessionMap.remove("currentPostId");
				return Command.POST_DETAIL;
			case 0:
				return Command.USER_HOME;
			}
		}
		return Command.USER_HOME;
	}

	// 게시글 추가 메서드
	public Command postInsert() {
		PostService postService = PostService.getInstance();
		PostVo post = new PostVo();
		UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser");
		if (loginUserVo.getRole() != 0) { 				// 관리자의 공지 추가
			System.out.println("공지사항 쓰기");
			String title = ScanUtil.nextLine("제목 >> ");
			String content = ScanUtil.nextLine("내용 >> ");
			post.setTitle(title);
			post.setContent(content);
			post.setUser_id(loginUserVo.getUser_id()); 
		} else { 										// 사용자의 게시글 추가
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
			System.out.println("게시글 등록에 실패했습니다.");
		}
		return Command.POST_LIST;
	}

	// 게시글 수정 메서드
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
	
}
