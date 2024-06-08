package com.utnans.accountservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "There was an issue processing your request. Please try again later.")
public class GeneralServiceException extends RuntimeException {
}
