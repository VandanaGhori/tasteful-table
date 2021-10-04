package com.example.tastefultable;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.tastefultable.Adapter.IngredientsAdapter;
import com.example.tastefultable.model.Favourite;
import com.example.tastefultable.model.GeneralApiResponse;
import com.example.tastefultable.model.Ingredients;
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

public class RecipePreparationActivity extends AppCompatActivity {
    ImageView mRecipeImage,mAddFavouriteRecipe;
    TextView mTextViewRecipeName;
    TextView mTextViewTime;
    TextView mTextViewPreparationsSteps;
    TextView mNumberOfIngredients;
    String res;
    List<Ingredients> mIngredientsList;
    ListView mIngredientsListView;
    IngredientsAdapter ingredientsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preparation_recipe);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initializeData();

        /*boolean isFavourite = false;
        if(!isFavourite) {
            mAddFavouriteRecipe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAddFavouriteRecipe.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_baseline_favorite_24));

                    likeRecipe();

                    Toast.makeText(getApplicationContext(),"Added",Toast.LENGTH_LONG).show();
                }
            });
        } else {

        }*/

        // Through intent
        getRecipeData();
        // Through AsyncTask
        getIngredientsData();

        // For getting preparations Data
        getPreparationsApiResults();
        //getPreparationsData();
    }

    private void likeRecipe() {
        Intent intent = getIntent();
        Recipe recipe = (Recipe) intent.getSerializableExtra("recipe");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://tastefultable.000webhostapp.com/tastefulTable/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitObjectPreparationsAPI service = retrofit.create(RetrofitObjectPreparationsAPI.class);

        Call<GeneralApiResponse<Favourite>> repos = service.likeRecipe(recipe.getId());

        repos.enqueue(new Callback<GeneralApiResponse<Favourite>>() {
            @Override
            public void onResponse(Call<GeneralApiResponse<Favourite>> call, Response<GeneralApiResponse<Favourite>> response) {
                GeneralApiResponse<Favourite> apiResponse = response.body();

                if(apiResponse == null) return;

                if(apiResponse.isSuccess() && apiResponse.getError_code() == 200) {
                    //Favourite favourite = apiResponse.getData();
                    Toast.makeText(getApplicationContext(), "Recipe is marked as a favourite one.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GeneralApiResponse<Favourite>> call, Throwable t) {
                Toast.makeText(RecipePreparationActivity.this, "API response failure for favourite recipe"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*private void getPreparationsData() {
        Intent intent = getIntent();
        Recipe recipe = (Recipe) intent.getSerializableExtra("recipe");

        try {
            GetPreparationsAsyncTask getPreparationsAsyncTask = new GetPreparationsAsyncTask();
            res = getPreparationsAsyncTask.execute(GetPreparationsAsyncTask.URL + recipe.getId()).get();
            StringBuilder resultsSteps = new StringBuilder();

            if(res.length() != 0) {
                Log.i("Result Preparations :", res);
                JSONObject jsonObject = new JSONObject(res);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for(int i =0; i<jsonArray.length();i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    int order_no = object.getInt("order_no");
                    String steps = object.getString("steps");
                    resultsSteps.append(String.valueOf(order_no) + ". " + steps + "\n");
                }
                //Log.i("Result = ", resultsSteps.toString());
                mTextViewPreparationsSteps.setText(resultsSteps);
            } else {
                mTextViewPreparationsSteps.setText("No Steps.");
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }*/

    // For getting preparations Data
    private void getPreparationsApiResults() {
        // For passing id as the query parameter
        Intent intent = getIntent();
        Recipe recipe = (Recipe) intent.getSerializableExtra("recipe");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://tastefultable.000webhostapp.com/tastefulTable/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitObjectPreparationsAPI service = retrofit.create(RetrofitObjectPreparationsAPI.class);

        // At the time of Requesting API pass parameter id.
        Call<GeneralApiResponse<List<Preparations>>> repos = service.listPreparations(recipe.getId());

        repos.enqueue(new Callback<GeneralApiResponse<List<Preparations>>>() {
            @Override
            public void onResponse(Call<GeneralApiResponse<List<Preparations>>> call, Response<GeneralApiResponse<List<Preparations>>> response) {

                GeneralApiResponse<List<Preparations>> apiResponse = response.body();
                //Log.i("PREPARATIONS RESPONSE", "onResponse: " + apiResponse);

                if(apiResponse == null) return;
                //boolean success_key = apiResponse.isSuccess();
                //Log.i("Success", "onResponse: " + success_key);

                if(apiResponse.isSuccess() && apiResponse.getError_code() == 200) {
                    List<Preparations> list = (List<Preparations>) apiResponse.getData();
                   // Log.i("Api response:", "onResponse: " + list.size());
                    showPreparationSteps(list);
                }
            }

            @Override
            public void onFailure(Call<GeneralApiResponse<List<Preparations>>> call, Throwable t) {
                Toast.makeText(RecipePreparationActivity.this, "API call Failure On Preparations " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showPreparationSteps(List<Preparations> pList) {
        if (pList != null && pList.size() != 0) {
            for (int i = 0; i < pList.size(); i++) {
                int id = pList.get(i).getId();
                int rec_id = pList.get(i).getRec_id();
                String steps = pList.get(i).getSteps();
                int order_no = pList.get(i).getOrder_no();

                // Setting the size of TextView via Java code
                mTextViewPreparationsSteps.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
                mTextViewPreparationsSteps.append(order_no + ". " + steps + "\n\n");
            }
        } else {
            mTextViewPreparationsSteps.setText("Preparations steps are not there!");
            return;
        }
    }

    private void initializeData() {
        mRecipeImage = (ImageView) findViewById(R.id.imageViewRecipe);
        mAddFavouriteRecipe = (ImageView) findViewById(R.id.myFavourite);
        mTextViewRecipeName = (TextView) findViewById(R.id.textViewRecipeTitle);
        mTextViewTime = (TextView) findViewById(R.id.timeTextView);
        mTextViewPreparationsSteps = (TextView) findViewById(R.id.textViewPreparationsSteps);
        mNumberOfIngredients = (TextView) findViewById(R.id.numberOfIngredients);
    }

    private void getRecipeData() {
        Intent intent = getIntent();
        Recipe recipe = (Recipe) intent.getSerializableExtra("recipe");

        // Convert image_url into image
        Glide.with(this).load(recipe.getImg_url()).into(mRecipeImage);
        mTextViewRecipeName.setText(recipe.getName());
        // For fetching integer minutes from the string (190 Minutes)
        String time[] = recipe.getTime().split(" ");
        //Toast.makeText(this,"Time : " + time[0],Toast.LENGTH_LONG).show();

        int t = Integer.parseInt(time[0]);
        int hrs = t / 60;
        int minutes = t % 60;
        if (minutes > 0) {
            mTextViewTime.setText(hrs + " Hr " + minutes + " Mins");
        } else {
            mTextViewTime.setText(hrs + " Hr ");
        }
    }

    private void getIngredientsData() {
        Intent intent = getIntent();
        Recipe recipe = (Recipe) intent.getSerializableExtra("recipe");

        GetIngredientsAsyncTask getIngredientsAsyncTask = new GetIngredientsAsyncTask();
        try {
            res = getIngredientsAsyncTask.execute(GetIngredientsAsyncTask.URL + recipe.getId()).get();
            //Log.i("Result = ", res);

            if (res.length() != 0) {
                JSONObject jsonObject = new JSONObject(res);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                mIngredientsList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    int id = object.getInt("id");
                    int rec_id = object.getInt("rec_id");
                    String name = object.getString("name");
                    String qty = object.getString("qty");
                    String img_url = object.getString("img_url");

                    Ingredients ingredients = new Ingredients(id, rec_id, name, qty, img_url);
                    mIngredientsList.add(ingredients);

                }
                // Set count of total number of ingredients
                mNumberOfIngredients.setText(String.valueOf(mIngredientsList.size()));
                bindAdapter();
            } else {
                return;
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void bindAdapter() {
        mIngredientsListView = (ListView) findViewById(R.id.myIngredientsListView);
        ingredientsAdapter = new IngredientsAdapter(getApplicationContext(), mIngredientsList);
        mIngredientsListView.setAdapter(ingredientsAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
