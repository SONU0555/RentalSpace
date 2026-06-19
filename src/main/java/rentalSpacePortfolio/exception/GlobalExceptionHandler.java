package rentalSpacePortfolio.exception;

import RentSpace.common.dto.ErrorResponseDto;
import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    public static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    // Handle user not found  exception
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleUserNotFound(UserNotFoundException ex){
        
        logger.info("Creating exception for user not found");
        
        ErrorResponseDto response = handleErrorResponse(ex);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    
    // Handle generic exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(Exception ex){
        ErrorResponseDto error = new ErrorResponseDto();
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setMessage(ex.getMessage());
        error.setTimeStamp(System.currentTimeMillis());
        
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    // Handle validation exceptions
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex){
        
        Map<String, String> errors = new HashMap<>();
        
        // Loop through all errors and pick out the field name and message
        ex.getBindingResult().getFieldErrors().forEach(error -> 
                    errors.put(error.getField(), error.getDefaultMessage())
        );
        
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
    
    // Handle unauthorized access denied exceptions
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDto> handleAccessDeniedException(AccessDeniedException ex){
        ErrorResponseDto error = new ErrorResponseDto();
        error.setStatus(HttpStatus.FORBIDDEN.value());
        error.setMessage(ex.getMessage());
        error.setTimeStamp(System.currentTimeMillis());
        
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }
    
    // Handle bad request exceptions
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseDto> handleBadRequestException(BadRequestException ex){
        ErrorResponseDto error = new ErrorResponseDto();
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(ex.getMessage());
        error.setTimeStamp(System.currentTimeMillis());
        
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    
    
    // Handle property not found exception
//    @ExceptionHandler(PropertyNotAvailableException.class)
//    public ResponseEntity<ErrorResponseDto> handlePropertyNotAvailableException(PropertyNotAvailableException ex){
//        ErrorResponseDto error = new ErrorResponseDto();
//        error.setStatus(HttpStatus.CONFLICT.value());
//        error.setMessage(ex.getMessage());
//        error.setTimeStamp(System.currentTimeMillis());
//        
//        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
//    }
    
    // Handle property not found exception
//    @ExceptionHandler(BookingNotFoundException.class)
//    public ResponseEntity<ErrorResponseDto> handleBookingNotFoundException(BookingNotFoundException ex){
//
//        ErrorResponseDto response = handleErrorResponse(ex);
//        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
//    }
    
    // Handle Amenity not found exception
//    @ExceptionHandler(AmenityNotFoundException.class)
//    public ResponseEntity<ErrorResponseDto> handleAmenityNotFoundException(AmenityNotFoundException ex){
//        
//        ErrorResponseDto response = handleErrorResponse(ex);
//        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
//    }
    
    // Handle Amenity booking not found exception
//    @ExceptionHandler(BookingAmenityNotFound.class)
//    public ResponseEntity<ErrorResponseDto> handleBookingAmenityNotFound(BookingAmenityNotFound ex){
//        
//        ErrorResponseDto response = handleErrorResponse(ex);
//        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
//    }
    
    // Common method to create error response message for NOT_FOUND
    private ErrorResponseDto handleErrorResponse(Exception ex){
        ErrorResponseDto error = new ErrorResponseDto();
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(ex.getMessage());
        error.setTimeStamp(System.currentTimeMillis());
        
        return error;
    }

}