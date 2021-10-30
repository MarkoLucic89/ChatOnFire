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

import com.example.chatonfire.databinding.FragmentLogoutDialogBinding;

public class LogoutDialogFragment extends DialogFragment {

    private FragmentLogoutDialogBinding binding;
    private OnLogoutListener onLogoutListener;

    public LogoutDialogFragment(OnLogoutListener onLogoutListener) {
        this.onLogoutListener = onLogoutListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLogoutDialogBinding.inflate(inflater, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListeners();
    }

    private void setListeners() {
        binding.textViewLogoutDialogConfirm.setOnClickListener(v -> {
            onLogoutListener.onLogOut();
            dismiss();
        });
        binding.textViewLogoutDialogNo.setOnClickListener(v -> dismiss());
    }

    public interface OnLogoutListener {
        void onLogOut();
    }

}