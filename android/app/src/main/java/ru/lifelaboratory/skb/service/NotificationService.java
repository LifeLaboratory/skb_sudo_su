package ru.lifelaboratory.skb.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.lifelaboratory.skb.Constants;
import ru.lifelaboratory.skb.Entity.Item;
import ru.lifelaboratory.skb.MainActivity;
import ru.lifelaboratory.skb.R;

public class NotificationService extends Service {

    private Integer idUser = null;

    public NotificationService() { }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(Constants.LOG, "RunAfterBootService onCreate() method.");
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        idUser = intent.getIntExtra(Constants.USER_ID, -1);
        ru.lifelaboratory.skb.REST.Item toServerItem = MainActivity.server.create(ru.lifelaboratory.skb.REST.Item.class);
        toServerItem.getUserListForService(idUser)
                .enqueue(new Callback<List<Item>>() {
                    @Override
                    public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                        for (Item item : response.body()) {
                            try {
                                Log.d(Constants.LOG, item.getExpiredEnd());
                                DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
                                Date oldDate = new Date();
                                Date newDate = format.parse(item.getExpiredEnd());
                                double days = (newDate.getTime()-oldDate.getTime())/1000.0/60.0/60.0/24.0;
                                Log.d(Constants.LOG, String.valueOf(days).concat(" дней"));
                                if (days < 2.0) {
                                    final NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
                                    builder.setContentTitle("Просрочка")
                                            .setAutoCancel(true)
                                            .setChannelId("skb_chanel")
                                            .setColor(getResources().getColor(R.color.colorAccent))
                                            .setStyle(new NotificationCompat.BigTextStyle().bigText("Срок годности у ".concat(item.getName()).concat(" завтра иссякнет")))
                                            .setContentText("Срок годности подходит к концу")
                                            .setSmallIcon(R.mipmap.ic_launcher_round);

                                    final NotificationManager manager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                    manager.notify((int) newDate.getTime(), builder.build());
                                }
                            } catch (ParseException e) {
                                Log.e(Constants.LOG, e.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Item>> call, Throwable t) {

                    }
                });
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
