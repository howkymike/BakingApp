package com.howky.mike.bakingapp.RecipeDetail;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.howky.mike.bakingapp.BakingAdapter;
import com.howky.mike.bakingapp.IngredientWidget.IngredientsWidget;
import com.howky.mike.bakingapp.IngredientWidget.WidgetService;
import com.howky.mike.bakingapp.R;
import com.howky.mike.bakingapp.StepDetail.StepDetailFragment;
import com.howky.mike.bakingapp.provider.BakingContract;
import com.howky.mike.bakingapp.provider.BakingProvider;
import com.howky.mike.bakingapp.utils.JsonUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RecipeDetailActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>, RecipeDetailFragment.OnFragmentInteractionListener,
        StepDetailFragment.OnFragmentInteractionListener{

    private static final String LOG_TAG = RecipeDetailActivity.class.getSimpleName();
    private static final String BUNDLE_CAKE_ID ="bundle_cake_id";

    private static final int SINGLE_LOADER_ID = 2;
    long mCakeId;
    private RecyclerView mIngredientsRecyclerView, mStepsRecyclerView;
    private LinearLayoutManager mLinearLayoutManager, mLinearLayoutManagerSteps;
    private IngredientsAdapter mIngredientsAdapter;
    private StepsAdapter mStepsAdapter;
    public static String[] mStepDesc, mStepVideoURL, mStepVideoThumbnail;
    public static String mTitle;
    public static boolean mTwoPane;
    public static FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);



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

        if (findViewById(R.id.recipe_detail_linearLayout_tablet )!= null) {
            mTwoPane = true;

            mFragmentManager = getSupportFragmentManager();

            ImageButton prevButton = findViewById(R.id.step_detail_prev_imgbtn);
//            prevButton.setVisibility(View.GONE);
            ImageButton nextButton = findViewById(R.id.step_detail_next_imgbtn);
//            nextButton.setVisibility(View.GONE);

        } else {
            mTwoPane = false;
        }

        // set up Ingredients RecyclerView
        mIngredientsRecyclerView = findViewById(R.id.recipe_detail_ingredientsItems_rv);
        mIngredientsRecyclerView.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mIngredientsRecyclerView.setLayoutManager(mLinearLayoutManager);
        mIngredientsAdapter = new IngredientsAdapter(this);
        mIngredientsRecyclerView.setAdapter(mIngredientsAdapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(
                mIngredientsRecyclerView.getContext(), mLinearLayoutManager.getOrientation());
        mIngredientsRecyclerView.addItemDecoration(itemDecoration);
        mIngredientsRecyclerView.setNestedScrollingEnabled(false);

        // set up Steps RecyclerView
        mStepsRecyclerView = findViewById(R.id.recipe_detail_steps_rv);
        //mStepsRecyclerView.setHasFixedSize(true);
        mLinearLayoutManagerSteps = new LinearLayoutManager(this);
        mStepsRecyclerView.setLayoutManager(mLinearLayoutManagerSteps);
        mStepsAdapter = new StepsAdapter(this);
        mStepsRecyclerView.setAdapter(mStepsAdapter);
//        RecyclerView.ItemDecoration itemDecorationSteps = new DividerItemDecoration(
//                mStepsRecyclerView.getContext(), mLinearLayoutManagerSteps.getOrientation());
//        mStepsRecyclerView.addItemDecoration(itemDecorationSteps);
        mStepsRecyclerView.setNestedScrollingEnabled(false);


        getSupportLoaderManager().initLoader(SINGLE_LOADER_ID, null, this);
    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        Log.d(LOG_TAG, "onCreateLoader");
        Uri SINGLE_CAKE_URI = BakingProvider.Cakes.withID(mCakeId);
        Log.d(LOG_TAG, "authority: " + SINGLE_CAKE_URI.getAuthority());
        Log.d(LOG_TAG, SINGLE_CAKE_URI.toString());
        return new CursorLoader(this, SINGLE_CAKE_URI, null,
                null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        Log.d(LOG_TAG, "onLoadFinished");
        //if (data == null || data.getCount() < 1) return;

        if (data == null) {
            Log.d(LOG_TAG, "data is null");
            return;
        }
        if (data.getCount() <1) {
            Log.d(LOG_TAG, "data is <1");
            return;
        }

        data.moveToFirst();

        // set up title
        mTitle = data.getString(data.getColumnIndex(BakingContract.CakeColumns.TITLE));
        Log.d(LOG_TAG, "title: " + mTitle);
        setTitle(mTitle);

        // set up ingredients
        String stringIngredients = data.getString(data.getColumnIndex(BakingContract.CakeColumns.INGREDIENTS));

        String[] ingredientsText = JsonUtils.getIngredients(stringIngredients);
        if (ingredientsText != null) {
            mIngredientsAdapter.swapData(ingredientsText);
        }

        //  set up steps
        String stringSteps = data.getString(data.getColumnIndex(BakingContract.CakeColumns.STEPS));

        try {
            JSONArray jsonSteps = new JSONArray(stringSteps);

            String[] shortDesc = new String[jsonSteps.length()];
            mStepDesc = new String[jsonSteps.length()];
            mStepVideoURL = new String[jsonSteps.length()];
            mStepVideoThumbnail = new String[jsonSteps.length()];

            JSONObject jsonStep;
            for (int i = 0; i < jsonSteps.length(); i++) {
                jsonStep = jsonSteps.getJSONObject(i);
                shortDesc[i] = jsonStep.getString("shortDescription");
                mStepDesc[i] = jsonStep.getString("description");
                mStepVideoURL[i] = jsonStep.getString("videoURL");
                mStepVideoThumbnail[i] = jsonStep.getString("thumbnailURL");
            }

            if (mTwoPane) {
                StepDetailFragment stepDetailFragment = StepDetailFragment.newInstance(1, 0);
                mFragmentManager.beginTransaction()
                        .add(R.id.step_detail_fragment_tablet_container, stepDetailFragment)
                        .commit();
            }

            // set up image
            String imageUrl = data.getString(data.getColumnIndex(BakingContract.CakeColumns.IMAGE));
            ImageView imageview = findViewById(R.id.recipe_detail_image_iv);
            if (imageUrl.equals("")) imageview.setVisibility(View.GONE);
            else Picasso.get().load(imageUrl).into(imageview);

            // set up servings
            int servings = data.getInt(data.getColumnIndex(BakingContract.CakeColumns.SERVINGS));
            String servingsText = getString(R.string.detail_cake_servings_descryption) + " " + servings;
            TextView servingsTextView  = findViewById(R.id.recipe_detail_servings_tv);
            servingsTextView.setText(servingsText);

            mStepsAdapter.swapData(shortDesc);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

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
    public void onItemSelected(int position) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
