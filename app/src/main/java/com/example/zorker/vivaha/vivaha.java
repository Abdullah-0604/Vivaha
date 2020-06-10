package com.example.zorker.vivaha;

import android.app.Application;
import android.app.Service;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

public class vivaha extends Application {

    private FirebaseAuth mauth;
    private DatabaseReference mref_users;

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        mauth = FirebaseAuth.getInstance();
        if (mauth.getCurrentUser()!=null){
        mref_users = FirebaseDatabase.getInstance().getReference("Users").child(mauth.getUid());
        if (mauth.getUid()!= null)
        {
            mref_users.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.hasChild("u_online"))
                    {
                        mref_users.child("u_online").onDisconnect().setValue(ServerValue.TIMESTAMP);

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }}
}
