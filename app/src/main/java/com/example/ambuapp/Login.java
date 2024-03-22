package com.example.ambuapp;

import static com.example.ambuapp.common.Constants.KEY_ROLE;
import static com.example.ambuapp.common.Constants.ROLE_ADMIN;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.ambuapp.dashboard.DashboardActivity;
import com.example.ambuapp.databinding.ActivityLoginBinding;
import com.example.ambuapp.entities.Admin;
import com.example.ambuapp.usermaps.UserMapActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity implements View.OnClickListener {


    private FirebaseAuth mAuth;
    private ActivityLoginBinding binding;
    FirebaseFirestore fStore;
    private String role;
    String type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        fStore= FirebaseFirestore.getInstance();

        init();
        setListeners();

        mAuth = FirebaseAuth.getInstance();
    }

    private void init() {
        role = getIntent().getStringExtra(KEY_ROLE);

        // Add logging statements to debug
        Log.d("LoginActivity", "Role: " + role);
        Log.d("LoginActivity", "KEY_ROLE: " + KEY_ROLE);

        if (role != null && role.equals(ROLE_ADMIN)) {
            binding.fgpass.setVisibility(View.GONE);
            binding.signUpContainer.setVisibility(View.GONE);
        }
    }


    private void setListeners() {
        binding.btnlogin.setOnClickListener(this);
        binding.fgpass.setOnClickListener(this);
        binding.txtSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.txtSignUp) {

            startActivity(new Intent(this, Registration.class));
        } else if (id == R.id.btnlogin) {

            userLogin();
        } else if (id == R.id.fgpass) {

            startActivity(new Intent(this, ForgotPassword.class));
        }

    }

    private void userLogin() {

        String email = binding.usrEmail.getText().toString().trim();
        String password = binding.usrPass.getText().toString().trim();

        if (email.isEmpty()) {
            binding.usrEmail.setError("Email is required");
            binding.usrEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.usrEmail.setError("Please provide valid email");
            binding.usrEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            binding.usrPass.setError("Password is required");
            binding.usrPass.requestFocus();
            return;
        }
        if (password.length() < 6) {
            binding.usrPass.setError("Password should not be less than 6 characters");
            binding.usrPass.requestFocus();
            return;
        }

        binding.progress.container.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


                        if (role.equals(ROLE_ADMIN)) {
                          String Uid= user.getUid();
                            DocumentReference df= fStore.collection("users").document(Uid);
                            df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    Log.d("TAG","onSuccess: "+ documentSnapshot.getData());
                                    type= documentSnapshot.getString("address");
                                    assert type != null;
                                    if (type.equals("Admin")){
                                        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                                        finishAffinity();

                                    } else {
                                        Toast.makeText(Login.this, "You are not authorized to login as admin", Toast.LENGTH_LONG).show();
                                    }


                                }
                            });

                          /*  FirebaseFirestore.getInstance()
                                    .collection("admins")
                                    .document(user.getUid())
                                    .get()
                                    .addOnCompleteListener(task1 -> {*/
                          /*              if (task1.isSuccessful()) {
                                            Admin admin = task1.getResult().toObject(Admin.class);
                                            if (admin != null) {


                                                startActivity(new Intent(this, DashboardActivity.class));
                                                finishAffinity();
                                            } else {
                                                Toast.makeText(Login.this, "You are not authorized to login as admin", Toast.LENGTH_LONG).show();
                                            }


                                        }else {
                                            Toast.makeText(Login.this, "Failed to Login! Something went wrong!", Toast.LENGTH_LONG).show();
                                        }

                                        binding.progress.container.setVisibility(View.GONE);
                                    });*/

                        } else {
                            if (user.isEmailVerified()) {
                                startActivity(new Intent(Login.this, BaasicActvity.class));

                                finishAffinity();
                            } else {


                                user.sendEmailVerification();
                                Toast.makeText(Login.this, "Please check your email to verify your account!", Toast.LENGTH_LONG).show();

                            }
                            binding.progress.container.setVisibility(View.GONE);
                        }


                    } else {
                        binding.progress.container.setVisibility(View.GONE);
                        Toast.makeText(Login.this, "Failed to Login! Please check your credentials!", Toast.LENGTH_LONG).show();
                    }
                });


    }

}
