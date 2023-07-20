package com.app.myproject.exceptionhandling.exceptions;

public class InsufficientBalanceException extends RuntimeException{
    public String toString() {
        return "Not enough money on balance";
    }
}
