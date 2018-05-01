package com.example.antoine.retrofitest.data.remote;

/**
 * Created by antoine on 25/04/2018.
 */

import com.example.antoine.retrofitest.data.model.Post;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIService {

    @POST("/posts")
    @FormUrlEncoded
    Call<Post> savePost(@Field("title") String title,
                        @Field("body") String body,
                        @Field("pswd") String pswd,
                        @Field("userId") long userId);
}