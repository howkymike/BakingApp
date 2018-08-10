package com.howky.mike.bakingapp.IngredientWidget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.util.Log;
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

    public static final String LOG_TAG = WidgetDataProvider.class.getSimpleName();


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
        /**This is done because the widget runs as a separate thread
         when compared to the current app and hence the app's data won't be accessible to it
         because I'm using a content provided **/
//        mData = mContext.getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,
//                new String[]{QuoteColumns._ID, QuoteColumns.SYMBOL, QuoteColumns.BIDPRICE,
//                        QuoteColumns.PERCENT_CHANGE, QuoteColumns.CHANGE, QuoteColumns.ISUP},
//                QuoteColumns.ISCURRENT + " = ?",
//                new String[]{"1"},null);
        if (mCakeId == 0) {
            mCakeId = 1;
        }
        Log.d("WidgetDataProvider", "mCakeId: " + mCakeId);
        mData = mContext.getContentResolver().query(BakingProvider.Cakes.withID(mCakeId),
                null,
                null,
                null,
                null);

        if (mData == null) {
            Log.d(LOG_TAG, "data is null");
            return;
        }
        if (mData.getCount() <1) {
            Log.d(LOG_TAG, "data is <1");
            return;
        }

        try {
            mData.moveToFirst();
            String stringJsonIngredients = mData.getString(mData.getColumnIndex(BakingContract.CakeColumns.INGREDIENTS));
            mIngredints = JsonUtils.getIngredients(stringJsonIngredients);
        } catch (Exception e) {
            e.printStackTrace();
        }
     //   mData.close();
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
        /** Populate your widget's single list item **/
        //mData.moveToPosition(position);
        Log.d(LOG_TAG, "widget ingredients: " + mIngredints[position]);
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
