package com.example.studentspace;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.example.studentspace.data.MyDbHandler;
import java.io.File;

public class ImageViewer extends AppCompatActivity {

    private ViewPager imageViewPager;
    private ImageAdapter imageAdapter;
    private String[] images;
    private int position;
    private String name;
    private Boolean optionsVisible;

    LinearLayout imageOptions;
    ImageButton imageOptionsDelete;
    ImageButton imageOptionsShare;
    MyDbHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        getSupportActionBar().hide();

        Intent intent = getIntent();
        position = intent.getExtras().getInt("position");
        name = intent.getExtras().getString("subjectName");

        db = new MyDbHandler(this);
        images = db.getMedia(name);

        imageAdapter = new ImageAdapter(this, images);
        imageViewPager = findViewById(R.id.imageViewPager);
        imageViewPager.setAdapter(imageAdapter);
        imageViewPager.setCurrentItem(position);

// Delete Photo
        imageOptionsDelete = findViewById(R.id.imageOptionsDelete);
        imageOptionsDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteImage(v, position);
            }
        });

// Share Photo
        imageOptionsShare = findViewById(R.id.imageOptionsShare);
        imageOptionsShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareImage(v, position);
            }
        });


        imageViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(final int position) {
// Delete Photo
                imageOptionsDelete = findViewById(R.id.imageOptionsDelete);
                imageOptionsDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteImage(v, position);
                    }
                });

// Share Photo
                imageOptionsShare = findViewById(R.id.imageOptionsShare);
                imageOptionsShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shareImage(v, position);
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

    private void shareImage(final View v, final int position) {
        Uri uri = Uri.parse(images[position]);
        String path = uri.getPath();
        File file = new File(path);
        Uri uri1 = FileProvider.getUriForFile(v.getContext(), "com.example.android.fileprovider", file);
        Log.d("attman", "Share Fp: " + uri1);
//        Log.d("attman", "Share uri: "+ urimethod);

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri1);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/*");
        startActivity(Intent.createChooser(intent, "Share Image"));
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
