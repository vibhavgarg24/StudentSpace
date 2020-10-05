package com.example.vibhavapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.example.vibhavapp.data.MyDbHandler;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class addSubject extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);
        getSupportActionBar().setTitle("Add Subject");
    }

    public void addButtonClick(View view) {

        MyDbHandler db = new MyDbHandler(addSubject.this);
        TextInputEditText enterSubjectName = findViewById(R.id.enterSubjectName);

        String subjectName = enterSubjectName.getText().toString().toUpperCase().trim();
        ArrayList<String> subjectNameList = db.getSubjectsName();

        if (subjectNameList.contains(subjectName)) {
            Toast.makeText(this, "Duplicate Subject Found", Toast.LENGTH_SHORT).show();
        } else if (subjectName.isEmpty()) {
            Toast.makeText(this, "Subject Name can't be Empty", Toast.LENGTH_SHORT).show();
        } else {
            db.addSubject(subjectName);
            Toast.makeText(this, "Subject Added Successfully", Toast.LENGTH_SHORT).show();

            finish();
        }
    }
}
