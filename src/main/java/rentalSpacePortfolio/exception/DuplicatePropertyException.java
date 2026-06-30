package rentalSpacePortfolio.exception;



public class DuplicatePropertyException extends RuntimeException{
    public DuplicatePropertyException(String message){
        super(message);
    }
}