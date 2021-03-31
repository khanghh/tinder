package com.example.model;

import java.util.Date;
import java.text.SimpleDateFormat;


public class Message {
    private int conversation_id;
    private int sender_id;
    private String message;
    private Date created_at;
    private boolean is_received;
    private int message_id;
    private String conversation_name;

    public Message(){

    }

    public Message(Integer user_id, Integer conversation_id, String body) {
        sender_id = user_id;
        message = body;
        created_at = new Date();
        this.conversation_id = conversation_id;
        this.is_received = false;
        this.conversation_name = "";
    }

    public Message(Integer user_id, Integer conversation_id, String body, String conversation_name) {
        sender_id = user_id;
        message = body;
        created_at = new Date();
        this.conversation_id = conversation_id;
        this.is_received = false;
        this.conversation_name = conversation_name;
    }

    public String getMessage() {
        return message;
    }

    public int getConversation_id() {
        return conversation_id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_date) {
        this.created_at = created_date;
    }

    public int getSender_id() {
        return sender_id;
    }

    public void setSender_id(int sender_id) {
        this.sender_id = sender_id;
    }

    public void setConversation_id(int conversation_id) {
        this.conversation_id = conversation_id;
    }

    public void setIs_received(boolean is_received) {
        this.is_received = is_received;
    }

    public boolean getIs_received() {
        return this.is_received;
    }

    public void setMessage_id(int message_id) {
        this.message_id = message_id;
    }

    public int getMessage_id() {
        return message_id;
    }

    public String getConversation_name() {
        return conversation_name;
    }

    public void setConversation_name(String conversation_name) {
        this.conversation_name = conversation_name;
    }
}
