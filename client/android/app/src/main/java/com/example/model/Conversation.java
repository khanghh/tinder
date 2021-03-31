package com.example.model;

public class Conversation {
    private int id;
    private int creator_id;
    private int member_id;
    private String created_at;
    private String updated_at;
    private String deleted_at;
    // the name of member id
    private String conversation_name;

    // add lasted message to update UI in MatchListApdapter
    private String lasted_message;

    public Conversation(int creator_id, int member_id) {
        this.creator_id = creator_id;
        this.member_id = member_id;
    }

    public Conversation(int id, int creator_id, int member_id, String conversation_name) {
        this.id = id;
        this.creator_id = creator_id;
        this.member_id = member_id;
        this.conversation_name = conversation_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(int creator_id) {
        this.creator_id = creator_id;
    }

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public void setLasted_message(String lasted_message) {
        this.lasted_message = lasted_message;
    }

    public String getLasted_message() {
        return lasted_message;
    }

    public void setConversation_name(String conversation_name) {
        this.conversation_name = conversation_name;
    }

    public String getConversation_name() {
        return conversation_name;
    }
}
