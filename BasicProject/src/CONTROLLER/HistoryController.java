package CONTROLLER;

import java.util.List;
import SERVICE.HistoryService;
import UTIL.Command;
import VO.HistoryVo;
import VO.UsersVo;

public class HistoryController {
    private HistoryService historyService;
    private static HistoryController instance;
	private HistoryController() {
        this.historyService = HistoryService.getInstance(); // Singleton 인스턴스 생성


	}

	public static HistoryController getInstance() {
		if (instance == null)
			instance = new HistoryController();
		return instance;
	}
    

    // 거래 완료 처리 메서드
    public void completeTransaction(String buyerId, String sellerId, int postId) {
        historyService.processTransaction(buyerId, sellerId, postId);
        System.out.println("거래가 완료되었습니다.");
        System.out.println("구매자 ID: " + buyerId);
        System.out.println("판매자 ID: " + sellerId);
        System.out.println("게시글 ID: " + postId);
    }

    // 거래 내역 출력 메서드
    public void printTransactionHistory(List<HistoryVo> historyList) {
        System.out.println("======= 거래 내역 =======");
        for (HistoryVo history : historyList) {
            String transactionStatus;
            switch (history.getTransaction_status()) {
                case 1:
                    transactionStatus = "판매중";
                    break;
                case 2:
                    transactionStatus = "예약중";
                    break;
                case 3:
                    transactionStatus = "거래 완료";
                    break;
                default:
                    transactionStatus = "알 수 없음";
            }
            System.out.println("거래 번호: " + history.getTransaction_id() + " | 구매자 ID: " + history.getBuyer_id()
                + " | 판매자 ID: " + history.getSeller_id() + " | 게시글 ID: " + history.getPost_id()
                + " | 거래 날짜: " + history.getTransaction_date() + " | 거래 상태: " + transactionStatus);
        }
    }

    // 현재 로그인한 사용자의 거래 내역 조회 메서드
    public Command viewTransactionHistory() {
        // 세션에서 로그인한 사용자 정보 가져오기
        UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser");

        // 로그인한 사용자가 없다면 경고 메시지 출력 및 홈으로 이동
        if (loginUserVo == null) {
            System.out.println("로그인이 필요합니다. 먼저 로그인하세요.");
            return Command.LOGIN;
        }

        // 사용자 ID로 거래 내역 가져오기
        String userId = loginUserVo.getUser_id();
        List<HistoryVo> historyList = historyService.getTransactionHistory(userId);


        return Command.USER_HOME; // 거래 내역 조회 후 홈으로 이동
    }
}
