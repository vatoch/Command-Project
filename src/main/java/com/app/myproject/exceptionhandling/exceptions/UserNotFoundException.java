package com.app.myproject.exceptionhandling.exceptions;

public class UserNotFoundException extends RuntimeException{



    public String toString() {
        return "User Not Found";
    }
}
