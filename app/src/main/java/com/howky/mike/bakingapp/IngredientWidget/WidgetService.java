package com.howky.mike.bakingapp.IngredientWidget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class WidgetService extends RemoteViewsService {

    public static final String CAKE_ID_EXTRA = "cakeId";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetDataProvider(this, intent);
    }
}
