package com.john.platzigram.models;

/**
 * Created by John on 6/5/2018.
 */

public class Post {
    private String post;
    private String username;
    private String time;
    private String like_number = "0";

    public Post(String post, String username, String time, String like_number) {
        this.post = post;
        this.username = username;
        this.time = time;
        this.like_number = like_number;
    }

    //GETTERS SETTERS...

    public String getpost() {
        return post;
    }

    public void setpost(String post) {
        this.post = post;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLike_number() {
        return like_number;
    }

    public void setLike_number(String like_number) {
        this.like_number = like_number;
    }
}
