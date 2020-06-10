package com.example.zorker.vivaha.Matches;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zorker.vivaha.Account.QuestionsModalClass;
import com.example.zorker.vivaha.Account.UserDetails;
import com.example.zorker.vivaha.Chat.ChatActivity;
import com.example.zorker.vivaha.Inbox.InviteModalClass;
import com.example.zorker.vivaha.OtherUsersProfile;
import com.example.zorker.vivaha.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchesAdapter extends RecyclerView.Adapter<MatchesAdapter.MatchesViewHolder> {

    private List<UserDetails> uList = new ArrayList<>();
    private Context mContext;
    private DatabaseReference mref_root;
    private FirebaseAuth mauth;
    private String temp_current_status="not_friends";
    //private String frQ1, frQ2, frQ3, frQ4, frQ5;
   // private String urQ1, urQ2, urQ3, urQ4, urQ5;
    //private int count = 0;

    public MatchesAdapter(List<UserDetails> list, Context context) {
        uList = list;
        mContext = context;

    }

    @NonNull
    @Override
    public MatchesAdapter.MatchesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.matches_single_layout,null);
        return new MatchesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MatchesAdapter.MatchesViewHolder matchesViewHolder, int i) {

        mauth = FirebaseAuth.getInstance();
        mref_root = FirebaseDatabase.getInstance().getReference();
       /*
        String frId = qList.get(i).getF_id();
        String current_id = mauth.getCurrentUser().getUid();

        if (current_id.equals(frId))
        {
            urQ1 = qList.get(i).getQ1();
            urQ2 = qList.get(i).getQ2();
            urQ3 = qList.get(i).getQ3();
            urQ4 = qList.get(i).getQ4();
            urQ5 = qList.get(i).getQ5();
        }
        if (!current_id.equals(frId))
        {
            frQ1 = qList.get(i).getQ1();
            frQ2 = qList.get(i).getQ2();
            frQ3 = qList.get(i).getQ3();
            frQ4 = qList.get(i).getQ4();
            frQ5 = qList.get(i).getQ5();
        }

            if (urQ1.equals(frQ1)) {
                count++;
            }
            if (urQ2.equals(frQ2)) {
                count++;
            }
            if (urQ3.equals(frQ3)) {
                count++;
            }
            if (urQ4.equals(frQ4)) {
                count++;
            }
            if (urQ5.equals(frQ5)) {
                count++;
            }
    */
       //------------------ppulating users data--------------------->


        final String user_id = uList.get(i).getU_id();

        //--------------------populating buttons ststus----->

        mref_root.child("Friends").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild(user_id))
                {
                    matchesViewHolder.button_matches_message.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.button_circle_varcolor));
                }
                else
                {
                    matchesViewHolder.button_matches_message.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.button_circle_not_available));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //-------------------all users data population--------------------------------------->


        Picasso.get().load(uList.get(i).getU_profile_picture()).placeholder(R.mipmap.default_profile).into(matchesViewHolder.iv_picture);
        matchesViewHolder.tv_name.setText(uList.get(i).getU_fname()+" "+uList.get(i).getU_lname());
        matchesViewHolder.tv_age.setText(uList.get(i).getU_age()+" "+"years");
        matchesViewHolder.tv_gender.setText(uList.get(i).getU_gender());
        matchesViewHolder.tv_height.setText(uList.get(i).getU_height_feet()+" ft."+""+uList.get(i).getU_height_inch()+" in.");
        matchesViewHolder.tv_religion.setText(uList.get(i).getU_religion());
        matchesViewHolder.tv_community.setText(uList.get(i).getU_community()+",");
        matchesViewHolder.tv_martial_status.setText(uList.get(i).getU_martial());
        matchesViewHolder.tv_lives_in.setText("Local address : "+uList.get(i).getU_local_address());
        matchesViewHolder.tv_education_level.setText("Education level: "+uList.get(i).getU_edu_level());

        //--------------------buttons work ------------------->

        matchesViewHolder.button_matches_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mref_root.child("Friends").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.hasChild(user_id))
                        {
                            matchesViewHolder.button_matches_message.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.button_circle_varcolor));
                            Intent chatIntent = new Intent(mContext,ChatActivity.class);
                            chatIntent.putExtra("friend_user_id",user_id);
                            mContext.startActivity(chatIntent);
                        }

                        else
                        {

                            matchesViewHolder.button_matches_message.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.button_circle_not_available));
                            Toast.makeText(mContext, "You are not friends", Toast.LENGTH_SHORT).show();

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        matchesViewHolder.button_matches_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* mref_root.child("Invite_requests").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                       if (dataSnapshot.hasChild(user_id))
                       {
                        String currentStatus = dataSnapshot.child(user_id).child("current_status").getValue().toString();
                        if (currentStatus.equals("not_friends_sent")) {
                            matchesViewHolder.button_matches_invite.setImageResource(R.drawable.icon_cancel);
                            Map cancelRequest = new HashMap();
                            cancelRequest.put("Invite_requests" + "/" + FirebaseAuth.getInstance().getUid() + "/" + user_id, null);
                            cancelRequest.put("Invite_requests" + "/" + user_id + "/" + FirebaseAuth.getInstance().getUid(), null);

                            mref_root.updateChildren(cancelRequest, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                                    if (databaseError == null) {
                                        Toast.makeText(mContext, "Request cancelled", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                        }

                        } else {

                            Map addingCurrentStatus = new HashMap();
                            addingCurrentStatus.put("Invite_requests" + "/" + FirebaseAuth.getInstance().getUid() + "/" + user_id + "/" + "request_type", "invite_sent");
                            addingCurrentStatus.put("Invite_requests" + "/" + user_id + "/" + FirebaseAuth.getInstance().getUid() + "/" + "request_type", "invite_received");
                            addingCurrentStatus.put("Invite_requests" + "/" + FirebaseAuth.getInstance().getUid() + "/" + user_id + "/" + "current_status", "not_friends_sent");
                            addingCurrentStatus.put("Invite_requests" + "/" + user_id + "/" + FirebaseAuth.getInstance().getUid() + "/" + "current_status", "not_friends_received");
                            addingCurrentStatus.put("Invite_requests" + "/" + FirebaseAuth.getInstance().getUid() + "/" + user_id + "/" + "friend_id", user_id);
                            addingCurrentStatus.put("Invite_requests" + "/" + user_id + "/" + FirebaseAuth.getInstance().getUid() + "/" + "friend_id", FirebaseAuth.getInstance().getUid());
                            mref_root.updateChildren(addingCurrentStatus, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                                    if (databaseError == null) {
                                        Toast.makeText(mContext, "Invite_sent", Toast.LENGTH_SHORT).show();
                                        matchesViewHolder.button_matches_invite.setImageResource(R.drawable.icon_cancel);
                                    }


                                }
                            });
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                */
              Intent profile_intent = new Intent(mContext,OtherUsersProfile.class);
              profile_intent.putExtra("other_user_id",user_id);
              mContext.startActivity(profile_intent);




            }
        });



    }

    @Override
    public int getItemCount() {
        return uList.size();
    }

    public class MatchesViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_picture;
        ImageButton button_matches_invite, button_matches_message;
        TextView tv_name,tv_age,tv_gender,tv_height,tv_religion,tv_community,tv_martial_status,tv_lives_in,tv_education_level;
        public MatchesViewHolder(@NonNull View itemView) {
            super(itemView);


             iv_picture = itemView.findViewById(R.id.iv_matches_user_picture);
             tv_name = itemView.findViewById(R.id.tv_matches_name);
             tv_age = itemView.findViewById(R.id.tv_matches_age);
             tv_gender = itemView.findViewById(R.id.tv_matches_gender);
             tv_height = itemView.findViewById(R.id.tv_matches_height);
             tv_religion = itemView.findViewById(R.id.tv_matches_religion);
             tv_community = itemView.findViewById(R.id.tv_matches_community);
             tv_martial_status = itemView.findViewById(R.id.tv_matches_matrial_status);
             tv_lives_in = itemView.findViewById(R.id.tv_matches_lives_in);
             tv_education_level = itemView.findViewById(R.id.tv_matches_edu_level);
             button_matches_invite = itemView.findViewById(R.id.button_matches_invite);
             button_matches_message = itemView.findViewById(R.id.button_matches_message);


        }
    }
}