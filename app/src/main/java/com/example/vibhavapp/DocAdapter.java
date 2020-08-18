package com.example.vibhavapp;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ActionMode;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vibhavapp.adapter.recyclerViewAdapter;
import com.example.vibhavapp.data.MyDbHandler;

import java.io.File;
import java.util.ArrayList;

public class DocAdapter extends RecyclerView.Adapter<DocAdapter.ViewHolder> {
    private Context context;
    private String[] docPathList;
    private String[] docNameList;
    private MyDbHandler db;
    private DocAdapter adapter;
    private Uri uri;
//    private Uri uri1;

//    DocFragment docFragment;

    public DocAdapter(Context context, String[] docPathList, String[] docNameList) {
        this.context = context;
        this.docPathList = docPathList;
        this.docNameList = docNameList;
        this.db = new MyDbHandler(context);
        this.adapter = this;
//        this.docFragment = docFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doc_card, parent, false);
//        return new ViewHolder(view, docFragment);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        // fixed hanging view for null string
        if (docPathList[0].equals("")) {
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setEnabled(false);
        }

// URI INIT
//        else {
        uri = Uri.parse(docPathList[position]);
        String path = uri.getPath();
        File file = new File(path);
        final Uri uri1 = FileProvider.getUriForFile(context, "com.example.android.fileprovider", file);
//            Log.d("attman", "FinalClickUri: " + uri1);
//            Log.d("attman", "FinalExt: " + getFileExt(uri1));
//        }

// Setting Texts
        holder.docName.setText(docNameList[position]);
        if (!uri1.toString().equals("")) {
            holder.docIcon.setText(getFileExt(uri1));
        }


// DOC CLICK
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                //    intent.putExtra(Intent.EXTRA_STREAM, uri1);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setData(uri1);
                context.startActivity(Intent.createChooser(intent, "Open File With.."));
            }
        });


    }

    @Override
    public int getItemCount() {
        return docNameList.length;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView docName;
        TextView docIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            docName = itemView.findViewById(R.id.docName);
            docIcon = itemView.findViewById(R.id.docIcon);

//            itemView.setOnLongClickListener(docFragment);
//            itemView.setOnClickListener(this);
        }

//        @Override
//        public void onClick(View v) {
//            docFragment.makeSekection(v, getAdapterPosition());
//        }
    }

//    public void removeItems(ArrayList<Integer> selectedList) {
//        Log.d("attman", "Menu Delete: " + selectedList);
//    }

    private String getFileExt(Uri uri) {
        ContentResolver c = context.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(c.getType(uri));
    }

}