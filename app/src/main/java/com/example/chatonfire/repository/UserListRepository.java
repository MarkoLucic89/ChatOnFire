package com.example.chatonfire.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.chatonfire.model.User;
import com.example.chatonfire.utility.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class UserListRepository {

    //livedata
    private MutableLiveData<List<User>> usersMutableLiveData;
    private MutableLiveData<User> currentUserMutableLiveData;

    //firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    //users
    private User currentUser;
    private List<User> users;

    public UserListRepository() {
        usersMutableLiveData = new MutableLiveData<>();
        currentUserMutableLiveData = new MutableLiveData<>();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        users = new ArrayList<>();
        getUsers();
        getCurrentUser();
    }

    public MutableLiveData<List<User>> getUsersMutableLiveData() {
        return usersMutableLiveData;
    }


    public MutableLiveData<User> getCurrentUserMutableLiveData() {
        return currentUserMutableLiveData;
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

    public void getUsers() {
//        firebaseFirestore.collection(Constants.KEY_FIRESTORE_COLLECTION_USERS)
//                .document(firebaseAuth.getCurrentUser().getUid())
//                .addSnapshotListener((value, error) -> {
//                    users.clear();
//                    firebaseFirestore.collection(Constants.KEY_FIRESTORE_COLLECTION_USERS)
//                            .addSnapshotListener((value1, error1) -> {
//                                for (DocumentSnapshot documentSnapshot : value1.getDocuments()) {
//                                    currentUser = documentSnapshot.toObject(User.class);
//                                    if (!firebaseAuth.getCurrentUser().getUid().equals(currentUser.getUser_id())) {
//                                        users.add(currentUser);
//                                    }
//                                }
//                                usersMutableLiveData.postValue(users);
//                            });
//                });

                    firebaseFirestore.collection(Constants.KEY_FIRESTORE_COLLECTION_USERS)
                            .addSnapshotListener((value1, error1) -> {
                                users.clear();
                                for (DocumentSnapshot documentSnapshot : value1.getDocuments()) {
                                    User currentUser = documentSnapshot.toObject(User.class);
                                    if (!firebaseAuth.getCurrentUser().getUid().equals(currentUser.getUser_id())) {
                                        users.add(currentUser);
                                    }
                                }
                                usersMutableLiveData.postValue(users);
                            });
    }

    public void clearUsers() {
        users.clear();
        usersMutableLiveData.postValue(users);
    }

}
