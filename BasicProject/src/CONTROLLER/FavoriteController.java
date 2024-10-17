package CONTROLLER;

import java.util.List;
import java.util.Scanner;

import SERVICE.FavoriteService;
import SERVICE.PostService;
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
   public static FavoriteController getInstance() {
      if (instance == null)
         instance = new FavoriteController();
      return instance;
   }
	// 관심 상품 등록
   public Command addFavorite(int postId) {
	    FavoriteVo favorite = new FavoriteVo();
	    UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser");

	    favorite.setUser_id(loginUserVo.getUser_id());
	    favorite.setPost_id(postId);

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

    // 사용자의 관심 상품 목록 조회
    public Command viewFavorites() {
        List<FavoriteVo> favorites = favoriteService.getFavoritesByUser();
        
        System.out.println("관심 상품 목록:" + favorites.size());
         
        if (favorites.isEmpty()) {
            System.out.println("관심 상품이 없습니다.");
            
        } else {
        	System.out.printf("%-15s %-30s %-10s%n", "게시글 번호", "게시글 제목", "작성자");
            System.out.println("-------------------------------------------------------------");
             
            for (FavoriteVo favorite : favorites) {
                System.out.printf("%-20d %-30s %-10s%n", 
                                  favorite.getPost_id(), 
                                  favorite.getPost_title(), 
                                  favorite.getAuthor());
            }
        }
        System.out.println("-------------------------------------------------------------");
        return Command.FAVORITE_DELETE; 
    }
    //관리자가 회원 ID로 조회하는 사용자의 찜목록
    public Command viewFavorites(String userId) {
        List<FavoriteVo> favorites = favoriteService.getFavoritesByUser();
        
        System.out.println("관심 상품 목록:" + favorites.size());
         
        if (favorites.isEmpty()) {
            System.out.println("관심 상품이 없습니다.");
            
        } else {
        	System.out.printf("%-15s %-30s %-10s%n", "게시글 번호", "게시글 제목");
            System.out.println("-------------------------------------------------------------");
             
            for (FavoriteVo favorite : favorites) {
                System.out.printf("%-20d %-30s %-10s%n", 
                                  favorite.getPost_id(), 
                                  favorite.getPost_title());
            }
        }
        System.out.println("-------------------------------------------------------------");
        return Command.USER_HOME; // 게시물 목록으로 돌아가기
    }

    // 관심 상품 삭제
    public Command deleteFavorite() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("삭제할 게시글 번호 입력: ");
        int postId = scanner.nextInt();

        UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser");
        String currentUserId = loginUserVo.getUser_id(); // 현재 로그인한 사용자 ID를 가져옵니다.

        // 삭제 메서드 호출
        boolean isDeleted = favoriteService.deleteFavorite(currentUserId, postId);
        
        if (isDeleted) {
            System.out.println("관심 상품 삭제 완료.");
            return Command.USER_HOME;
        } else 
            System.out.println("해당 게시글의 관심 상품이 존재하지 않거나 삭제할 수 없습니다.");
        
        return Command.USER_HOME;
    }

    // 특정 사용자가 특정 게시글을 즐겨찾기 했는지 확인
    public void checkFavorite() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("사용자 ID 입력: ");
        String userId = scanner.next();
        System.out.print("게시글 ID 입력: ");
        int postId = scanner.nextInt();

//        boolean isFavorite = favoriteService.isFavoriteEx(userId, postId);
//        if (isFavorite) {
//            System.out.println("이 사용자는 이 게시글을 즐겨찾기 했습니다.");
//        } else {
//            System.out.println("이 사용자는 이 게시글을 즐겨찾기 하지 않았습니다.");
//        }
    }
    
    // 관심 상품 관리 메뉴 표시
    public Command displayMenu() {
        while (true) {
            System.out.println();
            int choice = ScanUtil.nextInt("1.관심 상품 삭제 0.돌아가기 >>");
            switch (choice) {
            	case 1:
            		 deleteFavorite();
            		break;
                case 0:
                    System.out.println("종료합니다.");
                    return Command.USER_HOME;
                default:
                    System.out.println("잘못된 선택입니다.");
            }
        }
    }

	
    
}
