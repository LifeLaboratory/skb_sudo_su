package ru.lifelaboratory.skb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

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
        // Do something with the result here
        Log.v("BarcodeResult", rawResult.getText()); // Prints scan results
        mScannerView.resumeCameraPreview(this);
    }

}
