package com.howky.mike.bakingapp.StepDetail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.howky.mike.bakingapp.R;
import com.howky.mike.bakingapp.RecipeDetail.RecipeDetailActivity;
import com.howky.mike.bakingapp.RecipeDetail.StepsAdapter;

public class StepDetailActivity extends AppCompatActivity implements
        StepDetailFragment.OnFragmentInteractionListener {

    private static final String TAG = StepDetailActivity.class.getSimpleName();

    private int mOrientation;
    private int mStepId;
    private int mStepsCount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        setTitle(RecipeDetailActivity.mTitle);

        mOrientation = getResources().getConfiguration().orientation;

        Intent receivedIntent = getIntent();
        mStepsCount = receivedIntent.getIntExtra(StepsAdapter.INTENT_STEPS_COUNT, 0);
        if (mStepsCount == 0) {
            Log.e(TAG, "Error 0 steps found!");
            return;
        }

        mStepId = receivedIntent.getIntExtra(StepsAdapter.INTENT_STEP_ID, -1);
        if (mStepId == -1) {
            Log.e(TAG, "Error getting step ID!");
        } else {

            if (savedInstanceState == null) {
                StepDetailFragment stepDetailFragment = StepDetailFragment.newInstance(mStepsCount, mStepId);
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.step_fragment_container, stepDetailFragment)
                        .commit();
            }
        }

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
