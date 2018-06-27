package com.john.platzigram.services;

import com.john.platzigram.models.Post;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by John on 6/26/2018.
 */

public interface PostService {

    @GET("/posts")
    Call<List<Post>> getAllPosts(@Header("Authorization") String token);

    @Multipart
    @POST("/posts")
    Call<Post> createPost(
            @Header("Authorization") String token,
            @Part("title") RequestBody title,
            @Part("description") RequestBody description,
            @Part MultipartBody.Part file);

    @DELETE("/posts/{id}")
    Call<Post> deletePost(@Header("Authorization") String token, @Path("id") int id);
}
