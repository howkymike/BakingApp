package com.howky.mike.bakingapp.IngredientWidget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.howky.mike.bakingapp.R;
import com.howky.mike.bakingapp.provider.BakingContract;
import com.howky.mike.bakingapp.provider.BakingProvider;
import com.howky.mike.bakingapp.utils.JsonUtils;

public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private Cursor mData;
    private Intent mIntent;
    private long mCakeId;
    String[] mIngredints;



    public WidgetDataProvider(Context context, Intent intent) {
        mContext = context;
        mIntent = intent;
        mCakeId = intent.getLongExtra(WidgetService.CAKE_ID_EXTRA, 1);
    }

    private void initCursor(){
        if (mData != null) {
            mData.close();
        }
        final long identityToken = Binder.clearCallingIdentity();

        if (mCakeId == 0) {
            mCakeId = 1;
        }
        mData = mContext.getContentResolver().query(BakingProvider.Cakes.withID(mCakeId),
                null,
                null,
                null,
                null);

        if (mData == null || mData.getCount() < 1) {
            return;
        }

        try {
            mData.moveToFirst();
            String stringJsonIngredients = mData.getString(mData.getColumnIndex(BakingContract.CakeColumns.INGREDIENTS));
            mIngredints = JsonUtils.getIngredients(stringJsonIngredients);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Binder.restoreCallingIdentity(identityToken);
    }


    @Override
    public void onCreate() {
        initCursor();
        if (mData != null) {
            mData.moveToFirst();
        }
    }

    @Override
    public void onDataSetChanged() {
        initCursor();
    }

    @Override
    public void onDestroy() {
        mData.close();
    }

    @Override
    public int getCount() {
        return mIngredints.length;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.ingredient_list_wdget);
        remoteViews.setTextViewText(R.id.widget_ingredient_tv ,mIngredints[position]);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
