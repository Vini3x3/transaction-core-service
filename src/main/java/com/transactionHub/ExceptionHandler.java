package com.transactionHub;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class ExceptionHandler implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception e) {
        if (e instanceof NotFoundException) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (e instanceof IllegalArgumentException illegalArgumentException) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(illegalArgumentException.getMessage())
                    .build();
        }
        return null;
    }
}
