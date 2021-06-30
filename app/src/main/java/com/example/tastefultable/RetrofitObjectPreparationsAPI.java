package com.example.tastefultable;
import com.example.tastefultable.model.ApiResponse;
import com.example.tastefultable.model.Preparations;
import com.example.tastefultable.model.User;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitObjectPreparationsAPI {
    @GET("preparations/read.php/")
    Call<ApiResponse<List<Preparations>>> listPreparations(@Query("id") int id);

    @POST("users/read.php")
    @FormUrlEncoded
    Call<List<User>> userRegistration(@Field("name") String name,
                                      @Field("email") String email,
                                      @Field("password") String password);

    // OR

    /*@POST("users/read.php")
    @FormUrlEncoded
    Call<List<User>> userRegistration(@Body User user);*/
}
