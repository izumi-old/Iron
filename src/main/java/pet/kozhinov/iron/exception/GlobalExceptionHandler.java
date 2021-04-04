package pet.kozhinov.iron.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static pet.kozhinov.iron.utils.Constants.HANDLER_ERRORS_PARAMETER;
import static pet.kozhinov.iron.utils.Constants.HANDLER_MESSAGE_PARAMETER;
import static pet.kozhinov.iron.utils.Constants.HANDLER_STATUS_PARAMETER;
import static pet.kozhinov.iron.utils.Constants.HANDLER_TIMESTAMP_PARAMETER;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(HANDLER_TIMESTAMP_PARAMETER, LocalDateTime.now());
        body.put(HANDLER_MESSAGE_PARAMETER, ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<Object> handleInternalServerErrorException(InternalServerErrorException ex) {
        return handleAllUncaughtException(ex);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleAllUncaughtException(RuntimeException ex) {
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
}
