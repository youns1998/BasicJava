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
   private FavoriteService favoriteService = new FavoriteService();
   
   
   private static FavoriteController instance;
   
   private FavoriteController() {

   }
   public static FavoriteController getInstance() {
      if (instance == null)
         instance = new FavoriteController();
      return instance;
   }
	// 관심 상품 등록
    public Command addFavorite() {
        FavoriteVo favorite = new FavoriteVo();
        PostVo postvo = new PostVo();
        UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser");
       
        favorite.setUser_id(loginUserVo.getUser_id());
        int postId = ScanUtil.nextInt("관심 상품으로 등록할 게시물 번호를 입력하세요: ");
        favorite.setPost_id(postId); // 입력받은 ID를 설정
        
        if (favoriteService.isFavoriteExists(loginUserVo.getUser_id(), postId)) {
            System.out.println("관심 상품 목록에 동일한 상품이 존재합니다");
            return Command.POST_LIST; // 동일한 상품이 존재할 경우 상세 페이지로 돌아가기
        }
        favoriteService.addFavorite(favorite);
        System.out.println("관심 상품 등록 완료.");
        return Command.POST_LIST; // 게시물 목록으로 돌아가기
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
        return Command.USER_HOME; // 게시물 목록으로 돌아가기
    }

    // 관심 상품 삭제
    public void deleteFavorite() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("삭제할 게시글 번호 입력: ");
        int postId = scanner.nextInt();

        UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser");
        String currentUserId = loginUserVo.getUser_id(); // 현재 로그인한 사용자 ID를 가져옵니다.

        // 삭제 메서드 호출
        boolean isDeleted = favoriteService.deleteFavorite(currentUserId, postId);
        
        if (isDeleted) {
            System.out.println("관심 상품 삭제 완료.");
        } else {
            System.out.println("해당 게시글의 관심 상품이 존재하지 않거나 삭제할 수 없습니다.");
        }
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
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println();
        	System.out.println("1. 관심 상품 보기");
        	System.out.println("2. 관심 상품 삭제");
            System.out.println("0. 종료");
            System.out.print("선택: ");
            int choice = scanner.nextInt();

            switch (choice) {
            	case 1:
            		viewFavorites();
            		break;
            	case 2:
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
