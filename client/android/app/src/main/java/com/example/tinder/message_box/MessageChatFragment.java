package com.example.tinder.message_box;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.model.Message;
import com.example.rest.RetrofitClient;
import com.example.rest.service.MessageService;
import com.example.tinder.R;
import com.example.internet_connection.SocketIO;
import com.example.tinder.authentication.UserAuth;
import com.github.nkzawa.emitter.Emitter;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.navigation.NavType;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MessageChatFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MessageChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessageChatFragment extends Fragment {
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private MessageChatAdapter messageChatAdapter;
    private Button sendButton;
    private EditText messageText;
    private OnFragmentInteractionListener mListener;
    private String CHANNEL_ID = "notification_recevied_message";
    private static SocketIO mSocket;
    private Bundle mBundle;
    private int conversation_id;
    private String conversation_name;
    private ArrayList<Message> historical_messages;
    private int last_base_time = -1;

    public MessageChatFragment() {
        // Required empty public constructor
    }

    public MessageChatFragment(Bundle saved_bundle) {
        this.mBundle = saved_bundle;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init socket
        mSocket = UserAuth.getInstance().getSocketIO();

        // listen new message
        mSocket.getInstance().on("chat_message", onNewMessage).on("chat_message_result", onSentMessageSuccess);

        // get data from the previous fragment
        mBundle = getArguments();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_message_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sendButton = view.findViewById(R.id.button_chatbox_send);

        messageText = view.findViewById(R.id.edittext_chatbox);

        recyclerView = view.findViewById(R.id.reyclerview_message_list);

        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        conversation_id = (int) mBundle.getSerializable("conversation_id");
        conversation_name = (String) mBundle.getSerializable("member_name");

        // get historical message
        historical_messages = loadMessages();

        messageChatAdapter = new MessageChatAdapter(historical_messages);
        recyclerView.setAdapter(messageChatAdapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = messageText.getText().toString().trim();
                int USER_ID = UserAuth.getInstance().getUser().getId();
                Log.i("conversation_id", Integer.toString(conversation_id));
                if (msg.length() > 0) {
                    addMessage(USER_ID, conversation_id, msg);
                    // create data object
                    JSONObject message = new JSONObject();
                    try {

                        message.put("sender_id", USER_ID);
                        message.put("conversation_id", conversation_id);
                        message.put("message", msg);
                    } catch (JSONException e) {
                        Log.e("JSOn exception", e.toString());
                    }

                    mSocket.push_data(message, "chat_message");
                }
                messageText.getText().clear();

                // set the focus on the last element
                int last_position = messageChatAdapter.getItemCount() - 1;
                if (last_position > 0) {
                    recyclerView.smoothScrollToPosition(last_position);
                }
            }
        });

    }

    private void addMessage(Integer user_id, Integer conversation_id, String message) {
        Message new_message = new Message(user_id, conversation_id, message, conversation_name);
        messageChatAdapter.addMessage(new_message);
    }


    // receving a new message
    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        // get information from received message
                        Integer sender_id;
                        Integer conversation_id;
                        String message;
                        try {
                            JSONObject data = new JSONObject(args[0].toString());
                            sender_id = data.getInt("sender_id");
                            conversation_id = data.getInt("conversation_id");
                            message = data.getString("message");
                        } catch (JSONException e) {
                            return;
                        }

                        // create notification
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), CHANNEL_ID)
                                .setSmallIcon(R.drawable.ic_message)
                                .setContentTitle("Tin nhắn mới từ Tinder")
                                .setContentText(message)
                                .setStyle(new NotificationCompat.BigTextStyle()
                                        .bigText(message))
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setAutoCancel(true)
                                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

                        // show notification
                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getActivity());

                        // notificationId is a unique int for each notification that you must define
                        int notificationId = 456;
                        notificationManager.notify(notificationId, builder.build());

                        // add message to view
                        addMessage(sender_id, conversation_id, message);

                        // set the focus on the last element
                        int last_position = messageChatAdapter.getItemCount() - 1;
                        if (last_position > 0) {
                            recyclerView.smoothScrollToPosition(last_position);
                        }
                    }
                });
            }

        }
    };

    // update the status of sent message
    private Emitter.Listener onSentMessageSuccess = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("Sent message result", "Sent message result");
                        // get information from received message
                        Integer message_id;
                        Integer conversation_id;
                        Boolean is_received;
                        try {
                            JSONObject data = new JSONObject(args[0].toString());
                            message_id = data.getInt("message_id");
                            conversation_id = data.getInt("conversation_id");
                            is_received = data.getBoolean("is_received");
                        } catch (JSONException e) {
                            return;
                        }

                        Log.d("is_received", is_received.toString());
                        // update the status of sent message
                        if (is_received) {
                            Log.d("Result message", "Sent message success");
                        }
                    }
                });
            }

        }
    };


    public ArrayList<Message> loadMessages() {

        int USER_ID = UserAuth.getInstance().getUser().getId();

        final ArrayList<Message> all_messages = new ArrayList<>();

        final MessageService messageService = RetrofitClient.getMessageService();
        Log.d("authenToken", UserAuth.getInstance().getUser().getAuthen_token());
        Log.d("basetime", Integer.toString(last_base_time));
        messageService.getHistoricalMessage("Barer " + UserAuth.getInstance().getUser().getAuthen_token(),
                conversation_id, last_base_time).enqueue(
                new Callback<MessageService.MessageResponse>() {
                    @Override
                    public void onResponse(Call<MessageService.MessageResponse> call, Response<MessageService.MessageResponse> response) {
                        Log.i("onResponse", "Send request to get historical message with code: " + response.code());
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                all_messages.addAll(response.body().getAllMessages(conversation_name));

                                // update the last_base_time
                                last_base_time = response.body().getLastBaseTime();

                                messageChatAdapter.setMessageList(null);
                                messageChatAdapter.setMessageList(response.body().getAllMessages(conversation_name));
                                messageChatAdapter.notifyDataSetChanged();
                                recyclerView.setAdapter(messageChatAdapter);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MessageService.MessageResponse> call, Throwable t) {
                        Log.i("onFailure", "Send request to get historical message failed.");
                        t.printStackTrace();
                    }
                }
        );

        return all_messages;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
