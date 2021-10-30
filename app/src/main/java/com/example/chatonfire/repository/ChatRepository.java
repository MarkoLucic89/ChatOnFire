package com.example.chatonfire.repository;

import android.app.Application;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;

import com.example.chatonfire.model.ChatMessage;
import com.example.chatonfire.model.Conversation;
import com.example.chatonfire.model.User;
import com.example.chatonfire.utility.Constants;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ChatRepository {

    private Application application;

    //firebase
    private FirebaseFirestore firebaseFirestore;

    //chat
    private ChatMessage chatMessage;
    private List<ChatMessage> chatMessages;
    private MutableLiveData<List<ChatMessage>> chatMessageListMutableLiveData;

    //conversation
    private Conversation conversation;

    //mutableLiveData
    private MutableLiveData<User> userSenderMutableLiveData;
    private MutableLiveData<Conversation> conversationMutableLiveData;


    public ChatRepository(Application application) {
        this.application = application;
        firebaseFirestore = FirebaseFirestore.getInstance();
        userSenderMutableLiveData = new MutableLiveData<>();
        chatMessageListMutableLiveData = new MutableLiveData<>();
        conversationMutableLiveData = new MutableLiveData<>();
        chatMessages = new ArrayList<>();
    }

    public MutableLiveData<Conversation> getConversationMutableLiveData() {
        return conversationMutableLiveData;
    }

    public MutableLiveData<User> getUserSenderMutableLiveData() {
        return userSenderMutableLiveData;
    }

    public MutableLiveData<List<ChatMessage>> getChatMessageListMutableLiveData() {
        return chatMessageListMutableLiveData;
    }

    public void loadUserSender(String user_id) {
        firebaseFirestore
                .collection(Constants.KEY_FIRESTORE_COLLECTION_USERS)
                .document(user_id)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    userSenderMutableLiveData.postValue(documentSnapshot.toObject(User.class));
                });
    }

    public void listenMessages(User userSender, User userReceiver) {
        firebaseFirestore
                .collection(Constants.KEY_FIRESTORE_COLLECTION_CHAT_MESSAGES)
                .whereEqualTo(Constants.KEY_SENDER_ID, userSender.getUser_id())
                .whereEqualTo(Constants.KEY_RECEIVER_ID, userReceiver.getUser_id())
                .addSnapshotListener(chatListener);

        firebaseFirestore
                .collection(Constants.KEY_FIRESTORE_COLLECTION_CHAT_MESSAGES)
                .whereEqualTo(Constants.KEY_SENDER_ID, userReceiver.getUser_id())
                .whereEqualTo(Constants.KEY_RECEIVER_ID, userSender.getUser_id())
                .addSnapshotListener(chatListener);
    }

    private final EventListener<QuerySnapshot> chatListener = (value, error) -> {
        if (error != null) {
            showToast(error.getMessage());
            return;
        }
        if (value != null) {
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    chatMessage = documentChange.getDocument().toObject(ChatMessage.class);
                    chatMessages.add(chatMessage);
                    chatMessages.sort(Comparator.comparing(ChatMessage::getDate_object));
                }
            }
            chatMessageListMutableLiveData.postValue(chatMessages);
        }
    };

    private void showToast(String message) {
        Toast.makeText(application, message, Toast.LENGTH_SHORT).show();
    }


    public void addMessage(ChatMessage chatMessage) {
        firebaseFirestore
                .collection(Constants.KEY_FIRESTORE_COLLECTION_CHAT_MESSAGES)
                .add(chatMessage)
                .addOnFailureListener(e -> showToast(e.getMessage()));
    }

    public void addConversation(Conversation conversation) {
        firebaseFirestore
                .collection(Constants.KEY_FIRESTORE_COLLECTION_CONVERSATIONS)
                .add(conversation);
    }

    public void updateConversation(Conversation conversation) {
        firebaseFirestore
                .collection(Constants.KEY_FIRESTORE_COLLECTION_CONVERSATIONS)
                .document(conversation.getId())
                .set(conversation);
    }

    public void checkConversation(String senderId, String receiverId) {
        checkConversationMethod(senderId, receiverId);
        checkConversationMethod(receiverId, senderId);
    }

    private void checkConversationMethod(String senderId, String receiverId) {
        firebaseFirestore
                .collection(Constants.KEY_FIRESTORE_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_SENDER_ID, senderId)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, receiverId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        conversation = queryDocumentSnapshots.getDocuments().get(0).toObject(Conversation.class);
                        conversationMutableLiveData.postValue(conversation);
                    }
                });
    }
}
