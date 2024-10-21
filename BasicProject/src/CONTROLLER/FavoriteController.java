package CONTROLLER;

import java.util.List;
import java.util.Scanner;

import SERVICE.FavoriteService;
import SERVICE.PostService;
import SERVICE.UsersService;
import UTIL.Command;
import UTIL.ScanUtil;
import VO.FavoriteVo;
import VO.PostVo;
import VO.UsersVo;

public class FavoriteController {
   private FavoriteService favoriteService = FavoriteService.getInstance(); // 싱글톤 인스턴스를 가져옴
   
   private static FavoriteController instance;
   
   private FavoriteController() {

   }

   // 싱글톤 패턴으로 인스턴스 생성
   public static FavoriteController getInstance() {
      if (instance == null)
         instance = new FavoriteController();
      return instance;
   }

   // 관심 상품 등록 메서드
   public Command addFavorite(int postId) {
	    FavoriteVo favorite = new FavoriteVo();
	    UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser"); // 로그인한 사용자 정보 가져오기
	    
	    PostService postService = PostService.getInstance();
	    PostVo postvo = postService.getPost(postId); 	// 게시글 조회
	    System.out.println(postvo.getUser_id());
	    
	    // 사용자가 자신의 게시글을 찜하려고 할 경우 처리
	    if (loginUserVo.getUser_id().equals(postvo.getUser_id())) {
	        System.out.println("자기 게시글은 찜할 수 없습니다.");
	        return Command.POST_DETAIL; // 상세보기로 돌아가기
	    }

	    // 관심 상품 객체에 사용자 ID와 게시글 ID 설정
	    favorite.setUser_id(loginUserVo.getUser_id());
	    favorite.setPost_id(postId);

	    // 이미 찜한 상품인지 확인
	    if (favoriteService.isFavoriteExists(loginUserVo.getUser_id(), postId)) {
	        System.out.println("이미 찜한 상품입니다.");
	    } else {
	        favoriteService.addFavorite(favorite);
	        System.out.println("관심 상품 등록 완료.");
	    }

	    // 현재 게시글 ID를 유지한 채로 상세보기로 돌아가기
	    MainController.sessionMap.put("currentPostId", postId);
	    return Command.POST_DETAIL;
	}

    // 사용자의 관심 상품 목록 조회 메서드
    public Command viewFavorites() {
        List<FavoriteVo> favorites = favoriteService.getFavoritesByUser(); // 사용자의 찜 목록 가져오기
        UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser"); // 로그인한 사용자 정보 가져오기

        // 관리자는 관리자용 찜 목록 조회 화면으로 이동
        if (loginUserVo.getRole() != 0) {
        	return Command.USER_FAVORITE;
        }

        // 찜한 상품이 있는지 확인
        System.out.println("찜 목록: " + favorites.size() + "개의 게시물을 찜했습니다");
         
        if (favorites.isEmpty()) {
            System.out.println("관심 상품이 없습니다.");
        } else {
            // 관심 상품 목록 출력
            System.out.printf("%-15s %-30s %-10s%n", "게시글 번호", "게시글 제목", "작성 시간");
            System.out.println("-------------------------------------------------------------");
             
            for (FavoriteVo favorite : favorites) {
                System.out.printf("%-20d %-30s %-10s%n", 
                                  favorite.getPost_id(), 
                                  favorite.getPost_title(), 
                                  favorite.getCreated_at());
            }
        }
        System.out.println("-------------------------------------------------------------");
        return Command.FAVORITE_DELETE; 
    }

