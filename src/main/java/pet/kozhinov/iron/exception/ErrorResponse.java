package pet.kozhinov.iron.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ErrorResponse {
    private String message;
    private Collection<String> details;
}
