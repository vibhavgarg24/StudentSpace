package com.example.vibhavapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.audiofx.Equalizer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vibhavapp.data.MyDbHandler;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.karumi.dexter.listener.single.SnackbarOnPermanentlyDeniedPermissionListener;

import java.util.List;

public class insideSubject extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_subject);

        SharedPreferences shrd = getSharedPreferences("newInstall", MODE_PRIVATE);
        final int attendanceCriteriaAll = shrd.getInt("attendanceCriteria", -1);

        MyDbHandler db = new MyDbHandler(insideSubject.this);

//        final EditText subjectName = findViewById(R.id.subjectName);
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

//        int attendanceCriteriaInt = db.getCriteria(name);

//        subjectName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                subjectName.setEnabled(true);
//            }
//        });
//
//        subjectName.setText(name);

        if (!toAttend.equals(""))
            toAttendText.setText(toAttend);
        else
            toAttendText.setText(" --- ");

        if (!notes.equals(""))
            notesView.setText(db.getNotes(name));

//                seekBar enable/disable using checkBox

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
//                if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                    Toast.makeText(v.getContext(), "Please Allow Storage Permission to continue.", Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent();
//                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                    Uri uri = Uri.fromParts("package", getPackageName(), null);
//                    intent.setData(uri);
//                    startActivity(intent);
//                }
                PermissionListener snkBarPermissionListener = SnackbarOnPermanentlyDeniedPermissionListener.Builder
                        .with(v, "Storage Access Required for Media")
                        .withOpenSettingsButton("SETTINGS")
                        .withCallback(new Snackbar.Callback()  {
                            @Override
                            public void onDismissed(Snackbar transientBottomBar, int event) {

                            }

                            @Override
                            public void onShown(Snackbar sb) {

                            }
                        }).build();

                Dexter.withContext(v.getContext())
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
//                                Toast.makeText(v.getContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(v.getContext(), subject_media.class);
                                intent.putExtra("subjectName", name);
                                startActivity(intent);

//                                Intent intent1 = new Intent(v.getContext(), ImageViewer.class);
//                                startActivity(intent1);

                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
//                                permissionDeniedResponse == PackageManager.
//                                        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) v.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                                            Toast.makeText(v.getContext(), "DNW..", Toast.LENGTH_SHORT).show();
//                                        }
//                                if (PermissionU)
//                                PermissionListener permissionListener = SnackbarOnPermanentlyDeniedPermissionListener.Builder
//                                        .with(v, "Storage Access Required for Media")
//                                        .withOpenSettingsButton("SETTINGS")
//                                        .withCallback(new Snackbar.Callback()  {
//                                            @Override
//                                            public void onDismissed(Snackbar transientBottomBar, int event) {
//
//                                            }
//                                            @Override
//                                            public void onShown(Snackbar sb) {
//
//                                            }
//                                        }).build();
                                if ( permissionDeniedResponse.isPermanentlyDenied()) {
//                                    Toast.makeText(v.getContext(), "Ik..", Toast.LENGTH_SHORT).show();
                                    Snackbar sb = Snackbar.make(findViewById(R.id.inSubParentLayout),"Requires Storage Permission to continue.",Snackbar.LENGTH_LONG)
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
//        EditText subjectName = findViewById(R.id.subjectName);
        final TextView criteriaInSubText = findViewById(R.id.criteriaInSubText);
        final CheckBox checkBox = findViewById(R.id.checkBox);

        final String notes = notesView.getText().toString();
        String notesDb = db.getNotes(name);
//        final String newName =  subjectName.getText().toString().toUpperCase().trim();

        boolean criteriaChanged = false;
        int sCriteria = 0;
        final String criteriaString = criteriaInSubText.getText().toString();
//        if ( !criteriaString.equals("")) {
            int criteriaInt = Integer.parseInt(criteriaString.substring(0, criteriaString.length() - 1));
//            criteriaChanged = (criteriaInt == db.getCriteria(name) ) ? false : true;
//        }

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

//        if (newName.equals(""))
//            Toast.makeText(insideSubject.this, "Subject Name can't be Empty", Toast.LENGTH_SHORT).show();
//        else if ( subjectsName.contains(newName) )
//            Toast.makeText(insideSubject.this, "Duplicate Subject Found", Toast.LENGTH_SHORT).show();
//        else {
            if (!notes.equals(notesDb)  || criteriaChanged) {
                AlertDialog.Builder builder = new AlertDialog.Builder(insideSubject.this);
                builder.setCancelable(false);
                builder.setTitle("SAVE CHANGES?");
                final boolean finalCriteriaChanged = criteriaChanged;
                final int finalSCriteria = sCriteria;
                builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        db.updateName(name, newName);
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
//        }
    }

    public void saveChangesButtonClick (View view) {

//        SharedPreferences shrd = getSharedPreferences("newInstall", MODE_PRIVATE);
//        final int attendanceCriteriaAll = shrd.getInt("attendanceCriteria", -1);

        MyDbHandler db = new MyDbHandler(insideSubject.this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String name = bundle.getString("subjectName");

//        EditText subjectName = findViewById(R.id.subjectName);
        TextView notesView = findViewById(R.id.notesView);
        final TextView criteriaInSubText = findViewById(R.id.criteriaInSubText);
        final CheckBox checkBox = findViewById(R.id.checkBox);

        String notes = db.getNotes(name);
        String newNotes = notesView.getText().toString();
//        String newName = subjectName.getText().toString().toUpperCase().trim();

        boolean criteriaChanged = false;
        String criteriaString = criteriaInSubText.getText().toString();
//        if ( !criteriaString.equals("")) {
        int criteriaInt = Integer.parseInt(criteriaString.substring(0, criteriaString.length() - 1));
//            criteriaChanged = (criteriaInt == db.getCriteria(name) ) ? false : true;
//        }

        if (checkBox.isChecked() && (db.getScriteria(name) == 1) ) {
            criteriaChanged = (criteriaInt == db.getCriteria(name)) ? false : true ;
        } else if (checkBox.isChecked() && (db.getScriteria(name) == 0)) {
            db.setScriteria(name, 1);
            criteriaChanged = true;
        } else if ( !checkBox.isChecked() && (db.getScriteria(name) == 1)) {
            db.setScriteria(name, 0);
            criteriaChanged = true;
        } else
            criteriaChanged = false;

//        List<String> subjectsName = db.getSubjectsName();
//        subjectsName.remove(name);

//        if (newName.equals(""))
//            Toast.makeText(view.getContext(), "Subject Name can't be Empty", Toast.LENGTH_SHORT).show();
//        else if ( subjectsName.contains(newName) )
//            Toast.makeText(view.getContext(), "Duplicate Subject Found", Toast.LENGTH_SHORT).show();
//        else {
        if ( !newNotes.equals(notes) || criteriaChanged ) {
//                db.updateName(name, newName);
            db.addNotes(name, newNotes);
//
            if (criteriaChanged) {
                db.setCriteria(name, Integer.parseInt(criteriaString.substring(0, criteriaString.length() - 1)));
            }
            Toast.makeText(view.getContext(), "Changes Saved Successfully", Toast.LENGTH_SHORT).show();
        }
//            finish();
//        }
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
//                finish();
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

    /*public static boolean hasPermission(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
                    return false;
            }
        }
        return true;
    }*/
}
