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
import com.example.chatonfire.adapters.ChatAdapter;
import com.example.chatonfire.databinding.FragmentChatBinding;
import com.example.chatonfire.dialogs.UserInfoDialogFragment;
import com.example.chatonfire.model.ChatMessage;
import com.example.chatonfire.model.Conversation;
import com.example.chatonfire.model.User;
import com.example.chatonfire.viewmodel.ChatViewModel;

import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ChatFragment extends Fragment {

    //ui
    private FragmentChatBinding binding;
    private NavController navController;
    private ChatAdapter chatAdapter;

    //viewModel
    private ChatViewModel chatViewModel;

    //user
    private User userSender;
    private User userReceiver;

    //chat
    private ChatMessage chatMessage;
    private String senderId;
    private String receiverId;
    private String message;
    private Date dateObject;

    //conversation
    private Conversation currentConversation;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //safe args
        userSender = ChatFragmentArgs.fromBundle(getArguments()).getSenderId();
        userReceiver = ChatFragmentArgs.fromBundle(getArguments()).getReceiverId();

        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);

        chatViewModel.listenMessages(userSender, userReceiver);

        chatViewModel.getChatMessageListMutableLiveData().observe(this, chatMessages -> {
            chatAdapter.setChatMessages(chatMessages);
            progressBarVisible(false);
            binding.recyclerViewChat.scrollToPosition(chatMessages.size() - 1);
            chatAdapter.notifyDataSetChanged();
        });

        chatViewModel.checkConversation(userSender.getUser_id(), userReceiver.getUser_id());
        chatViewModel.getConversationMutableLiveData().observe(this, conversation -> {
            currentConversation = conversation;
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        binding.textViewChatUsername.setText(userReceiver.getUser_name());
        progressBarVisible(true);
        initRecyclerView();
        setListeners();
    }

    private void setListeners() {
        binding.buttonChatSend.setOnClickListener(v -> sendMessage());
        binding.imageViewChatBack.setOnClickListener(v ->
                navController.navigate(R.id.action_chatFragment_to_recentConversationsFragment));
        binding.imageViewChatInfo.setOnClickListener(v -> {
            UserInfoDialogFragment userInfoDialogFragment = new UserInfoDialogFragment(userReceiver);
            userInfoDialogFragment.show(getChildFragmentManager(), null);
        });
    }


    private void sendMessage() {
        if (getMessageData()) {
            chatViewModel.addMessage(chatMessage);
            binding.editTextChatMessage.getText().clear();
            if (currentConversation == null) {
                addConversation();
            } else {
                updateConversation();
            }
        }
    }


    private void addConversation() {
        currentConversation = new Conversation(
                userSender.getUser_id(),
                userReceiver.getUser_id(),
                message,
                dateObject
        );
        chatViewModel.addConversation(currentConversation);
    }

    private void updateConversation() {
        currentConversation.setSender_id(userSender.getUser_id());
        currentConversation.setReceiver_id(userReceiver.getUser_id());
        currentConversation.setLast_message(message);
        currentConversation.setLast_date(dateObject);
        chatViewModel.updateConversation(currentConversation);
    }

    private boolean getMessageData() {
        senderId = userSender.getUser_id();
        receiverId = userReceiver.getUser_id();
        message = binding.editTextChatMessage.getText().toString().trim();
        dateObject = new Date();
        try {
            chatMessage = new ChatMessage(senderId, receiverId, message, dateObject);
        } catch (Exception e) {
            showToast(e.getMessage());
            return false;
        }
        if (message.isEmpty()) {
            showToast("Message text is empty");
            return false;
        } else {
            return true;
        }
    }

    private void progressBarVisible(boolean b) {
        if (b) {
            binding.progressBarChat.setVisibility(View.VISIBLE);
            binding.recyclerViewChat.setVisibility(View.GONE);
        } else {
            binding.progressBarChat.setVisibility(View.GONE);
            binding.recyclerViewChat.setVisibility(View.VISIBLE);
        }
    }

    private void initRecyclerView() {
        chatAdapter = new ChatAdapter(userReceiver, userSender);
        binding.recyclerViewChat.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewChat.setAdapter(chatAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void showToast(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }

}
