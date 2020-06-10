package com.example.zorker.vivaha.Inbox;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zorker.vivaha.Account.UserDetails;
import com.example.zorker.vivaha.Chat.ChatActivity;
import com.example.zorker.vivaha.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class InboxFragment extends Fragment {

    private RecyclerView recyclerView_invite;
    private DatabaseReference mref_root;
    private FirebaseAuth mAuth;
    private String friend_id, user_id,req_type;
    private InviteAdapter inviteAdapter;
    private List<InviteModalClass> inviteList = new ArrayList<>();
    private View view;

    public InboxFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if (view==null) {
            view = inflater.inflate(R.layout.fragment_inbox, container, false);

            //--------------------------------->
            mAuth = FirebaseAuth.getInstance();
            mref_root = FirebaseDatabase.getInstance().getReference();

            user_id = mAuth.getUid();


            //--------------------------------->
            recyclerView_invite = view.findViewById(R.id.recyclerView_invite);
            recyclerView_invite.setHasFixedSize(true);
            recyclerView_invite.setLayoutManager(new LinearLayoutManager(getContext()));

            inviteAdapter = new InviteAdapter(inviteList, getContext());

            mref_root.child("Invite_requests").child(mAuth.getUid()).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    InviteModalClass inviteModalClass = dataSnapshot.getValue(InviteModalClass.class);

                    if (inviteModalClass.getRequest_type().equals("invite_received")) {

                        inviteList.add(inviteModalClass);

                    }


                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            inviteAdapter.notifyDataSetChanged();
            recyclerView_invite.setAdapter(inviteAdapter);


        }



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();


    }
}
