package rentalSpacePortfolio.dto.response;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ApiResponse<T> {
    
    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timeStamp;
    
    public ApiResponse() {};
    
    public ApiResponse(boolean success, String message, T data){
        this.success = success;
        this.message = message;
        this.data = data;
        this.timeStamp = LocalDateTime.now();
    }
    
    public static <T> ApiResponse<T> success(String message, T data){
        return new ApiResponse<>(true, message, data);
    }
    
    public static <T> ApiResponse<T> error(String message, T data){
        return new ApiResponse<>(false, message, null);
    }
    
}