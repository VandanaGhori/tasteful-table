package com.example.tastefultable.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.tastefultable.R;
import com.example.tastefultable.model.Ingredients;

import java.util.ArrayList;
import java.util.List;

public class IngredientsAdapter extends BaseAdapter {
    Context context;
    List<Ingredients> mIngredientsList;
    CheckBox mIngredientsCheckBox;
    TextView mIngredientsTextView;

    public IngredientsAdapter(Context context, List<Ingredients> mIngredientsList) {
        this.context = context;
        this.mIngredientsList = mIngredientsList;
    }

    @Override
    public int getCount() {
        return mIngredientsList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        Ingredients ingredients =mIngredientsList.get(position);

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_ingredients, null);
        mIngredientsCheckBox = (CheckBox) view.findViewById(R.id.ingredientsCheckBox);
        mIngredientsTextView = (TextView) view.findViewById(R.id.ingredientsTextView);

        mIngredientsTextView.setText(ingredients.getQty() + " " + ingredients.getName());
        return view;
    }

}
