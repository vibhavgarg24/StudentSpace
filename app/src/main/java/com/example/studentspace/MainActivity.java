package com.example.studentspace;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;

public class MainActivity extends AppCompatActivity {

     @SuppressLint("SourceLockedOrientationActivity")
     @Override
    protected void onCreate(Bundle savedInstanceState) {

         File f = new File ("/data/data/com.example.studentspace/shared_prefs/newInstall.xml");
         if (f.exists()) {
             Log.d("attman", "File_Exist");
             SharedPreferences shrd = getSharedPreferences("newInstall", MODE_PRIVATE);
             int attendanceCriteriaInt = shrd.getInt("attendanceCriteria",-1);
             if (attendanceCriteriaInt != -1) {
                Intent intent = new Intent(this, subjects.class);
                startActivity(intent);
                finish();
             }
         }
         else {
             SharedPreferences shrd = getSharedPreferences("newInstall", MODE_PRIVATE);
             SharedPreferences.Editor editor = shrd.edit();
             editor.putInt("newInstall", 1);
             editor.apply();
             Log.d("attman", "shrd_created");
         }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

         final SeekBar seekBar = findViewById(R.id.seekBar);
         final TextView attendanceCriteria = findViewById(R.id.attendanceCriteria);

         seekBar.setMax(100);
         seekBar.setProgress(15);

         seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
             @Override
             public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                 attendanceCriteria.setText(progress + "%");
             }

             @Override
             public void onStartTrackingTouch(SeekBar seekBar) {
             }
             @Override
             public void onStopTrackingTouch(SeekBar seekBar) {
             }
         });
    }

    boolean doubleBackToExitPressedOnce;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    public void welcomeClick (View view) {
        Log.d("attman", "wel_clicked");

        String attendanceCriteriaString ;
        int attendanceCriteriaInt ;
        TextView attendanceCriteria = findViewById(R.id.attendanceCriteria);
        attendanceCriteriaString = attendanceCriteria.getText().toString();
        attendanceCriteriaInt = Integer.parseInt(attendanceCriteriaString.substring(0,attendanceCriteriaString.length()-1));

        SharedPreferences shrd = getSharedPreferences("newInstall", MODE_PRIVATE);
        SharedPreferences.Editor editor = shrd.edit();
        editor.putInt("attendanceCriteria", attendanceCriteriaInt);
        editor.apply();

        Intent intent = new Intent (this, subjects.class);
        startActivity(intent);
        finish();
    }

    public void plus1ButtonClick (View view) {
        SeekBar seekBar = findViewById(R.id.seekBar);
        TextView attendanceCriteria = findViewById(R.id.attendanceCriteria);

        int progress = seekBar.getProgress() + 1;
        if (progress != 101) {
            seekBar.setProgress(progress);
            attendanceCriteria.setText(progress + "%");
        }
    }

    public void minus1ButtonClick (View view) {
        SeekBar seekBar = findViewById(R.id.seekBar);
        TextView attendanceCriteria = findViewById(R.id.attendanceCriteria);

        int progress = seekBar.getProgress() - 1;
        if (progress != -1) {
            seekBar.setProgress(progress);
            attendanceCriteria.setText(progress + "%");
        }
    }

}
