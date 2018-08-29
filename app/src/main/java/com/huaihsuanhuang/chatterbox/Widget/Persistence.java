package com.huaihsuanhuang.chatterbox.Widget;

import android.app.Application;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Persistence extends Application {

    private FirebaseAuth mAuth;
    private DatabaseReference reference;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {


            reference = FirebaseDatabase.getInstance().
                    getReference().child("Users").child(mAuth.getCurrentUser().getUid());

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null) {
                        reference.child("online").onDisconnect().setValue(String.valueOf(System.currentTimeMillis()));

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
