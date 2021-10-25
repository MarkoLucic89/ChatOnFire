package com.example.chatonfire.adapters;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatonfire.databinding.ListItemRecievedMessageBinding;
import com.example.chatonfire.databinding.ListItemSentMessageBinding;
import com.example.chatonfire.model.ChatMessage;
import com.example.chatonfire.model.User;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;

    private List<ChatMessage> chatMessages = new ArrayList<>();
    private final User receiver;
    private final User sender;

    public ChatAdapter(User receiver, User sender) {
        this.receiver = receiver;
        this.sender = sender;
    }

    public void setChatMessages(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT) {
            return new SentMessageViewHolder(
                    ListItemSentMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        } else {
            return new ReceivedMessageViewHolder(
                    ListItemRecievedMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_SENT) {
            ((SentMessageViewHolder) holder).setData(chatMessages.get(position));
        } else {
            ((ReceivedMessageViewHolder) holder).setData(chatMessages.get(position), receiver.getUser_image());
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (chatMessages.get(position).getSender_id().equals(sender.getUser_id())) {
            return VIEW_TYPE_SENT;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    static class SentMessageViewHolder extends RecyclerView.ViewHolder {
        private final ListItemSentMessageBinding binding;

        public SentMessageViewHolder(ListItemSentMessageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void setData(ChatMessage chatMessage) {
            binding.textViewItemSentMessage.setText(chatMessage.getMessage());
            binding.textViewItemSentMessageDateTime.setText(formatDateTime(chatMessage.getDate_object()));
        }

        private String formatDateTime(Date date_object) {
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MMMM.yyyy. HH:mm", Locale.getDefault());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy, HH:mm", Locale.getDefault());
            return simpleDateFormat.format(date_object);
        }
    }

    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        private final ListItemRecievedMessageBinding binding;

        public ReceivedMessageViewHolder(ListItemRecievedMessageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void setData(ChatMessage chatMessage, String receiverProfilePicture) {
            binding.textViewItemReceivedMessage.setText(chatMessage.getMessage());
            binding.textViewItemReceivedMessageDateTime.setText(formatDateTime(chatMessage.getDate_object()));
            Picasso.get().load(receiverProfilePicture).into(binding.imageViewItemReceivedMessage);
        }

        private String formatDateTime(Date date_object) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MMMM.yyyy. HH:mm", Locale.getDefault());
            return simpleDateFormat.format(date_object);
        }
    }
}
