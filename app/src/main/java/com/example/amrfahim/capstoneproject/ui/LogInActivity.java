package com.example.amrfahim.capstoneproject.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.amrfahim.capstoneproject.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LogInActivity extends AppCompatActivity {

    @BindView(R.id.login_btn) Button logInBtn;
    @BindView(R.id.signup_btn_logIn) Button signUpBtn;
    @BindView(R.id.facebook_btn) Button facebookBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        ButterKnife.bind(this);


    }

    @Override
    protected void onStart() {
        super.onStart();
        final Intent logInIntent = new Intent(this, MainActivity.class);
        final Intent signUpIntent = new Intent(this, SignUpActivity.class);
        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(logInIntent);
            }
        });
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(signUpIntent);
            }
        });
    }
}
