package com.example.vibhavapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.example.vibhavapp.data.MyDbHandler;

public class ViewPdf extends AppCompatActivity {

    private WebView webView;
    public static final int PDF_SELECTION_CODE = 13;
    private MyDbHandler db;
    private String name;
    private boolean showFile = false;
    private String uri;
    private String fullUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pdf);

        db = new MyDbHandler(ViewPdf.this);
        webView = findViewById(R.id.webView);

        final Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        name = bundle.getString("subjectName");

        final Intent intent1 = getIntent();
        Bundle bundle1 = intent1.getExtras();
        showFile = bundle1.getBoolean("showFile");
        uri = bundle1.getString("uri");

        if (showFile)
            showPdfFromUri(uri);
        else
            selectPdfFromStorage();
    }

    private void selectPdfFromStorage() {
        Intent browseStorage = new Intent(Intent.ACTION_GET_CONTENT);
        browseStorage.setType("application/pdf");
        browseStorage.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult( Intent.createChooser(browseStorage, "Select PDF"), PDF_SELECTION_CODE );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PDF_SELECTION_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedPdfFromStorage = data.getData();
//            showPdfFromUri(selectedPdfFromStorage);
            db.addMedia(name, selectedPdfFromStorage.toString());
        }
        finish();
    }

    private void showPdfFromUri(String uri) {
//        fullUri = "https://drive.google.com/viewerng/viewer?embedded=true&url=" + "https://drive.google.com/file/d/1XZOWFT8LnqkD-V5q68cNbBUuP4v0e3Ny/view?usp=sharing";

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
//        webView.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//                super.onProgressChanged(view, newProgress);
//
//                if (newProgress == 100) {
//
//                }
//            }
//        });

        webView.loadUrl(uri);
    }
}
