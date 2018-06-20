package com.john.platzigram.services;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by John on 6/19/2018.
 */

public interface AuthenticationService {

    @POST("/sessions")
    Call<ResponseBody> login(@Body RequestBody params);

    @DELETE("/sessions")
    Call<ResponseBody> logout(@Header("Authorization") String token);
}
