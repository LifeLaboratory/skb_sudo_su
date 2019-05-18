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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.lifelaboratory.skb.Entity.Item;
import ru.lifelaboratory.skb.REST.User;

public class ProfileActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_main: {
                    Intent toMainActivity = new Intent(ProfileActivity.this, MainActivity.class);
                    startActivity(toMainActivity);
                    return true;
                }
                case R.id.navigation_profile:
                    Intent toProfileActivity = new Intent(ProfileActivity.this, ProfileActivity.class);
                    startActivity(toProfileActivity);
                    return true;
                case R.id.navigation_search:
                    Intent toSearchActivity = new Intent(ProfileActivity.this, SearchActivity.class);
                    startActivity(toSearchActivity);
                    return true;
            }
            return false;
        }
    };

    Dialog authDialog = null;
    Dialog registerDialog = null;
    View mainView = null;
    View dialogView = null;
    ArrayList<Item> items = null;
    MainListAdapter mainListAdapter = null;
    ListView mainList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mainView = (View) findViewById(R.id.container);


        SharedPreferences sp = getSharedPreferences(Constants.STORAGE, Context.MODE_PRIVATE);
        if (sp.getInt(Constants.USER_ID, -1) != -1) {
            ((LinearLayout) findViewById(R.id.ifNotAuth)).setVisibility(View.INVISIBLE);
            ((LinearLayout) findViewById(R.id.ifAuth)).setVisibility(View.VISIBLE);


            // список покупок
            items = new ArrayList<>();

            mainListAdapter = new MainListAdapter(this, items);
            mainList = (ListView) findViewById(R.id.lvMain);
            mainList.setAdapter(mainListAdapter);
            mainListAdapter.notifyDataSetChanged();

            ru.lifelaboratory.skb.REST.Item toServerItem = MainActivity.server.create(ru.lifelaboratory.skb.REST.Item.class);
            toServerItem.sales(sp.getInt(Constants.USER_ID, -1))
                    .enqueue(new Callback<List<Item>>() {
                        @Override
                        public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                            items.clear();
                            items.addAll(response.body());
                            mainListAdapter.notifyDataSetChanged();
                        }
                        @Override
                        public void onFailure(Call<List<Item>> call, Throwable t) { }
                    });
        }


        // авторизация
        ((Button) findViewById(R.id.btn_login)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authDialog = new Dialog(ProfileActivity.this);
                authDialog.setTitle("Вход");
                authDialog.setContentView(R.layout.dialog_auth);
                authDialog.show();

                ((Button) authDialog.findViewById(R.id.btn_dialog_cancel)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        authDialog.cancel();
                    }
                });

                ((Button) authDialog.findViewById(R.id.btn_dialog_auth)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        User toServerUser = MainActivity.server.create(User.class);
                        EditText loginEditText = (EditText) authDialog.findViewById(R.id.dialog_login);
                        EditText passwordEditText = (EditText) authDialog.findViewById(R.id.dialog_password);
                        toServerUser.auth(new ru.lifelaboratory.skb.Entity.User(loginEditText.getText().toString(), passwordEditText.getText().toString()))
                                .enqueue(new Callback<ru.lifelaboratory.skb.Entity.User>() {
                            @Override
                            public void onResponse(Call<ru.lifelaboratory.skb.Entity.User> call, Response<ru.lifelaboratory.skb.Entity.User> response) {
                                if (response.body().getId() != null) {
                                    SharedPreferences sp = getSharedPreferences(Constants.STORAGE, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor e = sp.edit();
                                    e.putInt(Constants.USER_ID, response.body().getId());
                                    e.apply();

                                    Snackbar.make(mainView, "Авторизация прошла успешно", Snackbar.LENGTH_LONG).show();
                                    authDialog.cancel();
                                    // TODO: сохранение данных в память телефона
                                    ((LinearLayout) findViewById(R.id.ifNotAuth)).setVisibility(View.INVISIBLE);
                                    ((LinearLayout) findViewById(R.id.ifAuth)).setVisibility(View.VISIBLE);
                                } else {
                                    Snackbar.make(mainView, "Ошибка авторизации ", Snackbar.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ru.lifelaboratory.skb.Entity.User> call, Throwable t) {
                                Snackbar.make(mainView, "Ошибка авторизации ", Snackbar.LENGTH_LONG).show();
                                Log.e(Constants.LOG, t.getMessage().concat(" <- ошибка авторизации"));
                            }
                        });
                    }
                });

            }
        });

        // регистрация
        ((Button) findViewById(R.id.btn_register)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerDialog = new Dialog(ProfileActivity.this);
                registerDialog.setTitle("Регистрация");
                registerDialog.setContentView(R.layout.dialog_register);
                registerDialog.show();
                dialogView = registerDialog.findViewById(R.id.dialog_register);

                ((Button) registerDialog.findViewById(R.id.btn_dialog_cancel)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        registerDialog.cancel();
                    }
                });

                ((Button) registerDialog.findViewById(R.id.btn_dialog_auth)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        User toServerUser = MainActivity.server.create(User.class);
                        EditText loginEditText = (EditText) registerDialog.findViewById(R.id.dialog_login);
                        EditText passwordEditText = (EditText) registerDialog.findViewById(R.id.dialog_password);
                        EditText rePasswordEditText = (EditText) registerDialog.findViewById(R.id.dialog_re_password);
                        EditText nameEditText = (EditText) registerDialog.findViewById(R.id.dialog_name);

                        if (!rePasswordEditText.getText().toString().equals(passwordEditText.getText().toString())) {
                            Snackbar.make(dialogView, "Пароли не совпадают", Snackbar.LENGTH_LONG).show();
                        } else {
                            toServerUser.register(new ru.lifelaboratory.skb.Entity.User(loginEditText.getText().toString(), passwordEditText.getText().toString(), nameEditText.getText().toString()))
                                    .enqueue(new Callback<ru.lifelaboratory.skb.Entity.User>() {
                                        @Override
                                        public void onResponse(Call<ru.lifelaboratory.skb.Entity.User> call, Response<ru.lifelaboratory.skb.Entity.User> response) {
                                            Snackbar.make(mainView, "Регистрация прошла успешно", Snackbar.LENGTH_LONG).show();
                                            registerDialog.cancel();
                                            // TODO: сохранение данных в память телефона
                                            ((LinearLayout) findViewById(R.id.ifNotAuth)).setVisibility(View.INVISIBLE);
                                            ((LinearLayout) findViewById(R.id.ifAuth)).setVisibility(View.VISIBLE);
                                            LinearLayout ifNotAuth = findViewById(R.id.ifNotAuth);
                                        }

                                        @Override
                                        public void onFailure(Call<ru.lifelaboratory.skb.Entity.User> call, Throwable t) {
                                            Snackbar.make(mainView, "Ошибка регистрации ", Snackbar.LENGTH_LONG).show();
                                            Log.e(Constants.LOG, t.getMessage().concat(" <- ошибка регистрации"));
                                        }
                                    });

                        }
                    }
                });

            }
        });

        ((Button) findViewById(R.id.btn_exit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = getSharedPreferences(Constants.STORAGE, Context.MODE_PRIVATE);
                SharedPreferences.Editor e = sp.edit();
                e.remove(Constants.USER_ID);
                e.apply();
                ((LinearLayout) findViewById(R.id.ifNotAuth)).setVisibility(View.VISIBLE);
                ((LinearLayout) findViewById(R.id.ifAuth)).setVisibility(View.INVISIBLE);
            }
        });
    }

}
