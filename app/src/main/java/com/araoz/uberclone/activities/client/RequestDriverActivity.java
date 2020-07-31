package com.araoz.uberclone.activities.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.araoz.uberclone.R;

import com.araoz.uberclone.providers.GeofireProvider;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.araoz.uberclone.modelos.*;
import com.araoz.uberclone.providers.TokenProvider;
import com.araoz.uberclone.providers.NotificationProvider;
import com.araoz.uberclone.modelos.FCMBody;
import com.araoz.uberclone.modelos.FCMResponse;
import com.araoz.uberclone.retrofit.IFCMapi;
import com.araoz.uberclone.retrofit.RetrofitClient;
import com.araoz.uberclone.channel.NotificationHelper;
import com.araoz.uberclone.services.MyFirebaseMessagingClient;

import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestDriverActivity extends AppCompatActivity {

    private LottieAnimationView mAnimation;
    private TextView mTextViewLookingFor;
    private Button mButtonCancelRequest;

    private GeofireProvider mGeofireProvider;

    private double mExtraOriginLat;
    private double mExtraOriginLng;
    private LatLng mOriginLatLng;

    private double mRadius = 0.1;
    private boolean mDriverFound = false;
    private String  mIdDriverFound = "";

    private LatLng mDriverFoundLatLng;
    private NotificationProvider mNotificationProvider;
    private TokenProvider mTokenProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_driver);

        mAnimation = findViewById(R.id.animation);

        mTextViewLookingFor = findViewById(R.id.textViewLookingFor);
        mButtonCancelRequest = findViewById(R.id.btnCancelRequest);
        mAnimation.playAnimation();

        mGeofireProvider = new GeofireProvider();
        mTokenProvider = new TokenProvider();
        mNotificationProvider = new NotificationProvider();

        mExtraOriginLat = getIntent().getDoubleExtra("origin_lat", 0);
        mExtraOriginLng = getIntent().getDoubleExtra("origin_lng", 0);

        mOriginLatLng = new LatLng(mExtraOriginLat, mExtraOriginLng);

        getClosestDriver();

    }
    private void getClosestDriver(){
        mGeofireProvider.getActiveDrivers(mOriginLatLng, mRadius).addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {

                if (!mDriverFound) {
                    mDriverFound = true;
                    mIdDriverFound = key;
                    mDriverFoundLatLng = new LatLng(location.latitude, location.longitude);
                    mTextViewLookingFor.setText("CONDUCTOR ENCONTRADO\nESPERANDO RESPUESTA");
                    sendNotification();
                    Log.d("DRIVER", "ID: " + mIdDriverFound);
                }

            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {
                // INGRESA CUANDO TERMINA LA BUSQUEDA DEL CONDUCTOR EN UN RADIO DE 0.1 KM
                if (!mDriverFound) {
                    mRadius = mRadius + 0.1f;
                    // NO ENCONTRO NINGUN CONDUCTOR
                    if (mRadius > 5) {
                        mTextViewLookingFor.setText("NO SE ENCONTRO UN CONDUCTOR");
                        Toast.makeText(RequestDriverActivity.this, "NO SE ENCONTRO UN CONDUCTOR", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else {
                        getClosestDriver();
                    }
                }
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });

    }


    private void sendNotification() {
        mTokenProvider.getToken(mIdDriverFound).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String token = dataSnapshot.child("token").getValue().toString();
                Map<String, String> map = new HashMap<>();
                map.put("title", "SOLICITUD DE SERVICIO ");
                map.put("body", "Un cliente esta solicitando un servicio");
                FCMBody fcmBody = new FCMBody(token, "high", map);
                mNotificationProvider.sendNotification(fcmBody).enqueue(new Callback<FCMResponse>() {
                    @Override
                    public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                        if (response.body() != null) {
                            if (response.body().getSuccess() == 1) {
                                Toast.makeText(RequestDriverActivity.this, "La notificacion se ha enviado correctamente", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RequestDriverActivity.this, "No se pudo enviar la notificacion", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<FCMResponse> call, Throwable t) {

                        Log.d("Error", "Error " + t.getMessage());
                    }
            });
        }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

}