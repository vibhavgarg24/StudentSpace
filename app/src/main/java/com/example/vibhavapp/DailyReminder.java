package com.example.vibhavapp;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import java.util.Calendar;

public class DailyReminder extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

//    int PENDING_INTENT_REQUEST_CODE = 11;
    int hour, min;
//    int notificationID = 23;
    Boolean calendarSetOnce = false, reminderSwitchState = false;
    Boolean monState, tueState, wedState, thuState, friState, satState, sunState;
    String timeInDb;
    Switch reminderSwitch;
    CheckBox mon, tue, wed, thu, fri, sat, sun;
    TextView monTv, tueTv, wedTv, thuTv, friTv, satTv, sunTv, time, timeTv;
    TimePickerDialog timePicker;
//    Calendar initCalendar = Calendar.getInstance();
//    Calendar calendar = Calendar.getInstance();
//    AlarmManager alarmManager;
    SharedPreferences shrd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_reminder);

        getSupportActionBar().setTitle("Daily Reminder");

        shrd = getSharedPreferences("newInstall", MODE_PRIVATE);
        calendarSetOnce = shrd.getBoolean("calendarSetOnce",false);
        reminderSwitchState = shrd.getBoolean("reminderSwitchState", false);
        monState = shrd.getBoolean("monState", true);
        tueState = shrd.getBoolean("tueState", true);
        wedState = shrd.getBoolean("wedState", true);
        thuState = shrd.getBoolean("thuState", true);
        friState = shrd.getBoolean("friState", true);
        satState = shrd.getBoolean("satState", true);
        sunState = shrd.getBoolean("sunState", false);
        timeInDb = shrd.getString("timeInDb", "08:00PM");

//        initCalendar();
//        calendar = Calendar.getInstance();

//        initCalendar.set(Calendar.HOUR_OF_DAY, 20);
//        initCalendar.set(Calendar.MINUTE, 0);
//        initCalendar.set(Calendar.SECOND, 0);

        timePicker = TimePickerDialog.newInstance(DailyReminder.this, 20, 0, 0, false);
        timePicker.setThemeDark(true);

        initCbandTv();
        initHrMin();

//        if (reminderSwitch.isChecked()) {
//            checkDays();
//        }
        reminderSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    enableCb();
                    enableTv();

                    shrd.edit().putBoolean("reminderSwitchState", true).apply();

                    checkDays();

                    Toast.makeText(DailyReminder.this, "Reminder Set At: " + time.getText(), Toast.LENGTH_SHORT).show();
                } else {
                    disableCb();
                    disableTv();

                    shrd.edit().putBoolean("reminderSwitchState", false).apply();

                    for (int i=1; i<8; i++) {
                        cancelNotification(i);
                    }

                    Toast.makeText(DailyReminder.this, "Reminder Cancelled", Toast.LENGTH_SHORT).show();
                }
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker.show(getFragmentManager(), "TimePickerDialog");

            }
        });
    }

    private void initHrMin() {
        String timeString = time.getText().toString();
        if (timeString.contains("AM")) {
            hour = Integer.parseInt(timeString.substring(0, 2));
        } else if (timeString.contains("PM")) {
            hour = Integer.parseInt(timeString.substring(0, 2)) + 12;
            if (hour == 24) {
                hour = 0;
            }
        }
        min = Integer.parseInt(timeString.substring(3, 5));
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
//        Calendar calendar = Calendar.getInstance();



//        calendar.set(Calendar.);
//        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
//        calendar.set(Calendar.MINUTE, minute);
//        calendar.set(Calendar.SECOND, 0);

        hour = hourOfDay;
        min = minute;

        calendarSetOnce = true;
        shrd.edit().putBoolean("calendarSetOnce", true).apply();

        for (int i=1; i<8; i++) {
            cancelNotification(i);
        }

        setTimeinTv(hourOfDay, minute);

        checkDays();
//        Log.d("attman", "Time: "+calendar.getTime());

        Toast.makeText(DailyReminder.this, "Reminder Set At: " + time.getText(), Toast.LENGTH_SHORT).show();
    }

    private void startNotification(int weekDay) {
        Calendar calendar = Calendar.getInstance();
//        Calendar calendar = Calendar.getInstance();
//        if (!calendarSetOnce) {
//            calendar = initCalendar;
//        }

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(DailyReminder.this, NotificationReceiver.class);
        intent.putExtra("NOTIFICATION_ID", weekDay);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(DailyReminder.this, weekDay, intent, 0);

//        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
//            calendar.add(Calendar.DATE, 7);
//        }
//        if (create) {

//        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.DAY_OF_WEEK, weekDay);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 7);
        }

        Log.d("attman", "Day: " + weekDay + " " +calendar.getTime());
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
//        }
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 7 * (AlarmManager.INTERVAL_DAY), pendingIntent);
//        calendar.clear();

//        } else {
//            alarmManager.cancel(pendingIntent);
//        }
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 7*(AlarmManager.INTERVAL_DAY), pendingIntent);
    }

    private void cancelNotification(int notificationID) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(DailyReminder.this, NotificationReceiver.class);
        intent.putExtra("NOTIFICATION_ID", notificationID);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(DailyReminder.this, notificationID, intent, 0);

//        if (alarmManager != null) {
        alarmManager.cancel(pendingIntent);
        Log.d("attman", "cancelled: " + notificationID);
