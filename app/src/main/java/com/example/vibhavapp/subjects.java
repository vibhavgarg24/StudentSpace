package com.example.vibhavapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;
import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class subjects extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;

    DrawerLayout drawer;
    Toolbar toolbar = null;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);

    ////        recyclerView Initialization
//        recyclerView = findViewById(R.id.recyclerView);
////        recyclerView.addItemDecoration(new DividerItemDecoration(subjects.this,LinearLayoutManager.VERTICAL));
//        recyclerView.setHasFixedSize(true);
//
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    //        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
    //        navigationView.setNavigationItemSelectedListener(this);
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                int id = item.getItemId();
//                Log.d("attman", "Edit_act");
//                if (id == R.id.nav_criteriaEdit) {
//                    item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//                        @Override
//                        public boolean onMenuItemClick(MenuItem item) {
//                            startActivity(new Intent(subjects.this, CriteriaEdit.class));
//                            return true;
//                        }
//                    });
////                    Toast.makeText(subjects.this, "Edit_activity", Toast.LENGTH_SHORT).show();
//                }
//
//                drawer = findViewById(R.id.drawer_layout);
//                drawer.closeDrawer(GravityCompat.START);
//
//                return true;
//            }
//        });
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    //        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
//            @Override
//            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
//
//                int id = destination.getId();
//                FloatingActionButton fab = findViewById(R.id.fab);
//                switch (id) {
//                    case R.id.nav_editCriteria :
//                        fab.hide();
//                        break;
//                    default:
//                        fab.show();
//                }
//            }
//        });


//        DB Creation
//        MyDbHandler db = new MyDbHandler(subjects.this);
//
////        db.addSubject(" ");
////        db.addPresent("Hindi");
////        db.addAbsent("Hindi");
//
//        subjectArrayList = db.getSubjects();
//        subjectNameList = db.getSubjectsName();
//
////        Log.d("attman",""+subjectArrayList.size());
//
////        for (subject subject: subjectArrayList) {
////            Log.d("attman" ,"Id: " + subject.getId() +"\n" +
////                                        "Name: " + subject.getName() +"\n" +
////                                        "Present: " + subject.getPresent() +"\n" +
////                                        "Absent: " + subject.getAbsent() +"\n"  );
////        }
////        subjectNameList.add(sub2.getName());
////        Log.d("attman", subjectArrayList.get(0).getName());
//
////        Using recyclerView
//        recyclerViewAdapter = new recyclerViewAdapter(subjects.this, subjectArrayList);
//        recyclerView.setAdapter(recyclerViewAdapter);
    }

    //    @Override
//    public void onResume() {
//
//        super.onResume();
//        MyDbHandler db = new MyDbHandler(subjects.this);
//        subjectArrayList = db.getSubjects();
//        subjectNameList = db.getSubjectsName();
//        recyclerViewAdapter = new recyclerViewAdapter(subjects.this, subjectArrayList);
//        recyclerView.setAdapter(recyclerViewAdapter);
//    }


//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//
//        if (hasFocus == true) {
//            super.onWindowFocusChanged(hasFocus);
//            MyDbHandler db = new MyDbHandler(subjects.this);
//            subjectArrayList = db.getSubjects();
//            subjectNameList = db.getSubjectsName();
//            recyclerViewAdapter = new recyclerViewAdapter(subjects.this, subjectArrayList);
//            recyclerView.setAdapter(recyclerViewAdapter);
//        }
////        super.onWindowFocusChanged(hasFocus);
//
//    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(navigationView))
            drawer.closeDrawer(navigationView);

        else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.subjects, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void fabClick (View view) {
        Intent intent = new Intent (this, addSubject.class);
        startActivity(intent);
    }

    //    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
//        Log.d("attman", "Edit_act");
//        if (id == R.id.nav_criteriaEdit) {
//            Toast.makeText(subjects.this, "Edit_activity", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(subjects.this, CriteriaEdit.class);
//            startActivity(intent);
//        }
//
//        drawer = findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.setCheckedItem(R.id.nav_home);
    }
}
