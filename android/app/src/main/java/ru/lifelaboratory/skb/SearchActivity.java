package ru.lifelaboratory.skb;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.lifelaboratory.skb.REST.Item;
import ru.lifelaboratory.skb.REST.User;

public class SearchActivity extends AppCompatActivity {

    private TextView searchView;
    private MainListAdapter mainListAdapter = null;
    private ListView mainList = null;
    private ArrayList<ru.lifelaboratory.skb.Entity.Item> items;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_main: {
                    Intent toMainActivity = new Intent(SearchActivity.this, MainActivity.class);
                    startActivity(toMainActivity);
                    return true;
                }
                case R.id.navigation_profile:
                    Intent toProfileActivity = new Intent(SearchActivity.this, ProfileActivity.class);
                    startActivity(toProfileActivity);
                    return true;
                case R.id.navigation_search:
                    Intent toSearchActivity = new Intent(SearchActivity.this, SearchActivity.class);
                    startActivity(toSearchActivity);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        ru.lifelaboratory.skb.REST.Item toServerItem = MainActivity.server.create(ru.lifelaboratory.skb.REST.Item.class);
        toServerItem.getList(0)
                .enqueue(new Callback<List<ru.lifelaboratory.skb.Entity.Item>>() {
                    @Override
                    public void onResponse(Call<List<ru.lifelaboratory.skb.Entity.Item>> call, Response<List<ru.lifelaboratory.skb.Entity.Item>> response) {
                        items.clear();
                        items.addAll(response.body());
                        mainListAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<List<ru.lifelaboratory.skb.Entity.Item>> call, Throwable t) {

                    }
                });

        searchView = (EditText) findViewById(R.id.search);
        items = new ArrayList<>();
        mainList = (ListView) findViewById(R.id.lvsearch);
        mainListAdapter = new MainListAdapter(SearchActivity.this, items);
        mainList.setAdapter(mainListAdapter);
        searchView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) { // Enter
                    String type = "name";
                    String whatToSearch = searchView.getText().toString();
                    if(whatToSearch.matches("[0-9]+")) type = "code";
                    else type = "name";
                    
                    Item search = MainActivity.server.create(Item.class);
                    search.search(type, whatToSearch).enqueue(new Callback<List<ru.lifelaboratory.skb.Entity.Item>>() {
                        @Override
                        public void onResponse(Call<List<ru.lifelaboratory.skb.Entity.Item>> call, Response<List<ru.lifelaboratory.skb.Entity.Item>> response) {
                            items.clear();
                            items.addAll(response.body());
                            mainListAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(Call<List<ru.lifelaboratory.skb.Entity.Item>> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "Ошибка соединения с сервером", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                return false;
            }
        });
    }

}
