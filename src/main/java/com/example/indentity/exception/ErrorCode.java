package com.example.indentity.exception;

public enum ErrorCode {
    UNCATEGORIZED(9999, "Uncategorized error"),
    INVALID_KEY(1001,"Invalid message key"),
    USER_EXISTS(1002, "User existed"),
    USER_NOTEXISTS(1005, "User not exists"),

    USERNAME_INVALID(1003, "Username  must be at least 3 characters"),
    PASSWORD_INVALID(1004, "Password must be at least 8 characters"),
    UNAUTHENTICATED(1006, "User is un authenticated");

    private int code;
    private String message;
    
    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
