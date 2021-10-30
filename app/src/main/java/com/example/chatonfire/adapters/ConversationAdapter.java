package com.example.chatonfire.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatonfire.databinding.ListItemRecentConversationsBinding;
import com.example.chatonfire.model.Conversation;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder> {

    private final RecentUserListener recentUserListener;
    private List<Conversation> conversations;

    public ConversationAdapter(RecentUserListener recentUserListener) {
        this.recentUserListener = recentUserListener;
    }

    public void setConversations(List<Conversation> conversations) {
        this.conversations = conversations;
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConversationViewHolder(
                ListItemRecentConversationsBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {
        holder.setData(conversations.get(position));
        holder.setListeners(conversations.get(position));
    }

    @Override
    public int getItemCount() {
        if (conversations == null) return 0;
        else return conversations.size();
    }

    public class ConversationViewHolder extends RecyclerView.ViewHolder {

        ListItemRecentConversationsBinding binding;

        public ConversationViewHolder(ListItemRecentConversationsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void setData(Conversation conversation) {
            Picasso.get().load(conversation.getRecent_user_image()).into(binding.imageViewItemRecentConversationProfilePicture);
            binding.textViewItemRecentUserName.setText(conversation.getRecent_user_name());
            binding.textViewItemRecentMessage.setText(conversation.getLast_message());
            binding.textViewItemLastMessageTime.setText(formatDateTime(conversation.getLast_date()));
        }

        private String formatDateTime(Date date_object) {
            Date date = new Date();
            if (date_object.getDate() == date.getDate()) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                return simpleDateFormat.format(date_object);
            } else {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yy.", Locale.getDefault());
                return simpleDateFormat.format(date_object);
            }
        }

        void setListeners(Conversation conversation) {
            binding.getRoot().setOnClickListener(v -> recentUserListener.onRecentUserClickListener(conversation));
        }
    }

    public interface RecentUserListener {
        void onRecentUserClickListener(Conversation conversation);
    }
}
