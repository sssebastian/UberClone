package com.araoz.uberclone.retrofit;

import com.araoz.uberclone.modelos.FCMBody;
import com.araoz.uberclone.modelos.FCMResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMapi {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAO4wCy_E:APA91bGK60jvrL9XUFZ7VoanWvkwJpx6OBOdMb6CjbRkVJzvgWU0gx7r3LuBwx1sFsocma3Otl5trzNVqrf05mIX7gRtvYm7PNexA7Fee_uOeQ7SIwa_ALgQHjXbbrC4OWBnVCs1MLld"
    })

    @POST("fcm/send")
    Call<FCMResponse> send(@Body FCMBody body);

}
