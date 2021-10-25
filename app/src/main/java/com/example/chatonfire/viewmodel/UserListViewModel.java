package com.example.chatonfire.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.chatonfire.model.User;
import com.example.chatonfire.repository.UserListRepository;

import java.util.List;

public class UserListViewModel extends AndroidViewModel {

    private UserListRepository userListRepository;

    private MutableLiveData<List<User>> mutableLiveData;
    private MutableLiveData<User> currentUserMutableLiveData;

    public UserListViewModel(@NonNull Application application) {
        super(application);
        userListRepository = new UserListRepository();
        mutableLiveData = userListRepository.getUsersMutableLiveData();
        currentUserMutableLiveData = userListRepository.getCurrentUserMutableLiveData();
    }

    public MutableLiveData<User> getCurrentUserMutableLiveData() {
        return currentUserMutableLiveData;
    }

    public MutableLiveData<List<User>> getMutableLiveData() {
        return mutableLiveData;
    }

    public void clearUsers() {
        userListRepository.clearUsers();
    }

}
