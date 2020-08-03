package com.brialchidimaar.latticeinovationassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.brialchidimaar.latticeinovationassignment.SharedPrefs.SharedPrefs;
import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {

    Button loginButton;
    EditText emailEdt, passwordEdt;
    TextView registerPageTextview;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        emailEdt = findViewById(R.id.email_log_edt);
        passwordEdt = findViewById(R.id.password_log_edt);
        loginButton = findViewById(R.id.login_btn);
        relativeLayout = findViewById(R.id.login_rl);
        registerPageTextview = findViewById(R.id.register_page_txt);

        registerPageTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailEdt.getText().toString();
                String password = passwordEdt.getText().toString();

                if (email.equals(SharedPrefs.getEmail("email", getApplicationContext()))
                        && password.equals(SharedPrefs.getPassword("password", getApplicationContext()))) {

                    SharedPrefs.setLogin("login", "login", getApplicationContext());
                    Toast.makeText(LoginActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                } else if(email.isEmpty() || password.isEmpty()) {
                    displaySnackbar("Fields Missing");
                } else {
                    displaySnackbar("Login Failed");
                }

            }
        });

    }

    public void displaySnackbar(String s){
        Snackbar snackbar = Snackbar
                .make(relativeLayout, s, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (SharedPrefs.getLogin("login", getApplicationContext()).equals("login")) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}
