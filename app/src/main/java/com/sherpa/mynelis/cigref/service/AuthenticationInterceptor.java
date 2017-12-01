package com.sherpa.mynelis.cigref.service;

import android.content.Intent;

import com.sherpa.mynelis.cigref.api.AccessToken;
import com.sherpa.mynelis.cigref.view.LoginActivity;
import com.sherpa.mynelis.cigref.view.MainActivity;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Alexis Largaiolli on 01/12/2017.
 */

public class AuthenticationInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        AccessToken token = AuthenticationService.getInstance().getmToken();
        if(token != null && token.needRefresh()){
            AuthenticationService.getInstance().refreshToken();
        }

        Response response = chain.proceed(request);

        return response;
    }
}
