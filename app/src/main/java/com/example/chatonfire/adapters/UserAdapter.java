package com.example.chatonfire.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatonfire.R;
import com.example.chatonfire.databinding.ListItemUserBinding;
import com.example.chatonfire.model.User;
import com.example.chatonfire.utility.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> users;
    private final OnUserClickListener onUserClickListener;

    public UserAdapter(OnUserClickListener onUserClickListener) {
        this.onUserClickListener = onUserClickListener;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemUserBinding binding = ListItemUserBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
        return new UserViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setUserItemUi(users.get(position));
        holder.setListeners(users.get(position));
    }

    @Override
    public int getItemCount() {
        if (users == null) return 0;
        else return users.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        ListItemUserBinding binding;

        public UserViewHolder(@NonNull ListItemUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void setUserItemUi(User user) {
            Picasso.get().load(user.getUser_image()).into(binding.imageViewItemProfilePicture);
            binding.textViewItemName.setText(user.getUser_name());
            binding.textViewItemEmail.setText(user.getUser_email());
            if (user.getUser_status() != null) {
                if (user.getUser_status().equals(Constants.STATUS_ONLINE)) {
                    binding.cardViewItemStatus.setBackgroundResource(R.drawable.background_status_online);
                } else {
                    binding.cardViewItemStatus.setBackgroundResource(R.drawable.background_status_offline);
                }
            } else {
                binding.cardViewItemStatus.setBackgroundResource(R.drawable.background_status_offline);
            }
        }

        void setListeners(User user) {
            binding.getRoot().setOnClickListener(v -> onUserClickListener.onUserClick(user));
        }
    }

    public interface OnUserClickListener {
        void onUserClick(User user);
    }

}
