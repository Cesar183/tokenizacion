package co.com.app.model.models.exception;

public class SA500Exception extends BusinessException{
    public SA500Exception(String message) {
        super(message, "SA500");
    }
}
