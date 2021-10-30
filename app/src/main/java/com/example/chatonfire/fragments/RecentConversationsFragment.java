package com.example.chatonfire.fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
import com.example.chatonfire.adapters.ConversationAdapter;
import com.example.chatonfire.databinding.FragmentRecentConversationsBinding;
import com.example.chatonfire.dialogs.LogoutDialogFragment;
import com.example.chatonfire.model.Conversation;
import com.example.chatonfire.model.User;
import com.example.chatonfire.utility.Constants;
import com.example.chatonfire.viewmodel.ConversationViewModel;
import com.squareup.picasso.Picasso;

@RequiresApi(api = Build.VERSION_CODES.N)
public class RecentConversationsFragment extends Fragment implements ConversationAdapter.RecentUserListener, LogoutDialogFragment.OnLogoutListener {

    private FragmentRecentConversationsBinding binding;
    private NavController navController;

    //viewModel
    private ConversationViewModel conversationViewModel;

    //user
    private User currentUser;

    //recyclerView
    private ConversationAdapter conversationAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        conversationViewModel = new ViewModelProvider(this).get(ConversationViewModel.class);
        conversationViewModel.getCurrentUser().observe(this, user -> {
            if (user != null) {
                setUi(user);
                currentUser = user;
            }
        });

        conversationViewModel.getIsUserSignedIn().observe(this, aBoolean -> {
            if (!aBoolean) {
                navController.navigate(R.id.action_recentConversationsFragment_to_loginFragment);
            }
        });

        conversationViewModel.getConversationListMutableLiveData().observe(this, conversations -> {
            conversationAdapter.setConversations(conversations);
            conversationAdapter.notifyDataSetChanged();
        });

        conversationViewModel.getRecentUserMutableLiveData().observe(this, user -> {
            RecentConversationsFragmentDirections
                    .ActionRecentConversationsFragmentToChatFragment recentConversationsFragmentToChatFragment =
                    RecentConversationsFragmentDirections.actionRecentConversationsFragmentToChatFragment(
                            currentUser,
                            user
                    );
            navController.navigate(recentConversationsFragmentToChatFragment);
        });
    }

    private void setUi(User user) {
        Picasso.get().load(user.getUser_image()).into(binding.imageViewToolbarRecentConversations);
        binding.textViewToolbarRecentConversations.setText(user.getUser_name());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRecentConversationsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        initRecyclerView();
        setListeners();
    }

    private void initRecyclerView() {
        conversationAdapter = new ConversationAdapter(this);
        binding.recyclerViewRecentConversations.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewRecentConversations.setAdapter(conversationAdapter);
    }

    private void setListeners() {
        binding.buttonRecentConversationsViewUsers.setOnClickListener(v ->
                navController.navigate(R.id.action_recentConversationsFragment_to_usersFragment));
        binding.imageViewRecentConversationsEditProfile.setOnClickListener(v ->
                navController.navigate(R.id.action_recentConversationsFragment_to_profileFragment));
        binding.imageViewRecentConversationsLogout.setOnClickListener(v -> {
            LogoutDialogFragment logoutDialogFragment = new LogoutDialogFragment(this);
            logoutDialogFragment.show(getChildFragmentManager(), null);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        conversationViewModel.setStatus(Constants.STATUS_ONLINE);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        conversationViewModel.setStatus(Constants.STATUS_OFFLINE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onRecentUserClickListener(Conversation conversation) {
        conversationViewModel.getRecentUser(conversation.getRecent_user_id());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        conversationViewModel.getConversationListMutableLiveData().observe(getViewLifecycleOwner(), conversations -> {
            conversationAdapter.setConversations(conversations);
            conversationAdapter.notifyDataSetChanged();
        });

        conversationViewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                setUi(user);
                currentUser = user;
            }
        });
    }

    @Override
    public void onLogOut() {
        conversationViewModel.signOut();
    }
}