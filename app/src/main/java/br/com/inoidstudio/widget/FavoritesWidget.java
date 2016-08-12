package br.com.inoidstudio.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class FavoritesWidget extends AppWidgetProvider {
    public static final String ACTION_EXTRA = "acao",
                               NEXT_ACTION = "next",
                               PREVIOUS_ACTION = "previous",
                               REMOVE_ACTION = "delete";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_favorites);
        for (int i = 0; i < appWidgetIds.length; i++){
            configClickBts(context, appWidgetIds[i], views);
        }

        appWidgetManager.updateAppWidget(appWidgetIds, views);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);

        Intent it = new Intent(context, FavoritesService.class);
        it.putExtra(ACTION_EXTRA, REMOVE_ACTION);
        it.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        context.startService(it);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);

        context.stopService(new Intent(context, FavoritesService.class));
    }

    private void configClickBts(Context context, int appWidgetId, RemoteViews views){
        views.setOnClickPendingIntent(R.id.btNext, servicePendingIntent(context, NEXT_ACTION, appWidgetId));

        views.setOnClickPendingIntent(R.id.btPrevious, servicePendingIntent(context, PREVIOUS_ACTION, appWidgetId));
    }

    private PendingIntent servicePendingIntent(Context context, String action, int appWidgetId) {
        Intent serviceIntent = new Intent(context, FavoritesService.class);
        serviceIntent.putExtra(ACTION_EXTRA, action);
        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);

        PendingIntent pit = PendingIntent.getService(context, appWidgetId, serviceIntent,0);
        return pit;
    }
}
