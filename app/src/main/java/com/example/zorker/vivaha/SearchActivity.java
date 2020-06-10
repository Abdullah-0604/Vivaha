package com.example.zorker.vivaha;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.zorker.vivaha.Account.UserDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SearchRecyclerViewAdapter searchRecyclerViewAdapter;
    private List<UserDetails> mlist = new ArrayList<>();
    DatabaseReference mref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mref = FirebaseDatabase.getInstance().getReference("Users");
        final FirebaseAuth mauth = FirebaseAuth.getInstance();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_search);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        searchRecyclerViewAdapter = new SearchRecyclerViewAdapter(mlist,this);
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Searching...");
        dialog.show();

        mref.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {



                Bundle bundle = new Bundle();
                bundle = getIntent().getExtras();
                String gender = bundle.getString("gender_search");
                String religion = bundle.getString("religion_search");
                String community = bundle.getString("community_search");
                String age_from = bundle.getString("age_from");
                String age_to = bundle.getString("age_to");
                String height_feet = bundle.getString("height_feet");
                String height_inch = bundle.getString("height_inch");
                String uid = mauth.getUid();


                UserDetails userDetails = dataSnapshot.getValue(UserDetails.class);
                searchRecyclerViewAdapter.notifyDataSetChanged();
                int age_from_int = Integer.parseInt(age_from);
                int age_to_int = Integer.parseInt(age_to);
                int height_feet_int = Integer.parseInt(height_feet);
                int height_inch_int = Integer.parseInt(height_inch);
                float inch_calc_input = height_inch_int/10;
                float total_height_input = height_feet_int+inch_calc_input;
                float inch_calc_database = Integer.parseInt(userDetails.getU_height_inch())/10;
                float total_height_database = Integer.parseInt(userDetails.getU_height_feet())+inch_calc_database;

                if (!userDetails.getU_id().equals(uid)&& userDetails.getU_gender().equals(gender)&& userDetails.getU_religion().equals(religion)&& userDetails.getU_community().equals(community)&& (Integer.parseInt(userDetails.getU_age()) >= age_from_int || Integer.parseInt(userDetails.getU_age())>= age_to_int  )&& (Integer.parseInt(userDetails.getU_age()) <= age_to_int ||Integer.parseInt(userDetails.getU_age())<=age_from_int) && total_height_database>=total_height_input)
                {

                    mlist.add(userDetails);
                    dialog.dismiss();

                }
                else
                {
                    dialog.dismiss();
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

        searchRecyclerViewAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(searchRecyclerViewAdapter);

    }
}
