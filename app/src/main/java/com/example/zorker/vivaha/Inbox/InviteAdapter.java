package com.example.zorker.vivaha.Inbox;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.google.firebase.auth.FirebaseAuth;
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
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.support.v4.content.ContextCompat.startActivity;

public class InviteAdapter extends RecyclerView.Adapter<InviteAdapter.InviteViewHolder> {

    private Context mcontext;
    private List<InviteModalClass> inviteList = new ArrayList<>();
    private DatabaseReference mref,mref_root;
    private FirebaseAuth mauth;
    private String current_status;

public InviteAdapter(List<InviteModalClass> list, Context context)
{
    mcontext = context;
    inviteList = list;


}
    @NonNull
    @Override
    public InviteAdapter.InviteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

    View view = LayoutInflater.from(mcontext).inflate(R.layout.invite_single_layout,null);
    return new InviteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final InviteAdapter.InviteViewHolder inviteViewHolder, int i) {

     final String friendId = inviteList.get(i).getFriend_id();
     current_status =inviteList.get(i).getCurrent_status();
     //---------------------------------->

        if (current_status.equals("friends"))
        {
            //inviteViewHolder.button_accept.setBackgroundDrawable(mcontext.getResources().getDrawable(R.drawable.button_registration));
            //inviteViewHolder.button_accept.setText("start conversation");
            inviteViewHolder.button_reject.setText("mark as read");
            inviteViewHolder.button_accept.setEnabled(false);
            inviteViewHolder.button_accept.setVisibility(View.INVISIBLE);
            inviteViewHolder.tv_after_accept.setText("and you are friends now.");
        }


        //------------------------------>


     mref_root = FirebaseDatabase.getInstance().getReference();
     mref = FirebaseDatabase.getInstance().getReference().child("Users").child(friendId);
     mref.addValueEventListener(new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

             UserDetails userDetails = dataSnapshot.getValue(UserDetails.class);
             inviteViewHolder.tv_name.setText(userDetails.getU_fname());
             Picasso.get().load(userDetails.getU_search_picture()).into(inviteViewHolder.iv_image);

         }

         @Override
         public void onCancelled(@NonNull DatabaseError databaseError) {

         }

     });

     inviteViewHolder.button_accept.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {



             if (current_status.equals("not_friends"))
             {
                 Toast.makeText(mcontext, current_status, Toast.LENGTH_SHORT).show();
                 final String date_of_friendship = DateFormat.getDateInstance().format(new Date());

                 Map friends_node = new HashMap();
                 friends_node.put("Friends" + "/" + FirebaseAuth.getInstance().getUid() + "/" + friendId + "/" + "date", date_of_friendship);
                 friends_node.put("Friends" + "/" + friendId + "/" + FirebaseAuth.getInstance().getUid() + "/" + "date", date_of_friendship);
                 friends_node.put("Invite_requests"+"/"+FirebaseAuth.getInstance().getUid()+"/"+friendId+"/"+"current_status","friends");
                 friends_node.put("Invite_requests"+"/"+friendId+"/"+FirebaseAuth.getInstance().getUid()+"/"+"current_status","friends");
                 mref_root.updateChildren(friends_node, new DatabaseReference.CompletionListener() {
                     @Override
                     public void onComplete(@Nullable final DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                         if (databaseError==null)
                         {
                             mref_root.child("Invite_requests").child(FirebaseAuth.getInstance().getUid()).child(friendId).addValueEventListener(new ValueEventListener() {
                                 @Override
                                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                     InviteModalClass inviteModalClass = dataSnapshot.getValue(InviteModalClass.class);
                                     String status = inviteModalClass.getCurrent_status();
                                     current_status = status;
                                     inviteViewHolder.tv_after_accept.setText("  and you are friends now.");
                                     inviteViewHolder.button_accept.setText("start conversation");
                                     inviteViewHolder.button_accept.setBackgroundDrawable(mcontext.getResources().getDrawable(R.drawable.button_registration));
                                     inviteViewHolder.button_reject.setText("mark as read");



                                 }

                                 @Override
                                 public void onCancelled(@NonNull DatabaseError databaseError) {

                                 }
                             });


                         }
                     }
                 });

             }
             //--------------------------------------------->
             if (current_status.equals("friends"))
             {
                 inviteViewHolder.button_accept.setEnabled(false);
                 inviteViewHolder.button_accept.setVisibility(View.INVISIBLE);
                 Intent chat_intent = new Intent(mcontext,ChatActivity.class);
                 chat_intent.putExtra("friend_user_id",friendId);
                 mcontext.startActivity(chat_intent);

             }
             //---------------------------->




         }

     });

     inviteViewHolder.button_reject.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {

             Map delete_invite = new HashMap();
             delete_invite.put("Invite_requests"+"/"+FirebaseAuth.getInstance().getUid()+"/"+friendId,null);
             delete_invite.put("Invite_requests"+"/"+friendId+"/"+FirebaseAuth.getInstance().getUid(),null);

             mref_root.updateChildren(delete_invite, new DatabaseReference.CompletionListener() {
                 @Override
                 public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                     if (databaseError== null)
                     {
                         Toast.makeText(mcontext, "Invite deleted", Toast.LENGTH_SHORT).show();
                         inviteViewHolder.button_reject.setEnabled(false);
                         inviteViewHolder.button_reject.setVisibility(View.INVISIBLE);
                         inviteViewHolder.button_accept.setEnabled(false);
                         inviteViewHolder.button_accept.setVisibility(View.INVISIBLE);
                         inviteViewHolder.tv_after_accept.setText("'s invitation was rejected.");
                     }


                 }
             });

         }
     });





    }

    @Override
    public int getItemCount() {
        return inviteList.size();
    }

    public class InviteViewHolder extends RecyclerView.ViewHolder {
        public InviteViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        TextView tv_name = itemView.findViewById(R.id.tv_name_invite_page);
        TextView tv_after_accept = itemView.findViewById(R.id.tv_after_accepting);
        CircleImageView iv_image = itemView.findViewById(R.id.iv_invite_page);
        Button button_accept = itemView.findViewById(R.id.button_invite_accept);
        Button button_reject = itemView.findViewById(R.id.button_invite_reject);

    }
}
