package sample.testcode.spring.api;

import org.springframework.http.HttpStatus;
import sample.testcode.spring.api.service.product.response.ProductResponse;

public class ApiResponse <T> {
    private int code;
    private HttpStatus status;
    private String message;
    private T data;


    public ApiResponse(HttpStatus status, String message, T data) {
        this.code = status.value();
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> of(HttpStatus status, T data) {
        return new ApiResponse<T>(status, status.name(), data);
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<T>(HttpStatus.OK, HttpStatus.OK.name(), data);
    }
}
