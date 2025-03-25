package io.github.evaggelos99.ems.common.api.controller.exceptions;

import org.springframework.http.HttpStatusCode;

import java.io.Serial;

public class UnauthorizedRoleException extends GenericRestOperationFailedException {

    @Serial
    private static final long serialVersionUID = -6720825971954657312L;
    private static final int CODE = 403;
    private static final HttpStatusCode STATUS_CODE = HttpStatusCode.valueOf(CODE);
    private static final String MESSAGE = "Your role does not have permission to access this resource";

    public HttpStatusCode getStatusCode() {
        return STATUS_CODE;
    }
    public String getMessage() {
        return MESSAGE;
    }

}
