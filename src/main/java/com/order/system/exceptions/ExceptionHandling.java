package com.order.system.exceptions;


import com.order.system.exceptions.models.*;
import com.order.system.models.HttpCustomResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.Objects;

import static com.order.system.constants.ErrorConstants.*;
import static org.springframework.http.HttpStatus.*;


@RestControllerAdvice
public class ExceptionHandling {

    @ExceptionHandler(TokenNotProvidedException.class)
    public ResponseEntity<HttpCustomResponse> tokenNotProvidedException() {
        return createHttpResponse(NOT_ACCEPTABLE, TOKEN_NOT_PROVIDED);
    }

    @ExceptionHandler(TokenIncorrectException.class)
    public ResponseEntity<HttpCustomResponse> tokenIncorrectException() {
        return createHttpResponse(NOT_ACCEPTABLE, INCORRECT_TOKEN);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<HttpCustomResponse> userNotFoundException() {
        return createHttpResponse(NOT_ACCEPTABLE, ACTIONS_COULD_NOT_BE_EXECUTED);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<HttpCustomResponse> userNotFoundException(UserNotFoundException e) {
        return createHttpResponse(NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<HttpCustomResponse> illegalStateException() {
        return createHttpResponse(NOT_IMPLEMENTED, UNEXPECTED_VALUE);
    }


    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<HttpCustomResponse> invalidCredentials(InvalidCredentialsException e) {
        return createHttpResponse(UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(InvalidDataFormatException.class)
    public ResponseEntity<HttpCustomResponse> invalidDataFormat() {
        return createHttpResponse(BAD_REQUEST, INVALID_DATA_FORMAT);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<HttpCustomResponse> accessDeniedException(AccessDeniedException e) {
        return createHttpResponse(FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(MenuNotExistException.class)
    public ResponseEntity<HttpCustomResponse> menuNotExistException() {
        return createHttpResponse(NOT_FOUND, MENU_NOT_EXIST);
    }

    @ExceptionHandler(ItemExistInTheCartException.class)
    public ResponseEntity<HttpCustomResponse> itemExistInTheCartException() {
        return createHttpResponse(CONFLICT, ITEM_EXIST_IN_THE_CART);
    }

    @ExceptionHandler(ItemNotExistInTheCartException.class)
    public ResponseEntity<HttpCustomResponse> itemNotExistInTheCartException() {
        return createHttpResponse(NOT_FOUND, ITEM_NOT_EXIST_IN_THE_CART);
    }

    @ExceptionHandler(ItemNotExitIntoMenuException.class)
    public ResponseEntity<HttpCustomResponse> itemNotExitIntoMenuException() {
        return createHttpResponse(NOT_FOUND, ITEM_NOT_EXIST_IN_THE_MENU);
    }

    @ExceptionHandler(IncorrectAmountToPayException.class)
    public ResponseEntity<HttpCustomResponse> incorrectAmountToPayException() {
        return createHttpResponse(BAD_REQUEST, INCORRECT_AMOUNT_TO_PAY);
    }

    @ExceptionHandler(OrderNotExistException.class)
    public ResponseEntity<HttpCustomResponse> orderNotExistException() {
        return createHttpResponse(NOT_FOUND, ORDER_NOT_EXIST);
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<HttpCustomResponse> cartNotFoundException() {
        return createHttpResponse(NOT_FOUND, CART_NOT_EXIST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<HttpCustomResponse> methodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        HttpMethod supportedMethod = Objects.requireNonNull(exception.getSupportedHttpMethods()).iterator().next();
        return createHttpResponse(METHOD_NOT_ALLOWED, String.format(METHOD_IS_NOT_ALLOWED, supportedMethod));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpCustomResponse> internalServerErrorException() {
        return createHttpResponse(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR_MSG);
    }


    private ResponseEntity<HttpCustomResponse> createHttpResponse(HttpStatus httpStatus, String message) {
        HttpCustomResponse httpCustomResponse = new HttpCustomResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(), message);

        return new ResponseEntity<>(httpCustomResponse, httpStatus);
    }
}
