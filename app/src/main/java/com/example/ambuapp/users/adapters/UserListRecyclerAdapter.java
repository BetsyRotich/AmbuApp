package com.example.ambuapp.users.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ambuapp.databinding.ListItemUserBinding;
import com.example.ambuapp.entities.User;

import java.util.Objects;

public class UserListRecyclerAdapter extends ListAdapter<User, UserListRecyclerAdapter.UserListViewHolder> {


    private static final DiffUtil.ItemCallback<User> diffCallback = new DiffUtil.ItemCallback<User>() {
        @Override
        public boolean areItemsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return oldItem.uid.equals(newItem.uid);
        }

        @Override
        public boolean areContentsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return Objects.equals(oldItem.email, newItem.email)
                    && Objects.equals(oldItem.username, newItem.username)
                    && Objects.equals(oldItem.phoneNumber, newItem.phoneNumber)
                    && Objects.equals(oldItem.address, newItem.address);
        }
    };
    private final OnItemClickListener listener;


    public UserListRecyclerAdapter(OnItemClickListener listener) {
        super(diffCallback);

        this.listener = listener;
    }

    @NonNull
    @Override
    public UserListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserListViewHolder(ListItemUserBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserListViewHolder holder, int position) {
        User user = getItem(position);

        holder.bind(user);

        holder.binding.getRoot().setOnClickListener(v -> this.listener.onItemClick(user, position));
    }

    static class UserListViewHolder extends RecyclerView.ViewHolder {

        final ListItemUserBinding binding;

        public UserListViewHolder(ListItemUserBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        public void bind(User user) {
            binding.name.setText(user.username);
            binding.email.setText(user.email);

        }


    }

    public interface OnItemClickListener {
        void onItemClick(User user, int position);
    }
}
