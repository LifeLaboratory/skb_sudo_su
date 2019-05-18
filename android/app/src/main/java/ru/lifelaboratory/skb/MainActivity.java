package ru.lifelaboratory.skb;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_main: {
                    mTextMessage.setText(R.string.title_main);
                    Intent toMainActivity = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(toMainActivity);
                    return true;
                }
                case R.id.navigation_profile:
                    mTextMessage.setText(R.string.title_profile);
                    Intent toProfileActivity = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(toProfileActivity);
                    return true;
                case R.id.navigation_search:
                    mTextMessage.setText(R.string.title_search);
                    Intent toSearchActivity = new Intent(MainActivity.this, SearchActivity.class);
                    startActivity(toSearchActivity);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        ((Button) findViewById(R.id.btn_scan)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
