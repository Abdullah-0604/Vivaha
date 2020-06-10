package com.example.zorker.vivaha.Chat;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zorker.vivaha.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversationFragment extends Fragment {

    private FirebaseAuth mauth;
    private DatabaseReference mref_chat;
    private DatabaseReference mref_user;
    private DatabaseReference mref_message;
    private RecyclerView recyclerView_conversation;


    public ConversationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mainView = inflater.inflate(R.layout.fragment_conversation, container, false);


        mauth = FirebaseAuth.getInstance();
        String id = mauth.getUid();
        mref_chat = FirebaseDatabase.getInstance().getReference().child("Chat").child(id);
        mref_user = FirebaseDatabase.getInstance().getReference().child("Users");
        mref_message = FirebaseDatabase.getInstance().getReference().child("Messages").child(id);
        recyclerView_conversation= mainView.findViewById(R.id.recyclerview_conversation_fragment);
        recyclerView_conversation.setHasFixedSize(true);
        recyclerView_conversation.setLayoutManager(new LinearLayoutManager(getContext()));



        return mainView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Query conversationQuery = mref_chat.orderByChild("time_stamp");
        FirebaseRecyclerAdapter<ConversationModalClass,ConversationViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ConversationModalClass, ConversationViewHolder>(
               ConversationModalClass.class,
                R.layout.conversation_single_layout,
                ConversationViewHolder.class,
                conversationQuery

        ) {
            @Override
            protected void populateViewHolder(final ConversationViewHolder viewHolder, ConversationModalClass model, int position) {

                final String friend_id = getRef(position).getKey();

                Query messageQuery = mref_message.child(friend_id).limitToLast(1);
                messageQuery.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        String messageBody = dataSnapshot.child("message_body").getValue().toString();
                        viewHolder.setMessage(messageBody);

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


                mref_user.child(friend_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String name = dataSnapshot.child("u_fname").getValue().toString()+" "+dataSnapshot.child("u_lname").getValue().toString();
                        String image = dataSnapshot.child("u_search_picture").getValue().toString();
                        if (dataSnapshot.hasChild("u_online")) {
                            String online = dataSnapshot.child("u_online").getValue().toString();
                            viewHolder.setOnline(Long.parseLong(online));
                        }

                        viewHolder.setName(name);
                        viewHolder.setImage(image);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                viewHolder.mview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent chat_intent = new Intent(getActivity(),ChatActivity.class);
                        chat_intent.putExtra("friend_user_id",friend_id);
                        startActivity(chat_intent);


                    }
                });



            }
        };
        recyclerView_conversation.setAdapter(firebaseRecyclerAdapter);


    }

    public static class ConversationViewHolder extends RecyclerView.ViewHolder {

        View mview;

        TextView tv_name , tv_message;
        CircleImageView iv_image,iv_online;

        public ConversationViewHolder(@NonNull View itemView) {
            super(itemView);
            mview = itemView;
        }

        public void setName(String name) {

            tv_name = mview.findViewById(R.id.tv_conv_page_name);
            tv_name.setText(name);

        }

        public void setImage(String image) {

            iv_image = mview.findViewById(R.id.iv_conv_page);
            Picasso.get().load(image).placeholder(R.mipmap.default_profile).into(iv_image);

        }

        public void setOnline(long online) {

            iv_online = mview.findViewById(R.id.iv_conv_page_online);
            if (online==1)
            {
                iv_online.setVisibility(View.VISIBLE);
            }
        }


        public void setMessage(String messageBody) {

            tv_message = mview.findViewById(R.id.tv_conv_page_message);
            tv_message.setText(messageBody);
        }
    }
}
