package com.sherpa.mynelis.cigref.utils;

/**
 * Created by Alexis Largaiolli on 21/10/17.
 */
public class APIError {

    private int statusCode;
    private String message;

    public APIError(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int status() {
        return statusCode;
    }

    public String message() {
        return message;
    }
}