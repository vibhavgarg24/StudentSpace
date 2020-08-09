package com.example.vibhavapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.vibhavapp.data.MyDbHandler;
//import com.github.barteksc.pdfviewer.PDFView;

public class PdfViewActivity extends AppCompatActivity {

    public static final int PDF_SELECTION_CODE = 13;
//    private PDFView pdfView;
    private MyDbHandler db;
    private String name;
    private boolean showFile = false;
    private String uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);

//        getSupportActionBar().setHideOnContentScrollEnabled(true);

        db = new MyDbHandler(PdfViewActivity.this);
//        pdfView = findViewById(R.id.pdfView);

        final Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        name = bundle.getString("subjectName");

        final Intent intent1 = getIntent();
        Bundle bundle1 = intent1.getExtras();
        showFile = bundle1.getBoolean("showFile");
        uri = bundle1.getString("uri");

        if (showFile)
            showPdfFromUri(Uri.parse(uri));
        else
            selectPdfFromStorage();
    }

    private void selectPdfFromStorage() {
//        Toast.makeText(this, "selectPDF", Toast.LENGTH_LONG).show();
        Intent browseStorage = new Intent(Intent.ACTION_GET_CONTENT);
        browseStorage.setType("application/pdf");
        browseStorage.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(
                Intent.createChooser(browseStorage, "Select PDF"), PDF_SELECTION_CODE
        );
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

    public void showPdfFromUri(Uri selectedPdfFromStorage) {
//        pdfView.fromUri(selectedPdfFromStorage).load();
    }
}