//        }
    }

    private void checkDays() {

        if (mon.isChecked())
            startNotification(2);
//            startNotification(2, true);
        mon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startNotification(2);
                    shrd.edit().putBoolean("monState", true).apply();
                } else {
                    cancelNotification(2);
                    shrd.edit().putBoolean("monState", false).apply();
                }
            }
        });

        if (tue.isChecked())
            startNotification(3);
        tue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startNotification(3);
                    shrd.edit().putBoolean("tueState", true).apply();
                } else {
                    cancelNotification(3);
                    shrd.edit().putBoolean("tueState", false).apply();
                }
            }
        });

        if (wed.isChecked())
            startNotification(4);
        wed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startNotification(4);
                    shrd.edit().putBoolean("wedState", true).apply();
                } else {
                    cancelNotification(4);
                    shrd.edit().putBoolean("wedState", false).apply();
                }
            }
        });

        if (thu.isChecked())
            startNotification(5);
        thu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startNotification(5);
                    shrd.edit().putBoolean("thuState", true).apply();
                } else {
                    cancelNotification(5);
                    shrd.edit().putBoolean("thuState", false).apply();
                }
            }
        });

        if (fri.isChecked())
            startNotification(6);
        fri.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startNotification(6);
                    shrd.edit().putBoolean("friState", true).apply();
                } else {
                    cancelNotification(6);
                    shrd.edit().putBoolean("friState", false).apply();
                }
            }
        });

        if (sat.isChecked())
            startNotification(7);
        sat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startNotification(7);
                    shrd.edit().putBoolean("satState", true).apply();
                } else {
                    cancelNotification(7);
                    shrd.edit().putBoolean("satState", false).apply();
                }
            }
        });

        if (sun.isChecked())
            startNotification(1);
        sun.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startNotification(1);
                    shrd.edit().putBoolean("sunState", true).apply();
                } else {
                    cancelNotification(1);
                    shrd.edit().putBoolean("sunState", false).apply();
                }
            }
        });
    }

    private void setTimeinTv(int hourOfDay, int minute) {
        String timeString = "";
        int hours = (hourOfDay > 12) ? hourOfDay-12 : hourOfDay;
        if (hourOfDay == 0) hours = 12;
        timeString += (hours < 10) ? "0"+hours+":" : hours+":";
        timeString += (minute < 10) ? "0"+minute : minute;
        timeString += (hourOfDay < 12) ? "AM" : "PM";
        time.setText(timeString);

        shrd.edit().putString("timeInDb", timeString).apply();
    }

    private void disableTv() {
        monTv.setTextColor(Color.DKGRAY);
        tueTv.setTextColor(Color.DKGRAY);
        wedTv.setTextColor(Color.DKGRAY);
        thuTv.setTextColor(Color.DKGRAY);
        friTv.setTextColor(Color.DKGRAY);
        satTv.setTextColor(Color.DKGRAY);
        sunTv.setTextColor(Color.DKGRAY);

        time.setTextColor(Color.DKGRAY);
        timeTv.setTextColor(Color.DKGRAY);

        time.setEnabled(false);
    }

    private void disableCb() {
        mon.setEnabled(false);
        tue.setEnabled(false);
        wed.setEnabled(false);
        thu.setEnabled(false);
        fri.setEnabled(false);
        sat.setEnabled(false);
        sun.setEnabled(false);
    }

    private void enableTv() {
        monTv.setTextColor(Color.WHITE);
        tueTv.setTextColor(Color.WHITE);
        wedTv.setTextColor(Color.WHITE);
        thuTv.setTextColor(Color.WHITE);
        friTv.setTextColor(Color.WHITE);
        satTv.setTextColor(Color.WHITE);
        sunTv.setTextColor(Color.WHITE);

        time.setTextColor(Color.WHITE);
        timeTv.setTextColor(Color.WHITE);

        time.setEnabled(true);
    }

    private void enableCb() {
        mon.setEnabled(true);
        tue.setEnabled(true);
        wed.setEnabled(true);
        thu.setEnabled(true);
        fri.setEnabled(true);
        sat.setEnabled(true);
        sun.setEnabled(true);
    }

    private void initCbandTv() {
        reminderSwitch = findViewById(R.id.reminderSwitch);
        reminderSwitch.setChecked(reminderSwitchState);

        mon = findViewById(R.id.mon);
        tue = findViewById(R.id.tue);
        wed = findViewById(R.id.wed);
        thu = findViewById(R.id.thu);
        fri = findViewById(R.id.fri);
        sat = findViewById(R.id.sat);
        sun = findViewById(R.id.sun);

        monTv = findViewById(R.id.monTv);
        tueTv = findViewById(R.id.tueTv);
        wedTv = findViewById(R.id.wedTv);
        thuTv = findViewById(R.id.thuTv);
        friTv = findViewById(R.id.friTv);
        satTv = findViewById(R.id.satTv);
        sunTv = findViewById(R.id.sunTv);

        timeTv = findViewById(R.id.timeTv);
        time = findViewById(R.id.time);
        time.setText(timeInDb);

        mon.setChecked(monState);
        tue.setChecked(tueState);
        wed.setChecked(wedState);
        thu.setChecked(thuState);
        fri.setChecked(friState);
        sat.setChecked(satState);
        sun.setChecked(sunState);

        if (reminderSwitchState) {
            enableCb();
            enableTv();
        } else {
            disableCb();
            disableTv();
        }
    }
}