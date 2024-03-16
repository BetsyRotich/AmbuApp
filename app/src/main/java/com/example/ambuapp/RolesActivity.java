package com.example.ambuapp;

import static com.example.ambuapp.common.Constants.KEY_ROLE;
import static com.example.ambuapp.common.Constants.ROLE_ADMIN;
import static com.example.ambuapp.common.Constants.ROLE_USER;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.ambuapp.dashboard.DashboardActivity;
import com.example.ambuapp.databinding.ActivityRolesBinding;
import com.example.ambuapp.entities.Admin;
import com.example.ambuapp.entities.User;
import com.example.ambuapp.usermaps.UserMapActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class RolesActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Roles";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener listener;
    private ActivityRolesBinding binding;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_roles);

        binding.btnUser.setOnClickListener(this);
        binding.btnAdmin.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        count = 0;
        binding.progress.container.setVisibility(View.VISIBLE);

        listener = firebaseAuth -> {
            FirebaseUser user = mAuth.getCurrentUser();

            if (user != null) {
                FirebaseFirestore.getInstance()
                        .collection("users")
                        .document(user.getUid())
                        .get().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {

                                User user1 = task.getResult().toObject(User.class);
                                if (user1 != null) {

                                    Log.d(TAG, "onStart: user success");
                                    startActivity(new Intent(this, ChatActivity.class));
                                    finishAffinity();
                                }

                            }

                            count++;
                            if (count == 2){
                                binding.progress.container.setVisibility(View.GONE);
                            }
                        });

                FirebaseFirestore.getInstance()
                        .collection("admins")
                        .document(user.getUid())
                        .get().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Admin admin = task.getResult().toObject(Admin.class);
                                if (admin != null) {
                                    Log.d(TAG, "onStart: admin success"  + admin.username);

                                    startActivity(new Intent(this, DashboardActivity.class));
                                    finishAffinity();
                                }
                            }

                            count++;
                            if (count == 2){
                                binding.progress.container.setVisibility(View.GONE);
                            }
                        });

            } else {
                binding.progress.container.setVisibility(View.GONE);
                Log.e("Home", "user not logged in");
            }
        };
        mAuth.addAuthStateListener(listener);

    }

    @Override
    protected void onStop() {
        super.onStop();

        mAuth.removeAuthStateListener(listener);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_user) {
            Intent intent = new Intent(this, GettingStarted.class);
            intent.putExtra(KEY_ROLE, ROLE_USER);
            startActivity(intent);
        } else if (id == R.id.btn_admin) {
            Intent intent = new Intent(this, Login.class);
            intent.putExtra(KEY_ROLE, ROLE_ADMIN);
            startActivity(intent);
        }
    }
}
