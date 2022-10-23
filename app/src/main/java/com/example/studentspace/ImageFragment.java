package com.example.studentspace;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import com.example.studentspace.data.MyDbHandler;
import java.util.Arrays;

public class ImageFragment extends Fragment {

    static GridView gridView;
    String[] images;
    static String name;

    public ImageFragment(String name) {
        this.name = name;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
// Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_image, container, false);

        MyDbHandler db = new MyDbHandler(getContext());
        images = db.getMedia(name);

        gridView = root.findViewById(R.id.gridView);
        gridView.setAdapter(new GridViewAdapter(getContext(), images));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!Arrays.equals(images, new String[]{""})) {
                    Intent intent1 = new Intent(getContext(), ImageViewer.class);
                    intent1.putExtra("position", position);
                    intent1.putExtra("subjectName", name);
                    startActivity(intent1);
                }
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        MyDbHandler db = new MyDbHandler(getContext());
        images = db.getMedia(name);
        gridView.setAdapter(new GridViewAdapter(getContext(), images));
    }

}
