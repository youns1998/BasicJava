package CONTROLLER;

import java.util.List;
import SERVICE.HistoryService;
import SERVICE.UsersService;
import UTIL.Command;
import VO.HistoryVo;
import VO.UsersVo;

public class HistoryController {
    private HistoryService historyService;
    private UsersService usersService;
    private static HistoryController instance;

    // HistoryController 생성자 (싱글톤 패턴 적용)
    private HistoryController() {
        this.historyService = HistoryService.getInstance(); // HistoryService의 싱글톤 인스턴스 가져오기
        this.usersService = UsersService.getInstance(); // UsersService 인스턴스 가져오기
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

    // 거래 내역 출력 메서드 (구매자와 판매자의 이메일, 전화번호도 함께 출력)
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

            // 구매자 정보와 판매자 정보 가져오기
            UsersVo buyer = usersService.getUserSelect(history.getBuyer_id());
            UsersVo seller = usersService.getUserSelect(history.getSeller_id());

            // 거래 정보 출력 (구매자와 판매자의 이메일 및 전화번호 포함)
            System.out.println("======거래내역======");
            System.out.printf("거래 번호: %s | 게시글 ID: %d | 거래 날짜: %s | 거래 상태: %s\n", 
                history.getTransaction_id(), history.getPost_id(), history.getTransaction_date(), transactionStatus);

            // 구매자 정보 출력
            if (buyer != null) {
                System.out.printf("구매자 정보: ID: %s | 이름: %s | 이메일: %s | 전화번호: %s\n",
                    buyer.getUser_id(), buyer.getUsername(), buyer.getEmail(), buyer.getPhone_number());
            } else {
                System.out.println("구매자 정보가 존재하지 않습니다.");
            }

            // 판매자 정보 출력
            if (seller != null) {
                System.out.printf("판매자 정보: ID: %s | 이름: %s | 이메일: %s | 전화번호: %s\n",
                    seller.getUser_id(), seller.getUsername(), seller.getEmail(), seller.getPhone_number());
            } else {
                System.out.println("판매자 정보가 존재하지 않습니다.");
            }

            System.out.println(); // 거래 내역 사이에 공백 추가
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

        // 거래 내역 출력
        printTransactionHistory(historyList);  // 이 부분에서만 거래 내역 출력

        // 거래 내역 조회 후 홈 화면으로 이동
        return Command.USER_HOME;
    }
}
