package com.example.tastefultable;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.tastefultable.model.Recipe;

public class RecipePreparationActivity extends AppCompatActivity {
    ImageView mRecipeImage;
    TextView mTextViewRecipeName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preparation_recipe);
        getSupportActionBar().setHomeButtonEnabled(true);
        initializeData();
        getRecipeData();
    }

    private void initializeData() {
        mRecipeImage = (ImageView) findViewById(R.id.imageViewRecipe);
        mTextViewRecipeName = (TextView) findViewById(R.id.textViewRecipeTitle);
    }

    private void getRecipeData() {
        Intent intent = getIntent();
        Recipe recipe = (Recipe) intent.getSerializableExtra("recipe");

        // Convert image_url into image
        Glide.with(this).load(recipe.getImg_url()).into(mRecipeImage);
        mTextViewRecipeName.setText(recipe.getName());
    }
}
