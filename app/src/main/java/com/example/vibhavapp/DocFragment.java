package com.example.vibhavapp;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.example.vibhavapp.data.MyDbHandler;

import java.util.ArrayList;

public class DocFragment extends Fragment {

    static String name;
    private RecyclerView docRecyclerView;
    private DocAdapter docAdapter;
    private String[] docPathList;
    private String[] docNameList;

//    boolean isContextualModeEnabled = false;
//    private ArrayList<Integer> selectedList;
//    private Toolbar toolbar;

    public DocFragment(String name) {
        this.name = name;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_doc, container, false);

//        toolbar = root.findViewById(R.id.toolbar);
//        selectedList = new ArrayList<>();

// DB Creation
        MyDbHandler db = new MyDbHandler(root.getContext());
        docNameList = db.getDocNames(name);
        docPathList = db.getDocPaths(name);

// RecyclerView Init
        docRecyclerView = root.findViewById(R.id.docRecyclerView);
        docRecyclerView.setHasFixedSize(true);
        docRecyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));

// Using RecyclerView
        docAdapter = new DocAdapter(root.getContext(), docPathList, docNameList);
        docRecyclerView.setAdapter(docAdapter);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        MyDbHandler db = new MyDbHandler(getContext());
        docPathList = db.getDocPaths(name);
        docNameList = db.getDocNames(name);

        docAdapter = new DocAdapter(getContext(), docPathList, docNameList);
        docRecyclerView.setAdapter(docAdapter);
    }

    //    @Override
//    public boolean onLongClick(View v) {
//        isContextualModeEnabled = true;
//        toolbar.inflateMenu(R.menu.drawer_menu);
//        toolbar.setBackgroundColor(Color.BLACK);
//        docAdapter.notifyDataSetChanged();
//
//        return true;
//    }
//
//    public void makeSekection(View v, int adapterPosition) {
////        if ( ((CheckBox) v).isChecked()) {
////
////        }
//
//        v.setBackgroundColor(Color.RED);
//        selectedList.add(adapterPosition);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() == R.id.drawerMenuDelete) {
//            docAdapter.removeItems(selectedList);
//            removeContextualActionMode();
//        } else if (item.getItemId() == android.R.id.home) {
//            removeContextualActionMode();
//            docAdapter.notifyDataSetChanged();
//        }
//
//        return true;
//    }
//
//    @SuppressLint("ResourceAsColor")
//    private void removeContextualActionMode() {
//        isContextualModeEnabled = false;
//        toolbar.getMenu().clear();
//        toolbar.setBackgroundColor(R.color.colorPrimary);
//        selectedList.clear();
//        docAdapter.notifyDataSetChanged();
//
//    }

    //    @Override
//    public void onResume() {
//        super.onResume();
//
//        MyDbHandler db = new MyDbHandler(getContext());
//        docNameList = db.getDocNames(name);
//        docPathList = db.getDocPaths(name);
//
//        docAdapter = new DocAdapter(getContext(), docPathList, docNameList, DocFragment.this);
//        docRecyclerView.setAdapter(docAdapter);
//    }

}

