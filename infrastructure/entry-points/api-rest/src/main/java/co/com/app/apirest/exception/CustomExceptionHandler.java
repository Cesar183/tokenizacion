package co.com.app.apirest.exception;

import co.com.app.model.models.exception.BP404006Exception;
import co.com.app.model.models.exception.BusinessException;
import co.com.app.model.models.exception.SA400Exception;
import co.com.app.model.models.exception.SA500Exception;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CustomExceptionHandler {
    private static final Pattern VALID_HEADER_REGEX = Pattern.compile("^[a-zA-Z0-9]*$");
    private static final String MESSAGE_ID = "Message-Id";
    private static final String _APPLICATION_ID = "_applicationId";
    private static final String _REQUEST_DATE_TIME = "_requestDateTime";
    private static final String _MESSAGE_ID = "_messageId";
    private static final String STATUS = "status";
    private static final String TITLE = "title";
    private static final String INVALID_HEADER_VALUE = "Invalid header value";
    private static final String META = "meta";
    private static final String CODE = "code";
    private static final String DETAIL ="detail";
    private static final String ERRORS = "errors";
    private static final String NOT_FOUND = "Not Found";
    private static final int CODE_NOT_FOUND = 404;
    private static final int CODE_INTERNAL_SERVER_ERROR = 500;

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<String> handleBusinessExceptions(BusinessException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error code: " + ex.getCode() + " , Message: "+ex.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex){
        List<String> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(String.join(", ", errors));
    }

    @ExceptionHandler(SA400Exception.class)
    public ResponseEntity<Map<String, Object>> handleInvalidParameterException(SA400Exception ex, HttpServletRequest request){
        Map<String, Object> errorResponse = new HashMap<>();
        Map<String, String> meta = new HashMap<>();
        Map<String, String> errorDetail = new HashMap<>();

        String _messageId = request.getHeader(MESSAGE_ID);

        if (isValid(_messageId)) {
            meta.put(_APPLICATION_ID, _messageId);
            meta.put(_REQUEST_DATE_TIME, LocalDateTime.now().toString());
            meta.put(_MESSAGE_ID, _messageId);
        } else {
            errorResponse.put(STATUS, CODE_INTERNAL_SERVER_ERROR);
            errorResponse.put(TITLE, INVALID_HEADER_VALUE);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        errorDetail.put(CODE, ex.getCode());
        errorDetail.put(DETAIL, ex.getMessage());

        errorResponse.put(META, meta);
        errorResponse.put(STATUS, CODE_NOT_FOUND);
        errorResponse.put(TITLE, NOT_FOUND);
        errorResponse.put(ERRORS, List.of(errorDetail));

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SA500Exception.class)
    public ResponseEntity<Map<String, Object>> handleInternalServerException(SA500Exception ex, WebRequest request){
        Map<String, Object> errorResponse = new HashMap<>();
        Map<String, String> meta = new HashMap<>();
        Map<String, String> errorDetail = new HashMap<>();

        String _messageId = request.getHeader(MESSAGE_ID);

        if (isValid(_messageId)) {
            meta.put(_APPLICATION_ID, _messageId);
            meta.put(_REQUEST_DATE_TIME, LocalDateTime.now().toString());
            meta.put(_MESSAGE_ID, _messageId);
        } else {
            errorResponse.put(STATUS, CODE_INTERNAL_SERVER_ERROR);
            errorResponse.put(TITLE, INVALID_HEADER_VALUE);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        errorDetail.put(CODE, "SA500");
        errorDetail.put(DETAIL, ex.getMessage());

        errorResponse.put(META, meta);
        errorResponse.put(STATUS, CODE_INTERNAL_SERVER_ERROR);
        errorResponse.put(TITLE, "Internal Server Error");
        errorResponse.put(ERRORS, List.of(errorDetail));

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(BP404006Exception.class)
    public ResponseEntity<Map<String, Object>> handleNotFoundException(BP404006Exception ex, WebRequest request){
        Map<String, Object> errorResponse = new HashMap<>();
        Map<String, String> meta = new HashMap<>();
        Map<String, String> errorDetail = new HashMap<>();

        String _messageId = request.getHeader(MESSAGE_ID);

        if (isValid(_messageId)) {
            meta.put(_APPLICATION_ID, _messageId);
            meta.put(_REQUEST_DATE_TIME, LocalDateTime.now().toString());
            meta.put(_MESSAGE_ID, _messageId);
        } else {
            errorResponse.put(STATUS, CODE_INTERNAL_SERVER_ERROR);
            errorResponse.put(TITLE, INVALID_HEADER_VALUE);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        errorDetail.put(CODE, "SA404");
        errorDetail.put(DETAIL, ex.getMessage());

        errorResponse.put(META, meta);
        errorResponse.put(STATUS, CODE_NOT_FOUND);
        errorResponse.put(TITLE, NOT_FOUND);
        errorResponse.put(ERRORS, List.of(errorDetail));

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    private boolean isValid(String headerValue) {
        return headerValue != null && VALID_HEADER_REGEX.matcher(headerValue).matches();
    }
}
