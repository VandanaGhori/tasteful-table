package com.example.tastefultable;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tastefultable.Adapter.RecipeAdapter;
import com.example.tastefultable.model.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView textViewHomeFragment;
    String result;
    RecyclerView recipeRecyclerView;
    List<Recipe> recipeList;
    RecipeAdapter recipeAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recipeRecyclerView = (RecyclerView) view.findViewById(R.id.recipeRecyclerView);
        recipeList = new ArrayList<>();
        getRecipesTask();
        bindAdapter();
        return view;
    }

    private void bindAdapter() {
        // Set Layout for the RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recipeRecyclerView.setLayoutManager(layoutManager);
        recipeAdapter = new RecipeAdapter(getContext(), recipeList);
        recipeRecyclerView.setAdapter(recipeAdapter);
        recipeAdapter.notifyDataSetChanged();
    }

    private void getRecipesTask() {
        GetRecipeAsyncTask getRecipeAsyncTask = new GetRecipeAsyncTask();
        try {
            result = getRecipeAsyncTask.execute(GetRecipeAsyncTask.URL).get();

            //Log.i("Result=", result);
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("data");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                int id = object.getInt("id");
                String name = object.getString("name");
                String img_url = object.getString("img_url");
                String time = object.getString("time");

                Recipe recipe = new Recipe(id, name, img_url, time);
                recipeList.add(recipe);
            }

            //Toast.makeText(getContext(), "Result = " + recipeList.toString(), Toast.LENGTH_LONG).show();
            //textViewHomeFragment.setText("Result = " + result);
        } catch (ExecutionException e) {
            e.printStackTrace();
            result = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            result = null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}