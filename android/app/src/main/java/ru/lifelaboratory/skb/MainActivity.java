package ru.lifelaboratory.skb;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.lifelaboratory.skb.Entity.Item;
import ru.lifelaboratory.skb.service.NotificationService;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_main: {
                    Intent toMainActivity = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(toMainActivity);
                    return true;
                }
                case R.id.navigation_profile:
                    Intent toProfileActivity = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(toProfileActivity);
                    return true;
                case R.id.navigation_search:
                    Intent toSearchActivity = new Intent(MainActivity.this, SearchActivity.class);
                    startActivity(toSearchActivity);
                    return true;
            }
            return false;
        }
    };

    MainListAdapter mainListAdapter = null;
    ListView mainList = null;
    public static Retrofit server = null;
    Dialog saleDialog = null;
    Dialog haveDialog = null;
    ArrayList<Item> items = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        (findViewById(R.id.btn_scan)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toScan = new Intent(MainActivity.this, ScanActivity.class);
                startActivity(toScan);
            }
        });

        server = new Retrofit.Builder()
                .baseUrl(Constants.SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // основной список
        items = new ArrayList<>();

        mainListAdapter = new MainListAdapter(this, items);
        mainList = (ListView) findViewById(R.id.lvMain);
        mainListAdapter.setDeleteListStatus(true);
        mainList.setAdapter(mainListAdapter);
        mainListAdapter.notifyDataSetChanged();

        SharedPreferences sp = getSharedPreferences(Constants.STORAGE, Context.MODE_PRIVATE);
        if (sp.getInt(Constants.USER_ID, -1) != -1) {
            ru.lifelaboratory.skb.REST.Item toServerItem = MainActivity.server.create(ru.lifelaboratory.skb.REST.Item.class);
            toServerItem.getUserList(0, sp.getInt(Constants.USER_ID, -1))
                    .enqueue(new Callback<List<Item>>() {
                        @Override
                        public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                            items.clear();
                            items.addAll(response.body());
                            mainListAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(Call<List<Item>> call, Throwable t) {

                        }
                    });
        }

        if (sp.getInt(Constants.USER_ID, -1) != -1) {
            NotificationChannel channel = new NotificationChannel("skb_chanel", "skb_chanel", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("skb_chanel");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            // запуск службы
            AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(getApplicationContext(), NotificationService.class);
            intent.putExtra(Constants.USER_ID, sp.getInt(Constants.USER_ID, -1));
            PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            long startTime = System.currentTimeMillis();
            long day = 1000 * 60 * 60 * 24;
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, startTime, day, pendingIntent);
        }
    }

}
