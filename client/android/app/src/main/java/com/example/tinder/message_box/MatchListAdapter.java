package com.example.tinder.message_box;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.model.Conversation;
import com.example.tinder.R;

import com.example.model.Message;

import java.util.ArrayList;

public class MatchListAdapter extends RecyclerView.Adapter<MatchListAdapter.MatchViewHolder> {

    private ArrayList<Conversation> conversations;

    public MatchListAdapter() {
        // empty constructor
        this.conversations = new ArrayList<>();
    }

    public MatchListAdapter(ArrayList<Conversation> conversations) {
        this.conversations = conversations;
    }

    public MatchListAdapter(Conversation new_conversation) {
        this.conversations.add(new_conversation);
    }


    @NonNull
    @Override
    public MatchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.match_item, viewGroup, false);
        return new MatchViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull MatchViewHolder matchViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return this.conversations.size();
    }

    public void createNewConversation(Conversation conversation) {
        this.conversations.add(conversation);
        notifyDataSetChanged();
    }

    public static class  MatchViewHolder extends RecyclerView.ViewHolder {

        public MatchViewHolder(View view) {
            super(view);
        }
    }

}
