package com.example.chatonfire.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatonfire.R;
import com.example.chatonfire.databinding.FragmentSplashBinding;
import com.google.firebase.auth.FirebaseAuth;

public class SplashFragment extends Fragment {

    private FragmentSplashBinding binding;
    private FirebaseAuth firebaseAuth;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSplashBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        navController = Navigation.findNavController(view);
    }

    @Override
    public void onStart() {
        super.onStart();
        new Handler().postDelayed(this::goToNextActivity, 3000);
    }

    private void goToNextActivity() {
        if (firebaseAuth.getCurrentUser() != null) {
            navController.navigate(R.id.action_splashFragment_to_recentConversationsFragment);
        } else {
            navController.navigate(R.id.action_splashFragment_to_loginFragment);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}