package com.example.zorker.vivaha;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.zorker.vivaha.Account.Login;
import com.example.zorker.vivaha.Account.RegisterUserDetails;

public class FrameHolderAccount extends AppCompatActivity implements View.OnClickListener {

    FrameLayout frameholder ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame_holder_account);
        frameholder = (FrameLayout) findViewById(R.id.framelayout_account);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.framelayout_account,new Login());
        fragmentTransaction.disallowAddToBackStack().commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {

        }
    }

}
