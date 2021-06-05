package pet.kozhinov.iron.utils;

import pet.kozhinov.iron.entity.Case;
import pet.kozhinov.iron.exception.BadRequestException;

import javax.validation.Valid;

public final class ValidationUtils {

    public static void validate(@Valid Case aCase) {
    }

    public static void validateId(String id) throws BadRequestException {
        if (id == null || id.isEmpty()) {
            throw new BadRequestException("Given id is incorrect");
        }

        try {
            Long.parseLong(id);
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Given id is not a number");
        }
    }

    private ValidationUtils() {}
}
