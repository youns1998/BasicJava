package SERVICE;

import java.sql.Date;
import java.util.List;

import CONTROLLER.HistoryController;
import DAO.HistoryDAO;
import VO.HistoryVo;
import VO.PostVo;

public class HistoryService {
    private HistoryDAO historyDAO;
    private static HistoryService instance;

	private HistoryService() {
        this.historyDAO = new HistoryDAO(); // HistoryDAO 인스턴스 생성 또는 싱글톤 사용 가능

	}

	public static HistoryService getInstance() {
		if (instance == null)
			instance = new HistoryService();
		return instance;
	}

    public void processTransaction(String buyerId, String sellerId, int postId) {
        // 현재 날짜를 가져옵니다.
        Date currentDate = new Date(System.currentTimeMillis());
        
        // 상태는 기본적으로 예약 중(2)으로 설정
        int transactionStatus = 2;  // 예약중 상태
        
        // 거래 내역 객체 생성
        HistoryVo history = new HistoryVo("0", buyerId, sellerId, postId, currentDate, transactionStatus);
        
        // 거래 내역 추가
        historyDAO.addTransaction(history);
    }
    // 거래 내역 추가 (예약중으로 상태 업데이트)
    public void addTransaction(String buyerId, String sellerId, int postId, int status) {
        Date currentDate = new Date(System.currentTimeMillis());
        HistoryVo history = new HistoryVo("0", buyerId, sellerId, postId, currentDate, status);
        historyDAO.addTransaction(history);
    }

    // 거래 상태 업데이트 (판매 완료로 상태 업데이트)
    public void updateTransactionStatus(int postId, int status) {
        historyDAO.updateTransactionStatus(postId, status);
    }

 // 거래 완료로 상태 변경
    public void updateTransactionToComplete(String buyerId, String sellerId, int postId) {
        historyDAO.updateTransactionStatus(postId, 3);  // 상태를 거래 완료(3)로 업데이트
    }
    // 거래 내역 삭제 (판매중으로 상태 변경 시)
    public void deleteTransaction(int postId) {
        historyDAO.deleteTransaction(postId);
    }

    // 거래 내역 조회
    public List<HistoryVo> getTransactionHistory(String userId) {
        return historyDAO.getTransactionHistory(userId);
    }
    
    public String getBuyerIdFromTransaction(int postId) {
        return historyDAO.getBuyerIdFromTransaction(postId);  // DAO에서 직접 호출
    }
    
    
}
