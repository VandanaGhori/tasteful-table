package com.example.tastefultable;
import com.example.tastefultable.model.Preparations;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitObjectPreparationsAPI {
    @GET("tastefulTable/api/preparations/read.php")
    Call<List<Preparations>> listPreparations();
}
