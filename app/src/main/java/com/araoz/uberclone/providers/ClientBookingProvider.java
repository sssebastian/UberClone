package com.araoz.uberclone.providers;

import com.araoz.uberclone.activities.client.ClientBooking;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ClientBookingProvider {

    private DatabaseReference mDatabase;

    public ClientBookingProvider() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("ClientBooking");
    }

    public Task<Void> create(ClientBooking clientBooking) {
        return mDatabase.child(clientBooking.getIdClient()).setValue(clientBooking);
    }

}
