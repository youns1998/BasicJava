package SERVICE;
import java.util.List;
import DAO.FavoriteDAO;
import DAO.PostDao;
import VO.FavoriteVo;
import VO.UsersVo;

public class FavoriteService {
    private FavoriteDAO favoriteDAO = new FavoriteDAO();

    private static FavoriteService instance;

	private FavoriteService() {

	}

	public static FavoriteService getInstance() {
		if (instance == null)
			instance = new FavoriteService();
		return instance;
	}
       // 관심 상품 등록
       public void addFavorite(FavoriteVo favorite) {
           favoriteDAO.addFavorite(favorite);
       }

       // 사용자가 본인의 관심 상품 목록 조회
       public List<FavoriteVo> getFavoritesByUser() {
    	   return favoriteDAO.getFavoritesByUser();
       }
       
       // 관리자가 사용자의 관심 상품 목록 조회
       public List<FavoriteVo> getFavoritesList(String userId) {
    	   return favoriteDAO.getFavoritesList(userId);
       }

       // 관심 상품 삭제
       public boolean deleteFavorite(String userId, int postId) {
         return favoriteDAO.deleteFavorite(userId, postId);
       }

       // 특정 사용자가 특정 게시글을 즐겨찾기 했는지 확인
       public boolean isFavoriteExists(String userId, int postId) {
           return favoriteDAO.isFavorite(userId, postId);
       }
       // 게시물에서 찜을한 사람의 수 확인
       public int countFavoritesForPost(int postId) {
    	   return favoriteDAO.countFavoritesForPost(postId);
       }
     
}

