package com.example.chatonfire.viewmodel;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.chatonfire.model.Conversation;
import com.example.chatonfire.model.User;
import com.example.chatonfire.repository.ConversationRepository;

import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ConversationViewModel extends AndroidViewModel {

    private ConversationRepository conversationRepository;

    //mutableLiveData
    private MutableLiveData<User> userMutableLiveData;
    private MutableLiveData<User> recentUserMutableLiveData;
    private MutableLiveData<Boolean> isUserSignedIn;
    private MutableLiveData<List<Conversation>> conversationListMutableLiveData;

    public ConversationViewModel(@NonNull Application application) {
        super(application);
        conversationRepository = new ConversationRepository(application);
        userMutableLiveData = conversationRepository.getCurrentUserMutableLiveData();
        recentUserMutableLiveData = conversationRepository.getRecentUserMutableLiveData();
        isUserSignedIn = conversationRepository.getIsUserSignedInMutableLiveData();
        conversationListMutableLiveData = conversationRepository.getConversationListMutableLiveData();
    }

    public MutableLiveData<User> getRecentUserMutableLiveData() {
        return recentUserMutableLiveData;
    }

    public MutableLiveData<List<Conversation>> getConversationListMutableLiveData() {
        return conversationListMutableLiveData;
    }

    public MutableLiveData<User> getCurrentUser() {
        return userMutableLiveData;
    }

    public MutableLiveData<Boolean> getIsUserSignedIn() {
        return isUserSignedIn;
    }

    public void signOut() {
        conversationRepository.signOut();
    }

    public void setStatus(String status) {
        conversationRepository.setStatus(status);
    }

    public void getRecentUser(String recent_user_id) {
        conversationRepository.getRecentUser(recent_user_id);
    }
}
