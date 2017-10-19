package com.sherpa.mynelis.cigref.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.sherpa.mynelis.cigref.api.AccessToken;
import com.sherpa.mynelis.cigref.api.NelisInterface;
import com.sherpa.mynelis.cigref.api.ServiceGenerator;
import com.sherpa.mynelis.cigref.api.Tools;
import com.sherpa.mynelis.cigref.api.exception.LoginTimeout;
import com.sherpa.mynelis.cigref.model.Credentials;
import com.yakivmospan.scytale.Crypto;
import com.yakivmospan.scytale.Options;
import com.yakivmospan.scytale.Store;

import javax.crypto.SecretKey;

/**
 * Created by Alexis Largaiolli on 19/10/17.
 */
public class AuthenticationService {

    private static final String SIGN_KEY = "auth_service";
    private static final String PREFERENCE_KEY = "auth_service_preferences";
    private static final String PREFERENCE_USERNAME_KEY = "ovASzcPyS3";
    private static final String PREFERENCE_PASSWORD_KEY = "wGDqgbqxR3";

    private static AccessToken mToken;

    public static boolean login(String username, String password) throws LoginTimeout {
        mToken = Tools.getToken(NelisInterface.API_ROOT, username, password);
//        NelisInterface client = ServiceGenerator.createService(NelisInterface.class);
//        client.getToken(NelisInterface.CLIENT_ID, NelisInterface.CLIENT_SECRET, "password", username, client);

        return mToken != null;
    }

    public static void logout(Context context) {
        mToken = null;
        removeStoredCredentials(context);
    }

    public static void storeCredential(Context context, String username, String password) {
        SecretKey key = getSecretKey(context);
        Crypto crypto = new Crypto(Options.TRANSFORMATION_SYMMETRIC);

        // Encrypt credentials
        String encryptedUsername = crypto.encrypt(username, key);
        String encryptedPassword = crypto.encrypt(password, key);

        // Store credentials to shared preferences
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PREFERENCE_USERNAME_KEY, encryptedUsername);
        editor.putString(PREFERENCE_PASSWORD_KEY, encryptedPassword);
        editor.commit();
    }

    public static Credentials loadCredential(Context context) {
        // Load encrypted credentials
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE);
        String encryptedUsername = sharedPref.getString(PREFERENCE_USERNAME_KEY, null);
        String encryptedPassword = sharedPref.getString(PREFERENCE_PASSWORD_KEY, null);
        if (encryptedUsername == null || encryptedPassword == null) {
            return null;
        }

        // Decrypt credentials
        SecretKey key = getSecretKey(context);
        Crypto crypto = new Crypto(Options.TRANSFORMATION_SYMMETRIC);
        String username = crypto.decrypt(encryptedUsername, key);
        String password = crypto.decrypt(encryptedPassword, key);

        if (username == null || password == null) {
            return null;
        }

        return new Credentials(username, password);
    }

    public static void removeStoredCredentials(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(PREFERENCE_USERNAME_KEY);
        editor.remove(PREFERENCE_PASSWORD_KEY);
        editor.commit();
    }

    private static SecretKey getSecretKey(Context context) {
        Store store = new Store(context);
        SecretKey key = null;
        if (!store.hasKey(SIGN_KEY)) {
            return store.generateSymmetricKey(SIGN_KEY, null);
        }
        return store.getSymmetricKey(SIGN_KEY, null);
    }

    public static AccessToken getmToken() {
        return mToken;
    }

}