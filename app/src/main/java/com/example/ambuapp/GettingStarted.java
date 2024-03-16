package com.example.ambuapp;

import static com.example.ambuapp.common.Constants.KEY_ROLE;
import static com.example.ambuapp.common.Constants.ROLE_ADMIN;
import static com.example.ambuapp.common.Constants.ROLE_USER;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.ambuapp.databinding.ActivityGettingStartedBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class GettingStarted extends AppCompatActivity implements View.OnClickListener {
    private Button btn_register;
    private Button btn_login;


    private FirebaseAuth.AuthStateListener listener;
    private ActivityGettingStartedBinding binding;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_getting_started);

        role = getIntent().getStringExtra(KEY_ROLE);

        if (role.equals(ROLE_ADMIN)) {

            binding.btnRegister.setVisibility(View.GONE);
        } else if (role.equals(ROLE_USER)) {

            binding.btnRegister.setVisibility(View.VISIBLE);
        }
        binding.role.setText(role);

        binding.btnLogin.setOnClickListener(this);
        binding.btnRegister.setOnClickListener(this);

    }



    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_login) {
            Intent intent = new Intent(GettingStarted.this, Login.class);
            intent.putExtra(KEY_ROLE, role);
            startActivity(intent);
        } else if (id == R.id.btn_register) {
            Intent intent = new Intent(GettingStarted.this, Registration.class);
            intent.putExtra(KEY_ROLE, role);
            startActivity(intent);
        }
    }
}
