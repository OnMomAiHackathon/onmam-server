package exception.user.join;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }// 이미 사용 중인 이메일입니다.
}
