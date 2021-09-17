package com.example.tastefultable;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.GridView;
import android.widget.Toast;

import com.example.tastefultable.Adapter.FavouriteRecipeAdapter;
import com.example.tastefultable.model.GeneralApiResponse;
import com.example.tastefultable.model.Preparations;
import com.example.tastefultable.model.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoriteFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    GridView favouriteGridView;
    FavouriteRecipeAdapter favouriteRecipeAdapter;
    List<Recipe> favouriteRecipeList;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavoriteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoriteFragment newInstance(String param1, String param2) {
        FavoriteFragment fragment = new FavoriteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_favorite, container, false);

        favouriteRecipeList = new ArrayList<>();
        getRecipesApiResults();

        //Log.i("FAVOURITE RECIPE LIST", "onCreateView: " + getRecipesApiResults().toString());

        Log.i("LIST = ", "onCreateView: "+ favouriteRecipeList);
        favouriteRecipeAdapter = new FavouriteRecipeAdapter(getContext(), favouriteRecipeList);

        favouriteGridView = (GridView) v.findViewById(R.id.myFavouriteGridView);
        favouriteGridView.setAdapter(favouriteRecipeAdapter);
        //favouriteGridView.notifyAll();

        return v;
    }

    // For getting preparations Data
    private void getRecipesApiResults() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://tastefultable.000webhostapp.com/tastefulTable/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitObjectPreparationsAPI service = retrofit.create(RetrofitObjectPreparationsAPI.class);

        // At the time of Requesting API pass parameter id.
        Call<GeneralApiResponse<List<Recipe>>> repos = service.listRecipes();

        repos.enqueue(new Callback<GeneralApiResponse<List<Recipe>>>() {
            @Override
            public void onResponse(Call<GeneralApiResponse<List<Recipe>>> call, Response<GeneralApiResponse<List<Recipe>>> response) {
                GeneralApiResponse<List<Recipe>> apiResponse = response.body();

                if(apiResponse != null) {
                    if(apiResponse.isSuccess() && apiResponse.getError_code() == 200) {
                        favouriteRecipeList.clear();
                        favouriteRecipeList.addAll(apiResponse.getData());
                        Log.i("RECIPELIST", "onResponse: " + favouriteRecipeList);

//                        String image = "https://images.pexels.com/photos/3004798/pexels-photo-3004798.jpeg?auto=compress&cs=tinysrgb&dpr=3&h=750&w=1260";
//                        favouriteRecipeList.add(new Recipe(1,"Name1",image,"2021-09-16"));
//                        favouriteRecipeList.add(new Recipe(2,"Name2",image,"2021-09-15"));
//                        favouriteRecipeList.add(new Recipe(3,"Name3",image,"2021-09-14"));
//                        favouriteRecipeList.add(new Recipe(4,"Name4",image,"2021-09-13"));

                        favouriteRecipeAdapter.notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onFailure(Call<GeneralApiResponse<List<Recipe>>> call, Throwable t) {
                Toast.makeText(getContext(), "API call Failure On Recipes " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        //return favouriteRecipeList;
    }
}