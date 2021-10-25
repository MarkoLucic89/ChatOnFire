package com.example.chatonfire.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.chatonfire.R;
import com.example.chatonfire.adapters.UserAdapter;
import com.example.chatonfire.databinding.FragmentUsersBinding;
import com.example.chatonfire.utility.Constants;
import com.example.chatonfire.viewmodel.LoginRegisterViewModel;
import com.example.chatonfire.viewmodel.UserListViewModel;
import com.example.chatonfire.model.User;
import com.google.firebase.auth.FirebaseAuth;

public class UsersFragment extends Fragment implements UserAdapter.OnUserClickListener {

    //ui
    private FragmentUsersBinding binding;
    private NavController navController;
    private UserAdapter userAdapter;

    //viewModel
    private UserListViewModel userListViewModel;
    private LoginRegisterViewModel loginRegisterViewModel;

    //user
    private User currentUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginRegisterViewModel = new ViewModelProvider(this).get(LoginRegisterViewModel.class);
        loginRegisterViewModel.getIsUserSignedIn().observe(this, aBoolean -> {
            if (!aBoolean) {
                navController.navigate(R.id.action_usersFragment_to_loginFragment);
            }
        });

        userListViewModel = new ViewModelProvider(this).get(UserListViewModel.class);
        userListViewModel.getCurrentUserMutableLiveData().observe(this, user -> {
            currentUser = user;
            binding.textViewUsersContacts.setText(currentUser.getUser_name());
        });
        userListViewModel.getMutableLiveData().observe(this, users -> {
            userAdapter.setUsers(users);
            userAdapter.notifyDataSetChanged();
            isProgressBarVisible(false);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUsersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        isProgressBarVisible(true);
        initRecyclerView();
        setListeners();
    }


    private void isProgressBarVisible(boolean visible) {
        if (visible) {
            binding.progressBarUsers.setVisibility(View.VISIBLE);
            binding.recyclerViewUsers.setVisibility(View.INVISIBLE);
        } else {
            binding.progressBarUsers.setVisibility(View.INVISIBLE);
            binding.recyclerViewUsers.setVisibility(View.VISIBLE);
        }
    }

    private void initRecyclerView() {
        userAdapter = new UserAdapter(this);
        binding.recyclerViewUsers.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewUsers.setAdapter(userAdapter);
    }


    private void setListeners() {
        binding.imageViewUsersLogout.setOnClickListener(v -> logOut());
        binding.imageViewUsersEditUser.setOnClickListener(v ->
                navController.navigate(R.id.action_usersFragment_to_profileFragment));
    }

    private void logOut() {
        loginRegisterViewModel.signOut();
    }

    @Override
    public void onUserClick(User user) {
        UsersFragmentDirections.ActionUsersFragmentToChatFragment usersFragmentToChatFragment =
                UsersFragmentDirections.actionUsersFragmentToChatFragment(user, currentUser);
        usersFragmentToChatFragment.setReceiverId(user);
        usersFragmentToChatFragment.setSenderId(currentUser);
        navController.navigate(usersFragmentToChatFragment);
    }

    private void showToast(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        loginRegisterViewModel.setStatus(Constants.STATUS_ONLINE);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        loginRegisterViewModel.setStatus(Constants.STATUS_OFFLINE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}