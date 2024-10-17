package SERVICE;

import java.sql.Date;
import java.util.List;

import CONTROLLER.HistoryController;
import DAO.HistoryDAO;
import VO.HistoryVo;
import VO.PostVo;

public class HistoryService {
	private HistoryDAO historyDAO;

	    public HistoryService() {
	        this.historyDAO = new HistoryDAO();
	    }

	    
	    public void processTransaction(String buyerId, String sellerId, int postId) {
	        // 현재 날짜를 가져옵니다.
	        Date currentDate = new Date(System.currentTimeMillis());
	        
	        // 거래 내역 객체 생성
	        HistoryVo history = new HistoryVo("0", buyerId, sellerId, postId, currentDate);
	        
	        // 거래 내역 추가
	        historyDAO.addTransaction(history);
	    }
	    
	    public List<HistoryVo> getTransactionHistory(String userId) {
	        return historyDAO.getTransactionHistory(userId);
	    }

	 
}
