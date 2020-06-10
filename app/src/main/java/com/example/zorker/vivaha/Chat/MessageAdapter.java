package com.example.zorker.vivaha.Chat;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zorker.vivaha.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Messages> mlist = new ArrayList<>();
    private Context mcontext;
    private DatabaseReference mref_root;
    private FirebaseAuth mauth;

    public MessageAdapter(List<Messages> list,Context context)
    {

        mlist = list;
        mcontext = context;

    }



    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.message_layout,null);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder messageViewHolder, int i) {

        mref_root = FirebaseDatabase.getInstance().getReference();
        mauth = FirebaseAuth.getInstance();

        Messages messages = mlist.get(i);

        String from = messages.getMessage_from();
        String  message = messages.getMessage_body();
        String type = messages.getMessage_type();

        mref_root.child("Users").child(from).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                String image = dataSnapshot.child("u_search_picture").getValue().toString();
                Picasso.get().load(image).placeholder(R.mipmap.default_profile).into(messageViewHolder.iv_friend);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ///------------------type of message-------------->

        if (type.equals("text")) {
            messageViewHolder.tv_message.setText(message);

        }

        //------------------------------------------------>

        //--------message style----------------------------->
       if (from.equals(mauth.getUid()))
       {

           messageViewHolder.tv_message.setBackgroundResource(R.drawable.message_send_style);
           messageViewHolder.iv_friend.setVisibility(View.INVISIBLE);
           messageViewHolder.tv_message.setTextColor(Color.parseColor("#ffffff"));

       }
       else
       {
           messageViewHolder.tv_message.setBackgroundResource(R.drawable.message_receive_style);
           messageViewHolder.iv_friend.setVisibility(View.VISIBLE);
           messageViewHolder.tv_message.setTextColor(Color.parseColor("#000000"));

       }
       //----------------------------------------------------->

    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        TextView tv_message;
        CircleImageView iv_friend;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_message= itemView.findViewById(R.id.message_layout_message);
            iv_friend = itemView.findViewById(R.id.message_layout_image);

        }
    }
}
