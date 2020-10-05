package com.example.vibhavapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.example.vibhavapp.data.MyDbHandler;
import java.util.ArrayList;

public class ReceiveMedia extends AppCompatActivity {

    private RecyclerView recRecyclerView;
    private ReceiveAdapter receiveAdapter;
    private ArrayList<String > recSubNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_media);

        getSupportActionBar().setTitle("Add Media to..");

//         DB Creation
        MyDbHandler db = new MyDbHandler(this);
        recSubNames = db.getSubjectsName();


        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (recSubNames.isEmpty()) {
            Toast.makeText(this, "No Subject Found", Toast.LENGTH_SHORT).show();

        } else {

//        RecyclerViewInit
            recRecyclerView = findViewById(R.id.receiveRecyclerView);
            recRecyclerView.setHasFixedSize(true);
            recRecyclerView.setLayoutManager(new LinearLayoutManager(this));

//        Using RecyclerView
            receiveAdapter = new ReceiveAdapter(this, recSubNames, db, intent);
            recRecyclerView.setAdapter(receiveAdapter);

            if (Intent.ACTION_SEND.equals(action) && type != null) {
                if ("text/plain".equals(type)) {
//                    Toast.makeText(this, "Plain Text Received", Toast.LENGTH_SHORT).show();
                    getSupportActionBar().setTitle("Add Notes to... ");
                } else if (type.startsWith("image/")) {
//                    Toast.makeText(this, "Single Image Received", Toast.LENGTH_SHORT).show();
                    getSupportActionBar().setTitle("Add Image to... ");
                } else {
                    getSupportActionBar().setTitle("Add Document to...");
                }
            } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
                if (type.startsWith("image/")) {
//                    Toast.makeText(this, "Multiple Images Received", Toast.LENGTH_SHORT).show();
                    getSupportActionBar().setTitle("Add Images to... ");
                } else {
                    getSupportActionBar().setTitle("Add Documents to...");
                }
            }
        }
    }
}