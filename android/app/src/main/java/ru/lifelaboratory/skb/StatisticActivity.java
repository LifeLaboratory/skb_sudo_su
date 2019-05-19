package ru.lifelaboratory.skb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.lifelaboratory.skb.REST.Statistic;

public class StatisticActivity extends AppCompatActivity {


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_main: {
                    Intent toMainActivity = new Intent(StatisticActivity.this, MainActivity.class);
                    startActivity(toMainActivity);
                    return true;
                }
                case R.id.navigation_profile:
                    Intent toProfileActivity = new Intent(StatisticActivity.this, ProfileActivity.class);
                    startActivity(toProfileActivity);
                    return true;
                case R.id.navigation_search:
                    Intent toSearchActivity = new Intent(StatisticActivity.this, SearchActivity.class);
                    startActivity(toSearchActivity);
                    return true;
            }
            return false;
        }
    };

    PieChartView pieChartView = null;
    PieChartView pieChartView1 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        pieChartView = findViewById(R.id.chart);
        pieChartView1 = findViewById(R.id.chart_stat);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        SharedPreferences sp = getSharedPreferences(Constants.STORAGE, Context.MODE_PRIVATE);
        ru.lifelaboratory.skb.REST.Statistic toServerStatistic = MainActivity.server.create(ru.lifelaboratory.skb.REST.Statistic.class);
        toServerStatistic.top(sp.getInt(Constants.USER_ID, -1), 1000)
                .enqueue(new Callback<List<Statistic.Item>>() {
                    @Override
                    public void onResponse(Call<List<Statistic.Item>> call, Response<List<Statistic.Item>> response) {
                        List<SliceValue> pieData = new ArrayList<>();

                        int[] colors = new int[]{Color.RED, Color.BLACK, Color.BLUE, Color.MAGENTA, Color.YELLOW, Color.GREEN};

                        for (int i = 0; i < response.body().size(); i++) {
                            if (response.body().get(i).getCategory() != null && response.body().get(i).getExpired() != 0.0)
                                pieData.add(new SliceValue(response.body().get(i).getExpired().intValue(), colors[i]).setLabel(response.body().get(i).getCategory().concat(" ").concat(String.valueOf(response.body().get(i).getExpired().intValue()))));
                        }
                        PieChartData pieChartData = new PieChartData(pieData);
                        pieChartData.setHasLabels(true);
                        pieChartView.setPieChartData(pieChartData);
                    }

                    @Override
                    public void onFailure(Call<List<Statistic.Item>> call, Throwable t) { }
                });

        toServerStatistic.all(sp.getInt(Constants.USER_ID, -1), 1000)
                .enqueue(new Callback<List<Statistic.Item>>() {
                    @Override
                    public void onResponse(Call<List<Statistic.Item>> call, Response<List<Statistic.Item>> response) {
                        List<SliceValue> pieData = new ArrayList<>();

                        int[] colors = new int[]{Color.RED, Color.BLACK, Color.BLUE, Color.MAGENTA, Color.YELLOW, Color.GREEN, Color.CYAN};

                        pieData.add(new SliceValue(response.body().get(0).getAll().intValue(), colors[0]).setLabel("Свежие ".concat(String.valueOf(response.body().get(0).getAll().intValue()))));
                        pieData.add(new SliceValue(response.body().get(0).getExpired().intValue(), colors[2]).setLabel("Испорченные ".concat(String.valueOf(response.body().get(0).getExpired().intValue()))));

                        PieChartData pieChartData = new PieChartData(pieData);
                        pieChartData.setHasLabels(true);
                        pieChartView1.setPieChartData(pieChartData);
                    }

                    @Override
                    public void onFailure(Call<List<Statistic.Item>> call, Throwable t) { }
                });
    }

}
