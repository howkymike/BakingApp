package com.howky.mike.bakingapp.RecipeDetail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.howky.mike.bakingapp.R;

/**
 * Converts cursor data for ingredients cards into visible list items in a RecyclerView
 */
public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder>{


    private Context mContext;
    private String[] mData;

    public IngredientsAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public IngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_ingredient_card, parent, false);
        IngredientsViewHolder viewHolder = new IngredientsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsViewHolder holder, int position) {
       // mData.moveToPosition(position);
        holder.ingredientTextView.setText(mData[position]);

    }

    @Override
    public int getItemCount() {
        if (null == mData) return 0;
        return mData.length;
    }

    public void swapData(String[] data) {
        mData = data;
        notifyDataSetChanged();
    }


    public class IngredientsViewHolder extends RecyclerView.ViewHolder implements
    RecyclerView.OnClickListener{

        final TextView ingredientTextView;

        public IngredientsViewHolder(View layoutview) {
            super(layoutview);
            ingredientTextView = layoutview.findViewById(R.id.item_ingredient_tv);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
