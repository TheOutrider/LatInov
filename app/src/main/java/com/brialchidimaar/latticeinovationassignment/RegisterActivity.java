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
import com.hbb20.CountryCodePicker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    EditText nameEDT, emailEDT, passwordEDT, addressEDT, numberEDT;
    Button registerButton;
    CountryCodePicker codePicker;
    TextView loginpageTextview;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameEDT = findViewById(R.id.name_reg_edt);
        emailEDT = findViewById(R.id.email_reg_edt);
        passwordEDT = findViewById(R.id.password_reg_edt);
        addressEDT = findViewById(R.id.address_reg_edt);
        numberEDT = findViewById(R.id.phone_reg_edt);

        registerButton = findViewById(R.id.register_btn);
        loginpageTextview = findViewById(R.id.login_page_txt);
        codePicker = findViewById(R.id.cpp);
        relativeLayout = findViewById(R.id.register_rl);

        String cpp = codePicker.getFullNumberWithPlus().replace(" ", "");

        loginpageTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEDT.getText().toString();
                String number = numberEDT.getText().toString();
                String email = emailEDT.getText().toString();
                String address = addressEDT.getText().toString();
                String password = passwordEDT.getText().toString();
                String cpp = codePicker.getFullNumberWithPlus().replace(" ", "");

                if(email.isEmpty() && password.isEmpty() && name.isEmpty() && number.isEmpty() && address.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Fields Missing", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 8 && password.length() > 15) {
                    Toast.makeText(RegisterActivity.this, "Invalid password", Toast.LENGTH_SHORT).show();
                } else if (number.length() != 10) {
                    Toast.makeText(RegisterActivity.this, "Invalid phone number", Toast.LENGTH_SHORT).show();
                } else if (isEmailValid(email) && isValidPassword(password) && !name.isEmpty() && !address.isEmpty() && !number.isEmpty()) {
                    registerNow(email, password, name, address, cpp);
                } else {
                    displaySnackbar("Registration Failed");
                }
            }
        });

    }

    private void registerNow(String email, String password, String name, String address, String number) {

        SharedPrefs.setName("name", name, getApplicationContext());
        SharedPrefs.setEmail("email", email, getApplicationContext());
        SharedPrefs.setNumber("number", number, getApplicationContext());
        SharedPrefs.setAddress("address", address, getApplicationContext());
        SharedPrefs.setPassword("password", password, getApplicationContext());

        SharedPrefs.setLogin("login", "login", getApplicationContext());

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public void displaySnackbar(String s){
        Snackbar snackbar = Snackbar
                .make(relativeLayout, s, Snackbar.LENGTH_LONG);
        snackbar.show();
    }


}
