package br.com.inoidstudio.widget;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

import java.util.HashMap;
import java.util.Map;

public class FavoritesService extends Service {
    private String[] sites = {
            "yahoo.com.br",
            "google.com.br",
            "developer.android.com",
            "java.com",
            "facebook.com",
            "twitter.com"
    };

    private Map<Integer, Integer> states;

    @Override
    public void onCreate() {
        super.onCreate();
        states = new HashMap<>();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null){
            String action = intent.getStringExtra(FavoritesWidget.ACTION_EXTRA);

            if (action != null){
                if (FavoritesWidget.NEXT_ACTION.equals(action) ||
                        FavoritesWidget.PREVIOUS_ACTION.equals(action)){
                    int appWidgetId = intent.getIntExtra(
                            AppWidgetManager.EXTRA_APPWIDGET_ID,
                            AppWidgetManager.INVALID_APPWIDGET_ID);

                    int position = newPosition(action, appWidgetId);

                    RemoteViews views = new RemoteViews(
                            this.getPackageName(), R.layout.widget_favorites);
                    views.setTextViewText(R.id.tvSite, sites[position]);

                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
                    appWidgetManager.updateAppWidget(appWidgetId, views);
                }else if (FavoritesWidget.REMOVE_ACTION.equals(action)){
                    int[] removedWidgets = intent.getIntArrayExtra(
                            AppWidgetManager.EXTRA_APPWIDGET_IDS);
                    for (int id : removedWidgets){
                        states.remove(id);
                    }
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private int newPosition(String action, int appWigetId){
        int position = 0;

        if (states.containsKey(appWigetId)){
            position = states.get(appWigetId);
        }else {
            states.put(appWigetId, position);
            return position;
        }if (FavoritesWidget.NEXT_ACTION.equals(action)){
            position++;
            if (position >= sites.length){
                position = 0;
            }
        }else if (FavoritesWidget.PREVIOUS_ACTION.equals(action)){
            position--;
            if (position < 0 ){
                position = sites.length -1;
            }
        }

        states.put(appWigetId, position);
        return position;
    }
}
