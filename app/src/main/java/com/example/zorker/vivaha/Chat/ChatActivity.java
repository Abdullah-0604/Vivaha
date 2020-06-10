package com.example.zorker.vivaha.Chat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zorker.vivaha.GetTimeAgo;
import com.example.zorker.vivaha.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    private android.support.v7.widget.Toolbar mtoolbar;
    private RecyclerView recyclerView_chat_activity;
    private ImageButton imageButton_add,imageButton_send;
    private EditText et_messages;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String friend_id, current_id;

    //----------for message refresh on scroll------>

    private int item_position = 0;
    private String messageKey_top= "";
    private String previous_key = "";
    private int Total_message_loading_page = 10;
    private int current_page = 1;


    //____Adaptermessage--------->

    private MessageAdapter messageAdapter;
    private List<Messages> messagesList = new ArrayList<>();

    private FirebaseAuth mauth;
    private DatabaseReference mRoot_reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        mauth = FirebaseAuth.getInstance();
        mRoot_reference = FirebaseDatabase.getInstance().getReference();
        current_id = mauth.getUid();

        //--------------------------------------------------->

        et_messages = findViewById(R.id.et_enter_message);
        imageButton_send = findViewById(R.id.imageButton_send);
        recyclerView_chat_activity = findViewById(R.id.recyclerview_chat_activity);
        recyclerView_chat_activity.setHasFixedSize(true);
        recyclerView_chat_activity.setLayoutManager(new LinearLayoutManager(this));
        swipeRefreshLayout = findViewById(R.id.swipeToRefresh_layout);
        //recyclerView_chat_activity.setAdapter(messageAdapter);



        //-----------------------initializing objects------->
        mtoolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_chat_activity);
        setSupportActionBar(mtoolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view_toolbar = inflater.inflate(R.layout.custom_toolbar_for_chat,null);
        actionBar.setCustomView(view_toolbar);
        //---------------for custom action bar----------->

        final TextView tv_toobar_name = findViewById(R.id.textView_user_name_chat_toolbar);
        final TextView tvv_last_seen = findViewById(R.id.textView_last_seen_chat_toolbar);


        //-------------------populating last seen------------------>

        friend_id = getIntent().getStringExtra("friend_user_id");

        mRoot_reference.child("Users").child(friend_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("u_fname").getValue().toString()+" "+dataSnapshot.child("u_lname").getValue().toString();
                tv_toobar_name.setText(name);

                String online = dataSnapshot.child("u_online").getValue().toString();

                if (Long.parseLong(online)==1)
                {
                    tvv_last_seen.setText("online");
                }
                else
                {

                    GetTimeAgo getTimeAgo = new GetTimeAgo();
                    long time = Long.parseLong(online);
                    String time_stamp = getTimeAgo.getTimeAgo(time,getApplicationContext());
                    tvv_last_seen.setText(time_stamp);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //------------------------------------------------------------------>

        addChatNodeToDatabase();
        sendMessage();
        loadMessages();
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

              /*  current_page++;
                item_position = 0;

                loadMoreMessages();

*/
            }
        });




    }

    private void loadMoreMessages() {

        DatabaseReference mref_message = mRoot_reference.child("Messages").child(current_id).child(friend_id);
        Query loadMore = mref_message.orderByKey().endAt(messageKey_top).limitToLast(10);

        loadMore.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Messages messages = dataSnapshot.getValue(Messages.class);

                String message_key = dataSnapshot.getKey();
                if (!previous_key.equals(message_key))
                {
                    messagesList.add(item_position++,messages);
                }
                else
                {

                 previous_key = messageKey_top;

                }
                if (item_position==1)
                {
                    messageKey_top = message_key;
                }

                swipeRefreshLayout.setRefreshing(false);
                recyclerView_chat_activity.scrollTo(10,0);
                messageAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void loadMessages() {

        messageAdapter = new MessageAdapter(messagesList,ChatActivity.this);
        DatabaseReference messageRef = mRoot_reference.child("Messages").child(current_id).child(friend_id);
        Query messageQuery = messageRef.limitToLast(current_page*Total_message_loading_page);
        messageRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Messages messages = dataSnapshot.getValue(Messages.class);
                item_position++;
                if (item_position==1)
                {

                    String message_key = dataSnapshot.getKey();
                    messageKey_top = message_key;
                    previous_key = message_key;

                }
                messagesList.add(messages);
                messageAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
                recyclerView_chat_activity.scrollToPosition(messagesList.size()-1);

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
        recyclerView_chat_activity.setAdapter(messageAdapter);




    }

    private void sendMessage() {

        imageButton_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message = et_messages.getText().toString();

                if (!TextUtils.isEmpty(message)) {

                    DatabaseReference push_reference = mRoot_reference.child("Messages").child(current_id).child(friend_id).push();
                    String push_id = push_reference.getKey();
                    Map messageDetails = new HashMap();
                    messageDetails.put("message_body", message);
                    messageDetails.put("message_type", "text");
                    messageDetails.put("seen", false);
                    messageDetails.put("time_stamp",ServerValue.TIMESTAMP);
                    messageDetails.put("message_from", current_id);

                    Map messageNode = new HashMap();
                    messageNode.put("Messages" + "/" + current_id + "/" + friend_id + "/"+push_id+"/", messageDetails);
                    messageNode.put("Messages" + "/" + friend_id + "/" + current_id + "/"+push_id+"/", messageDetails);

                    Map chatNode = new HashMap();
                    chatNode.put("Chat"+"/"+current_id+"/"+friend_id+"message_seen",true);
                    chatNode.put("Chat"+"/"+current_id+"/"+friend_id+"time_stamp",ServerValue.TIMESTAMP);

                    chatNode.put("Chat"+"/"+friend_id+"/"+current_id+"message_seen",false);
                    chatNode.put("Chat"+"/"+friend_id+"/"+current_id+"time_stamp",ServerValue.TIMESTAMP);


                    mRoot_reference.updateChildren(messageNode, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                            if (databaseError== null)
                            {
                                Toast.makeText(ChatActivity.this, "message sent", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                    et_messages.setText("");

                }
            }
        });

    }

    private void addChatNodeToDatabase() {

        mRoot_reference.child("Chat").child(current_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!dataSnapshot.hasChild(friend_id))
                {

                    Map chatAddmap = new HashMap();
                    chatAddmap.put("seen_status",false);
                    chatAddmap.put("time_stamp",ServerValue.TIMESTAMP);

                    Map chatAddNode = new HashMap();
                    chatAddNode.put("Chat"+"/"+current_id+"/"+friend_id+ "/",chatAddmap);
                    chatAddNode.put("Chat"+"/"+friend_id+"/"+current_id+"/",chatAddmap);

                    mRoot_reference.updateChildren(chatAddNode, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                            if (databaseError==null)
                            {
                                Toast.makeText(ChatActivity.this, "chat node created", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(ChatActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            }


                        }
                    });



                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        mRoot_reference.child("Users").child(mauth.getUid()).child("u_online").setValue(1);
    }
}
