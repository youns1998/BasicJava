package CONTROLLER;

import SERVICE.EmailService;
import UTIL.Command;
import UTIL.ScanUtil;
import java.util.HashMap;
import java.util.Map;

public class VerificationController {
    private static VerificationController instance;  // VerificationController 싱글톤 인스턴스를 저장할 변수
    private EmailService emailService = new EmailService();  // 이메일 전송을 담당하는 EmailService 인스턴스
    private Map<String, String> verificationCodes = new HashMap<>();  // 이메일과 인증 코드를 저장할 맵

    // 싱글톤 패턴을 적용한 VerificationController 인스턴스를 반환하는 메서드
    public static VerificationController getInstance() {
        if (instance == null) {  // 인스턴스가 존재하지 않으면 새로 생성
            instance = new VerificationController();
        }
        return instance;  // 이미 존재하는 인스턴스를 반환
    }

    // 이메일로 인증 코드를 생성하여 전송하는 메서드
    public Command sendVerificationCode(String email) {
        String code = String.format("%04d",(int) (Math.random() * 10000));  // 0000~9999 사이의 랜덤한 4자리 숫자 생성
        verificationCodes.put(email, code);  // 생성된 인증 코드를 맵에 저장
        emailService.sendVerificationEmail(email, code);  // EmailService를 이용하여 이메일로 인증 코드 전송
        System.out.println("이메일로 전송된 인증 코드를 확인하세요.");
        return Command.VERIFY_CODE;  // 인증 코드 입력 화면으로 이동하는 Command 반환
    }

    // 이메일과 입력된 인증 코드를 확인하는 메서드
    public boolean verifyCode(String email, String code) {
        if (verificationCodes.containsKey(email) && verificationCodes.get(email).equals(code)) {  // 이메일이 존재하고 인증 코드가 일치할 경우
            verificationCodes.remove(email);  // 인증 성공 후 해당 이메일의 인증 코드를 맵에서 제거
            return true;  // 인증 성공을 의미하는 true 반환
        }
        return false;  // 인증 실패 시 false 반환
    }
}
