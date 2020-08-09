package com.example.vibhavapp;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

public class ImageAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    public String[] images;

    LinearLayout imageOptions;
    ImageButton imageOptionsDelete;

//            = new int[] {R.drawable.cross, R.drawable.plus, R.drawable.tick};

    public ImageAdapter(Context context, String[] images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.photoview, container, false);
        PhotoView photoView = view.findViewById(R.id.photoView);
        Glide.with(context).load(Uri.parse(images[position])).into(photoView);
//        Glide.with(context).load(imageIds[position]).into(photoView);

//        imageOptionsDelete = view.findViewById(R.id.imageOptionsDelete);
//        imageOptionsDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(v.getContext(), "Delete position: "+position, Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        imageOptions = view.findViewById(R.id.imageOptions);
//        photoView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Toast.makeText(v.getContext(), "why", Toast.LENGTH_SHORT).show();
//                if (imageOptions.getVisibility() == View.VISIBLE) {
//                    imageOptions.setVisibility(View.GONE);
//                } else if (imageOptions.getVisibility() == View.GONE)
//                    imageOptions.setVisibility(View.VISIBLE);
//            }
//        });

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }


//    public void hideOptions (View v) {
//        Toast.makeText(v.getContext(), "haha", Toast.LENGTH_SHORT).show();
//    }


}

