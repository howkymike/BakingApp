package com.howky.mike.bakingapp.RecipeDetail;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.howky.mike.bakingapp.BakingAdapter;
import com.howky.mike.bakingapp.IngredientWidget.IngredientsWidget;
import com.howky.mike.bakingapp.IngredientWidget.WidgetService;
import com.howky.mike.bakingapp.R;
import com.howky.mike.bakingapp.StepDetail.StepDetailFragment;
import com.howky.mike.bakingapp.provider.BakingContract;

public class RecipeDetailActivity extends AppCompatActivity implements
        RecipeDetailFragment.OnFragmentInteractionListener,
        StepDetailFragment.OnFragmentInteractionListener{

    private static final String LOG_TAG = RecipeDetailActivity.class.getSimpleName();
    private static final String BUNDLE_CAKE_ID ="bundle_cake_id";

    long mCakeId;
    public static String mTitle;
    public static boolean mTwoPane;
    public static FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        
        if(getResources().getBoolean(R.bool.landscape_only)){
            if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE ||
                    getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        }

        Intent receivedIntent = getIntent();
        mCakeId = receivedIntent.getLongExtra(BakingAdapter.INTENT_CAKE_ID, BakingContract.INVALID_CAKE_ID);
        if(savedInstanceState != null) {
            Log.d(LOG_TAG, "there is not null saveInstanceState :D");
            mCakeId = savedInstanceState.getLong(BUNDLE_CAKE_ID, BakingContract.INVALID_CAKE_ID);
        }
        if (mCakeId == BakingContract.INVALID_CAKE_ID) {
            Log.d(LOG_TAG, "return coz no cake id");
            return;
        }
        updateIngredintsWidget(this, mCakeId);


        mFragmentManager = getSupportFragmentManager();
        if (findViewById(R.id.detail_fragment_tablet_container) == null) {
            mTwoPane = false;
            if (savedInstanceState == null) {
                RecipeDetailFragment recipeDetailFragment = RecipeDetailFragment.newInstance(mCakeId);
                mFragmentManager.beginTransaction()
                        .add(R.id.detail_fragment_container, recipeDetailFragment)
                        .commit();
            }
        } else {
            mTwoPane = true;
            if (savedInstanceState == null) {
                RecipeDetailFragment recipeDetailFragment = RecipeDetailFragment.newInstance(mCakeId);
                mFragmentManager.beginTransaction()
                        .add(R.id.detail_fragment_tablet_container, recipeDetailFragment)
                        .commit();
            }
        }
    }

    public static void updateIngredintsWidget(Context context, long cakeId) {
        Intent intent = new Intent(context, IngredientsWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(WidgetService.CAKE_ID_EXTRA, cakeId);
        int[] ids = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, IngredientsWidget.class));
        if (ids != null && ids.length > 0) {
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
            context.sendBroadcast(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateIngredintsWidget(this, mCakeId);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(LOG_TAG, "onSAveeInstanceState");
        outState.putLong(BUNDLE_CAKE_ID, mCakeId);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("test", "test");
        long tescik = intent.getLongExtra(BakingAdapter.INTENT_CAKE_ID, -1);
        Log.d(LOG_TAG, "get cake id: " + tescik);
        setIntent(intent);
    }

    @Override
    public void onItemSelected(int position, String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {}
}
