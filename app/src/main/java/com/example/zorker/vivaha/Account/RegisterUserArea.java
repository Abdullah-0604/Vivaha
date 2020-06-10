package com.example.zorker.vivaha.Account;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
public class RegisterUserArea extends Fragment {

    Button button_reguser_area;
    Spinner spin_state, spin_district, spin_community,spin_residence_status, spin_martial_state;
    EditText et_localarea,et_add_community_details;
    String email,pass,f_name,l_name,profile_for,gender,dob,age,religion,language,height_feet,height_inch,country;
    String state,district,community,residence_status,martial_state;


    public RegisterUserArea() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_user_area, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        et_localarea = (EditText) view.findViewById(R.id.et_reguser_address);
        et_add_community_details = (EditText) view.findViewById(R.id.et_reguser_add_community_details);
        spin_state = (Spinner) view.findViewById(R.id.spinner_reguser_state);
        spin_district = (Spinner) view.findViewById(R.id.spinner_reguser_district);
        spin_community = (Spinner) view.findViewById(R.id.spinner_reguser_community);
        spin_residence_status = (Spinner) view.findViewById(R.id.spinner_reguser_resisdence);
        spin_martial_state = (Spinner) view.findViewById(R.id.spinner_reguser_maritalstatus);

        spinner_state();
        spinner_district();
        spinner_community();
        spinner_residence();
        spinner_martial_status();

        button_reguser_area = (Button) view.findViewById(R.id.button_reguser_area);
        getprevious_reguser_details_data();


        button_reguser_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String localarea = et_localarea.getText().toString();
                String add_community_details = et_add_community_details.getText().toString();
                if (TextUtils.isEmpty(localarea))
                {
                    Toast.makeText(getActivity(), "please enter local address", Toast.LENGTH_SHORT).show();
                }
               else {
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("ru1_email", email);
                    bundle1.putString("ru1_pass", pass);
                    bundle1.putString("ru1_fname", f_name);
                    bundle1.putString("ru1_lname", l_name);
                    bundle1.putString("ru1_profile_for", profile_for);
                    bundle1.putString("ru1_gender", gender);
                    bundle1.putString("ru1_dob", dob);
                    bundle1.putString("ru1_age", age);
                    bundle1.putString("ru1_religion", religion);
                    bundle1.putString("ru1_language", language);
                    bundle1.putString("ru1_height_feet",height_feet);
                    bundle1.putString("ru1_height_inch",height_inch);
                    bundle1.putString("ru1_country", country);
                    bundle1.putString("ru1_state", state);
                    bundle1.putString("ru1_district", district);
                    bundle1.putString("ru1_local_address", localarea);
                    bundle1.putString("ru1_community", community);
                    bundle1.putString("ru1_add_community_details", add_community_details);
                    bundle1.putString("ru1_residence_status", residence_status);
                    bundle1.putString("ru1_martial_status", martial_state);

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    RegisterUserEduWork registerUserEduWork = new RegisterUserEduWork();
                    registerUserEduWork.setArguments(bundle1);
                    ft.replace(R.id.framelayout_account, registerUserEduWork);
                    ft.addToBackStack(null).commit();

                }
            }

        });
    }

    private void spinner_martial_status() {

        ArrayAdapter arrayAdaptermartial_status = new ArrayAdapter(this.getActivity(),android.R.layout.simple_spinner_dropdown_item);
        arrayAdaptermartial_status.addAll("Single","Committed","Divorced");
        spin_martial_state.setAdapter(arrayAdaptermartial_status);
        spin_martial_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                martial_state = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void spinner_residence() {

        ArrayAdapter arrayAdapterresidence = new ArrayAdapter(this.getActivity(), android.R.layout.simple_spinner_dropdown_item);
        arrayAdapterresidence.addAll("Permanent resident","Temporary resident","Citizen","NRN","Refugee");
        spin_residence_status.setAdapter(arrayAdapterresidence);
        spin_residence_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                residence_status = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void spinner_community() {

        ArrayAdapter arrayAdaptercommunity = new ArrayAdapter(this.getActivity(),android.R.layout.simple_spinner_dropdown_item);
        arrayAdaptercommunity.addAll("Brahmin(upadhaya)","Brahmin(Jaisi)","Chhetri","Magar","Rai","Limbu","Gurung","Kami","Damai","Sarki","Newar","etc");
        spin_community.setAdapter(arrayAdaptercommunity);
        spin_community.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                community = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void spinner_district() {

        ArrayAdapter arrayAdapterdistrict = new ArrayAdapter(this.getActivity(),android.R.layout.simple_spinner_dropdown_item);
        arrayAdapterdistrict.add("Items to be added");
        spin_district.setAdapter(arrayAdapterdistrict);
        spin_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                district = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void spinner_state() {

        ArrayAdapter arrayAdapterstate = new ArrayAdapter(this.getActivity(),android.R.layout.simple_spinner_dropdown_item);
        arrayAdapterstate.addAll("State no. 1",
                                    "State no. 2","State no. 3","State no. 4","State no. 5","State no. 6", "state no. 7");
        spin_state.setAdapter(arrayAdapterstate);
        spin_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                state = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void getprevious_reguser_details_data() {

        Bundle bundle = getArguments();
        email = bundle.getString("ru_email");
        pass = bundle.getString("ru_pass");
        f_name = bundle.getString("ru_fname");
        l_name = bundle.getString("ru_lname");
        gender = bundle.getString("ru_gender");
        profile_for = bundle.getString("ru_profile_for");
        dob = bundle.getString("ru_dob");
        age = bundle.getString("ru_age");
        religion = bundle.getString("ru_religion");
        language = bundle.getString("ru_language");
        height_feet = bundle.getString("ru_height_feet");
        height_inch = bundle.getString("ru_height_inch");
        country = bundle.getString("ru_country");
    }
}
