package exception;

import exception.auth.login.InvalidCredentialsException;
import exception.user.get.UserNotFoundException;
import exception.user.join.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        // 상세 메시지 설정
        ErrorResponse errorResponse = new ErrorResponse(
                "USER_ALREADY_EXISTS",
                "이미 존재하는 사용자입니다.",
                "해당 이메일 주소로 이미 가입된 사용자가 있습니다. 다른 이메일을 사용해 주세요."
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        // 상세 메시지 설정
        ErrorResponse errorResponse = new ErrorResponse(
                "USER_NOT_FOUND",
                "사용자를 찾을 수 없습니다.",
                "해당 ID나 이메일로 사용자를 찾을 수 없습니다. 입력 정보를 다시 확인해 주세요."
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        // 상세 메시지 설정
        ErrorResponse errorResponse = new ErrorResponse(
                "INVALID_CREDENTIALS",
                "잘못된 자격 증명입니다.",
                "입력하신 이메일 또는 비밀번호가 잘못되었습니다. 다시 시도해 주세요."
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        // 상세 메시지 설정
        ErrorResponse errorResponse = new ErrorResponse(
                "INVALID_ARGUMENT",
                "유효하지 않은 인수입니다.",
                "요청에 제공된 매개변수가 잘못되었습니다: " + ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        // 500 에러에 대한 응답 생성
        ErrorResponse errorResponse = new ErrorResponse(
                "INTERNAL_SERVER_ERROR",
                "예기치 않은 오류가 발생했습니다. 나중에 다시 시도해 주세요.",
                "시스템 내부 오류: " + ex.getMessage()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
