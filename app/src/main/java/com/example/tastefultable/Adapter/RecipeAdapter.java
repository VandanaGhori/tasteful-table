package com.example.tastefultable.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tastefultable.R;
import com.example.tastefultable.model.Recipe;

import java.util.List;
import java.util.zip.Inflater;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipe> mRecipeList;
    private Context context;

    public RecipeAdapter(Context context, List<Recipe> recipeList) {
        this.mRecipeList = recipeList;
        this.context = context;
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder{
        public ImageView mRecipeImage;
        public TextView mRecipeName;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            mRecipeImage = (ImageView) itemView.findViewById(R.id.recipeImage);
            mRecipeName = (TextView) itemView.findViewById(R.id.recipeName);
        }
    }

    // create a view from the row_recipe layout file and pass it to the ViewHolder class for getting reference of UI element.
    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_recipe,parent,false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeAdapter.RecipeViewHolder holder, int position) {
        Recipe recipe = mRecipeList.get(position);

        // Convert image_url into image
        Glide.with(context).load(recipe.getImg_url()).into(holder.mRecipeImage);
        ((RecipeViewHolder) holder).mRecipeName.setText(recipe.getName());
    }

    @Override
    public int getItemCount() {
        return mRecipeList.size();
    }
}
