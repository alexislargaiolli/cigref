package com.sherpa.mynelis.cigref.api;


import com.sherpa.mynelis.cigref.api.exception.LoginTimeout;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Response;



public class Tools {


    public static AccessToken getToken(String url, String login, String pwd) throws LoginTimeout{
        String urlChecked = url;
        AccessToken t = null;

        if(!url.contains("http") && !url.contains("HTTP")) {

            try {
                NelisInterface nelisInterface = RestClient
                        .getClient("http://"+url, true)
                        .create(NelisInterface.class);
                Call<AccessToken> call = nelisInterface.getToken(NelisInterface.CLIENT_ID,
                        NelisInterface.CLIENT_SECRET,
                        "password",
                        login,
                        pwd);

                Response<AccessToken> resp = call.execute();
                t = resp.body();

                if (resp.code() != 200) {
                    nelisInterface = RestClient
                            .getClient("https://"+url, true)
                            .create(NelisInterface.class);
                    call = nelisInterface.getToken(NelisInterface.CLIENT_ID,
                            NelisInterface.CLIENT_SECRET,
                            "password",
                            login,
                            pwd);

                    resp = call.execute();
                    t = resp.body();

                    if(resp.code() == 200)
                        urlChecked = "https://"+url;

                }
                else urlChecked = "http://"+url;

            } catch(SocketTimeoutException se){
                throw new LoginTimeout();
            } catch (Exception ex) {
                System.out.println("getToken Exception : "+ex.getMessage());
                ex.printStackTrace();
            }
        }
        else {
            try {
                NelisInterface nelisInterface = RestClient
                        .getClient(url, true)
                        .create(NelisInterface.class);
                Call<AccessToken> call = nelisInterface.getToken(NelisInterface.CLIENT_ID,
                        NelisInterface.CLIENT_SECRET,
                        "password",
                        login,
                        pwd);

                Response<AccessToken> resp = call.execute();
                t = resp.body();

                urlChecked = url;
            }
            catch(SocketTimeoutException se){
                throw new LoginTimeout();
            }
            catch (Exception ex) {
                System.out.println("getToken Exception : "+ex.getMessage());
            }
        }

        return t;
    }
}
