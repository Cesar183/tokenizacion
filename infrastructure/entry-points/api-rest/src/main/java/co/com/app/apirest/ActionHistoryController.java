package co.com.app.apirest;

import co.com.app.apirest.validator.ActionHistoryRequestValidator;
import co.com.app.model.models.dto.request.ApiRequest;
import co.com.app.model.models.dto.request.Data;
import co.com.app.model.models.dto.response.ResponseActionHistory;
import co.com.app.model.models.exception.*;
import co.com.app.usecase.usecasetokenizacion.UsecaseTokenizacionUseCase;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ActionHistoryController {
    private final UsecaseTokenizacionUseCase actionHistoryUseCase;

    @PostMapping(path = "/api", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getActionHistory(HttpServletRequest requestHeader, @RequestBody ApiRequest request) {
        ResponseEntity<?> result;
        String messageId = null;
        try {
            messageId = requestHeader.getHeader("Message-Id");
            ActionHistoryRequestValidator.validate(request);
            Data requestData = request.getData();
            ResponseActionHistory response = actionHistoryUseCase.doAction(requestData, messageId);
            ThreadContext.put("Message-Id", messageId);
            result = ResponseEntity.ok(response);
        } catch (BP404006Exception e) {
            result = buildErrorResponse(HttpStatus.NOT_FOUND, "BP404006", e.getMessage(), messageId);
        } catch (BP404005Exception e) {
            result = buildErrorResponse(HttpStatus.NOT_FOUND, "BP404005", e.getMessage(), messageId);
        } catch (BP400003Exception e) {
            result = buildErrorResponse(HttpStatus.BAD_REQUEST, "BP400003", e.getMessage(), messageId);
        } catch (SA400Exception | SA500Exception e) {
            result = buildErrorResponse(HttpStatus.BAD_REQUEST, "SA400", e.getMessage(), messageId);
        }
        return result;
    }
    private ResponseEntity<?> buildErrorResponse(HttpStatus status, String errorCode, String errorMessage, String messageId) {
        Map<String, Object> errorBody = new LinkedHashMap<>();
        if(messageId != null){
            errorBody.put("meta", Map.of(
                    "_messageId", messageId,
                    "_requestDateTime", LocalDateTime.now(),
                    "_applicationId", messageId
            ));
            errorBody.put("status", status.value());
            errorBody.put("title", status.getReasonPhrase());
            errorBody.put("errors", Collections.singletonList(
                    Map.of(
                            "code", errorCode,
                            "detail", errorMessage
                    )
            ));
        }
        return ResponseEntity.status(status).body(errorBody);
    }
}
