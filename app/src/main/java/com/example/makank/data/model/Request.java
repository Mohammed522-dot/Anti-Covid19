package com.example.makank.data.model;

public class Request {
    int id;
    String sender_name;
    int owner_id;
    String recived_id;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }

    public String getRecived_id() {
        return recived_id;
    }

    public void setRecived_id(String recived_id) {
        this.recived_id = recived_id;
    }
}
