package com.example.ambuapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ambuapp.entities.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.FirebaseFirestore;

public class Registration extends AppCompatActivity implements View.OnClickListener {

    private TextView btnRegister;
    private EditText txtUsername, txtEmail, txtPassword, txtPhone;
    Spinner Register_Spinner;

    private FirebaseAuth mAuth;
    private Object user;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();
        Register_Spinner= findViewById(R.id.spinnerRegister);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);

        txtUsername = (EditText) findViewById(R.id.inputUsername);
        txtEmail = (EditText) findViewById(R.id.inputEmail);
        txtPassword = (EditText) findViewById(R.id.inputPassword);
        txtPhone = (EditText) findViewById(R.id.inputPhone);


        db = FirebaseFirestore.getInstance();

        TextView btn = findViewById(R.id.txtLogin);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registration.this, Login.class));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegister:
                btnRegister();
                break;
        }

    }

    private void btnRegister() {

        String username = txtUsername.getText().toString().trim();
        String email = txtEmail.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();
        String phn = txtPhone.getText().toString().trim();
        String AccountType= Register_Spinner.getSelectedItem().toString();

        if (username.isEmpty()) {
            txtUsername.setError("UserName is required");
            txtUsername.requestFocus();
            return;
        } else if (email.isEmpty()) {
            txtEmail.setError("Email is required");
            txtEmail.requestFocus();
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            txtEmail.setError("Please provide valid email");
            txtEmail.requestFocus();
            return;
        } else if (password.isEmpty()) {
            txtPassword.setError("Password is required");
            txtPassword.requestFocus();
            return;
        } else if (password.length() < 6) {
            txtPassword.setError("Password should not be less than 6 characters");
            txtPassword.requestFocus();
            return;
        } else if (phn.isEmpty()) {
            txtPhone.setError("Phone Number Required ");
            txtPhone.requestFocus();
            return;
        } else if (AccountType.isEmpty()) {
            Toast.makeText(this, "Please select account type", Toast.LENGTH_SHORT).show();
            Register_Spinner.requestFocus();
        } else

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        User us = new User(FirebaseAuth.getInstance().getCurrentUser().getUid(), username, email, phn,AccountType);


                        db.collection("users").document(us.uid).set(us)

                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {

                                        Toast.makeText(Registration.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();

                                    } else {
                                        Toast.makeText(Registration.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                    }
                                });
                    } else {
                        // Registration failed
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            // Email address already in use
                            Toast.makeText(Registration.this, "The email address is already in use by another account.", Toast.LENGTH_LONG).show();
                        } else {
                            // Other registration failure
                            Log.d("onComplete: ", task.getException().toString());
                            Toast.makeText(Registration.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
}
