package com.araoz.uberclone.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.araoz.uberclone.activities.driver.MapDriverBookingActivity;
import com.araoz.uberclone.providers.AuthProvider;
import com.araoz.uberclone.providers.ClientBookingProvider;
import com.araoz.uberclone.providers.GeofireProvider;

public class AcceptReceiver extends BroadcastReceiver {

    private ClientBookingProvider mClientBookingProvider;
    private GeofireProvider mGeofireProvider;
    private AuthProvider mAuthProvider;


    @Override
    public void onReceive(android.content.Context context, Intent intent) {
        mAuthProvider = new AuthProvider();
        /*mGeofireProvider = new GeofireProvider("active_drivers");*/
        /* mGeofireProvider.removeLocation(mAuthProvider.getId());*/

        String idClient = intent.getExtras().getString("idClient");
        mClientBookingProvider = new ClientBookingProvider();
        mClientBookingProvider.updateStatus(idClient, "accept");

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(2);

        Intent intent1 = new Intent(context, MapDriverBookingActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent1.setAction(Intent.ACTION_RUN);
        context.startActivity(intent1);

    }
}
