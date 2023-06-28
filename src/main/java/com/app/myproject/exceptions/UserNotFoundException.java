package com.app.myproject.exceptions;

public class UserNotFoundException extends RuntimeException{



    public String toString() {
        return "User Not Found";
    }
}
