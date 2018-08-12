package com.howky.mike.bakingapp.IngredientWidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.RemoteViews;

import com.howky.mike.bakingapp.BakingAdapter;
import com.howky.mike.bakingapp.MainActivity;
import com.howky.mike.bakingapp.R;
import com.howky.mike.bakingapp.RecipeDetail.RecipeDetailActivity;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidget extends AppWidgetProvider {

    private static long cakeID;

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getLongExtra(WidgetService.CAKE_ID_EXTRA, -1) != -1) {
            cakeID = intent.getLongExtra(WidgetService.CAKE_ID_EXTRA, -1);
        }
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // intent with cakeId for ListView Adapter
        Intent intent = new Intent(context, WidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.putExtra(WidgetService.CAKE_ID_EXTRA, cakeID);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);
        Intent ingredientIntent;

        if (cakeID == -1 || cakeID == 0) {
            views.setViewVisibility(R.id.widget_empty_layout_tv, View.VISIBLE);
            views.setViewVisibility(R.id.widget_listView, View.GONE);

            ingredientIntent = new Intent(context, MainActivity.class);
        } else {
            views.setViewVisibility(R.id.widget_empty_layout_tv, View.GONE);
            views.setViewVisibility(R.id.widget_listView, View.VISIBLE);

            views.setRemoteAdapter(R.id.widget_listView, intent);

            ingredientIntent = new Intent(context, RecipeDetailActivity.class);
            ingredientIntent.putExtra(BakingAdapter.INTENT_CAKE_ID, cakeID);
        }


        // Implement onClickListener
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 5 , ingredientIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_listView);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}


