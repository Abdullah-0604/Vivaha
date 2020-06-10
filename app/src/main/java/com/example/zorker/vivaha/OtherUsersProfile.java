package com.example.zorker.vivaha;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zorker.vivaha.Account.UserDetails;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.zorker.vivaha.R.drawable.button_green;

public class OtherUsersProfile extends AppCompatActivity {

    private ImageView other_user_profile_picture;
    private TextView other_user_name,other_user_age,other_user_gender,other_user_height,other_user_dob,other_user_martial_status,other_user_language,other_user_religion;
    private TextView other_user_state,other_user_district,other_user_local_address,other_user_community,other_user_resisdence_status;
    private TextView other_user_edu_level,other_user_inst_name,other_user_edu_sector,other_user_job,other_user_post,other_user_org_name;
    private TextView other_user_total_family,other_user_total_brothers,other_user_total_sisters,other_user_f_occupation,other_user_m_occupation,other_user_f_income;
    private Button button_request,button_decline_request;

    private DatabaseReference mref_root;
    private DatabaseReference mref_user_database;
    private DatabaseReference mref_requests_database;
    private DatabaseReference mref_friends_database;
    private String current_status ;
    private String other_user_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_users_profile);
        current_status = "not_invited_status";
        other_user_id = getIntent().getStringExtra("other_user_id");
        mref_root = FirebaseDatabase.getInstance().getReference();
        mref_user_database = FirebaseDatabase.getInstance().getReference().child("Users").child(other_user_id);
        mref_requests_database = FirebaseDatabase.getInstance().getReference().child("Invite_requests");
        mref_friends_database = FirebaseDatabase.getInstance().getReference().child("Friends");
    //--------------------------------------------------------------------------------------------->

        other_user_profile_picture = (ImageView) findViewById(R.id.iv_other_user_profile_picture);
        other_user_name = (TextView) findViewById(R.id.tv_other_user_name);
        other_user_age = findViewById(R.id.tv_other_user_age);
        other_user_gender = findViewById(R.id.tv_other_user_gender);
        other_user_height = findViewById(R.id.tv_other_user_height);
        other_user_dob = findViewById(R.id.tv_other_user_dob);
        other_user_martial_status = findViewById(R.id.tv_other_user_martial_status);
        other_user_language = findViewById(R.id.tv_other_user_language);
        other_user_religion = findViewById(R.id.tv_other_user_religion);

        other_user_state = findViewById(R.id.tv_other_user_state);
        other_user_district = findViewById(R.id.tv_other_user_district);
        other_user_local_address = findViewById(R.id.tv_other_user_local_address);
        other_user_community = findViewById(R.id.tv_other_user_community);
        other_user_resisdence_status = findViewById(R.id.tv_other_user_residence_status);

        other_user_edu_level = findViewById(R.id.tv_other_user_edu_level);
        other_user_inst_name = findViewById(R.id.tv_other_user_inst_name);
        other_user_edu_sector = findViewById(R.id.tv_other_user_edu_sector);
        other_user_job = findViewById(R.id.tv_other_user_job);
        other_user_post = findViewById(R.id.tv_other_user_working_post);
        other_user_org_name = findViewById(R.id.tv_other_user_org_name);

        other_user_total_family = findViewById(R.id.tv_other_user_total_family);
        other_user_total_brothers = findViewById(R.id.tv_other_user_total_brothers);
        other_user_total_sisters = findViewById(R.id.tv_other_user_total_sisters);
        other_user_f_occupation = findViewById(R.id.tv_other_user_father_occupation);
        other_user_m_occupation = findViewById(R.id.tv_other_user_mother_occupation);
        other_user_f_income = findViewById(R.id.tv_other_user_fincome);

        button_request = (Button) findViewById(R.id.button_other_user_request);
        button_decline_request = (Button) findViewById(R.id.button_other_user_cancel_request);

        //------------------------------------------------------------------------------------------>

        button_decline_request.setVisibility(View.INVISIBLE);
        button_decline_request.setEnabled(false);

        mref_root.keepSynced(true);

        populatingDataFromDatabase();
        button_click();
        button_decline_click();







    }

    private void button_decline_click() {

        button_decline_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             Map declineRequest = new HashMap();
             declineRequest.put("Invite_requests"+"/"+FirebaseAuth.getInstance().getUid()+"/"+other_user_id,null);
             declineRequest.put("Invite_requests"+"/"+other_user_id+"/"+FirebaseAuth.getInstance().getUid(),null);

             mref_root.updateChildren(declineRequest, new DatabaseReference.CompletionListener() {
                 @Override
                 public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                     if (databaseError == null)
                     {
                         button_decline_request.setEnabled(false);
                         button_decline_request.setVisibility(View.INVISIBLE);
                         current_status="not_invited_status";
                         button_request.setText("invite");
                         button_request.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_registration));
                     }
                     else
                     {
                         String error = databaseError.getMessage();
                         Toast.makeText(OtherUsersProfile.this,error,Toast.LENGTH_SHORT).show();
                     }
                 }
             });



            }
        });
    }


    private void button_click() {
        button_request.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                button_request.setEnabled(false);

                if (current_status=="not_invited_status") {
                    Map requestReference = new HashMap();
                    requestReference.put("Invite_requests"+"/"+FirebaseAuth.getInstance().getUid()+"/"+other_user_id+"/"+"request_type","invite_sent");
                    requestReference.put("Invite_requests"+"/"+other_user_id+"/"+FirebaseAuth.getInstance().getUid()+"/"+"request_type","invite_received");
                    requestReference.put("Invite_requests"+"/"+FirebaseAuth.getInstance().getUid()+"/"+other_user_id+"/"+"current_status","not_friends");
                    requestReference.put("Invite_requests"+"/"+other_user_id+"/"+FirebaseAuth.getInstance().getUid()+"/"+"current_status","not_friends");
                    requestReference.put("Invite_requests"+"/"+FirebaseAuth.getInstance().getUid()+"/"+other_user_id+"/"+"friend_id",other_user_id);
                    requestReference.put("Invite_requests"+"/"+other_user_id+"/"+FirebaseAuth.getInstance().getUid()+"/"+"friend_id",FirebaseAuth.getInstance().getUid());

                    mref_root.updateChildren(requestReference, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                            if (databaseError != null)
                            {
                                Toast.makeText(OtherUsersProfile.this, "Invitation failed", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                current_status="invite_sent_status";
                                button_request.setText("Cancel request");



                            }
                            button_request.setEnabled(true);

                        }
                    });
                }

                //-----------------------------------invite_sent----------------->
                if (current_status=="invite_sent_status")
                {

                    Map requestReferenceTwo = new HashMap();
                    requestReferenceTwo.put("Invite_requests"+"/"+FirebaseAuth.getInstance().getUid()+"/"+other_user_id,null);
                    requestReferenceTwo.put("Invite_requests"+"/"+other_user_id+"/"+FirebaseAuth.getInstance().getUid(),null);

                    mref_root.updateChildren(requestReferenceTwo, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                            if (databaseError!=null)
                            {
                                String error = databaseError.getMessage();
                                Toast.makeText(OtherUsersProfile.this, error, Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                button_request.setText("invite");
                                current_status="not_invited_status";
                                button_decline_request.setVisibility(View.INVISIBLE);
                                button_decline_request.setEnabled(false);

                            }
                            button_request.setEnabled(true);

                        }
                    });


                }


                //-------------------------------------------invite_received---------->

                if (current_status=="invite_received_status")
                {


                    final String date_of_friendship = DateFormat.getDateInstance().format(new Date());
                    Map requestReferenceThree = new HashMap();
                    requestReferenceThree.put("Friends"+"/"+FirebaseAuth.getInstance().getUid()+"/"+other_user_id+"/"+"date",date_of_friendship);
                    requestReferenceThree.put("Friends"+"/"+other_user_id+"/"+FirebaseAuth.getInstance().getUid()+"/"+"date",date_of_friendship);
                    requestReferenceThree.put("Invite_requests"+"/"+FirebaseAuth.getInstance().getUid()+"/"+other_user_id,null);
                    requestReferenceThree.put("Invite_requests"+"/"+other_user_id+"/"+FirebaseAuth.getInstance().getUid(),null);

                    mref_root.updateChildren(requestReferenceThree, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                            if (databaseError==null)
                            {
                                current_status="friends_status";
                                button_request.setText("unfriend account");
                                button_decline_request.setVisibility(View.INVISIBLE);
                                button_decline_request.setEnabled(false);

                            }
                            else
                            {
                                String error = databaseError.getMessage();
                                Toast.makeText(OtherUsersProfile.this,error,Toast.LENGTH_SHORT).show();
                            }
                            button_request.setEnabled(true);
                            button_request.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_registration));

                        }
                    });
                }

                //------------------------------unfriend_account-------------------->

                if (current_status=="friends_status")
                {
                    Map requestReferenceFour = new HashMap();
                    requestReferenceFour.put("Friends"+"/"+FirebaseAuth.getInstance().getUid()+"/"+other_user_id,null);
                    requestReferenceFour.put("Friends"+"/"+other_user_id+"/"+FirebaseAuth.getInstance().getUid(),null);

                    mref_root.updateChildren(requestReferenceFour, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                            if (databaseError==null)
                            {
                                current_status="not_invited_status";
                                button_request.setText("invite");
                                button_decline_request.setVisibility(View.INVISIBLE);
                                button_decline_request.setEnabled(false);

                            }
                            else
                            {
                                String error = databaseError.getMessage();
                                Toast.makeText(OtherUsersProfile.this, error, Toast.LENGTH_SHORT).show();
                            }

                            button_request.setEnabled(true);

                        }
                    });
                }


            }
        });

    }

    private void populatingDataFromDatabase() {

        mref_user_database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                UserDetails userDetails = dataSnapshot.getValue(UserDetails.class);

                other_user_name.setText(dataSnapshot.child("u_fname").getValue().toString()+" "+dataSnapshot.child("u_lname").getValue().toString());
                String image_id = dataSnapshot.child("u_profile_picture").getValue().toString();
                Picasso.get().load(image_id).placeholder(R.mipmap.default_profile).into(other_user_profile_picture);
                other_user_age.setText(userDetails.getU_age()+"years ,");
                other_user_gender.setText(userDetails.getU_gender());
                other_user_height.setText(userDetails.getU_height_feet()+" ft."+userDetails.getU_height_inch()+" in.");
                other_user_dob.setText("Born in "+userDetails.getU_dob());
                other_user_martial_status.setText(userDetails.getU_martial());
                other_user_language.setText("Speaks "+userDetails.getU_language()+" ,");
                other_user_religion.setText(userDetails.getU_religion());

                other_user_state.setText("State: "+userDetails.getU_state());
                other_user_district.setText("District: "+userDetails.getU_district());
                other_user_local_address.setText("Current address: "+userDetails.getU_local_address());
                other_user_community.setText(userDetails.getU_community());
                other_user_resisdence_status.setText(userDetails.getU_residence());

                other_user_edu_level.setText("Education level: "+userDetails.getU_edu_level());
                other_user_inst_name.setText("Institution name: "+userDetails.getU_inst_name());
                other_user_edu_sector.setText("Education sector: "+userDetails.getU_edu_sector());
                other_user_job.setText(userDetails.getU_job()+" job");
                other_user_post.setText(userDetails.getU_work_post()+" at ");
                other_user_org_name.setText(userDetails.getU_org_name());

                other_user_total_family.setText(userDetails.getU_total_family()+" family members");
                other_user_total_brothers.setText(userDetails.getU_total_brothers()+" brother(s) , ");
                other_user_total_sisters.setText(userDetails.getU_total_sisters()+" sister(s)");
                other_user_f_occupation.setText("Father's occupaton: "+userDetails.getU_focupation());
                other_user_m_occupation.setText("Mother's occupation: "+userDetails.getU_moccupation());
                other_user_f_income.setText("Family income(annually): "+userDetails.getU_fincome()+" (NRS)");



                requestSyncWithDatabase();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //-------------------------
    }

    private void requestSyncWithDatabase() {

        mref_requests_database.child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(other_user_id))
                {

                    String req_type = dataSnapshot.child(other_user_id).child("request_type").getValue().toString();

                    if (req_type.equals("invite_sent"))
                    {
                        button_request.setText("Cancel request");
                        current_status = "invite_sent_status";
                        button_decline_request.setVisibility(View.INVISIBLE);
                        button_decline_request.setEnabled(false);

                    }
                    else if (req_type.equals("invite_received"))
                    {
                        button_decline_request.setVisibility(View.VISIBLE);
                        button_decline_request.setEnabled(true);

                        button_request.setText("Accept request");
                        current_status = "invite_received_status";
                        button_request.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_green));


                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mref_friends_database.child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.hasChild(other_user_id))
                {
                    button_request.setEnabled(true);
                    button_request.setText("unfriend account");
                    button_request.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_registration));
                    current_status="friends_status";
                    button_decline_request.setEnabled(false);
                    button_decline_request.setVisibility(View.INVISIBLE);

                }
                else
                {

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }



}














