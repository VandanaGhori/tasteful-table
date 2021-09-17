package com.example.tastefultable.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tastefultable.R;
import com.example.tastefultable.model.Recipe;

import java.util.List;

public class FavouriteRecipeAdapter extends BaseAdapter {
    Context context;
    List<Recipe> mFavouriteRecipeList;

    public FavouriteRecipeAdapter(Context context, List<Recipe> mFavouriteRecipeList) {
        this.context = context;
        this.mFavouriteRecipeList = mFavouriteRecipeList;
    }

    @Override
    public int getCount() {
        return mFavouriteRecipeList.size();
    }

    @Override
    public Object getItem(int position) {
        return mFavouriteRecipeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mFavouriteRecipeList.get(position).getId();
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ImageView recipeImage;
        TextView recipeName;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_favourite_recipe,parent,false);
        recipeImage = (ImageView) view.findViewById(R.id.myFavouriteRecipeImage);
        recipeName = (TextView) view.findViewById(R.id.recipeName);

        Glide.with(view.getContext()).load(mFavouriteRecipeList.get(position).getImg_url()).into(recipeImage);
        recipeName.setText(mFavouriteRecipeList.get(position).getName());

        return view;
    }
}
