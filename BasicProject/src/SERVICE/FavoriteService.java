package SERVICE;
<<<<<<< HEAD
import java.util.List;

import DAO.FavoriteDAO;
import VO.FavoriteVo;
import VO.PostVo;
import VO.UsersVo;
import VO.FavoriteVo;

public class FavoriteService {
    private FavoriteDAO favoriteDAO = new FavoriteDAO();

       // 관심 상품 등록
       public void addFavorite(FavoriteVo favorite) {
           favoriteDAO.addFavorite(favorite);
       }

       // 사용자의 관심 상품 목록 조회
       public List<FavoriteVo> getFavoritesByUser(String userId) {
           return favoriteDAO.getFavoritesByUser(userId);
       }

       // 관심 상품 삭제
       public void deleteFavorite(String userId, int postId) {
           favoriteDAO.deleteFavorite(userId, postId);
       }

       // 특정 사용자가 특정 게시글을 즐겨찾기 했는지 확인
       public boolean isFavorite(String userId, int postId) {
           return favoriteDAO.isFavorite(userId, postId);
       }
=======

import java.util.List;

import DAO.FavoriteDAO;
import VO.FavoriteVo;
import VO.PostVo;
import VO.UsersVo;
import VO.FavoriteVo;
ㄴ
public class FavoriteService {
	 private FavoriteDAO favoriteDAO = new FavoriteDAO();

	    // 관심 상품 등록
	    public void addFavorite(FavoriteVo favorite) {
	        favoriteDAO.addFavorite(favorite);
	    }

	    // 사용자의 관심 상품 목록 조회
	    public List<FavoriteVo> getFavoritesByUser(String userId) {
	        return favoriteDAO.getFavoritesByUser(userId);
	    }

	    // 관심 상품 삭제
	    public void deleteFavorite(String userId, int postId) {
	        favoriteDAO.deleteFavorite(userId, postId);
	    }

	    // 특정 사용자가 특정 게시글을 즐겨찾기 했는지 확인
	    public boolean isFavorite(String userId, int postId) {
	        return favoriteDAO.isFavorite(userId, postId);
	    }
>>>>>>> refs/remotes/origin/main

}
