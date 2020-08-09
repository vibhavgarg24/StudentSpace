package com.example.vibhavapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.vibhavapp.data.MyDbHandler;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class ImageViewer extends AppCompatActivity {

    private ViewPager imageViewPager;
    private ImageAdapter imageAdapter;
    private String[] images;
    private int position;
    private String name;
    private Boolean optionsVisible;

    LinearLayout imageOptions;
    View forClick;
    ImageButton imageOptionsDelete;
    MyDbHandler db;


//    private GridViewAdapter gridViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        getSupportActionBar().hide();

        Intent intent = getIntent();
        position = intent.getExtras().getInt("position");
        name = intent.getExtras().getString("subjectName");
//        gridViewAdapter = new GridViewAdapter(this);
//        imageIds = gridViewAdapter.images;

        db = new MyDbHandler(this);
        images = db.getMedia(name);

        imageAdapter = new ImageAdapter(this, images);
        imageViewPager = findViewById(R.id.imageViewPager);
        imageViewPager.setAdapter(imageAdapter);
        imageViewPager.setCurrentItem(position);


        imageOptionsDelete = findViewById(R.id.imageOptionsDelete);
        imageOptionsDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteImage(v, position);
            }
        });


        imageViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(final int position) {

                imageOptionsDelete = findViewById(R.id.imageOptionsDelete);
                imageOptionsDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteImage(v, position);
                    }
                });
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        imageOptions = findViewById(R.id.imageOptions);
        optionsVisible = false;
        imageViewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "aghh", Toast.LENGTH_SHORT).show();
                if(optionsVisible) {
                    imageOptions.setVisibility(View.INVISIBLE);
                    optionsVisible = false;
                } else {
                    imageOptions.setVisibility(View.VISIBLE);
                    optionsVisible = true;
                }
            }
        });
    }

    public void deleteImage(final View v, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Delete Image");
        builder.setMessage("Confirm to Delete this Image");
        builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(v.getContext(), "Delete position: "+ position, Toast.LENGTH_SHORT).show();
                db.deleteElement(name, position);
                finish();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
}
