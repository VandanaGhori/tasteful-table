package com.example.tastefultable;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.tastefultable.Adapter.IngredientsAdapter;
import com.example.tastefultable.model.Ingredients;
import com.example.tastefultable.model.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RecipePreparationActivity extends AppCompatActivity {
    ImageView mRecipeImage;
    TextView mTextViewRecipeName;
    TextView mTextViewTime;
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
        getRecipeData();
        getIngredientsData();
        //bindAdapter();
    }

    private void initializeData() {
        mRecipeImage = (ImageView) findViewById(R.id.imageViewRecipe);
        mTextViewRecipeName = (TextView) findViewById(R.id.textViewRecipeTitle);
        mTextViewTime = (TextView) findViewById(R.id.timeTextView);
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
        //Toast.makeText(this,"Time Hours: " + hrs + "Minutes: " + minutes,Toast.LENGTH_LONG).show();
        // String msg = minutes > 60 ? hrs + " Hr " + minutes + " Mins" : hrs + " Hr ";

        if (minutes > 0) {
            mTextViewTime.setText(hrs + " Hr " + minutes + " Mins");
        } else {
            mTextViewTime.setText(hrs + " Hr ");
        }
        //mTextViewTime.setText(new SimpleDateFormat("HH:MM", Locale.CANADA).format(t));
    }

    private void getIngredientsData() {
        Intent intent = getIntent();
        Recipe recipe = (Recipe) intent.getSerializableExtra("recipe");

        GetIngredientsAsyncTask getIngredientsAsyncTask = new GetIngredientsAsyncTask();
        try {
            res = getIngredientsAsyncTask.execute(GetIngredientsAsyncTask.URL + recipe.getId()).get();
            Log.i("Result = ", res);

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
        ingredientsAdapter = new IngredientsAdapter(getApplicationContext(),mIngredientsList);
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
