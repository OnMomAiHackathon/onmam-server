package exception.user.get;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message) {
        super(message);// 유저를 찾을 수 없습니다
    }
}
