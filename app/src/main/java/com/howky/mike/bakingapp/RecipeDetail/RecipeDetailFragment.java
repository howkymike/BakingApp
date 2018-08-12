package com.howky.mike.bakingapp.RecipeDetail;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.howky.mike.bakingapp.R;

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

    private static final int SINGLE_LOADER_ID = 2;
    long mCakeId;
    private RecyclerView mIngredientsRecyclerView, mStepsRecyclerView;
    private LinearLayoutManager mLinearLayoutManager, mLinearLayoutManagerSteps;
    private IngredientsAdapter mIngredientsAdapter;
    private StepsAdapter mStepsAdapter;
    public static String[] mStepDesc, mStepVideoURL;
    public static String mTitle;


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
    public static RecipeDetailFragment newInstance(String param1, String param2) {
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

//        TextView textView = rootView.findViewById(R.id.recipe_detail_steps_tv);
//
//        RecyclerView ingredientsRecyclerView = rootView.findViewById(R.id.recipe_detail_ingredientsItems_rv);
//        IngredientsAdapter mIngredintsAdapter = new IngredientsAdapter(getContext());
//        ingredientsRecyclerView.setAdapter(mIngredintsAdapter);
//
//        ingredientsRecyclerView.setOnClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//               mListener.onFragmentInteraction();
//            }
//        });

        // Return the root view
        return rootView;
    }
    public void onButtonPressed(int position) {
        if (mListener != null) {
            mListener.onItemSelected(position);
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
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

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
        void onItemSelected(int position);
    }

}
