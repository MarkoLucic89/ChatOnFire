package com.example.chatonfire.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.chatonfire.model.ChatMessage;
import com.example.chatonfire.model.User;
import com.example.chatonfire.repository.ChatRepository;

import java.util.List;

public class ChatViewModel extends AndroidViewModel {

    private Application application;
    private ChatRepository chatRepository;
    private User userSender;
    private User userReceiver;

    //mutableLiveData
    private MutableLiveData<User> userSenderMutableLiveData;
    private MutableLiveData<List<ChatMessage>> chatMessageListMutableLiveData;

    public ChatViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        chatRepository = new ChatRepository(application);
        userSenderMutableLiveData = chatRepository.getUserSenderMutableLiveData();
        chatMessageListMutableLiveData = chatRepository.getChatMessageListMutableLiveData();
    }

    public MutableLiveData<User> getUserSenderMutableLiveData() {
        return userSenderMutableLiveData;
    }

    public MutableLiveData<List<ChatMessage>> getChatMessageListMutableLiveData() {
        return chatMessageListMutableLiveData;
    }

    public void loadUserSender(String user_id) {
        chatRepository.loadUserSender(user_id);
    }

    public void listenMessages(User userSender, User userReceiver) {
        chatRepository.listenMessages(userSender, userReceiver);
    }

    public void addMessage(ChatMessage chatMessage) {
        chatRepository.addMessage(chatMessage);
    }
}
