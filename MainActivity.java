package com.example.miner0001;

import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private WebView webView;
    private StratumClient stratumClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        stratumClient = new StratumClient(webView);
        webView.addJavascriptInterface(new WebAppInterface(), "Android");
        webView.loadUrl("file:///android_asset/index.html");
    }

    private class WebAppInterface {
        @JavascriptInterface
        public void startMining(String pool, String wallet, String worker, String password) {
            stratumClient.connect(pool, wallet + "." + worker, password);
        }

        @JavascriptInterface
        public void stopMining() {
            stratumClient.disconnect();
        }
    }
}