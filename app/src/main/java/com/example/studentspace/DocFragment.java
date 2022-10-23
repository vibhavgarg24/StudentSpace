package com.example.studentspace;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.studentspace.data.MyDbHandler;

public class DocFragment extends Fragment  {

    static String name;
    private RecyclerView docRecyclerView;
    private DocAdapter docAdapter;
    private String[] docPathList;
    private String[] docNameList;

    public DocFragment(String name) {
        this.name = name;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
// Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_doc, container, false);

// DB Creation
        MyDbHandler db = new MyDbHandler(root.getContext());
        docNameList = db.getDocNames(name);
        docPathList = db.getDocPaths(name);

// RecyclerView Init
        docRecyclerView = root.findViewById(R.id.docRecyclerView);
        docRecyclerView.setHasFixedSize(true);
        docRecyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));

// Using RecyclerView
        docAdapter = new DocAdapter(root.getContext(), name,  docPathList, docNameList, DocFragment.this);
        docRecyclerView.setAdapter(docAdapter);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        MyDbHandler db = new MyDbHandler(getContext());
        docPathList = db.getDocPaths(name);
        docNameList = db.getDocNames(name);

        docAdapter = new DocAdapter(getContext(), name, docPathList, docNameList, this);
        docRecyclerView.setAdapter(docAdapter);
    }

}

