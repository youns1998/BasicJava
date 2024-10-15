package CONTROLLER;

import java.util.List;
import java.util.Scanner;

import SERVICE.FavoriteService;
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
        int postId = ScanUtil.nextInt("관심 상품으로 등록할 게시물 ID를 입력하세요: ");
        favorite.setPost_id(postId); // 입력받은 ID를 설정
        
        if(postId == favorite.getPost_id()) {
        	System.out.println("찜 목록에 동일한 상품이 존재합니다");
        	return Command.POST_DETAIL;										// 여기부분 수정해야함
        }
        favoriteService.addFavorite(favorite);
        System.out.println("관심 상품 등록 완료.");
        return Command.POST_LIST; // 게시물 목록으로 돌아가기
    }
    // 사용자의 관심 상품 목록 조회
    public void viewFavorites() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("사용자 ID 입력: ");
        String userId = scanner.next();

        List<FavoriteVo> favorites = favoriteService.getFavoritesByUser(userId);
        System.out.println("관심 상품 목록:");
        for (FavoriteVo favorite : favorites) {
            System.out.println("게시글 ID: " + favorite.getPost_id());
            
        }
    }

    // 관심 상품 삭제
    public void deleteFavorite() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("사용자 ID 입력: ");
        String userId = scanner.next();
        System.out.print("삭제할 게시글 ID 입력: ");
        int postId = scanner.nextInt();

        favoriteService.deleteFavorite(userId, postId);
        System.out.println("관심 상품 삭제 완료.");
    }

    // 특정 사용자가 특정 게시글을 즐겨찾기 했는지 확인
    public void checkFavorite() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("사용자 ID 입력: ");
        String userId = scanner.next();
        System.out.print("게시글 ID 입력: ");
        int postId = scanner.nextInt();

        boolean isFavorite = favoriteService.isFavorite(userId, postId);
        if (isFavorite) {
            System.out.println("이 사용자는 이 게시글을 즐겨찾기 했습니다.");
        } else {
            System.out.println("이 사용자는 이 게시글을 즐겨찾기 하지 않았습니다.");
        }
    }
    
    // 관심 상품 관리 메뉴 표시
    public void displayMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1. 관심 상품 등록");
            System.out.println("2. 관심 상품 목록 조회");
            System.out.println("3. 관심 상품 삭제");
            System.out.println("4. 특정 게시글 즐겨찾기 확인");
            System.out.println("5. 종료");
            System.out.print("선택: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    addFavorite();
                    break;
                case 2:
                    viewFavorites();
                    break;
                case 3:
                    deleteFavorite();
                    break;
                case 4:
                    checkFavorite();
                    break;
                case 5:
                    System.out.println("종료합니다.");
                    return;
                default:
                    System.out.println("잘못된 선택입니다.");
            }
        }
    }

	
    
}
