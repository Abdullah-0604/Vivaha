package com.example.zorker.vivaha.Account;


import android.app.DatePickerDialog;
import android.content.Intent;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zorker.vivaha.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterUserDetails extends Fragment {

    Button btn_user_details,btn_datepick;
    TextView tv_date;
    EditText reguser_email, reguser_password, reguser_fname, reguser_lname;
    Spinner spin_gender,spin_profile_for,spin_religion,spin_language,spin_height_feet,spin_height_inch,spin_country;
    FirebaseAuth mauth;
    String gender,profile_for,dob,age,religion,language,height_feet,height_inch,country;


    public RegisterUserDetails() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_register_user_details, container, false);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv_date = (TextView) view.findViewById(R.id.tv_date_format);
        reguser_email = (EditText) view.findViewById(R.id.et_reguserdetails_email);
        reguser_password =(EditText) view.findViewById(R.id.et_reguserdetails_password);
        reguser_fname = (EditText) view.findViewById(R.id.et_reguser_firstname);
        reguser_lname = (EditText) view.findViewById(R.id.et_reguser_lastname);
        spin_height_feet = (Spinner) view.findViewById(R.id.spinner_reguser_height_feet);
        spin_height_inch = (Spinner) view.findViewById(R.id.spinner_reguser_height_inch);
        spin_gender = (Spinner) view.findViewById(R.id.spinner_reguser_gender);
        spin_profile_for = (Spinner) view.findViewById(R.id.spinner_reguser_profilefor);
        spin_religion = (Spinner) view.findViewById(R.id.spinner_reguser_religion);
        spin_language = (Spinner) view.findViewById(R.id.spinner_reguser_language);
        spin_country = (Spinner) view.findViewById(R.id.spinner_reguser_country);

        spinner_profile_for();
        spinner_gender();
        spinner_religion();
        spinner_language();
        spinner_height_feet();
        spinner_height_inch();
        spinner_country();

        mauth = FirebaseAuth.getInstance();

        btn_datepick = (Button) view.findViewById(R.id.button_datepicker);
        btn_datepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int myear = c.get(Calendar.YEAR);
                int mmonth = c.get(Calendar.MONTH);
                int mday = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),datepickerListener,myear,mmonth,mday);
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                datePickerDialog.show();

            }
        });

        btn_user_details = (Button) view.findViewById(R.id.button_reguser_details);

        btn_user_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(reguser_email.getText().toString()))
                {
                    Toast.makeText(getActivity(), "email cannot be blank", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(reguser_password.getText().toString())|| reguser_password.getText().toString().length() < 6)
                {
                    Toast.makeText(getActivity(), "enter password correctly with minimum 6 characters", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(reguser_fname.getText().toString()) || TextUtils.isEmpty(reguser_lname.getText().toString()))
                {
                    Toast.makeText(getActivity(), "enter first and last name correctly", Toast.LENGTH_SHORT).show();
                }

                else {
                    sendtoreg_user_area();
                }

            }
        });
    }

    private void spinner_height_inch() {

        ArrayAdapter<Integer> adapter_inch = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_dropdown_item);
        ArrayList<Integer> inch_list = new ArrayList<>();
        for (int j=0;j<=9;j++)
            inch_list.add(j);
        adapter_inch.addAll(inch_list);
        spin_height_inch.setAdapter(adapter_inch);
        spin_height_inch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                height_inch = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void spinner_height_feet() {
        ArrayAdapter<Integer> adapter_feet = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item);
        ArrayList<Integer> feet_list = new ArrayList<>();
        for (int i=3;i<=10;i++)
        feet_list.add(i);
        adapter_feet.addAll(feet_list);
        spin_height_feet.setAdapter(adapter_feet);
        spin_height_feet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                height_feet = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private DatePickerDialog.OnDateSetListener datepickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR,year);
            c.set(Calendar.MONTH,month);
            c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            String format = new SimpleDateFormat("yyyy.MM.dd").format(c.getTime());
            dob = format;
            tv_date.setText(dob);
            age = Integer.toString(calculateAge(c.getTimeInMillis()));

        }
    };

    private int calculateAge(long date) {

        Calendar dob = Calendar.getInstance();
        dob.setTimeInMillis(date);

        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH))
        {
            age--;
        }
        return age;
    }

    private void spinner_country() {

        ArrayAdapter arrayAdapter = new ArrayAdapter(this.getActivity(),android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter.add("Nepal");
        spin_country.setAdapter(arrayAdapter);
        country = spin_country.getSelectedItem().toString();

    }

    private void spinner_language() {
        ArrayAdapter arrayAdapterlanguage = new ArrayAdapter(this.getActivity(),android.R.layout.simple_spinner_dropdown_item);
        arrayAdapterlanguage.addAll("Nepali","Maithili","Newari","Bhojpuri","Tamang","Magar","Bajjika","Doteli","Tharu","Urdu");
        spin_language.setAdapter(arrayAdapterlanguage);
        spin_language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                language = parent.getItemAtPosition(position).toString();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });
    }

    private void spinner_religion() {

        ArrayAdapter arrayAdapterreligion = new ArrayAdapter(this.getActivity(),android.R.layout.simple_spinner_dropdown_item);
        arrayAdapterreligion.addAll("Hindu","Buddhist", "Muslim","Christian","No Religion","spiritual");
        spin_religion.setAdapter(arrayAdapterreligion);
        spin_religion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                religion = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void spinner_profile_for() {

        ArrayAdapter arrayAdapter = new ArrayAdapter(this.getActivity(),android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter.addAll("Self","Brother","Sister","Son","Daughter","Friend");
        spin_profile_for.setAdapter(arrayAdapter);
        spin_profile_for.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                profile_for = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void spinner_gender() {

        ArrayAdapter arrayAdapter = new ArrayAdapter(this.getActivity(),android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter.add("Male");
        arrayAdapter.add("Female");
        arrayAdapter.add("Others");
        spin_gender.setAdapter(arrayAdapter);
        spin_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

             gender = parent.getItemAtPosition(position).toString();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void sendtoreg_user_area() {
        String email = reguser_email.getText().toString();
        String password = reguser_password.getText().toString();
        String f_name = reguser_fname.getText().toString();
        String l_name = reguser_lname.getText().toString();

        Bundle bundle = new Bundle();
        bundle.putString("ru_email",email);
        bundle.putString("ru_pass",password);
        bundle.putString("ru_fname",f_name);
        bundle.putString("ru_lname",l_name);
        bundle.putString("ru_gender",gender);
        bundle.putString("ru_profile_for",profile_for);
        bundle.putString("ru_dob",dob);
        bundle.putString("ru_age",age);
        bundle.putString("ru_religion",religion);
        bundle.putString("ru_language",language);
        bundle.putString("ru_height_feet",height_feet);
        bundle.putString("ru_height_inch",height_inch);
        bundle.putString("ru_country",country);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        RegisterUserArea registerUserArea = new RegisterUserArea();
        registerUserArea.setArguments(bundle);

        fragmentTransaction.replace(R.id.framelayout_account,registerUserArea);

        fragmentTransaction.addToBackStack(null).commit();

    }



}