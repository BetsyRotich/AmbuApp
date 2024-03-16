package com.example.ambuapp.users;

import static com.example.ambuapp.common.Constants.KEY_DATA;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ambuapp.R;
import com.example.ambuapp.databinding.ActivityUserListBinding;
import com.example.ambuapp.entities.User;
import com.example.ambuapp.users.adapters.UserListRecyclerAdapter;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class UserListActivity extends AppCompatActivity implements UserListRecyclerAdapter.OnItemClickListener {

    private ActivityUserListBinding binding;
    private UserListRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_list);

        binding.toolbar.setTitle("Users");

        adapter = new UserListRecyclerAdapter(this);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));


        binding.btnAdd.setOnClickListener(v -> startActivity(new Intent(this, CreateUserActivity.class)));
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());


    }



    @Override
    protected void onResume() {
        super.onResume();

        binding.progress.container.setVisibility(View.VISIBLE);

        FirebaseFirestore.getInstance().collection("users").get().addOnCompleteListener(task -> {

            if (task.isSuccessful()) {

                List<User> data = task.getResult().toObjects(User.class);
                adapter.submitList(data);
            }

            binding.progress.container.setVisibility(View.GONE);

        });
    }

    @Override
    public void onItemClick(User user, int position) {
        Intent intent = new Intent(this, CreateUserActivity.class);
        intent.putExtra(KEY_DATA, user);
        startActivity(intent);
    }
}
