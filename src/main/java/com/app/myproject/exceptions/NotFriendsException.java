package com.app.myproject.exceptions;

public class NotFriendsException extends RuntimeException{
    public String toString() {
        return "User not in friends list";
    }
}
