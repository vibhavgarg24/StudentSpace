package com.example.vibhavapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.vibhavapp.data.MyDbHandler;

public class GridViewAdapter extends BaseAdapter {

    private Context context;

//    public int[] images = {R.drawable.one, R.drawable.two, R.drawable.three, R.drawable.four,
//            R.drawable.one, R.drawable.two, R.drawable.three, R.drawable.four,
//            R.drawable.one, R.drawable.two, R.drawable.three, R.drawable.four,
//            R.drawable.one, R.drawable.two, R.drawable.three, R.drawable.four,
//            R.drawable.one, R.drawable.two, R.drawable.three, R.drawable.four,
//            R.drawable.one, R.drawable.two, R.drawable.three, R.drawable.four};
    public String[] images ;

    public GridViewAdapter(Context context, String[] images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return images[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(context);
        imageView.setImageURI(null);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(5, 5, 5, 5);
        Glide.with(context).load(Uri.parse(images[position])).into(imageView);
//        imageView.setImageURI(Uri.parse(images[position]));
//        imageView.setImageResource(images[position]);
        imageView.setLayoutParams(new GridView.LayoutParams(340,340));
        return imageView;
    }
}
