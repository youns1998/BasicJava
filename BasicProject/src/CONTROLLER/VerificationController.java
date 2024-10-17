package CONTROLLER;

import SERVICE.EmailService;
import UTIL.Command;
import UTIL.ScanUtil;
import java.util.HashMap;
import java.util.Map;

public class VerificationController {
    private static VerificationController instance;
    private EmailService emailService = new EmailService();
    private Map<String, String> verificationCodes = new HashMap<>();

    public static VerificationController getInstance() {
        if (instance == null) {
            instance = new VerificationController();
        }
        return instance;
    }

    public Command sendVerificationCode(String email) {
        String code = String.valueOf((int) (Math.random() * 10000)); // 랜덤한 4자리 코드 생성
        verificationCodes.put(email, code);
        emailService.sendVerificationEmail(email, code);
        System.out.println("이메일로 전송된 인증 코드를 확인하세요.");
        return Command.VERIFY_CODE;
    }

    public boolean verifyCode(String email, String code) {
        if (verificationCodes.containsKey(email) && verificationCodes.get(email).equals(code)) {
            verificationCodes.remove(email); // 성공 시 코드 삭제
            return true;
        }
        return false;
    }
}
