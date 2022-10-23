package com.example.studentspace;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import com.example.studentspace.data.MyDbHandler;
import java.io.File;

public class DocAdapter extends RecyclerView.Adapter<DocAdapter.ViewHolder> {
    private Context context;
    private String[] docPathList;
    private String[] docNameList;
    private MyDbHandler db;
    private String name;
    private DocAdapter adapter;
    private Uri uri;

    private ActionMode actionMode;
    private DocFragment docFragment;
    private boolean contextModeEnabled = false;
    private int longClickPosition;

    public DocAdapter(Context context, String name, String[] docPathList, String[] docNameList, DocFragment docFragment) {
        this.context = context;
        this.name = name;
        this.docPathList = docPathList;
        this.docNameList = docNameList;
        this.db = new MyDbHandler(context);
        this.adapter = this;
        this.docFragment = docFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doc_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        Uri uri1 = null;

// fixed hanging view for null string
        if (docPathList[0].equals("")) {
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setEnabled(false);
        }

// URI INIT

        else {
        uri = Uri.parse(docPathList[position]);
        String path = uri.getPath();
        File file = new File(path);
        uri1 = FileProvider.getUriForFile(context, "com.example.android.fileprovider", file);
//            Log.d("attman", "FinalClickUri: " + uri1);
//            Log.d("attman", "FinalExt: " + getFileExt(uri1));
        }

// Setting Texts
        holder.docName.setText(docNameList[position]);
        if (uri1 != null) {
            holder.docIcon.setText(getFileExt(uri1));
        }


// DOC CLICK
        final Uri finalUri = uri1;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!contextModeEnabled) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        //    intent.putExtra(Intent.EXTRA_STREAM, uri1);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.setData(finalUri);
                        context.startActivity(Intent.createChooser(intent, "Open File With.."));
                    }
                }
            });


// DOC LONG CLICK
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (actionMode != null) {
                    return false;
                }
                actionMode = ( (AppCompatActivity) docFragment.getActivity()).startSupportActionMode(myCallback);
                longClickPosition = holder.getAdapterPosition();
                v.setBackgroundColor(Color.DKGRAY);
                contextModeEnabled = true;
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return docNameList.length;
    }

    private ActionMode.Callback myCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.drawer_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.drawerMenuDelete:
                    deleteDoc(longClickPosition);
//                    Toast.makeText(context, "Delete Clicked " + longClickPosition, Toast.LENGTH_SHORT).show();
                    mode.finish();
                    return true;
                case R.id.drawerMenuShare:
                    shareDoc(longClickPosition);
//                    Toast.makeText(context, "Share Clicked " + longClickPosition, Toast.LENGTH_SHORT).show();
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            contextModeEnabled =  false;
            actionMode = null;
            docFragment.onResume();
        }
    };

    private void shareDoc(int longClickPosition) {
        Uri uri = Uri.parse(docPathList[longClickPosition]);
        String path = uri.getPath();
        File file = new File(path);
        Uri uri1 = FileProvider.getUriForFile(context, "com.example.android.fileprovider", file);
        Log.d("attman", "Share Fp: " + uri1);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri1);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("*/*");
        context.startActivity(Intent.createChooser(intent, "Share Document"));
    }

    private void deleteDoc(final int longClickPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Document");
        builder.setMessage("Confirm to Delete this Document");
        builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(context, "Delete position: " + position, Toast.LENGTH_SHORT).show();
                db.deleteDocName(name, longClickPosition);
                db.deleteDocPath(name, longClickPosition);
                docFragment.onResume();
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView docName;
        TextView docIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            docName = itemView.findViewById(R.id.docName);
            docIcon = itemView.findViewById(R.id.docIcon);

        }
    }

    private String getFileExt(Uri uri) {
        ContentResolver c = context.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(c.getType(uri));
    }

}