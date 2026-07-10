package rentalSpacePortfolio.exception;


public class MaxUploadCountExceededException extends RuntimeException{
    public MaxUploadCountExceededException(String message){
        super(message);
    }
}