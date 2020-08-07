package com.araoz.uberclone.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.araoz.uberclone.providers.AuthProvider;
import com.araoz.uberclone.providers.ClientBookingProvider;

public class CancelReceiver extends BroadcastReceiver {

    private ClientBookingProvider mClientBookingProvider;
    private AuthProvider mAuthProvider;

    @Override
    public void onReceive(Context context, Intent intent) {

        mAuthProvider = new AuthProvider();

        String idClient = intent.getExtras().getString("idClient");
        mClientBookingProvider = new ClientBookingProvider();
        mClientBookingProvider.updateStatus(idClient, "cancel");

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(2);

    }

}
