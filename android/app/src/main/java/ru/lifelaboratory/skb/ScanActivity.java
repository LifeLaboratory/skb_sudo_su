package ru.lifelaboratory.skb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.Result;

import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.lifelaboratory.skb.REST.Item;

public class ScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{
    ZXingScannerView mScannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
    }
    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler((ZXingScannerView.ResultHandler) this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        Log.d("BarcodeResult", rawResult.getText());
        mScannerView.stopCamera();

        Item search = MainActivity.server.create(Item.class);
        search.search("code", rawResult.getText())
                .enqueue(new Callback<List<ru.lifelaboratory.skb.Entity.Item>>() {
                    @Override
                    public void onResponse(Call<List<ru.lifelaboratory.skb.Entity.Item>> call, Response<List<ru.lifelaboratory.skb.Entity.Item>> response) {

                        if (response.body() != null) {
                            if (response.body().size() != 0) {
                                Intent toItemInfo = new Intent(ScanActivity.this, ItemInfoActivity.class);
                                toItemInfo.putExtra(Constants.ITEM_ID, response.body().get(0).getId());
                                startActivity(toItemInfo);
                            } else {
                                Toast.makeText(ScanActivity.this, "Нет информаци о товаре", Toast.LENGTH_SHORT).show();
                                Intent toItemInfo = new Intent(ScanActivity.this, MainActivity.class);
                                toItemInfo.putExtra(Constants.ITEM_ID, response.body().get(0).getId());
                                startActivity(toItemInfo);
                            }

                        }
                    }
                    @Override
                    public void onFailure(Call<List<ru.lifelaboratory.skb.Entity.Item>> call, Throwable t) { }
                });
    }

}
