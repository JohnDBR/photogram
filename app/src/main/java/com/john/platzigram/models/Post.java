package com.john.platzigram.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by John on 6/26/2018.
 */

public class Post implements Serializable {

    @SerializedName("id")
    private Integer id;
    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private String description;
    @SerializedName("picture")
    private Picture picture;
    @SerializedName("user")
    private User user;
    @SerializedName("created_at")
    private String created_at;

    public Post(Integer id, String title, String description, Picture picture, User user, String created_at) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.picture = picture;
        this.user = user;
        this.created_at = created_at;
    }

    //GETTERS SETTERS...

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
