package com.example.zorker.vivaha.Account;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.zorker.vivaha.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterUserEduWork extends Fragment {

    Button button_reguser_eduwork;
    Spinner spin_edu_level, spin_edu_sector, spin_job;
    EditText et_edu_inst_name, et_job_org, et_job_work_post;
    String email, pass,f_name,l_name, profile_for , gender , dob,age, religion, language,height_feet,height_inch, country;
    String state, district , local_address, community , add_community_details, residence , martial_status;
    String edu_level , edu_sector, job ;


    public RegisterUserEduWork() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_user_edu_work, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        button_reguser_eduwork = (Button) view.findViewById(R.id.button_reguser_edu_work);
        et_edu_inst_name = (EditText) view.findViewById(R.id.et_reguser_institution_name);
        et_job_org = (EditText) view.findViewById(R.id.et_organization_name);
        et_job_work_post = (EditText) view.findViewById(R.id.et_reguser_orgpost);
        spin_edu_level = (Spinner) view.findViewById(R.id.spinner_reguser_edulevel);
        spin_edu_sector = (Spinner) view.findViewById(R.id.spinner_reguser_edufield);
        spin_job = (Spinner) view.findViewById(R.id.spinner_reguser_job);

        sppinner_edu_level();
        spinner_edu_sector();
        Spinner_job();

        getReguserAreaData();

        button_reguser_eduwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String inst_name = et_edu_inst_name.getText().toString();
                String org_name = et_job_org.getText().toString();
                String work_post = et_job_work_post.getText().toString();

                if (TextUtils.isEmpty(inst_name))
                {
                    Toast.makeText(getActivity(), "please enter institution name", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(org_name))
                {
                    Toast.makeText(getActivity(), "please enter the organization name", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(work_post))
                {
                    Toast.makeText(getActivity(), "please specify your working post", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(inst_name) && TextUtils.isEmpty(org_name) && TextUtils.isEmpty(work_post) )
                {
                    Toast.makeText(getActivity(), "please enter the details correctly", Toast.LENGTH_SHORT).show();
                }
                else {
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("ru2_email", email);
                    bundle1.putString("ru2_pass", pass);
                    bundle1.putString("ru2_fname", f_name);
                    bundle1.putString("ru2_lname", l_name);
                    bundle1.putString("ru2_profile_for", profile_for);
                    bundle1.putString("ru2_gender", gender);
                    bundle1.putString("ru2_dob", dob);
                    bundle1.putString("ru2_age", age);
                    bundle1.putString("ru2_religion", religion);
                    bundle1.putString("ru2_language", language);
                    bundle1.putString("ru2_height_feet",height_feet);
                    bundle1.putString("ru2_height_inch",height_inch);
                    bundle1.putString("ru2_country", country);
                    bundle1.putString("ru2_state", state);
                    bundle1.putString("ru2_district", district);
                    bundle1.putString("ru2_local_address", local_address);
                    bundle1.putString("ru2_community", community);
                    bundle1.putString("ru2_add_community_details", add_community_details);
                    bundle1.putString("ru2_residence_status", residence);
                    bundle1.putString("ru2_martial_status", martial_status);
                    bundle1.putString("ru2_edu_level", edu_level);
                    bundle1.putString("ru2_inst_name", inst_name);
                    bundle1.putString("ru2_edu_sector", edu_sector);
                    bundle1.putString("ru2_job", job);
                    bundle1.putString("ru2_org_name", org_name);
                    bundle1.putString("ru2_work_post", work_post);

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    RegisterUserFamily registerUserFamily = new RegisterUserFamily();
                    registerUserFamily.setArguments(bundle1);
                    ft.replace(R.id.framelayout_account, registerUserFamily);
                    ft.addToBackStack(null).commit();
                }
            }
        });
    }

    private void Spinner_job() {

        ArrayAdapter arrayAdapterjob = new ArrayAdapter(this.getActivity(),android.R.layout.simple_spinner_dropdown_item);
        arrayAdapterjob.addAll("Government","Private","Own Business","NGO","INGO","Others");
        spin_job.setAdapter(arrayAdapterjob);
        spin_job.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                job = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void spinner_edu_sector() {

        ArrayAdapter arrayAdaptersector = new ArrayAdapter(this.getActivity(),android.R.layout.simple_spinner_dropdown_item);
        arrayAdaptersector.addAll("Enineering","Commerce","Business","Hotel management","Nursing","MBBS","Medical field","Law","Information Technology(IT)","Others");
        spin_edu_sector.setAdapter(arrayAdaptersector);
        spin_edu_sector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                edu_sector = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void sppinner_edu_level() {

        ArrayAdapter arrayAdapteredulevel = new ArrayAdapter(this.getActivity(),android.R.layout.simple_spinner_dropdown_item);
        arrayAdapteredulevel.addAll("SEE","NEB(+ 2)","Undergraduate","Graduate","PhD");
        spin_edu_level.setAdapter(arrayAdapteredulevel);
        spin_edu_level.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                edu_level = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getReguserAreaData() {

        final Bundle bundle = getArguments();
        email =bundle.getString("ru1_email");
        pass=bundle.getString("ru1_pass");
        f_name = bundle.getString("ru1_fname");
        l_name = bundle.getString("ru1_lname");
        profile_for = bundle.getString("ru1_profile_for");
        gender = bundle.getString("ru1_gender");
        dob = bundle.getString("ru1_dob");
        age = bundle.getString("ru1_age");
        religion = bundle.getString("ru1_religion");
        language = bundle.getString("ru1_language");
        height_feet = bundle.getString("ru1_height_feet");
        height_inch = bundle.getString("ru1_height_inch");
        country = bundle.getString("ru1_country");
        state = bundle.getString("ru1_state");
        district = bundle.getString("ru1_district");
        local_address = bundle.getString("ru1_local_address");
        community = bundle.getString("ru1_community");
        add_community_details = bundle.getString("ru1_add_community_details");
        residence = bundle.getString("ru1_residence_status");
        martial_status = bundle.getString("ru1_martial_status");


    }
}
