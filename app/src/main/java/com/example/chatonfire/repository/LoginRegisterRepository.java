package com.example.chatonfire.repository;


import android.app.Application;
import android.net.Uri;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.chatonfire.model.User;
import com.example.chatonfire.utility.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class LoginRegisterRepository {

    private Application application;
    private MutableLiveData<List<User>> usersMutableLiveData;
    private MutableLiveData<User> currentUserMutableLiveData;
    private MutableLiveData<Boolean> isUserSignedInMutableLiveData;
    private MutableLiveData<Boolean> isUserSignedUpMutableLiveData;

    //firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStorage;

    //users
    private User currentUser;

    public LoginRegisterRepository(Application application) {
        this.application = application;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        currentUserMutableLiveData = new MutableLiveData<>();
        isUserSignedInMutableLiveData = new MutableLiveData<>();
        isUserSignedUpMutableLiveData = new MutableLiveData<>();
        usersMutableLiveData = new MutableLiveData<>();
        if (firebaseAuth.getCurrentUser() != null) {
            getCurrentUser();
        }

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

    public MutableLiveData<Boolean> getIsUserSignedUpMutableLiveData() {
        return isUserSignedUpMutableLiveData;
    }

    public void register(String name, String email, String password, Uri imageUri) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    StorageReference imageReference = firebaseStorage
                            .getReference(Constants.KEY_STORAGE_UPLOADS)
                            .child(email);

                    imageReference.putFile(imageUri)
                            .addOnSuccessListener(taskSnapshot -> {
                                imageReference
                                        .getDownloadUrl()
                                        .addOnSuccessListener(uri -> {
                                            String image = uri.toString();
                                            User user = new User(name, email, password, image);
                                            firebaseFirestore
                                                    .collection(Constants.KEY_FIRESTORE_COLLECTION_USERS)
                                                    .document(firebaseAuth.getCurrentUser().getUid())
                                                    .set(user).addOnSuccessListener(unused -> {
                                                isUserSignedUpMutableLiveData.postValue(true);
                                            }).addOnFailureListener(e -> {
                                                showToast(e.getMessage());
                                                isUserSignedUpMutableLiveData.postValue(false);
                                            });
                                        }).addOnFailureListener(e -> showToast(e.getMessage()));

                            }).addOnFailureListener(e -> showToast(e.getMessage()));
                }).addOnFailureListener(e -> {
            showToast(e.getMessage());
        });
    }

    public void login(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> isUserSignedInMutableLiveData.postValue(true))
                .addOnFailureListener(e -> isUserSignedInMutableLiveData.postValue(false));
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

    public void updateUser(User currentUser) {
        firebaseFirestore
                .collection(Constants.KEY_FIRESTORE_COLLECTION_USERS)
                .document(currentUser.getUser_id())
                .set(currentUser)
                .addOnFailureListener(e -> showToast(e.getMessage()));
    }

    public void updateUserWithUri(User currentUser, Uri imageUri) {
        StorageReference profileImageReference = firebaseStorage
                .getReference(Constants.KEY_STORAGE_UPLOADS)
                .child(currentUser.getUser_email());

        profileImageReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    profileImageReference.getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                currentUser.setUser_image(uri.toString());
                                updateUser(currentUser);
                            })
                            .addOnFailureListener(e -> showToast(e.getMessage()));
                })
                .addOnFailureListener(e -> showToast(e.getMessage()));
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

}
