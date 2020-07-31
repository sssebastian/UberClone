package com.araoz.uberclone.providers;

import com.araoz.uberclone.modelos.FCMBody;
import com.araoz.uberclone.modelos.FCMResponse;
import com.araoz.uberclone.retrofit.IFCMapi;
import com.araoz.uberclone.retrofit.RetrofitClient;

import retrofit2.Call;

public class NotificationProvider {

    private String url = "https://fcm.googleapis.com";

    public NotificationProvider() {
    }

    public Call<FCMResponse> sendNotification(FCMBody body){
        return RetrofitClient.getClientObject(url).create(IFCMapi.class).send(body);
    }

}
