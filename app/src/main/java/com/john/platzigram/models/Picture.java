package com.john.platzigram.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by John on 6/25/2018.
 */

public class Picture {

    @SerializedName("id")
    private Integer id;
    @SerializedName("name")
    private String name;
    @SerializedName("url")
    private String url;
    @SerializedName("created_at")
    private String created_at;

    public Picture(Integer id, String name, String url, String created_at) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.created_at = created_at;
    }

    //GETTERS SETTERS...

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
