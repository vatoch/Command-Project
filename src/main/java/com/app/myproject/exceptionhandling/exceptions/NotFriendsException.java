package com.app.myproject.exceptionhandling.exceptions;

public class NotFriendsException extends RuntimeException{
    public String toString() {
        return "User not in friends list";
    }
}
