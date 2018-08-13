package com.howky.mike.bakingapp.RecipeDetail;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.howky.mike.bakingapp.R;
import com.howky.mike.bakingapp.StepDetail.StepDetailFragment;
import com.howky.mike.bakingapp.provider.BakingContract;
import com.howky.mike.bakingapp.provider.BakingProvider;
import com.howky.mike.bakingapp.utils.JsonUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecipeDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecipeDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String BUNDLE_CAKE_ID ="bundle_cake_id";
    private static final String LOG_TAG = RecipeDetailFragment.class.getSimpleName();

    private static final int SINGLE_LOADER_ID = 2;
    long mCakeId;
    private RecyclerView mIngredientsRecyclerView, mStepsRecyclerView;
    private LinearLayoutManager mLinearLayoutManager, mLinearLayoutManagerSteps;
    private IngredientsAdapter mIngredientsAdapter;
    private StepsAdapter mStepsAdapter;
    public static String[] mStepDesc, mStepVideoURL, mStepVideoThumbnail;
    public static String mTitle;

    private ImageView mImageView;
    private TextView mServingsTextView;

    private OnFragmentInteractionListener mListener;

    public RecipeDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RecipeDetailFragment.
     */
    public static RecipeDetailFragment newInstance(long cakeId) {
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        Bundle args = new Bundle();
        args.putLong(BUNDLE_CAKE_ID, cakeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCakeId = getArguments().getLong(BUNDLE_CAKE_ID, -1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        mImageView = rootView.findViewById(R.id.recipe_detail_image_iv);
        mServingsTextView = rootView.findViewById(R.id.recipe_detail_servings_tv);

        // set up Ingredients RecyclerView
        mIngredientsRecyclerView = rootView.findViewById(R.id.recipe_detail_ingredientsItems_rv);
        mIngredientsRecyclerView.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mIngredientsRecyclerView.setLayoutManager(mLinearLayoutManager);
        mIngredientsAdapter = new IngredientsAdapter(getContext());
        mIngredientsRecyclerView.setAdapter(mIngredientsAdapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(
                mIngredientsRecyclerView.getContext(), mLinearLayoutManager.getOrientation());
        mIngredientsRecyclerView.addItemDecoration(itemDecoration);
        mIngredientsRecyclerView.setNestedScrollingEnabled(false);

        // set up Steps RecyclerView
        mStepsRecyclerView = rootView.findViewById(R.id.recipe_detail_steps_rv);
        mStepsRecyclerView.setHasFixedSize(true);
        mLinearLayoutManagerSteps = new LinearLayoutManager(getContext());
        mStepsRecyclerView.setLayoutManager(mLinearLayoutManagerSteps);
        mStepsAdapter = new StepsAdapter(getContext());
        mStepsRecyclerView.setAdapter(mStepsAdapter);
        RecyclerView.ItemDecoration itemDecorationSteps = new DividerItemDecoration(
                mStepsRecyclerView.getContext(), mLinearLayoutManagerSteps.getOrientation());
        mStepsRecyclerView.addItemDecoration(itemDecorationSteps);
        mStepsRecyclerView.setNestedScrollingEnabled(false);

        getLoaderManager().initLoader(SINGLE_LOADER_ID, null, this);


        // Return the root view
        return rootView;
    }
    public void onButtonPressed(int position) {
        if (mListener != null) {
            mListener.onItemSelected(position, "");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        Log.d(LOG_TAG, "onCreateLoader");
        Uri SINGLE_CAKE_URI = BakingProvider.Cakes.withID(mCakeId);
        Log.d(LOG_TAG, "authority: " + SINGLE_CAKE_URI.getAuthority());
        Log.d(LOG_TAG, SINGLE_CAKE_URI.toString());
        return new CursorLoader(getContext(), SINGLE_CAKE_URI, null,
                null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        Log.d(LOG_TAG, "onLoadFinished");
        if (data == null || data.getCount() < 1) return;

        data.moveToFirst();

        // set up title
        mTitle = data.getString(data.getColumnIndex(BakingContract.CakeColumns.TITLE));
        Log.d(LOG_TAG, "title: " + mTitle);
        if (mListener != null) {
            mListener.onItemSelected(-1, mTitle);
        }

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

            if (RecipeDetailActivity.mTwoPane) {
                StepDetailFragment stepDetailFragment = StepDetailFragment.newInstance(mStepDesc.length, 0);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .add(R.id.step_detail_fragment_tablet_container, stepDetailFragment)
                        .commit();
            }

            // set up image
            String imageUrl = data.getString(data.getColumnIndex(BakingContract.CakeColumns.IMAGE));
            if (imageUrl.equals("")) mImageView.setVisibility(View.GONE);
            else Picasso.get().load(imageUrl).into(mImageView);

            // set up servings
            int servings = data.getInt(data.getColumnIndex(BakingContract.CakeColumns.SERVINGS));
            String servingsText = getString(R.string.detail_cake_servings_descryption) + " " + servings;
            mServingsTextView.setText(servingsText);

            mStepsAdapter.swapData(shortDesc);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {}

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onItemSelected(int position, String title);
    }

}
