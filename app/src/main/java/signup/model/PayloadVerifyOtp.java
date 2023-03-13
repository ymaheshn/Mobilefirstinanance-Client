package signup.model;

public class PayloadVerifyOtp {

    private String To;
    private String Code;

    public PayloadVerifyOtp(String to, String code) {
        To = to;
        Code = code;
    }
}
