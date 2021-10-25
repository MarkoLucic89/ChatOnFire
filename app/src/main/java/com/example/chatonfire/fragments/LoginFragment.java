package com.example.chatonfire.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.chatonfire.R;
import com.example.chatonfire.databinding.FragmentLoginBinding;
import com.example.chatonfire.viewmodel.LoginRegisterViewModel;

public class LoginFragment extends Fragment {

    //ui
    private FragmentLoginBinding binding;
    private NavController navController;
    private ProgressDialog progressDialog;

    //user
    private String email;
    private String password;

    //viewModel
    private LoginRegisterViewModel loginRegisterViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(getContext());
        loginRegisterViewModel = new ViewModelProvider(this).get(LoginRegisterViewModel.class);
        loginRegisterViewModel.getIsUserSignedIn().observe(this, aBoolean -> {
            if (aBoolean) {
                progressDialog.dismiss();
                showToast("User successfully signed in");
                navController.navigate(R.id.action_loginFragment_to_usersFragment);
            } else {
                showToast("User unsuccessfully signed in");
                binding.textInputEditTextLoginEmail.getText().clear();
                binding.textInputEditTextLoginPassword.getText().clear();
                binding.textInputEditTextLoginEmail.requestFocus();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        clearTextHelpers();
        setListeners();
        initLoginViewModel();
    }

    private void initLoginViewModel() {

    }

    private void setListeners() {
        binding.buttonLogin.setOnClickListener(v -> login());
        binding.textViewForgotPassword.setOnClickListener(v -> showToast("Sorry, can't help you"));
        binding.textViewSignUp.setOnClickListener(v ->
                navController.navigate(R.id.action_loginFragment_to_registerFragment));
    }

    private void login() {
        getEmailAndPassword();
        if (isUserDataNotEmpty()) {
            progressDialog.show();
            progressDialog.setMessage("signing in...");
            loginRegisterViewModel.login(email, password);
        }
    }

    private boolean isUserDataNotEmpty() {
        if (email.isEmpty() || password.isEmpty()) {
            setHelperTexts();
            showToast("Please, enter all fields");
            return false;
        } else {
            return true;
        }
    }

    private void getEmailAndPassword() {
        email = binding.textInputEditTextLoginEmail.getText().toString().trim();
        password = binding.textInputEditTextLoginPassword.getText().toString().trim();
    }

    private void showToast(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }

    private void clearTextHelpers() {
        binding.textInputLayoutLoginEmail.setHelperText("");
        binding.textInputLayoutLoginPassword.setHelperText("");
    }

    private void setHelperTexts() {
        binding.textInputLayoutLoginEmail.setHelperText("Required");
        binding.textInputLayoutLoginPassword.setHelperText("Required");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}