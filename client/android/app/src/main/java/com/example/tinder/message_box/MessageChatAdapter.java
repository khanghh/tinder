package com.example.tinder.message_box;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.model.User;
import com.example.rest.RetrofitClient;
import com.example.rest.service.MessageService;
import com.example.tinder.R;
import com.example.model.Message;
import com.example.tinder.authentication.UserAuth;

import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageChatAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_MESSAGE_SENT = UserAuth.getInstance().getUser().getId();
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private static final DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private Context mContext;

    private ArrayList<Message> mMessageList;

    public MessageChatAdapter() {
        // empty constructor
        this.mMessageList = new ArrayList<>();
    }

    public MessageChatAdapter(ArrayList<Message> mMessageList) {
        this.mMessageList = mMessageList;

        this.addMessageList(mMessageList);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = null;
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            v = inflater.inflate(R.layout.item_message_sent, viewGroup, false);
            return new SentMessageHolder(v);
        }
        else {
            v = inflater.inflate(R.layout.item_message_received, viewGroup, false);
            return new ReceivedMessageHolder(v);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (i == 0) {
            // onTopReached
            Log.e("onTopReached", "Reach the last message in the view.");
        }
        Message message = mMessageList.get(i);
        int user_id = message.getSender_id();
        if (user_id == VIEW_TYPE_MESSAGE_SENT) {
            ((SentMessageHolder) viewHolder).bind(message);
        }
        else {
            ((ReceivedMessageHolder) viewHolder).bind(message);
        }
    }

    public void addMessage(Message message) {
        // add message into dataset of Apdapter
        mMessageList.add(message);

        // notify Dataset changed
        notifyDataSetChanged();
    }

    public void addMessageList(ArrayList<Message> messageArrayList) {
        this.mMessageList.addAll(messageArrayList);
        notifyDataSetChanged();
    }

    public void updateStatusMessage(int position, Boolean is_received) {
        Message message = mMessageList.get(position);
        message.setIs_received(is_received);

        notifyDataSetChanged();
    }

    public void setMessageList(ArrayList<Message> mMessageList) {
        this.mMessageList = mMessageList;
    }

    public ArrayList<Message> getMessageList() {
        return mMessageList;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = mMessageList.get(position);
        int user_id = message.getSender_id();
        if (user_id == VIEW_TYPE_MESSAGE_SENT) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @Override
    public int getItemCount() {
        return this.mMessageList.size();
    }


    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView messageTime;

        SentMessageHolder(View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.text_message_body);
            messageTime  = itemView.findViewById(R.id.sent_message_time);
        }

        void bind(Message message) {

            messageText.setText(message.getMessage());
            Date created_at = message.getCreated_at();
            String created_at_str = sdf.format(created_at);
            messageTime.setText(created_at_str);
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView messageTime;
        TextView nameText;

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            messageTime  = itemView.findViewById(R.id.received_message_time);
            messageText = itemView.findViewById(R.id.text_message_body);
            nameText = itemView.findViewById(R.id.text_message_name);
        }

        void bind(Message message) {
            messageText.setText(message.getMessage());
            Date created_at = message.getCreated_at();
            String created_at_str = sdf.format(created_at);
            messageTime.setText(created_at_str);
            nameText.setText(message.getConversation_name());
        }
    }

}
