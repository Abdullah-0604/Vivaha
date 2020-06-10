package com.example.zorker.vivaha.Account;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zorker.vivaha.MainActivity;
import com.example.zorker.vivaha.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.text.NumberFormat;


public class Login extends Fragment {
    private Button btn_login,btn_register;
    private EditText et_email,et_password;
    FirebaseAuth mauth;
    private ProgressDialog progressDialog;
    public Login() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mauth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getActivity());

        et_email = (EditText) view.findViewById(R.id.et_login_email);
        et_password = (EditText) view.findViewById(R.id.et_login_password);
        btn_login = (Button) view.findViewById(R.id.button_login);
        btn_register = (Button) view.findViewById(R.id.button_register);



        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_email.getText().toString();
                String password = et_password.getText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password))
                {
                    Toast.makeText(getActivity(), "please enter your email and password correctly", Toast.LENGTH_SHORT).show();
                }
                else {
                    progressDialog.setMessage("Logging in");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    mauth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), "welcome", Toast.LENGTH_SHORT).show();
                                Intent main_act = new Intent(getActivity(), MainActivity.class);
                                startActivity(main_act);
                                getActivity().finish();



                            } else {
                                progressDialog.hide();
                                Toast.makeText(getActivity(), "Please enter correct email and password", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.framelayout_account,new RegisterUserDetails());
                fragmentTransaction.addToBackStack(null).commit();

            }
        });

    }
}
