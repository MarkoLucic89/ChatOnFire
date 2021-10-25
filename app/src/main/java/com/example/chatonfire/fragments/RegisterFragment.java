package com.example.chatonfire.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.chatonfire.R;
import com.example.chatonfire.databinding.FragmentRegisterBinding;
import com.example.chatonfire.model.User;
import com.example.chatonfire.utility.Constants;
import com.example.chatonfire.viewmodel.LoginRegisterViewModel;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class RegisterFragment extends Fragment {

    //ui
    private FragmentRegisterBinding binding;
    private NavController navController;
    private ProgressDialog progressDialog;

    //user data
    private String name;
    private String email;
    private String password;
    private String confirmPassword;
    private String image;
    private Uri currentImageUri;

    //viewModel
    private LoginRegisterViewModel loginRegisterViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(getContext());
        loginRegisterViewModel = new ViewModelProvider(this).get(LoginRegisterViewModel.class);
        loginRegisterViewModel.getIsUserSignedUp().observe(this, aBoolean -> {
            progressDialog.dismiss();
            if (aBoolean) {
                showToast("User " + name + " successfully signed up");
                navController.navigate(R.id.action_registerFragment_to_usersFragment);
            } else {
                showToast("User " + name + " unsuccessfully signed up");
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        clearHelperTexts();
        setListeners();
    }

    private void setListeners() {
        binding.imageViewRegister.setOnClickListener(v -> pickImage());
        binding.buttonRegister.setOnClickListener(v -> register());
        binding.textViewSignIn.setOnClickListener(v ->
                navController.navigate(R.id.action_registerFragment_to_loginFragment));
    }

    private void pickImage() {
        if (checkPermission()) {
            pickImageLauncher.launch("image/*");
        } else {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    private final ActivityResultLauncher<String> pickImageLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.GetContent(),
                    result -> {
                        if (result != null) {
                            binding.imageViewRegister.setImageURI(result);
                            binding.textViewPickImage.setVisibility(View.GONE);
                            currentImageUri = result;
                        }
                    }
            );

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.RequestPermission(),
                    result -> {
                        if (result) {
                            pickImageLauncher.launch("image/*");
                        } else {
                            showToast("READ EXTERNAL STORAGE PERMISSION DENIED!");
                        }
                    }
            );

    private boolean checkPermission() {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
                getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    private void register() {
        getUserData();
        if (isImagePicked() && isUserDataNotEmpty() && checkPassword()) {
            clearHelperTexts();
            progressDialog.show();
            progressDialog.setMessage("signing up...");
            loginRegisterViewModel.register(name, email, password, currentImageUri);
        }
    }

    private boolean isImagePicked() {
        if (currentImageUri == null) {
            showToast("Pick some profile image");
            return false;
        } else {
            return true;
        }
    }

    private boolean isUserDataNotEmpty() {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            setHelperTexts();
            showToast("Please, enter all fields");
            return false;
        } else {
            return true;
        }
    }

    private boolean checkPassword() {
        if (!password.equals(confirmPassword)) {
            binding.textInputLayoutRegisterPassword.setHelperText("Password do not match");
            binding.textInputLayoutRegisterConfirmPassword.setHelperText("Password do not match");
            binding.textInputEditTextRegisterPassword.getText().clear();
            binding.textInputEditTextRegisterConfirmPassword.getText().clear();
            binding.textInputEditTextRegisterPassword.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private void getUserData() {
        name = binding.textInputEditTextRegisterName.getText().toString().trim();
        email = binding.textInputEditTextRegisterEmail.getText().toString().trim();
        password = binding.textInputEditTextRegisterPassword.getText().toString().trim();
        confirmPassword = binding.textInputEditTextRegisterConfirmPassword.getText().toString().trim();
    }

    private void clearHelperTexts() {
        binding.textInputLayoutRegisterName.setHelperText("");
        binding.textInputLayoutRegisterEmail.setHelperText("");
        binding.textInputLayoutRegisterPassword.setHelperText("");
        binding.textInputLayoutRegisterConfirmPassword.setHelperText("");
    }

    private void setHelperTexts() {
        binding.textInputLayoutRegisterName.setHelperText("Required");
        binding.textInputLayoutRegisterEmail.setHelperText("Required");
        binding.textInputLayoutRegisterPassword.setHelperText("Required");
        binding.textInputLayoutRegisterConfirmPassword.setHelperText("Required");
    }

    private void showToast(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}