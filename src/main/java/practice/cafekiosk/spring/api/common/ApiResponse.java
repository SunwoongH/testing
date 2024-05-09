package practice.cafekiosk.spring.api.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ApiResponse<T> {
    private int code;
    private HttpStatus status;
    private String message;
    private T data;

    public static <T> ApiResponse<T> of(HttpStatus status, String message, T data) {
        return ApiResponse.<T>builder()
                .code(status.value())
                .status(status)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> of(HttpStatus status, T data) {
        return ApiResponse.of(status, status.name(), data);
    }

    public static <T> ApiResponse<T> ok(T data) {
        return ApiResponse.<T>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(data)
                .build();
    }
}
