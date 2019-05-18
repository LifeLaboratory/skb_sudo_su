package ru.lifelaboratory.skb;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

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
                        Snackbar.make(view, "Туть будет авторизация", Snackbar.LENGTH_LONG).show();
                    }
                });

            }
        });
    }

}
