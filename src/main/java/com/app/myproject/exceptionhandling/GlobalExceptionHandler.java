package com.app.myproject.exceptionhandling;


import com.app.myproject.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionBody> handleUserNotFoundException(UserNotFoundException userNotFoundException, WebRequest webRequest) {
        ExceptionBody body = ExceptionBody.builder().message("User not found").build();
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);

    }
    @ExceptionHandler(NotFriendsException.class)
    public ResponseEntity<ExceptionBody> handleNotFriendsException(NotFriendsException notFriendsException,WebRequest webRequest) {
        ExceptionBody body = ExceptionBody.builder().message("You are not friends with this user").build();
        return new ResponseEntity<>(body,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ExceptionBody> handInsufficientBalanceException(InsufficientBalanceException insufficientBalanceException,WebRequest webRequest) {
        ExceptionBody body = ExceptionBody.builder().message("Not sufficient balance, transaction delcined").build();
        return new ResponseEntity<>(body,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(CommandNotFoundException.class)
    public ResponseEntity<ExceptionBody> handleCommandNotFoundException(CommandNotFoundException commandNotFoundException,WebRequest request) {
        ExceptionBody body = ExceptionBody.builder().message("Command not found").build();
        return new ResponseEntity<>(body,HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(CommandAlreadyBoughtException.class)
    public ResponseEntity<ExceptionBody> handleCommandAlreadyBoughtException(CommandAlreadyBoughtException commandAlreadyBoughtException,WebRequest webRequest) {
        ExceptionBody body = ExceptionBody.builder().message("Command is already bought").build();
        return new ResponseEntity<>(body,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(CommandNotOwnedException.class)
    public ResponseEntity<ExceptionBody> handleCommandNotOwnedException(CommandNotOwnedException commandNotOwnedException,WebRequest request) {
        ExceptionBody body = ExceptionBody.builder().message("You haven't bought this command").build();
        return new ResponseEntity<>(body,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionBody> handleRuntimeException(RuntimeException runtimeException,WebRequest webRequest) {
        ExceptionBody body= ExceptionBody.builder().message("Internal server error").build();
        return new ResponseEntity<>(body,HttpStatus.BAD_REQUEST);
    }







}
