package com.example.giaodientest.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.giaodientest.R;

public class ActivityTinTuc extends AppCompatActivity {
    WebView wvTinTuc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tin_tuc);

        wvTinTuc = findViewById(R.id.wvTinTuc);

        Intent intent = getIntent();
        if (intent == null) finish();
        assert intent != null;

        String url = intent.getStringExtra("đường dẫn");
        if (url == null) finish();

//        WebSettings webSettings = wvTinTuc.getSettings();
//        webSettings.setJavaScriptEnabled(true);

        wvTinTuc.setWebViewClient(new WebViewClient());
        wvTinTuc.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        if (wvTinTuc.canGoBack())
            wvTinTuc.goBack();
        else
            super.onBackPressed();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
    }
}