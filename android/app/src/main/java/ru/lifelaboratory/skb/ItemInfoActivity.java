package ru.lifelaboratory.skb;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.lifelaboratory.skb.Entity.AddItem;
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
    private Dialog saleDialog = null;
    private Dialog haveDialog = null;
    private ru.lifelaboratory.skb.Entity.Item item = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);

        mTextMessage = (TextView) findViewById(R.id.item_name);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mainView = (View) findViewById(R.id.container);

        Integer whatToSearch = getIntent().getIntExtra(Constants.ITEM_ID, -1);
        Log.e(Constants.LOG, String.valueOf(whatToSearch));
        Item search = MainActivity.server.create(Item.class);
        search.info(whatToSearch.toString()).enqueue(new Callback<List<ru.lifelaboratory.skb.Entity.Item>>() {
            @Override
            public void onResponse(Call<List<ru.lifelaboratory.skb.Entity.Item>> call, Response<List<ru.lifelaboratory.skb.Entity.Item>> response) {
                if (response.body() == null) {
                    Snackbar.make(mainView, "Неверный штрихкод", Snackbar.LENGTH_SHORT).show();
                } else {
                    ItemInfoActivity.this.item = response.body().get(0);
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

        ((Button) findViewById(R.id.btn_have)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                haveDialog = new Dialog(ItemInfoActivity.this);
                haveDialog.setTitle("Добавить");
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
                            SharedPreferences sp = ItemInfoActivity.this.getSharedPreferences(Constants.STORAGE, Context.MODE_PRIVATE);
                            if (sp.getInt(Constants.USER_ID, -1) != -1) {
                                ru.lifelaboratory.skb.REST.Item toServerItem = MainActivity.server.create(ru.lifelaboratory.skb.REST.Item.class);
                                toServerItem.addToNomenclature(new AddItem(sp.getInt(Constants.USER_ID, -1), item.getId()))
                                        .enqueue(new Callback<ru.lifelaboratory.skb.Entity.Item>() {
                                            @Override
                                            public void onResponse(Call<ru.lifelaboratory.skb.Entity.Item> call, Response<ru.lifelaboratory.skb.Entity.Item> response) { }
                                            @Override
                                            public void onFailure(Call<ru.lifelaboratory.skb.Entity.Item> call, Throwable t) { }
                                        });
                            }
                            haveDialog.cancel();
                        }
                    });
            }
        });

        ((Button) findViewById(R.id.btn_sale)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saleDialog = new Dialog(ItemInfoActivity.this);
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
                        SharedPreferences sp = ItemInfoActivity.this.getSharedPreferences(Constants.STORAGE, Context.MODE_PRIVATE);
                        if (sp.getInt(Constants.USER_ID, -1) != -1) {
                            ru.lifelaboratory.skb.REST.Item toServerItem = MainActivity.server.create(ru.lifelaboratory.skb.REST.Item.class);
                            toServerItem.addToSale(new AddItem(sp.getInt(Constants.USER_ID, -1), item.getId()))
                                    .enqueue(new Callback<ru.lifelaboratory.skb.Entity.Item>() {
                                        @Override
                                        public void onResponse(Call<ru.lifelaboratory.skb.Entity.Item> call, Response<ru.lifelaboratory.skb.Entity.Item> response) { }
                                        @Override
                                        public void onFailure(Call<ru.lifelaboratory.skb.Entity.Item> call, Throwable t) { }
                                    });
                        }
                        saleDialog.cancel();
                    }
                });
            }
        });
    }

}
