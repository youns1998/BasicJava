package CONTROLLER;

import java.util.List;
import java.util.Scanner;
<<<<<<< HEAD

import DAO.FavoriteDAO;
import SERVICE.FavoriteService;
import VO.FavoriteVo;

public class FavoriteController {
   private FavoriteService favoriteService = new FavoriteService();
   
   
   private static FavoriteController instance;
   
   private FavoriteController() {
=======
>>>>>>> refs/remotes/origin/main

<<<<<<< HEAD
   }
   public static FavoriteController getInstance() {
      if (instance == null)
         instance = new FavoriteController();
      return instance;
   }
   
   // 관심 상품 등록
    public void addFavorite() {
        Scanner scanner = new Scanner(System.in);
        FavoriteVo favorite = new FavoriteVo();
=======
import DAO.FavoriteDAO;
import SERVICE.FavoriteService;
import VO.FavoriteVo;

public class FavoriteController {
	private FavoriteService favoriteService = new FavoriteService();
	
	
	private static FavoriteController instance;
	
	private FavoriteController() {
>>>>>>> refs/remotes/origin/main

<<<<<<< HEAD
        System.out.print("사용자 ID 입력: ");
        favorite.setUser_id(scanner.next());
        System.out.print("게시글 ID 입력: ");
        favorite.setPost_id(scanner.nextInt());

        favoriteService.addFavorite(favorite);
        System.out.println("관심 상품 등록 완료.");
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

   
=======
	}
	public static FavoriteController getInstance() {
		if (instance == null)
			instance = new FavoriteController();
		return instance;
	}
	
	// 관심 상품 등록
    public void addFavorite() {
        Scanner scanner = new Scanner(System.in);
        FavoriteVo favorite = new FavoriteVo();

        System.out.print("사용자 ID 입력: ");
        favorite.setUser_id(scanner.next());
        System.out.print("게시글 ID 입력: ");
        favorite.setPost_id(scanner.nextInt());

        favoriteService.addFavorite(favorite);
        System.out.println("관심 상품 등록 완료.");
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

	
>>>>>>> refs/remotes/origin/main
    
}
