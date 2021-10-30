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
import com.example.chatonfire.databinding.FragmentProfileBinding;
import com.example.chatonfire.model.User;
import com.example.chatonfire.viewmodel.LoginRegisterViewModel;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    //ui
    private FragmentProfileBinding binding;
    private NavController navController;
    private ProgressDialog progressDialog;

    //viewModel
    private LoginRegisterViewModel loginRegisterViewModel;

    //user
    private User currentUser;
    private String name;
    private String email;
    private String image;
    private Uri imageUri;

    private boolean isUserUpdated;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(getContext());
        loginRegisterViewModel = new ViewModelProvider(this).get(LoginRegisterViewModel.class);
        loginRegisterViewModel.getCurrentUser().observe(this, user -> {
            currentUser = user;
            if (isUserUpdated) {
                progressDialog.dismiss();
                navController.navigate(R.id.action_profileFragment_to_recentConversationsFragment);
            } else {
                setUi(user);
            }
        });
    }

    private void setUi(User currentUser) {
        Picasso.get().load(currentUser.getUser_image()).into(binding.imageViewProfilePicture);
        binding.textInputEditTextUserName.setText(currentUser.getUser_name());
        binding.textInputEditTextUserEmail.setText(currentUser.getUser_email());
        isImageProgressBarVisible(false);

    }

    private void isImageProgressBarVisible(boolean b) {
        if (b) {
            binding.imageViewProfilePicture.setVisibility(View.GONE);
            binding.progressBarProfile.setVisibility(View.VISIBLE);
        } else {
            binding.imageViewProfilePicture.setVisibility(View.VISIBLE);
            binding.progressBarProfile.setVisibility(View.GONE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        isImageProgressBarVisible(true);
        setListeners();
    }

    private void setListeners() {
        binding.buttonProfileSaveChanges.setOnClickListener(v -> updateUser());
        binding.buttonProfilePickPicture.setOnClickListener(v -> pickImage());
        binding.imageViewViewProfileBack.setOnClickListener(v ->
                navController.navigate(R.id.action_profileFragment_to_recentConversationsFragment));
    }

    private void updateUser() {
        getUserData();
        if (isNotEmpty()) {
            progressDialog.show();
            progressDialog.setMessage("updating user...");
            currentUser.setUser_name(name);
            currentUser.setUser_email(email);
            if (imageUri != null) {
                loginRegisterViewModel.updateUserWithUri(currentUser, imageUri);
            } else {
                currentUser.setUser_image(image);
                loginRegisterViewModel.updateUser(currentUser);
            }
        }
        isUserUpdated = true;
    }

    private void getUserData() {
        name = binding.textInputEditTextUserName.getText().toString().trim();
        email = binding.textInputEditTextUserEmail.getText().toString().trim();
        if (imageUri == null) {
            image = currentUser.getUser_image();
        }
    }

    private boolean isNotEmpty() {
        if (name.isEmpty() || email.isEmpty()) {
            showToast("Please, enter all fields");
            return false;
        } else {
            return true;
        }
    }


    private boolean checkPermission() {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
                getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    private void pickImage() {
        if (checkPermission()) {
            goToGallery();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    goToGallery();
                } else {
                    showToast("READ EXTERNAL STORAGE PERMISSION DENIED!");
                }
            });

    private void goToGallery() {
        chooseImageLauncher.launch("image/*");
    }

    private final ActivityResultLauncher<String> chooseImageLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            result -> {
                if (result != null) {
                    binding.imageViewProfilePicture.setImageURI(result);
                    imageUri = result;
                }
            }
    );


    private void showToast(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStop() {
        super.onStop();
        isUserUpdated = false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}