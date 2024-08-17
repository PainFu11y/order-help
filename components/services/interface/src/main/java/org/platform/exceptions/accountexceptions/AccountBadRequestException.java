package org.platform.exceptions.accountexceptions;

import org.platform.exceptions.BadRequestException;
import org.platform.exceptions.ResourceAlreadyExistsException;

public class AccountBadRequestException extends BadRequestException {
    public AccountBadRequestException(String errorMessage) {
        super(errorMessage);
    }
}