package com.john.platzigram.services;

import com.john.platzigram.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by John on 6/19/2018.
 */

public interface UserService {

    @GET("/users")
    Call<List<User>> getAllUsers();

    @GET("/users/{id}")
    Call<User> getUser(@Path("id") int id);

    @POST("/users")
    Call<User> createUser(@Body User user);

    @PUT("/users/{id}")
    Call<User> updateUser(@Path("id") int id, @Body User user);

    @DELETE("/users/{id}")
    Call<User> deleteUser(@Path("id") int id);
}
