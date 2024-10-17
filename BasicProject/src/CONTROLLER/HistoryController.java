package CONTROLLER;

import java.util.List;

import SERVICE.HistoryService;
import UTIL.Command;
import VO.HistoryVo;
import VO.UsersVo;

public class HistoryController {
	private static HistoryController instance;
	private HistoryService historyService;
	 
	    public HistoryController() {
	        this.historyService = new HistoryService();
	    }

	    public static HistoryController getInstance() {
	        if (instance == null)
	           instance = new HistoryController();
	        return instance;
	     }
	    
	    public void completeTransaction(String buyerId, String sellerId, int postId) {
	        // 거래가 완료되었음을 처리합니다.
	        historyService.processTransaction(buyerId, sellerId, postId);
	        
	        // 결과 출력 (예: 콘솔에 출력)
	        System.out.println("거래가 완료되었습니다: " +
	                "구매자 ID: " + buyerId + ", " +
	                "판매자 ID: " + sellerId + ", " +
	                "게시글 ID: " + postId);
	    }
	    
	    
	 // 현재 로그인한 사용자의 거래 내역 조회 메서드
	    public Command viewTransactionHistory() {
	        // 세션에서 로그인한 사용자 정보 가져오기
	        UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser");
	        
	        // 로그인한 사용자가 없다면 경고 메시지 출력
	        if (loginUserVo == null) {
	            System.out.println("로그인한 사용자가 없습니다.");
	            return Command.HOME;
	        }

	        String userId = loginUserVo.getUser_id(); // 사용자 ID 가져오기
	        List<HistoryVo> historyList = historyService.getTransactionHistory(userId);
	        
	        if (historyList.isEmpty()) {
	            System.out.println("거래 내역이 없습니다.");
	        } else {
	            System.out.println("거래 내역:");
	            for (HistoryVo history : historyList) {
	                System.out.println("거래 번호 : " + history.getTransaction_id() +
	                                   ", 구매자 ID : " + history.getBuyer_id() +
	                                   ", 판매자 ID : " + history.getSeller_id() +
	                                   ", 게시글 ID : " + history.getPost_id() +
	                                   ", 거래 날짜 : " + history.getTransaction_date());
	            }
	        }
			return Command.USER_HOME;
			
	    }


}
