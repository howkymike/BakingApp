package com.howky.mike.bakingapp;

import android.content.res.Configuration;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import android.support.design.widget.Snackbar;

import com.howky.mike.bakingapp.utils.JsonUtils;
import com.howky.mike.bakingapp.provider.BakingContract;
import com.howky.mike.bakingapp.provider.BakingProvider;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int LOADER_ID_FROM_DB = 0;
    private static final int LOADER_ID_FROM_WEB = 1;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private BakingAdapter mAdapter;

    private boolean mIsConnected;
    private boolean mIsTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIsTablet = checkIfTablet();

        // Set up RecyclerView
        mRecyclerView = findViewById(R.id.main_rv);
        mRecyclerView.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(this);
        if (mIsTablet && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        } else {
            if (mIsTablet) {
                mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            } else {
                mRecyclerView.setLayoutManager(mLinearLayoutManager);
            }
        }
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                mRecyclerView.getContext(),
                mLinearLayoutManager.getOrientation()
        );
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mAdapter = new BakingAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        // Init a suitable Loader
        mIsConnected = isNetworkAvailable();
        if (mIsConnected) {
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.activity_main_relativeLayout), getResources().getText(R.string.connected_to_internet), Snackbar.LENGTH_SHORT);
            snackbar.show();

            getSupportLoaderManager().initLoader(LOADER_ID_FROM_WEB, null,this);
            // JsonUtils.loadUrlData(this);
        }
        else {
            getSupportLoaderManager().initLoader(LOADER_ID_FROM_DB, null,this);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_share) {
            Toast.makeText(this, "Not yet implemented!", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String sortOrder = BakingContract.CakeColumns._ID + " ASC";

        switch (id) {
            case LOADER_ID_FROM_DB:
                return new CursorLoader(this, BakingProvider.Cakes.CONTENT_URI,
                        null, null, null, sortOrder);
            case LOADER_ID_FROM_WEB:
                getContentResolver().delete(BakingProvider.Cakes.CONTENT_URI, null, null);
                JsonUtils.loadUrlData(this);
                return new CursorLoader(this, BakingProvider.Cakes.CONTENT_URI,
                        null, null, null, sortOrder);
            default:
                Log.e(LOG_TAG, "Unhandled Loader ID!");
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    private boolean checkIfTablet() {
        boolean istablet = false;

        DisplayMetrics metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float yInches= metrics.heightPixels/metrics.ydpi;
        float xInches= metrics.widthPixels/metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches*xInches + yInches*yInches);
        if (diagonalInches>=6.5){
            istablet = true;
        }
        return istablet;
    }
}
