package com.app.myproject.exceptions;

public class InsufficientBalanceException extends RuntimeException{
    public String toString() {
        return "Not enough money on balance";
    }
}
