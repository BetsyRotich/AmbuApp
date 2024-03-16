package com.example.ambuapp.users;

import static com.example.ambuapp.common.Constants.KEY_DATA;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.ambuapp.R;
import com.example.ambuapp.databinding.ActivityCreateUserBinding;
import com.example.ambuapp.entities.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableReference;



import org.json.JSONException;
import org.json.JSONObject;

import javax.annotation.Nullable;

public class CreateUserActivity extends AppCompatActivity {

    private ActivityCreateUserBinding binding;

    @Nullable
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_user);

        user = (User) getIntent().getSerializableExtra(KEY_DATA);


        setUpToolbar(user);
        setFields(user);

        binding.btnSubmit.setOnClickListener(v -> {
            if (user != null) {
                updateUser(user);
            } else {
                createUser();
            }
        });




    }



    private void createUser() {
        binding.progress.container.setVisibility(View.VISIBLE);

        HttpsCallableReference createUserFunction = FirebaseFunctions.getInstance().getHttpsCallable("createUser");

        JSONObject user = new JSONObject();
        try {
            user.put("username",binding.editTextName.getText().toString());
            user.put("email",binding.editTextEmail.getText().toString());
            user.put("phoneNumber",binding.editTextName.getText().toString());
            user.put("address",binding.editTextPhone.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        createUserFunction.call(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()){

                finish();
            } else {
                Log.d("Hello", "task failed" + task.getException());
            }
            binding.progress.container.setVisibility(View.GONE);
        });



    }

    private void updateUser(User user) {
        binding.progress.container.setVisibility(View.VISIBLE);

        user.username = binding.editTextName.getText().toString();
        user.phoneNumber = binding.editTextPhone.getText().toString();
        user.address = binding.editTextAddress.getText().toString();
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(user.uid)
                .set(user, SetOptions.merge()).addOnCompleteListener(this, task -> {

                    if (task.isSuccessful()){

                        finish();
                    } else {
                        Log.d("Hello", "task failed " + task.getException());
                    }

                    binding.progress.container.setVisibility(View.GONE);
                });
    }

    private void setFields(User user) {

        if (user != null) {
            binding.editTextName.setText(user.username);
            binding.editTextEmail.setText(user.email);
            binding.editTextPhone.setText(user.phoneNumber);
            binding.editTextAddress.setText(user.address);

            binding.editTextEmail.setEnabled(false);
            binding.editTextEmail.setFocusable(false);
            binding.editTextEmail.setClickable(false);
            binding.editTextEmail.setCursorVisible(false);
        } else {
            binding.editTextEmail.setEnabled(true);
            binding.editTextEmail.setFocusable(true);
            binding.editTextEmail.setClickable(true);
            binding.editTextEmail.setCursorVisible(true);
        }
    }

    private void setUpToolbar(User user) {
        if (user != null) {
            binding.toolbar.setTitle("Edit User");
            binding.toolbar.inflateMenu(R.menu.menu_delete);
            binding.toolbar.setOnMenuItemClickListener(v->{

                if (v.getItemId() == R.id.delete){
                    deleteUser(user);
                    return true;
                }

                return false;
            });
        } else {
            binding.toolbar.setTitle("Create User");
        }


        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void deleteUser(User user) {
        binding.progress.container.setVisibility(View.VISIBLE);

        HttpsCallableReference createUserFunction = FirebaseFunctions.getInstance().getHttpsCallable("deleteUser");
        JSONObject data = new JSONObject();
        try {
            data.put("uid",user.uid);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        createUserFunction.call(data).addOnCompleteListener(task -> {
            if (task.isSuccessful()){

                finish();
            } else {
                Log.d("Hello", "task failed " + task.getException());
            }

            binding.progress.container.setVisibility(View.GONE);
        });
    }
}
