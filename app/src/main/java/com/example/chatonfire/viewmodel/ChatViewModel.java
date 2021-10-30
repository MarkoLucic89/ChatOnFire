package com.example.chatonfire.viewmodel;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.chatonfire.model.ChatMessage;
import com.example.chatonfire.model.Conversation;
import com.example.chatonfire.model.User;
import com.example.chatonfire.repository.ChatRepository;

import java.util.Date;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ChatViewModel extends AndroidViewModel {

    private ChatRepository chatRepository;

    //mutableLiveData
    private MutableLiveData<User> userSenderMutableLiveData;
    private MutableLiveData<List<ChatMessage>> chatMessageListMutableLiveData;
    private MutableLiveData<Conversation> conversationMutableLiveData;


    public ChatViewModel(@NonNull Application application) {
        super(application);
        chatRepository = new ChatRepository(application);
        userSenderMutableLiveData = chatRepository.getUserSenderMutableLiveData();
        chatMessageListMutableLiveData = chatRepository.getChatMessageListMutableLiveData();
        conversationMutableLiveData = chatRepository.getConversationMutableLiveData();
    }

    public MutableLiveData<Conversation> getConversationMutableLiveData() {
        return conversationMutableLiveData;
    }

    public MutableLiveData<List<ChatMessage>> getChatMessageListMutableLiveData() {
        return chatMessageListMutableLiveData;
    }

    public void listenMessages(User userSender, User userReceiver) {
        chatRepository.listenMessages(userSender, userReceiver);
    }

    public void addMessage(ChatMessage chatMessage) {
        chatRepository.addMessage(chatMessage);
    }

    //conversation methods

    public void addConversation(Conversation conversation) {
        chatRepository.addConversation(conversation);
    }

    public void updateConversation(Conversation conversation) {
        chatRepository.updateConversation(conversation);
    }

    public void checkConversation(String senderId, String receiverId) {
        chatRepository.checkConversation(senderId, receiverId);
    }


}
