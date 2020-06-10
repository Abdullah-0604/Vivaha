package com.example.zorker.vivaha.Account;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.zorker.vivaha.FrameHolderAccount;
import com.example.zorker.vivaha.MainActivity;
import com.example.zorker.vivaha.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterUserFamily extends Fragment {

    private Button button_reguser_family;
    EditText et_total_fmembers, et_total_brothers,et_total_sisters,et_fincome;
    Spinner spin_father_occupation, spin_mother_occupation;
    String email, pass,f_name,l_name, profile_for , gender, dob,age, religion, language,height_feet,height_inch,country;
    String state, district , local_address , community , add_community_details, residence , martial_status;
    String edu_level ,inst_name, edu_sector , job , org_name, work_post;
    String total_family , total_brothers , total_sisters ,total_fincome, f_occupation, m_occupation;
    FirebaseAuth mauth;
    DatabaseReference mref;
    private ProgressDialog mpgogress;


    public RegisterUserFamily() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_user_family, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mauth = FirebaseAuth.getInstance();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mauth = FirebaseAuth.getInstance();
        mpgogress = new ProgressDialog(getActivity());


       button_reguser_family = (Button) view.findViewById(R.id.button_reguser_family);
       et_total_fmembers = (EditText) view.findViewById(R.id.et_reguser_familyno);
       et_total_brothers = (EditText) view.findViewById(R.id.et_reguser_brotherno);
       et_total_sisters  = (EditText) view.findViewById(R.id.et_reguser_sisterno);
       et_fincome = (EditText) view.findViewById(R.id.et_reguser_family_income);
       spin_father_occupation = (Spinner) view.findViewById(R.id.spinner_reguser_father_occupation);
       spin_mother_occupation = (Spinner) view.findViewById(R.id.spinner_reguser_mother_occupation);

       total_family = et_total_fmembers.getText().toString();
       total_brothers = et_total_brothers.getText().toString();
       total_sisters = et_total_sisters.getText().toString();
       total_fincome = et_fincome.getText().toString();

       spinner_father_occupation();
       spinner_mother_occupation();
       getDAtaFromRegUserEdu();


        button_reguser_family.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String totalFmem = et_total_fmembers.getText().toString();
                final String sisters = et_total_sisters.getText().toString();
                final String brothers = et_total_brothers.getText().toString();
                final String fincome = et_fincome.getText().toString();
                if (TextUtils.isEmpty(totalFmem))
                {
                    Toast.makeText(getActivity(), "please specify your total family members", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(brothers)|| TextUtils.isEmpty(sisters) )
                {
                    Toast.makeText(getActivity(), "please specify your total brothers and sisters correctly", Toast.LENGTH_SHORT).show();
                }
                else {

                    mpgogress.setTitle("Signing Up");
                    mpgogress.setMessage("It may take a little while");
                    mpgogress.setCanceledOnTouchOutside(false);
                    mpgogress.show();

                    mauth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                mpgogress.dismiss();
                                String usid = mauth.getUid();
                                mref = FirebaseDatabase.getInstance().getReference("Users").child(usid);

                                //UserDetails userDetails = new UserDetails(usid, email, pass, f_name, l_name, profile_for, gender, dob,religion, language, country, state, district, local_address, community, add_community_details, residence, martial_status, edu_level, inst_name, edu_sector, job, org_name, work_post, totalFmem, brothers, sisters, f_occupation, m_occupation, fincome);
                               // mref.setValue(userDetails);
                                UserDetails details = new UserDetails();
                                details.setU_id(usid);
                                details.setU_email(email);
                                details.setU_password(pass);
                                details.setU_fname(f_name);
                                details.setU_lname(l_name);
                                details.setU_profile_for(profile_for);
                                details.setU_gender(gender);
                                details.setU_dob(dob);
                                details.setU_age(age);
                                details.setU_religion(religion);
                                details.setU_language(language);
                                details.setU_height_feet(height_feet);
                                details.setU_height_inch(height_inch);
                                details.setU_country(country);
                                details.setU_state(state);
                                details.setU_district(district);
                                details.setU_local_address(local_address);
                                details.setU_community(community);
                                details.setU_add_community_details(add_community_details);
                                details.setU_residence(residence);
                                details.setU_martial(martial_status);
                                details.setU_edu_level(edu_level);
                                details.setU_inst_name(inst_name);
                                details.setU_edu_sector(edu_sector);
                                details.setU_job(job);
                                details.setU_org_name(org_name);
                                details.setU_work_post(work_post);
                                details.setU_total_family(totalFmem);
                                details.setU_total_brothers(brothers);
                                details.setU_total_sisters(sisters);
                                details.setU_focupation(f_occupation);
                                details.setU_moccupation(m_occupation);
                                details.setU_fincome(fincome);
                                details.setU_profile_picture("default");
                                details.setU_search_picture("default");
                                mref.setValue(details);
                                 Intent intent = new Intent(getActivity(),MainActivity.class);
                                 startActivity(intent);


                            } else {
                                mpgogress.hide();
                                Toast.makeText(getActivity(), "Signing up failed. please try again", Toast.LENGTH_SHORT).show();
                            }

                        }


                    });

                }
            }
        });

    }



    private void getDAtaFromRegUserEdu() {
        Bundle bundle = getArguments();
        email = bundle.getString("ru2_email");
        pass = bundle.getString("ru2_pass");
        f_name = bundle.getString("ru2_fname");
        l_name = bundle.getString("ru2_lname");
        profile_for = bundle.getString("ru2_profile_for");
        gender = bundle.getString("ru2_gender");
        dob = bundle.getString("ru2_dob");
        age = bundle.getString("ru2_age");
        religion = bundle.getString("ru2_religion");
        language = bundle.getString("ru2_language");
        height_feet = bundle.getString("ru2_height_feet");
        height_inch = bundle.getString("ru2_height_inch");
        country = bundle.getString("ru2_country");
        state = bundle.getString("ru2_state");
        district = bundle.getString("ru2_district");
        local_address = bundle.getString("ru2_local_address");
        community = bundle.getString("ru2_community");
        add_community_details = bundle.getString("ru2_add_community_details");
        residence = bundle.getString("ru2_residence_status");
        martial_status = bundle.getString("ru2_martial_status");
        edu_level = bundle.getString("ru2_edu_level");
        inst_name = bundle.getString("ru2_inst_name");
        edu_sector = bundle.getString("ru2_edu_sector");
        job = bundle.getString("ru2_job");
        org_name = bundle.getString("ru2_org_name");
        work_post = bundle.getString("ru2_work_post");

    }

    private void spinner_mother_occupation() {

        ArrayAdapter arrayAdaptermoccupaton = new ArrayAdapter(this.getActivity(),android.R.layout.simple_spinner_dropdown_item);
        arrayAdaptermoccupaton.addAll("Teacher","Military","Doctor","Lawyer","Politician","Worker","Banker","Engineer","House Wife","Nurse","Foreign employee","NGO/INGO","Governmental job","Private job","Others");
        spin_mother_occupation.setAdapter(arrayAdaptermoccupaton);
        spin_mother_occupation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                m_occupation = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void spinner_father_occupation() {

        ArrayAdapter arrayAdapterfoccupation = new ArrayAdapter(this.getActivity(),android.R.layout.simple_spinner_dropdown_item);
        arrayAdapterfoccupation.addAll("Teacher","Military","Doctor","Lawyer","Politician","Worker","Banker","Engineer","Foreign employee","NGO/INGO","Governmental job","Private job","Others");
        spin_father_occupation.setAdapter(arrayAdapterfoccupation);
        spin_father_occupation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                f_occupation = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}