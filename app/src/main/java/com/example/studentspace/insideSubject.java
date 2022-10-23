package com.example.studentspace;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.studentspace.data.MyDbHandler;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import java.util.List;

public class insideSubject extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_subject);

        SharedPreferences shrd = getSharedPreferences("newInstall", MODE_PRIVATE);
        final int attendanceCriteriaAll = shrd.getInt("attendanceCriteria", -1);

        MyDbHandler db = new MyDbHandler(insideSubject.this);

        TextView notesView = findViewById(R.id.notesView);
        TextView toAttendText = findViewById(R.id.toAttendInSub);
        final CheckBox checkBox = findViewById(R.id.checkBox);
        final SeekBar seekBar3 = findViewById(R.id.seekBar3);
        final TextView criteriaInSubText = findViewById(R.id.criteriaInSubText);
        final Button plus1Button3 = findViewById(R.id.plus1Button3);
        final Button minus1Button3 = findViewById(R.id.minus1Button3);
        final ImageButton btnMedia = findViewById(R.id.btnMedia);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        final String name = bundle.getString("subjectName");
        String toAttend = bundle.getString("toAttendText");
        String notes = db.getNotes(name);

        setTitle(name);

        if (!toAttend.equals(""))
            toAttendText.setText(toAttend);
        else
            toAttendText.setText(" --- ");

        if (!notes.equals(""))
            notesView.setText(db.getNotes(name));

        final int subjectCriteria = db.getCriteria(name);
        final int scriteria = db.getScriteria(name);
        criteriaInSubText.setText(subjectCriteria + "%");
        seekBar3.setProgress(subjectCriteria);
        if (scriteria == 1) {
            checkBox.setChecked(true);
            seekBar3.setEnabled(true);
            plus1Button3.setVisibility(View.VISIBLE);
            minus1Button3.setVisibility(View.VISIBLE);
            seekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    criteriaInSubText.setText(progress + "%");
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        } else {
            checkBox.setChecked(false);
            seekBar3.setEnabled(false);
            plus1Button3.setVisibility(View.INVISIBLE);
            minus1Button3.setVisibility(View.INVISIBLE);
            criteriaInSubText.setTextColor(Color.GRAY);
        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                seekBar3.setEnabled(isChecked);
                if (isChecked) {
                    plus1Button3.setVisibility(View.VISIBLE);
                    minus1Button3.setVisibility(View.VISIBLE);
                    seekBar3.setProgress(subjectCriteria);
                    criteriaInSubText.setText(subjectCriteria + "%");
                    criteriaInSubText.setTextColor(Color.WHITE);
                    seekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            criteriaInSubText.setText(progress + "%");
                        }
                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {
                        }
                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                        }
                    });
                }
                else {
                    plus1Button3.setVisibility(View.INVISIBLE);
                    minus1Button3.setVisibility(View.INVISIBLE);
                    seekBar3.setProgress(attendanceCriteriaAll);
                    criteriaInSubText.setText(attendanceCriteriaAll + "%");
                    criteriaInSubText.setTextColor(Color.GRAY);
                }
            }
        });


        plus1Button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int progress = seekBar3.getProgress() + 1;
                if (progress != 101) {
                    seekBar3.setProgress(progress);
                    criteriaInSubText.setText(progress + "%");
                }
            }
        });


        minus1Button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int progress = seekBar3.getProgress() - 1;
                if (progress != -1) {
                    seekBar3.setProgress(progress);
                    criteriaInSubText.setText(progress + "%");
                }
            }
        });


        btnMedia.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(final View v) {
                Dexter.withContext(v.getContext())
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent = new Intent(v.getContext(), subject_media.class);
                                intent.putExtra("subjectName", name);
                                startActivity(intent);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                                if ( permissionDeniedResponse.isPermanentlyDenied()) {
                                    Snackbar sb = Snackbar.make(findViewById(R.id.inSubParentLayout),"Requires Storage Permission to Access the Subject Drawer.",Snackbar.LENGTH_LONG)
                                            .setAction("SETTINGS", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent();
                                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                                                    intent.setData(uri);
                                                    startActivity(intent);
                                                }
                                            });
                                    sb.show();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });
    }

    @Override
    public void onBackPressed() {
        final MyDbHandler db = new MyDbHandler(insideSubject.this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        final String name = bundle.getString("subjectName");

        TextView notesView = findViewById(R.id.notesView);
        final TextView criteriaInSubText = findViewById(R.id.criteriaInSubText);
        final CheckBox checkBox = findViewById(R.id.checkBox);

        final String notes = notesView.getText().toString();
        String notesDb = db.getNotes(name);

        boolean criteriaChanged = false;
        int sCriteria = 0;
        final String criteriaString = criteriaInSubText.getText().toString();
        int criteriaInt = Integer.parseInt(criteriaString.substring(0, criteriaString.length() - 1));

        if (checkBox.isChecked() && (db.getScriteria(name) == 1) ) {
            sCriteria = 1;
            criteriaChanged = (criteriaInt == db.getCriteria(name)) ? false : true ;
        } else if (checkBox.isChecked() && (db.getScriteria(name) == 0)) {
            sCriteria = 1;
            criteriaChanged = true;
        } else if ( !checkBox.isChecked() && (db.getScriteria(name) == 1)) {
            sCriteria = 0;
            criteriaChanged = true;
        } else {
            criteriaChanged = false;
        }
        List<String> subjectsName = db.getSubjectsName();
        subjectsName.remove(name);

        if (!notes.equals(notesDb)  || criteriaChanged) {
            AlertDialog.Builder builder = new AlertDialog.Builder(insideSubject.this);
            builder.setCancelable(false);
            builder.setTitle("SAVE CHANGES?");
            final boolean finalCriteriaChanged = criteriaChanged;
            final int finalSCriteria = sCriteria;
            builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    db.addNotes(name, notes);

                    if (finalCriteriaChanged) {
                        db.setCriteria(name, Integer.parseInt(criteriaString.substring(0, criteriaString.length()-1)));
                    }

                    db.setScriteria(name, finalSCriteria);

                    Toast.makeText(insideSubject.this, "Changes Saved Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    finish();
                }
            });
            builder.setCancelable(true);
            builder.show();
        } else {
            super.onBackPressed();
        }
    }

    public void saveChangesButtonClick (View view) {
        MyDbHandler db = new MyDbHandler(insideSubject.this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String name = bundle.getString("subjectName");

        TextView notesView = findViewById(R.id.notesView);
        final TextView criteriaInSubText = findViewById(R.id.criteriaInSubText);
        final CheckBox checkBox = findViewById(R.id.checkBox);

        String notes = db.getNotes(name);
        String newNotes = notesView.getText().toString();

        boolean criteriaChanged = false;
        String criteriaString = criteriaInSubText.getText().toString();
        int criteriaInt = Integer.parseInt(criteriaString.substring(0, criteriaString.length() - 1));

        if (checkBox.isChecked() && (db.getScriteria(name) == 1) ) {
            criteriaChanged = (criteriaInt == db.getCriteria(name)) ? false : true ;
        } else if (checkBox.isChecked() && (db.getScriteria(name) == 0)) {
            db.setScriteria(name, 1);
            criteriaChanged = true;
        } else if ( !checkBox.isChecked() && (db.getScriteria(name) == 1)) {
            db.setScriteria(name, 0);
            criteriaChanged = true;
        } else {
            criteriaChanged = false;
        }

        if ( !newNotes.equals(notes) || criteriaChanged ) {
            db.addNotes(name, newNotes);
            if (criteriaChanged) {
                db.setCriteria(name, Integer.parseInt(criteriaString.substring(0, criteriaString.length() - 1)));
            }
            Toast.makeText(view.getContext(), "Changes Saved Successfully", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    public void deleteNotesButtonClick (final View view) {
        final MyDbHandler db = new MyDbHandler(insideSubject.this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        final String name = bundle.getString("subjectName");
        TextView notesView = findViewById(R.id.notesView);
        String notes = notesView.getText().toString();

        if ( !notes.equals("") ) {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("Delete Notes:");
            builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    TextView notesView = findViewById(R.id.notesView);

                    notesView.setText("");
                    db.deleteNotes(name);
                    Toast.makeText(view.getContext(), "Notes Deleted", Toast.LENGTH_SHORT).show();

                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.setMessage("Confirm to Delete Notes");
            builder.show();
        } else {
            Toast.makeText(view.getContext(), "Notes Already Empty", Toast.LENGTH_SHORT).show();
        }
    }
}
