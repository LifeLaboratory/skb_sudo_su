package ru.lifelaboratory.skb;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import java.util.ArrayList;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.lifelaboratory.skb.Entity.Item;

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
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item("Один"));
        items.add(new Item("Два"));
        items.add(new Item("Три"));

        mainListAdapter = new MainListAdapter(this, items);
        mainList = (ListView) findViewById(R.id.lvMain);
        mainList.setAdapter(mainListAdapter);
        mainListAdapter.notifyDataSetChanged();

        mainList.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this){
            public void onSwipeRight() {
                // свайп слева направо, добавление в список покупок
                saleDialog = new Dialog(MainActivity.this);
                saleDialog.setTitle("Вход");
                saleDialog.setContentView(R.layout.dialog_add_to_sale);
                saleDialog.show();

                ((Button) saleDialog.findViewById(R.id.btn_dialog_no)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        saleDialog.cancel();
                    }
                });

                ((Button) saleDialog.findViewById(R.id.btn_dialog_yes)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        User toServerUser = MainActivity.server.create(User.class);
                        saleDialog.cancel();
                    }
                });
            }
            public void onSwipeLeft() {
                // свайп справа налево, добавление в список наличия
                haveDialog = new Dialog(MainActivity.this);
                haveDialog.setTitle("Вход");
                haveDialog.setContentView(R.layout.dialog_add_to_have);
                haveDialog.show();

                ((Button) haveDialog.findViewById(R.id.btn_dialog_no)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        haveDialog.cancel();
                    }
                });

                ((Button) haveDialog.findViewById(R.id.btn_dialog_yes)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        User toServerUser = MainActivity.server.create(User.class);
                        haveDialog.cancel();
                    }
                });
            }

        });
    }

}
