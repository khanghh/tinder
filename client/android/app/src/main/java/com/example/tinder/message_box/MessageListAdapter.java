package com.example.tinder.message_box;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.model.Conversation;
import com.example.tinder.R;
import com.example.tinder.authentication.UserAuth;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageHolder> {

    private ArrayList<Conversation> conversations;

    private static OnItemClickListener listener = null;

    public MessageListAdapter(ArrayList<Conversation> conversations) {
        this.conversations = conversations;
    }

    public MessageListAdapter(Conversation new_conversation) {
        this.conversations.add(new_conversation);
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_item, viewGroup, false);
        return new MessageHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageHolder messageHolder, int i) {
        Conversation curr_conversation = conversations.get(i);
        messageHolder.bind(curr_conversation.getConversation_name());

        // add item click event
        messageHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MessageListAdapter.listener != null) {
                    MessageListAdapter.listener.onClick(messageHolder.view, messageHolder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.conversations.size();
    }



    @Override
    public long getItemId(int position) {
        return this.conversations.get(position).getId();
    }

    public Conversation getItemByPosition(int position) {
        return this.conversations.get(position);
    }

    public static class MessageHolder extends RecyclerView.ViewHolder {

        private View view;
        private TextView lasted_msg;
        private TextView user_name;
        private CircleImageView user_image;

        public View getView() {
            return view;
        }

        public MessageHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            lasted_msg = itemView.findViewById(R.id.lasted_msg_txtView);
            user_name = itemView.findViewById(R.id.user_name_txtView);
            user_image = itemView.findViewById(R.id.user_circelImageView);
        }

        void bind(String name) {
            user_name.setText(name);
        }

        public void setLastedMessage(String message) {
            lasted_msg.setText(message);
        }

        public void setUsername(String username) {
            user_name.setText(username);
        }

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        MessageListAdapter.listener = listener;
    }

    public interface OnItemClickListener {
        void onClick(View v, int position);
    }

    public void updateConversation(int conversation_id, String message) {
        int position = getPositionByConversationID(conversation_id);
        this.conversations.get(position).setLasted_message(message);
        notifyDataSetChanged();
    }

    public int getPositionByConversationID(int conversation_id) {
        int position = -1;
        for (int i = 0; i < getItemCount(); i++) {
            if (conversations.get(i).getId() == conversation_id) {
                position = i;
                break;
            }
        }
        return position;
    }

    public void setConversations(ArrayList<Conversation> new_conversations) {
        this.conversations = new_conversations;
    }

}
