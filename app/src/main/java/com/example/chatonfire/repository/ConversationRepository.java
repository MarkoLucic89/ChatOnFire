package com.example.chatonfire.repository;

import android.app.Application;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;

import com.example.chatonfire.model.Conversation;
import com.example.chatonfire.model.User;
import com.example.chatonfire.utility.Constants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ConversationRepository {

    //mutableLiveData
    private MutableLiveData<List<Conversation>> conversationListMutableLiveData;
    private MutableLiveData<User> currentUserMutableLiveData;
    private MutableLiveData<User> recentUserMutableLiveData;
    private MutableLiveData<Boolean> isUserSignedInMutableLiveData;
    private MutableLiveData<Boolean> isUserSignedUpMutableLiveData;

    //firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    //users
    private User currentUser;

    //conversations
    private List<Conversation> conversations;

    private Application application;

    public ConversationRepository(Application application) {
        this.application = application;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        currentUserMutableLiveData = new MutableLiveData<>();
        recentUserMutableLiveData = new MutableLiveData<>();
        isUserSignedInMutableLiveData = new MutableLiveData<>();
        isUserSignedUpMutableLiveData = new MutableLiveData<>();
        conversationListMutableLiveData = new MutableLiveData<>();
        if (firebaseAuth.getCurrentUser() != null) {
            getCurrentUser();
        }
        conversations = new ArrayList<>();
//        loadConversations();
        getRecentConversation();
    }

    public MutableLiveData<User> getRecentUserMutableLiveData() {
        return recentUserMutableLiveData;
    }

    public MutableLiveData<List<Conversation>> getConversationListMutableLiveData() {
        return conversationListMutableLiveData;
    }

    public void getCurrentUser() {
        firebaseFirestore
                .collection(Constants.KEY_FIRESTORE_COLLECTION_USERS)
                .document(firebaseAuth.getCurrentUser().getUid())
                .addSnapshotListener((value, error) -> {
                    currentUser = value.toObject(User.class);
                    currentUserMutableLiveData.postValue(currentUser);
                });
    }


    public MutableLiveData<User> getCurrentUserMutableLiveData() {
        return currentUserMutableLiveData;
    }

    public MutableLiveData<Boolean> getIsUserSignedInMutableLiveData() {
        return isUserSignedInMutableLiveData;
    }


    public void signOut() {
        firebaseFirestore.collection(Constants.KEY_FIRESTORE_COLLECTION_USERS)
                .document(firebaseAuth.getCurrentUser().getUid())
                .update(Constants.STATUS, Constants.STATUS_OFFLINE)
                .addOnSuccessListener(unused -> {
                    firebaseAuth.signOut();
                    isUserSignedInMutableLiveData.postValue(false);
                })
                .addOnFailureListener(e -> showToast(e.getMessage()));
    }

    private void showToast(String message) {
        Toast.makeText(application, message, Toast.LENGTH_SHORT).show();
    }

    public void setStatus(String status) {
//        firebaseFirestore.collection(Constants.KEY_FIRESTORE_COLLECTION_USERS)
//                .document(firebaseAuth.getCurrentUser().getUid())
//                .get()
//                .addOnSuccessListener(documentSnapshot -> {
//                    User user = documentSnapshot.toObject(User.class);
//                    user.setUser_status(status);
//                    updateUser(user);
//                });

        if (firebaseAuth.getCurrentUser() != null) {
            firebaseFirestore.collection(Constants.KEY_FIRESTORE_COLLECTION_USERS)
                    .document(firebaseAuth.getCurrentUser().getUid())
                    .update(Constants.STATUS, status)
                    .addOnFailureListener(e -> showToast(e.getMessage()));
        }
    }

//    public void loadConversations() {
//        firebaseFirestore
//                .collection(Constants.KEY_FIRESTORE_COLLECTION_CONVERSATIONS)
//                .addSnapshotListener((value, error) ->  {
//                    if (value != null) {
//                        conversations.clear();
//                        for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
//                            Conversation conversation = documentSnapshot.toObject(Conversation.class);
//                            if (firebaseAuth.getCurrentUser().getUid().equals(conversation.getReceiver_id())) {
//                                conversation.setRecent_user_id(conversation.getSender_id());
//                                conversation.setRecent_user_name(conversation.getSender_name());
//                                conversation.setRecent_user_image(conversation.getSender_image());
//                                conversations.add(conversation);
//                            }
//                            if (firebaseAuth.getCurrentUser().getUid().equals(conversation.getSender_id())) {
//                                conversation.setRecent_user_id(conversation.getReceiver_id());
//                                conversation.setRecent_user_name(conversation.getReceiver_name());
//                                conversation.setRecent_user_image(conversation.getReceiver_image());
//                                conversations.add(conversation);
//                            }
//                        }
//                        conversations.sort(Comparator.comparing(Conversation::getLast_date));
//                        conversationListMutableLiveData.postValue(conversations);
//                    }
//        });
//    }

    public void getRecentConversation() {
        firebaseFirestore
                .collection(Constants.KEY_FIRESTORE_COLLECTION_USERS)
                .addSnapshotListener((value, error) -> {
                   List<User> users = value.toObjects(User.class);
                    firebaseFirestore
                            .collection(Constants.KEY_FIRESTORE_COLLECTION_CONVERSATIONS)
                            .addSnapshotListener((value1, error1) ->  {
                                if (value1 != null) {
                                    conversations.clear();
                                    for (DocumentSnapshot documentSnapshot : value1.getDocuments()) {
                                        Conversation conversation = documentSnapshot.toObject(Conversation.class);
                                        if (firebaseAuth.getCurrentUser().getUid().equals(conversation.getReceiver_id())) {
                                            for (User user : users) {
                                                if (user.getUser_id().equals(conversation.getSender_id())) {
                                                    conversation.setRecent_user_id(user.getUser_id());
                                                    conversation.setRecent_user_name(user.getUser_name());
                                                    conversation.setRecent_user_image(user.getUser_image());
                                                    conversations.add(conversation);
                                                }
                                            }
                                        }
                                        if (firebaseAuth.getCurrentUser().getUid().equals(conversation.getSender_id())) {
                                            for (User user : users) {
                                                if (user.getUser_id().equals(conversation.getReceiver_id())) {
                                                    conversation.setRecent_user_id(user.getUser_id());
                                                    conversation.setRecent_user_name(user.getUser_name());
                                                    conversation.setRecent_user_image(user.getUser_image());
                                                    conversations.add(conversation);
                                                }
                                            }
                                        }
                                    }
                                    conversations.sort(Comparator.comparing(Conversation::getLast_date));
                                    conversationListMutableLiveData.postValue(conversations);
                                }
                            });
                });
    }

    public void getRecentUser(String recent_user_id) {
        firebaseFirestore
                .collection(Constants.KEY_FIRESTORE_COLLECTION_USERS)
                .document(recent_user_id)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    recentUserMutableLiveData.postValue(documentSnapshot.toObject(User.class));
                });
    }
}
