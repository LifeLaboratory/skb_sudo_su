package ru.lifelaboratory.skb;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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
                case R.id.navigation_main:
                    Intent toMainActivity = new Intent(ItemInfoActivity.this, MainActivity.class);
                    startActivity(toMainActivity);
                    return true;
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

    private View mainView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);

        mTextMessage = (TextView) findViewById(R.id.item_name);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mainView = (View) findViewById(R.id.container);

        String whatToSearch = getIntent().getStringExtra(Constants.ITEM_ID);
        Log.e(Constants.LOG, whatToSearch);
        Item search = MainActivity.server.create(Item.class);
        search.info(whatToSearch).enqueue(new Callback<List<ru.lifelaboratory.skb.Entity.Item>>() {
            @Override
            public void onResponse(Call<List<ru.lifelaboratory.skb.Entity.Item>> call, Response<List<ru.lifelaboratory.skb.Entity.Item>> response) {
                if (response.body() == null) {
                    Snackbar.make(mainView, "Неверный штрихкод", Snackbar.LENGTH_SHORT).show();
                } else {
                    mTextMessage.setText(response.body().get(0).getTitle());
                    ImageView photo = (ImageView) findViewById(R.id.item_photo);
                    Picasso.with(getApplicationContext())
                            .load(response.body().get(0).getImg())
                            .placeholder(R.drawable.ic_launcher_foreground)
                            .error(R.drawable.ic_launcher_foreground)
                            .into(photo);
                    if (response.body().get(0).getCode() != null)
                        ((TextView) findViewById(R.id.item_gost)).setText(response.body().get(0).getGost());
                    if (response.body().get(0).getWeight() != null)
                        ((TextView) findViewById(R.id.item_weight)).setText(response.body().get(0).getWeight());
                    if (response.body().get(0).getStorageConditions() != null)
                        ((TextView) findViewById(R.id.item_storage_conditions)).setText(response.body().get(0).getStorageConditions());
                    if (response.body().get(0).getGmo() != null)
                        ((TextView) findViewById(R.id.item_gmo)).setText(response.body().get(0).getGmo());
                    if (response.body().get(0).getStorageConditions() != null)
                        ((TextView) findViewById(R.id.item_packing)).setText(response.body().get(0).getStorageConditions());
                    if (response.body().get(0).getEnergy() != null)
                        ((TextView) findViewById(R.id.item_energy)).setText(response.body().get(0).getEnergy());
                    if (response.body().get(0).getShelfLife() != null)
                        ((TextView) findViewById(R.id.item_shelf_life)).setText(response.body().get(0).getShelfLife());
                }
            }

            @Override
            public void onFailure(Call<List<ru.lifelaboratory.skb.Entity.Item>> call, Throwable t) {
                Log.e(Constants.LOG, t.getMessage());
            }
        });
    }

}