    // 관리자가 회원 ID로 조회하는 사용자의 찜목록
    public Command adminviewFavorites() {
        FavoriteService favoriteservice = FavoriteService.getInstance(); // FavoriteService 인스턴스 가져오기
        UsersService userService = UsersService.getInstance(); // UsersService 인스턴스 가져오기
        String userId = ScanUtil.nextLine("찜 목록을 조회할 회원 ID>>"); // 조회할 사용자 ID 입력받기
        System.out.println();
        List<FavoriteVo> favorites =  favoriteservice.getFavoritesList(userId); // 해당 사용자의 찜 목록 가져오기
        UsersVo user = userService.getUserSelect(userId); // 해당 사용자 정보 가져오기
        
        // 입력한 사용자가 없을 경우 처리
        if (user == null) {						
        	System.out.println("등록된 회원이 아닙니다");
        	return Command.USER_LIST;
        } else {
            // 사용자의 찜 목록 출력
            System.out.println();
            System.out.println("찜한 게시글 갯수: " + favorites.size() + "개");
            if (favorites.isEmpty()) {
                System.out.println("찜한 상품이 없습니다.");
                return Command.USER_LIST;
            } else {
                System.out.printf("%-20s %-25s %-10s%n", "게시글 번호", "게시글 제목", "작성자");
                System.out.println("-------------------------------------------------------------");
                
                for (FavoriteVo favorite : favorites) {
                    System.out.printf("%-15d %-25s %-10s%n", 
                                      favorite.getPost_id(), 
                                      favorite.getPost_title(), 
                                      favorite.getAuthor());
                }
            }
            System.out.println("-------------------------------------------------------------");
        }
        return Command.USER_HOME; // 사용자 홈 화면으로 돌아가기
    }

    // 관심 상품 삭제 메서드
    public Command deleteFavorite() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("삭제할 게시글 번호 입력: ");
        int postId = scanner.nextInt(); // 삭제할 게시글 번호 입력받기

        UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser"); // 로그인한 사용자 정보 가져오기
        String currentUserId = loginUserVo.getUser_id(); // 현재 로그인한 사용자 ID 가져오기

        // 관심 상품 삭제 메서드 호출
        boolean isDeleted = favoriteService.deleteFavorite(currentUserId, postId);
        
        // 삭제 성공 여부에 따른 메시지 출력
        if (isDeleted) {
            System.out.println("관심 상품 삭제 완료.");
            return Command.USER_HOME;
        } else {
            System.out.println("해당 게시글의 관심 상품이 존재하지 않거나 삭제할 수 없습니다.");
        }
        
        return Command.USER_HOME;
    }

    // 특정 사용자가 특정 게시글을 즐겨찾기 했는지 확인하는 메서드
    public void checkFavorite() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("사용자 ID 입력: ");
        String userId = scanner.next(); // 사용자 ID 입력받기
        System.out.print("게시글 ID 입력: ");
        int postId = scanner.nextInt(); // 게시글 ID 입력받기

        // 즐겨찾기 여부 확인 메서드 호출 (주석 처리된 코드로, 기능 추가 필요)
//        boolean isFavorite = favoriteService.isFavoriteEx(userId, postId);
//        if (isFavorite) {
//            System.out.println("이 사용자는 이 게시글을 즐겨찾기 했습니다.");
//        } else {
//            System.out.println("이 사용자는 이 게시글을 즐겨찾기 하지 않았습니다.");
//        }
    }
    
    // 찜한 사람의 명수 확인 메서드
    public int countFavoritesForPost(int postId) {
    	FavoriteService favoriteService = FavoriteService.getInstance(); // FavoriteService 인스턴스 가져오기
    	int cnt = 0;
    	cnt = favoriteService.countFavoritesForPost(postId); // 해당 게시글을 찜한 사람 수를 계산
    	return cnt;
    }

    // 관심 상품 관리 메뉴 표시 메서드
    public Command displayMenu() {
        while (true) {
            System.out.println();
            int choice = ScanUtil.nextInt("1.관심 상품 삭제 0.돌아가기 >>"); // 사용자 선택 입력받기
            switch (choice) {
                case 1:
                    deleteFavorite(); // 관심 상품 삭제 호출
                    break;
                case 0:
                    System.out.println("종료합니다.");
                    return Command.USER_HOME; // 사용자 홈으로 돌아가기
                default:
                    System.out.println("잘못된 선택입니다.");
            }
        }
    }
}
