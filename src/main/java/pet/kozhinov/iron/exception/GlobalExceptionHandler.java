package pet.kozhinov.iron.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static pet.kozhinov.iron.utils.Constants.HANDLER_ERRORS_PARAMETER;
import static pet.kozhinov.iron.utils.Constants.HANDLER_STATUS_PARAMETER;
import static pet.kozhinov.iron.utils.Constants.HANDLER_TIMESTAMP_PARAMETER;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({BadRequestException.class, ValidationException.class,
            javax.validation.ValidationException.class})
    public ResponseEntity<Object> handleBadRequestException(RuntimeException ex) {
        return simpleHandleException(HttpStatus.BAD_REQUEST, "Bad request", ex);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException ex) {
        return simpleHandleException(HttpStatus.NOT_FOUND, "Not found", ex);
    }

    @ExceptionHandler({ForbiddenException.class, AccessDeniedException.class})
    public ResponseEntity<Object> handleForbiddenException(RuntimeException ex) {
        return simpleHandleException(HttpStatus.FORBIDDEN, "Forbidden", ex);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleAllUncaughtException(RuntimeException ex) {
        log.error(ex.getMessage());

        Map<String, Object> body = new LinkedHashMap<>();
        body.put(HANDLER_TIMESTAMP_PARAMETER, LocalDateTime.now());

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put(HANDLER_TIMESTAMP_PARAMETER, LocalDate.now());
        body.put(HANDLER_STATUS_PARAMETER, status.value());

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        body.put(HANDLER_ERRORS_PARAMETER, errors);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> simpleHandleException(HttpStatus status, String message, RuntimeException ex) {
        log.info("An expected exception on user behaviour: " + ex.getMessage());
        ErrorResponse error = new ErrorResponse(message, Collections.singletonList(ex.getLocalizedMessage()));
        return new ResponseEntity<>(error, status);
    }
}
