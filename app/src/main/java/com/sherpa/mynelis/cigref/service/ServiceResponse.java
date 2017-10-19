package com.sherpa.mynelis.cigref.service;

import java.util.ArrayList;

/**
 * Created by Alexis Largaiolli on 19/10/17.
 */
public interface ServiceResponse<T> {

    void onSuccess(T datas);

    void onError(ServiceReponseErrorType error, String errorMessage);


    public enum ServiceReponseErrorType{
        NETWORK
    }

}
