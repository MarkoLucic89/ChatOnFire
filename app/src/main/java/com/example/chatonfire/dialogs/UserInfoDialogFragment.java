package com.example.chatonfire.dialogs;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatonfire.databinding.FragmentUserInfoDialogBinding;
import com.example.chatonfire.model.User;
import com.squareup.picasso.Picasso;

public class UserInfoDialogFragment extends DialogFragment {

    private FragmentUserInfoDialogBinding binding;
    private User user;

    public UserInfoDialogFragment(User user) {
        this.user = user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserInfoDialogBinding.inflate(inflater, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Picasso.get().load(user.getUser_image()).into(binding.imageViewUserInfoImage);
        binding.textViewUserInfoName.setText(user.getUser_name());
        binding.textViewUserInfoEmail.setText(user.getUser_email());
        binding.buttonUserInfoOk.setOnClickListener(v -> dismiss());
    }
}