package com.howky.mike.bakingapp.RecipeDetail;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.howky.mike.bakingapp.R;
import com.howky.mike.bakingapp.StepDetail.StepDetailActivity;


public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsViewHolder> {

    public static final String INTENT_STEP_ID = "step_id";
    public static final String INTENT_STEPS_COUNT = "steps_count";

    private Context mContext;
    private String[] mData;

    public StepsAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public StepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_step_card, parent, false);
        StepsViewHolder viewHolder = new StepsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StepsViewHolder holder, int position) {
        holder.stepsTextView.setText(mData[position]);
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

public class StepsViewHolder extends RecyclerView.ViewHolder implements
        RecyclerView.OnClickListener{

    final TextView stepsTextView;
    final CardView cardView;

    public StepsViewHolder(View layoutview) {
        super(layoutview);
        stepsTextView = layoutview.findViewById(R.id.item_step_tv);
        cardView = layoutview.findViewById(R.id.item_step_cv);
        cardView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int adapterPosition = getAdapterPosition();

        Intent openDetailStepIntent = new Intent(mContext, StepDetailActivity.class);
        openDetailStepIntent.putExtra(INTENT_STEP_ID, adapterPosition);
        openDetailStepIntent.putExtra(INTENT_STEPS_COUNT, getItemCount());
        mContext.startActivity(openDetailStepIntent);

    }
}
}
