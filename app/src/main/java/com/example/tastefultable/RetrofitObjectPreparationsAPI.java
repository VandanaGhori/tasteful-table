package com.example.tastefultable;
import com.example.tastefultable.model.GeneralApiResponse;
import com.example.tastefultable.model.Preparations;
import com.example.tastefultable.model.Recipe;
import com.example.tastefultable.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitObjectPreparationsAPI {

    @GET("preparations/read.php")
    Call<GeneralApiResponse<List<Preparations>>> listPreparations(@Query("id") int id);

    @POST("users/registration.php")
    @FormUrlEncoded
    Call<GeneralApiResponse<User>> userRegistration(@Field("name") String name,
                                      @Field("email") String email,
                                      @Field("password") String password);

    @POST("users/login.php")
    @FormUrlEncoded
    Call<GeneralApiResponse<User>> userLogin(@Field("email") String email,
                                             @Field("password") String password);

    @GET("recipes/read.php")
    Call<GeneralApiResponse<List<Recipe>>> listRecipes();
}
