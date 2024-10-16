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
   
   
   private String padAndTruncate(String text, int maxLength) {
       if (text == null) {
           text = "";  // null일 경우 빈 문자열로 처리
       }
       int textLength = 0;

       // 한글과 영문의 길이 계산을 다르게 함
       for (char c : text.toCharArray()) {
           if (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.HANGUL_SYLLABLES ||
               Character.UnicodeBlock.of(c) == Character.UnicodeBlock.HANGUL_JAMO ||
               Character.UnicodeBlock.of(c) == Character.UnicodeBlock.HANGUL_COMPATIBILITY_JAMO) {
               textLength += 2; // 한글은 2로 계산
           } else {
               textLength += 1; // 나머지는 1로 계산
           }
       }

       if (textLength > maxLength) {
           StringBuilder truncated = new StringBuilder();
           int currentLength = 0;
           for (char c : text.toCharArray()) {
               int charLength = (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.HANGUL_SYLLABLES ||
                                 Character.UnicodeBlock.of(c) == Character.UnicodeBlock.HANGUL_JAMO ||
                                 Character.UnicodeBlock.of(c) == Character.UnicodeBlock.HANGUL_COMPATIBILITY_JAMO) ? 2 : 1;

               if (currentLength + charLength > maxLength) {
                   break;
               }
               truncated.append(c);
               currentLength += charLength;
           }
           return truncated.toString();
       } else {
           int padding = maxLength - textLength;
           return text + " ".repeat(padding); // 지정된 길이에 맞춰 오른쪽으로 공백 추가
       }
   }
   private static final int TITLE_MAX_LEN = 10;
   private static final int AUTHOR_MAX_LEN = 5;
   private static final int STATUS_MAX_LEN = 5;
   private static PostController instance;
    private CommentController commentController = CommentController.getInstance(); // CommentController 인스턴스 생성
    private FavoriteController favoriteController = FavoriteController.getInstance();
    private Command returnToPostList() {
        PostController postController = PostController.getInstance();
        return postController.postList();  // 게시물 목록을 출력하도록 호출
    }
   private PostController() {

   }

   public static PostController getInstance() {
      if (instance == null)
         instance = new PostController();
      return instance;
   }
   //상세 게시글 보기
   public Command detailPost() {
       int postId = ScanUtil.nextInt("보고싶은 글 번호를 입력하세요: ");
       return detailPost(postId);
   }
   
   public Command detailPost(int postId) {
       System.out.println("+==============================================================================+");
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
	        System.out.println("==== 댓글 목록 ====");
	        for (CommentsVo comment : comments) {
	            System.out.println("댓글 번호: " + comment.getComment_id() +
	                               ", 작성자: " + comment.getUser_id() +
	                               ", 내용: " + comment.getContent() +
	                               ", 작성 시간: " + comment.getCreated_at());
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
	        	return favoriteController.addFavorite(postId); // 현재 보고 있는 게시물 번호 전달
	        case 0:
	            return returnToPostList();
	        default:
	            System.out.println("잘못된 선택입니다. 다시 시도하세요.");
	            return commentMenu(postId); // 다시 메뉴 호출
	    }
	}



	
	
	
	
	private Command viewComments(int postId) {
	    // List<CommentVo> comments = commentService.getComments(postId);
	    return Command.POST_LIST; 
	}
	private Command updateComment(int postId) {
	    return Command.POST_LIST; 
	}
	private Command deleteComment(int postId) {
	    return Command.POST_LIST; 
	}
	
	//게시물 상세보기
	private void displayPostDetails(PostVo post) {
	    CommentsService commentsService = CommentsService.getInstance();
	    FavoriteService favoriteService = FavoriteService.getInstance();
	    UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser");

	    int commentCount = commentsService.getCommentCount(post.getPost_id());
	    boolean isFavorite = favoriteService.isFavoriteExists(loginUserVo.getUser_id(), post.getPost_id());


       String borderLine = "+==============================================================================+";
       System.out.println(borderLine);

	    // 작성자, 제목, 가격, 상태, 찜 상태 출력
	    System.out.printf("| 작성자: %-12s 제목: %-20s 가격: %-8s 상태: %-3s %s\n", 
	        padAndTruncate(post.getUser_id(), 12), 
	        padAndTruncate(post.getTitle(), 20), 
	        padAndTruncate(post.getPrice() + "원", 8), 
	        padAndTruncate(post.getCondition(), 3),
	        isFavorite ? "♡ 찜한 상품" : " "
	    );
	    System.out.println(borderLine);


       // 내용 출력
       System.out.printf("| 내용: %-72s \n", padAndTruncate(post.getContent(), 72));
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


   


   

   
   // 아스키 아트 박스 출력 함수 (마지막 닫는 라인을 출력하지 않음)
    private void printAsciiArtBox(String content, boolean isLast) {
        int width = 80; // 가로 너비 설정 (박스 내부 포함하여 고정)
        String borderLine = "+" + "-".repeat(width - 2) + "+"; // 박스의 상단과 하단 라인
        System.out.println(borderLine);
        System.out.printf("| %-"+ (width - 3) +"s\n", content); // 박스 내용 출력 (마지막 | 생략)
        if (!isLast) { // 마지막 박스가 아닌 경우에만 닫는 선을 출력
            System.out.println(borderLine);
        }
    }

   // 덜 진한 빨간색 설정
    private static final String ANSI_LIGHT_RED = "\033[38;5;203m"; // 덜한 빨간색
    private static final String ANSI_BOLD = "\033[1m";
    private static final String ANSI_RESET = "\033[0m";

    public Command postList() {
          int width = 80;
          System.out.println("+" + "=".repeat(width - 2) + "+"); // 상단 경계선
          
          // 게시물 목록 출력
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
                  String title = padRight(truncate(post.getTitle(), 15), 20);
                  String author = padRight(truncate(post.getUser_id(), 5), 6);
                  String status = padRight(truncate(post.getCondition(), 10), 10);
                  String content = String.format(
                      "%-2d | 제목: %-20s | 가격: %-5s | 작성자: %-6s | 상태: %-10s", 
                      post.getPost_id(),
                      title, 
                      post.getPrice(),
                      author,
                      status
                  );
                  printAsciiArtBox(content, i == userPosts.size() - 1);
              }
          }
          
          System.out.println("+" + "=".repeat(width - 2) + "+"); // 하단 경계선
          
          // 화면 클리어
          System.out.print("\033[H\033[2J");
          System.out.flush();

		    // 메뉴 선택으로 이동
		    if (loginUserVo.getRole() != 0) {
		        int input = ScanUtil.nextInt("1.공지 작성 2.글 삭제 3.수정 4.상세보기 0.관리자 화면으로 >> ");
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
		        int input = ScanUtil.nextInt("1.판매 글 작성 2. 게시물 삭제 3. 게시물 수정 4.상세 보기 0.내 화면으로 >> ");
		        switch (input) {
		            case 1:
		                return Command.POST_INSERT;
		            case 2:
		                return Command.POST_DELETE;
		            case 3:
		                return Command.POST_UPDATE;
		            case 4:
		                MainController.sessionMap.remove("currentPostId"); // 처음 접근 시에는 postId를 초기화
		                return Command.POST_DETAIL;
		            case 0:
		                return Command.USER_HOME;
		        }
		    }
		    return Command.USER_HOME;
		}


    // 문자열 길이를 제한하고, 초과하면 ... 추가
    private String truncate(String text, int maxLength) {
        return text.length() > maxLength ? text.substring(0, maxLength - 3) + "..." : text;
    }

   // 한글과 영문 모두 정렬을 맞추기 위해 패딩을 추가하는 함수
    private String padRight(String text, int length) {
        if (text == null) {
            text = "";  // null일 경우 빈 문자열로 처리
        }
        int textLength = text.codePoints().map(cp -> Character.isAlphabetic(cp) ? 1 : 2).sum();
        int padSize = length - textLength;
        return text + " ".repeat(Math.max(0, padSize));
    }

   //게시글 추가 메서드
   public Command postInsert() {   
      PostService postService = PostService.getInstance();
      PostVo post = new PostVo();
      UsersVo loginUserVo = (UsersVo)MainController.sessionMap.get("loginUser");
      if (loginUserVo.getRole() != 0) {      // 관리자의 공지 추가
           System.out.println("공지사항 쓰기");
           String title = ScanUtil.nextLine("제목 >> ");
           String content = ScanUtil.nextLine("내용 >> ");
           
           post.setTitle(title);
           post.setContent(content);
           post.setUser_id(loginUserVo.getUser_id()); // 관리자 ID 설정
           
      }else {                  //사용자의 게시글 추가
      String Title = ScanUtil.nextLine("글 제목 >> ");
      int price = ScanUtil.nextInt("가격 >> ");
      
      //카테고리 리스트 보여주는 곳
      CategoryService cateservice = CategoryService.getInstance();
      List<CategoryVo> catevo = cateservice.getCategoryList();
       for (CategoryVo category : catevo) {
              System.out.println("분류번호: " + category.getCategory_id() + ", 카테고리: " + category.getCategory_name());
          }
       System.out.println("======================================================================");
       System.out.println("보기의 분류에 맞게 번호를 입력해주세요 ");
      int category = ScanUtil.nextInt("카테고리 >> ");
      //여기까지 
      
      String content = ScanUtil.nextLine("글 내용 입력 >> ");
         post.setTitle(Title);
          post.setPrice(price);
          post.setCategory_id(category);
          post.setContent(content);
          post.setCondition("판매중");
          post.setUser_id(loginUserVo.getUser_id());
      }
          int result = postService.insertPost(post); // 게시글 추가 메서드 호출
          if (result > 0) {
              System.out.println("게시글이 성공적으로 등록되었습니다.");
          } else {
              System.out.println("게시글 등록에 실패했습니다.");
          }
      return Command.POST_LIST;
      }
   
   //게시글 수정 메서드
   public Command postUpdate() {      
      UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser");
       int choice = ScanUtil.nextInt("수정할 내 글 번호를 입력하세요: ");
       PostService postService = PostService.getInstance();
       
       // 글 상세보기로 들어가기 (선택한 게시물을 가져옴)
       PostVo post = postService.getPost(choice); // 선택한 게시물 가져오기
       if (post == null) {
           System.out.println("해당 게시물을 찾을 수 없습니다.");
           return Command.POST_LIST; 
       }
       
       // 사용자 권한 확인
       if (post.getUser_id().equals(loginUserVo.getUser_id()) || loginUserVo.getRole() != 0) {
           // 수정 진행
           postService.updatePostSelect(post); // updatePostMenu를 호출하여 수정 진행
       } else {
           System.out.println("다른 사용자의 글은 수정할 수 없습니다.");
       }

       return Command.POST_LIST; // 수정 완료 후 사용자 홈으로 돌아감
   }
   
   //게시글 삭제 메서드
   public Command postDelete() {
       UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser");
       int choice = ScanUtil.nextInt("삭제할 글 번호를 입력하세요: ");
       PostService postService = PostService.getInstance();
       
       // 글 상세보기로 들어가기 (선택한 게시물을 가져옴)
       PostVo post = postService.getPost(choice); // 선택한 게시물 가져오기
       if (post == null) {
           System.out.println("해당 게시물을 찾을 수 없습니다.");
           return Command.POST_LIST; // 게시판 목록으로 돌아가기
       }

       // 사용자 권한 확인
       if (post.getUser_id().equals(loginUserVo.getUser_id()) || loginUserVo.getRole() != 0) {
           // 자신의 글이거나 관리자라면 삭제 가능
           postService.deletePost(post.getPost_id());
       } else {
           System.out.println("다른 사용자의 글은 삭제할 수 없습니다.");
       }

       return Command.POST_LIST;
   }
}



