package ru.lifelaboratory.skb;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.lifelaboratory.skb.REST.Item;

public class ItemInfoActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_main: {
                    Intent toMainActivity = new Intent(ItemInfoActivity.this, MainActivity.class);
                    startActivity(toMainActivity);
                    return true;
                }
                case R.id.navigation_profile:
                    Intent toProfileActivity = new Intent(ItemInfoActivity.this, ProfileActivity.class);
                    startActivity(toProfileActivity);
                    return true;
                case R.id.navigation_search:
                    Intent toSearchActivity = new Intent(ItemInfoActivity.this, SearchActivity.class);
                    startActivity(toSearchActivity);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        String whatToSearch = getIntent().getStringExtra(Constants.ITEM_ID);
        Item search = MainActivity.server.create(Item.class);
        search.search("code", whatToSearch).enqueue(new Callback<List<ru.lifelaboratory.skb.Entity.Item>>() {
            @Override
            public void onResponse(Call<List<ru.lifelaboratory.skb.Entity.Item>> call, Response<List<ru.lifelaboratory.skb.Entity.Item>> response) {
                mTextMessage.setText(response.body().get(0).getTitle());
            }

            @Override
            public void onFailure(Call<List<ru.lifelaboratory.skb.Entity.Item>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Ошибка соединения с сервером", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
