package com.john.platzigram.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by John on 6/26/2018.
 */

public class Like implements Serializable {
    @SerializedName("id")
    private Integer id;
    @SerializedName("user")
    private User user;

    public Like(Integer id, User user) {
        this.id = id;
        this.user = user;
    }

    //GETTERS SETTERS...

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
