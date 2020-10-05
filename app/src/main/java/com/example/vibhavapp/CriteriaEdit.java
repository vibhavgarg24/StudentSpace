package com.example.vibhavapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.vibhavapp.data.MyDbHandler;
import java.util.List;

public class CriteriaEdit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criteria_edit);
        getSupportActionBar().setTitle("Edit Attendance Criterion");

        final SeekBar seekBar2 = findViewById(R.id.seekBar2);
        final TextView attendanceCriteria2 = findViewById(R.id.attendanceCriteria2);

        SharedPreferences shrd = getSharedPreferences("newInstall", MODE_PRIVATE);
        final int attendanceCriteriaInt2 = shrd.getInt("attendanceCriteria", -1);

        seekBar2.setMax(100);
        seekBar2.setProgress(attendanceCriteriaInt2);
        attendanceCriteria2.setText(attendanceCriteriaInt2 + "%");


        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                attendanceCriteria2.setText(progress + "%");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        Button updateCriteriaButton = findViewById(R.id.updateCriteriaButton);
        updateCriteriaButton.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                final MyDbHandler db = new MyDbHandler(CriteriaEdit.this);
                final List<String> allSubsNames = db.getSubjectsName();

                boolean hasScriteria = false;
                for (String name : allSubsNames) {
                    if (db.getScriteria(name) == 1) {
                        hasScriteria = true;
                        break;
                    }
                }

                String attendanceCriteriaString2 ;
                final int attendanceCriteriaInt3 ;
                TextView attendanceCriteria2 = findViewById(R.id.attendanceCriteria2);
                attendanceCriteriaString2 = attendanceCriteria2.getText().toString();
                attendanceCriteriaInt3 = Integer.parseInt(attendanceCriteriaString2.substring(0,attendanceCriteriaString2.length()-1));

                SharedPreferences shrd = getSharedPreferences("newInstall", MODE_PRIVATE);
                SharedPreferences.Editor editor = shrd.edit();
                editor.putInt("attendanceCriteria", attendanceCriteriaInt3);
                editor.apply();

                if (hasScriteria) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("Modify Criterion");
                    builder.setMessage("Update criterion of Subjects with specific criterion too?");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            db.editCriteriaAll(attendanceCriteriaInt3);
                            for (String name : allSubsNames) {
                                db.setScriteria(name, 0);
                            }
                            dialog.cancel();

                            Toast.makeText(CriteriaEdit.this, "Changes Saved Successfully", Toast.LENGTH_SHORT).show();

                            finish();

                        }
                    });
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            db.editCriteria(attendanceCriteriaInt3);
                            dialog.cancel();

                            if (attendanceCriteriaInt3 != attendanceCriteriaInt2)
                                Toast.makeText(CriteriaEdit.this, "Changes Saved Successfully", Toast.LENGTH_SHORT).show();
                            finish();

                        }
                    });
                    builder.setCancelable(true);
                    builder.show();
                }
                else {
                    if (attendanceCriteriaInt3 != attendanceCriteriaInt2)
                        Toast.makeText(CriteriaEdit.this, "Changes Saved Successfully", Toast.LENGTH_SHORT).show();
                        db.editCriteria(attendanceCriteriaInt3);
                    finish();
                }
            }
        });

        Button plus1Button2 = findViewById(R.id.plus1Button2);
        plus1Button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int progress = seekBar2.getProgress() + 1;
                if (progress != 101) {
                    seekBar2.setProgress(progress);
                    attendanceCriteria2.setText(progress + "%");
                }
            }
        });

        Button minus1Button2 = findViewById(R.id.minus1Button2);
        minus1Button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int progress = seekBar2.getProgress() - 1;
                if (progress != -1) {
                    seekBar2.setProgress(progress);
                    attendanceCriteria2.setText(progress + "%");
                }
            }
        });
    }
}
