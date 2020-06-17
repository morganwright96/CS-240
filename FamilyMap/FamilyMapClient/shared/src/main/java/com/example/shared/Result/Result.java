package com.example.shared.Result;

/**
 * This is a basic response object and will be used by inheritance in each of the response classes
 */
public class Result {

    private String message = null;
    private boolean success = false;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
