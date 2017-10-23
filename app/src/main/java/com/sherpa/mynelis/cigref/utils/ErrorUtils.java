package com.sherpa.mynelis.cigref.utils;

import java.io.IOException;

import retrofit2.Response;

/**
 * Created by Alexis Largaiolli on 21/10/17.
 */
public class ErrorUtils {

    public static APIError parseError(Response<?> response) {
        String message = "";
        try {
            message = response.errorBody().string();
        } catch (IOException e) {

        }
        APIError error = new APIError(response.code(), message);
        return error;
    }
}