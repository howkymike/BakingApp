package com.howky.mike.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.howky.mike.bakingapp.RecipeDetail.RecipeDetailActivity;
import com.howky.mike.bakingapp.provider.BakingContract;

/**
 * Converts cursor data for recipe cards into visible list items in a RecyclerView
 */
public class BakingAdapter extends RecyclerView.Adapter<BakingAdapter.BakingViewHolder> {

    public static final String INTENT_CAKE_ID = "cake_id";

    private Context mContext;
    private Cursor mData;

    public BakingAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public BakingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe_card, parent, false);
        BakingViewHolder viewHolder = new BakingViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BakingViewHolder holder, int position) {
        mData.moveToPosition(position);

        String name = mData.getString(mData.getColumnIndex(BakingContract.CakeColumns.TITLE));
        String servings = mData.getString(mData.getColumnIndex(BakingContract.CakeColumns.SERVINGS));

        holder.nameTextView.setText(name);
        String servingsText = mContext.getResources().getString(R.string.servings_text) + " " + servings;
        holder.servingsTextView.setText(servingsText);

    }

    @Override
    public int getItemCount() {
        if (null == mData) return 0;
        return mData.getCount();
    }

    public void swapCursor(Cursor data) {
        mData = data;
        notifyDataSetChanged();
    }

    public class BakingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

         final TextView nameTextView;
         final TextView servingsTextView;
         final ConstraintLayout constraintLayout;

        BakingViewHolder(View layoutView) {
            super(layoutView);
            nameTextView = layoutView.findViewById(R.id.item_name_tv);
            servingsTextView = layoutView.findViewById(R.id.item_servings_tv);
            constraintLayout = layoutView.findViewById(R.id.item_layout_cl);
            constraintLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mData.moveToPosition(adapterPosition);
            long cakeId = mData.getLong(mData.getColumnIndex(BakingContract.CakeColumns.CAKE_ID));

            Intent openDetailsIntent = new Intent(mContext, RecipeDetailActivity.class);
            openDetailsIntent.putExtra(INTENT_CAKE_ID, cakeId);
            mContext.startActivity(openDetailsIntent);
        }
    }
}
