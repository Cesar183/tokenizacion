package co.com.app.model.models.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessException extends RuntimeException{
    private final String code;

    public BusinessException(String message, String code){
        super(message);
        this.code = code;
    }
}
