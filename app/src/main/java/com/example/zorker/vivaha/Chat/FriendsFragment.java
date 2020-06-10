package com.example.zorker.vivaha.Chat;


import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zorker.vivaha.OtherUsersProfile;
import com.example.zorker.vivaha.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment {

    private RecyclerView recyclerView_friends;

    private FirebaseAuth mauth;
    private DatabaseReference mref_users;
    private DatabaseReference mref_friends;
    private String fname_user;
    private String lname_user;



    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_friends, container, false);

        //---------------Referencing database---------------->
        mauth = FirebaseAuth.getInstance();
        String current_id = mauth.getUid();
        mref_users = FirebaseDatabase.getInstance().getReference("Users");
        mref_friends = FirebaseDatabase.getInstance().getReference("Friends").child(current_id);

        mref_friends.keepSynced(true);
        mref_users.keepSynced(true);
        //-------------------------------------------------->

        recyclerView_friends = (RecyclerView) view.findViewById(R.id.recyclerview_friends_fragment);
        recyclerView_friends.setHasFixedSize(true);
        recyclerView_friends.setLayoutManager(new LinearLayoutManager(getContext()));


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        //----------------------Recyclerview adapter setup ----------------->

        FirebaseRecyclerAdapter<FriendsModalClass,FriendsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<FriendsModalClass, FriendsViewHolder>(
                FriendsModalClass.class,
                R.layout.friend_list_layout,
                FriendsViewHolder.class,
                mref_friends

        ) {
            @Override
            protected void populateViewHolder(final FriendsViewHolder viewHolder, FriendsModalClass model, int position) {

                final String friend_id = getRef(position).getKey();

                viewHolder.setDate(model.getDate());

                //--------------------populating friends data ---------------->

                mref_users.child(friend_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        fname_user = dataSnapshot.child("u_fname").getValue().toString();
                        lname_user = dataSnapshot.child("u_lname").getValue().toString();
                        String image = dataSnapshot.child("u_search_picture").getValue().toString();

                        if (dataSnapshot.hasChild("u_online")) {

                            String online_status = dataSnapshot.child("u_online").getValue().toString();

                            viewHolder.setOnlineStatus(Long.parseLong(online_status));

                        }
                        viewHolder.setName(fname_user+" "+lname_user);
                        viewHolder.setImage(image);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                viewHolder.mview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence friend_click_opt[] =new CharSequence[] {"View profile","Send message"};

                        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                        alert.setItems(friend_click_opt, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                           switch (which)
                           {
                               case 0:
                                   Intent viewIntent = new Intent(getActivity(),OtherUsersProfile.class);
                                   viewIntent.putExtra("other_user_id",friend_id);
                                   startActivity(viewIntent);
                                   return;
                               case 1:
                                   Intent chat_intent = new Intent(getActivity(),ChatActivity.class);
                                   chat_intent.putExtra("friend_user_id",friend_id);
                                   startActivity(chat_intent);
                                   return;
                                   default:
                                       return;
                           }
                            }
                        });
                        alert.show();
                    }
                });



            }
            //------------------------------------------------------->

        };

        recyclerView_friends.setAdapter(firebaseRecyclerAdapter);



    }

    public static class FriendsViewHolder extends RecyclerView.ViewHolder {

        View mview;

        public FriendsViewHolder(@NonNull View itemView) {
            super(itemView);
            mview = itemView;
        }

        public void setDate(String date) {

            TextView tv_date_of_friendship = (TextView)mview.findViewById(R.id.rv_tv_friends_fragment_date);
            tv_date_of_friendship.setText("Since "+date);

        }

        public void setName(String name) {

            TextView name_user = (TextView)mview.findViewById(R.id.rv_tv_friends_fragment_name) ;
            name_user.setText(name);
        }

        public void setOnlineStatus(long online) {

            CircleImageView online_status = (CircleImageView) mview.findViewById(R.id.rv_iv_friends_fragment_online);
            if (online==1)
            {
                online_status.setVisibility(View.VISIBLE);
            }
            else
            {
                online_status.setVisibility(View.INVISIBLE);
            }
        }

        public void setImage(String image) {

            CircleImageView circleImageView = (CircleImageView) mview.findViewById(R.id.rv_iv_profile_pic_friends_fragment);
            Picasso.get().load(image).placeholder(R.mipmap.default_profile).into(circleImageView);

        }
    }
}
