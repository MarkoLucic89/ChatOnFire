package com.example.chatonfire.viewmodel;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.chatonfire.model.User;
import com.example.chatonfire.repository.LoginRegisterRepository;
import com.google.firebase.auth.FirebaseUser;

public class LoginRegisterViewModel extends AndroidViewModel {

    private LoginRegisterRepository loginRegisterRepository;
    private MutableLiveData<User> userMutableLiveData;
    private MutableLiveData<Boolean> isUserSignedIn;
    private MutableLiveData<Boolean> isUserSignedUp;

    public LoginRegisterViewModel(@NonNull Application application) {
        super(application);
        loginRegisterRepository = new LoginRegisterRepository(application);
        userMutableLiveData = loginRegisterRepository.getCurrentUserMutableLiveData();
        isUserSignedIn = loginRegisterRepository.getIsUserSignedInMutableLiveData();
        isUserSignedUp = loginRegisterRepository.getIsUserSignedUpMutableLiveData();
    }

    public MutableLiveData<User> getCurrentUser() {
        return userMutableLiveData;
    }

    public MutableLiveData<Boolean> getIsUserSignedIn() {
        return isUserSignedIn;
    }

    public MutableLiveData<Boolean> getIsUserSignedUp() {
        return isUserSignedUp;
    }

    public void register(String name, String email, String password, Uri uri) {
        loginRegisterRepository.register(name, email, password, uri);
    }

    public void login(String email, String password) {
        loginRegisterRepository.login(email, password);
    }

    public void updateUser(User currentUser) {
        loginRegisterRepository.updateUser(currentUser);
    }

    public void updateUserWithUri(User currentUser, Uri imageUri) {
        loginRegisterRepository.updateUserWithUri(currentUser, imageUri);
    }
}
