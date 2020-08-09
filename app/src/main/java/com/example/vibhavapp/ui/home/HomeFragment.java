package com.example.vibhavapp.ui.home;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vibhavapp.R;
import com.example.vibhavapp.adapter.recyclerViewAdapter;
import com.example.vibhavapp.data.MyDbHandler;
import com.example.vibhavapp.model.subject;
import com.example.vibhavapp.subjects;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;
    private ImageView emptySub_image;
    private recyclerViewAdapter recyclerViewAdapter;
    public static ArrayList<subject> subjectArrayList;
    public static ArrayList<String> subjectNameList;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
//                textView.setText(s);
            }
        });

        //        DB Creation
        MyDbHandler db = new MyDbHandler(root.getContext());

        subjectArrayList = db.getSubjects();
        subjectNameList = db.getSubjectsName();

        emptySub_image = root.findViewById(R.id.emptySub_image);
//        if (subjectArrayList.isEmpty())
//            emptySub_image.setVisibility(View.VISIBLE);
//        else
            emptySub_image.setVisibility(View.INVISIBLE);

//        recyclerView Initialization
        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));

//        Using recyclerView
        recyclerViewAdapter = new recyclerViewAdapter(root.getContext(), subjectArrayList);
        recyclerView.setAdapter(recyclerViewAdapter);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
//        getActivity().setTitle("Home");
        MyDbHandler db = new MyDbHandler(getContext());
        subjectArrayList = db.getSubjects();
        subjectNameList = db.getSubjectsName();
//        ImageView emptySub_image = findViewById(R.id.emptySub_image);
//        if (subjectArrayList.isEmpty())
//            emptySub_image.setVisibility(View.VISIBLE);
//        else {
            emptySub_image.setVisibility(View.INVISIBLE);
            recyclerViewAdapter = new recyclerViewAdapter
                    (getContext(), subjectArrayList);
            recyclerView.setAdapter(recyclerViewAdapter);
//        }
    }

//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        if (!hidden) {
//            if (subjectArrayList.isEmpty())
//                emptySub_image.setVisibility(View.VISIBLE);
//            else
//                emptySub_image.setVisibility(View.INVISIBLE);
//        }
//    }


//    @Override
//    public void onStart() {
//        super.onStart();
//        if (subjectArrayList.isEmpty())
//            emptySub_image.setVisibility(View.VISIBLE);
//        else
//            emptySub_image.setVisibility(View.INVISIBLE);
//    }


//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//            if (subjectArrayList.isEmpty())
//                emptySub_image.setVisibility(View.VISIBLE);
//            else
//                emptySub_image.setVisibility(View.INVISIBLE);
//        }
//    }


//    @Override
//    public void onPause() {
//        super.onPause();
//        if (subjectArrayList.isEmpty())
//            emptySub_image.setVisibility(View.VISIBLE);
//        else
//            emptySub_image.setVisibility(View.INVISIBLE);
//
//    }
}
