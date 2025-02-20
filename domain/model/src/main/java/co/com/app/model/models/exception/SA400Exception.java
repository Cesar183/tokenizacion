package co.com.app.model.models.exception;

public class SA400Exception extends BusinessException{
    public SA400Exception(String message) {
        super(message, "SA400");
    }
}
