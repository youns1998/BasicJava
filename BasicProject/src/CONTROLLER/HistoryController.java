package CONTROLLER;

import java.util.List;
import SERVICE.HistoryService;
import UTIL.Command;
import VO.HistoryVo;
import VO.UsersVo;

public class HistoryController {
    private HistoryService historyService;
    private static HistoryController instance;
    
    // HistoryController 생성자 (싱글톤 패턴 적용)
    private HistoryController() {
        this.historyService = HistoryService.getInstance(); // HistoryService의 싱글톤 인스턴스 가져오기
    }

    // HistoryController 인스턴스를 반환하는 메서드 (싱글톤 패턴)
    public static HistoryController getInstance() {
        if (instance == null)
            instance = new HistoryController();
        return instance;
    }
    
    // 거래 완료 처리 메서드
    public void completeTransaction(String buyerId, String sellerId, int postId) {
        // 거래를 처리하는 서비스 호출
        historyService.processTransaction(buyerId, sellerId, postId);
        System.out.println("거래가 완료되었습니다.");
        System.out.println("구매자 ID: " + buyerId);
        System.out.println("판매자 ID: " + sellerId);
        System.out.println("게시글 ID: " + postId);
    }

    // 거래 내역 출력 메서드
    public void printTransactionHistory(List<HistoryVo> historyList) {
        // 전달받은 거래 내역 리스트를 반복하여 출력
        for (HistoryVo history : historyList) {
            // 거래 상태에 따른 문자열 변환
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
            // 거래 정보 출력
            System.out.println("거래 번호: " + history.getTransaction_id() + " | 구매자 ID: " + history.getBuyer_id()
                + " | 판매자 ID: " + history.getSeller_id() + " | 게시글 ID: " + history.getPost_id()
                + " | 거래 날짜: " + history.getTransaction_date() + " | 거래 상태: " + transactionStatus);
        }
    }

    // 현재 로그인한 사용자의 거래 내역 조회 메서드
    public Command viewTransactionHistory() {
        // 세션에서 로그인한 사용자 정보를 가져옴
        UsersVo loginUserVo = (UsersVo) MainController.sessionMap.get("loginUser");

        // 만약 사용자가 로그인하지 않았다면, 로그인 요청 및 홈 화면으로 이동
        if (loginUserVo == null) {
            System.out.println("로그인이 필요합니다. 먼저 로그인하세요.");
            return Command.LOGIN;
        }

        // 로그인한 사용자의 ID로 거래 내역 조회
        String userId = loginUserVo.getUser_id();
        List<HistoryVo> historyList = historyService.getTransactionHistory(userId);

        // 거래 내역 조회 후 홈 화면으로 이동
        return Command.USER_HOME;
    }
}
