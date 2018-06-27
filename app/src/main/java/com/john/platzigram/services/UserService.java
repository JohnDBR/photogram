package com.john.platzigram.services;

import com.john.platzigram.models.Post;
import com.john.platzigram.models.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by John on 6/19/2018.
 */

public interface UserService {

    @GET("/users")
    Call<List<User>> getAllUsers();

    @GET("/users/{id}")
    Call<User> getUser(@Path("id") int id);

    @Multipart
    @POST("/users")
    Call<User> createUser(
            @Part("email") RequestBody email,
            @Part("username") RequestBody username,
            @Part("name") RequestBody name,
            @Part("password") RequestBody password,
            @Part MultipartBody.Part file);

    @Multipart
    @PUT("/users/{id}")
    Call<User> updateUser(
            @Header("Authorization") String token,
            @Path("id") int id,
            @Part("email") RequestBody email,
            @Part("username") RequestBody username,
            @Part("name") RequestBody name,
            @Part("password") RequestBody password,
            @Part MultipartBody.Part file);

    @DELETE("/users/{id}")
    Call<User> deleteUser(@Header("Authorization") String token, @Path("id") int id);

    @GET("/users/{user_id}/posts")
    Call<List<Post>> getAllPosts(@Header("Authorization") String token, @Path("user_id") int id);
}
