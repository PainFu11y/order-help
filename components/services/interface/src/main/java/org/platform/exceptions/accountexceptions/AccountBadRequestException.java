package org.platform.exceptions.accountexceptions;

import org.platform.exceptions.BadRequestException;

public class AccountBadRequestException extends BadRequestException {
    public AccountBadRequestException(String errorMessage) {
        super(errorMessage);
    }
}
