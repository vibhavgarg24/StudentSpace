package com.example.vibhavapp;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vibhavapp.data.MyDbHandler;
import com.google.android.material.snackbar.Snackbar;

import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ReceiveAdapter extends RecyclerView.Adapter<ReceiveAdapter.ViewHolder> {

    Context context;
    ArrayList<String> recSubNameList;
    MyDbHandler db;
    Intent intent;
    String name;
    ConstraintLayout parentLayout;

    public ReceiveAdapter(Context context, ArrayList<String> recSubNameList, MyDbHandler db, Intent intent, ConstraintLayout parentLayout) {
        this.context = context;
        this.recSubNameList = recSubNameList;
        this.db = db;
        this.intent = intent;
        this.parentLayout = parentLayout;
    }

    @NonNull
    @Override
    public ReceiveAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.receive_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiveAdapter.ViewHolder holder, final int position) {


        name = recSubNameList.get(position);
        holder.recSubName.setText(name);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subjectName = recSubNameList.get(position);
                String action = intent.getAction();
                String type = intent.getType();

                if (Intent.ACTION_SEND.equals(action) && type != null) {
                    if ("text/plain".equals(type)) {
                        handleSharedText(subjectName);
                    } else if (type.startsWith("image/")) {
                        handleSharedSingleImage(subjectName);
                    } else {
                        handleSharedSingleDoc(subjectName);
                    }
                } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
                    if (type.startsWith("image/")) {
                        handleSharedMultipleImages(subjectName);
                    } else {
                        handleSharedMultipleDocs(subjectName);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return recSubNameList.size();
    }

    public void handleSharedText (String subjectName) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            String oldNotes = db.getNotes(subjectName);
            String newNotes = oldNotes + "\n" + sharedText;
            db.addNotes(subjectName, newNotes);

            Snackbar sb = Snackbar.make(parentLayout, "Shared Text Added to Notes of " + subjectName, Snackbar.LENGTH_LONG);
            sb.show();
//            Toast.makeText(context, "Shared Text Added to Notes of " + subjectName, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "No Notes to Add", Toast.LENGTH_SHORT).show();
        }
    }

    public void handleSharedSingleImage (String subjectName) {
        Uri uri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (uri != null) {
            String path = getPath(context, uri);
            String fileName = getFileName(uri);
            File file = new File(path);
            Uri uri1 = Uri.fromFile(file);
            db.addMedia(subjectName, uri1.toString());

            Log.d("attman", "Path: " + path + " Name: " + fileName);
            Log.d("attman", "Uri: " + uri1.toString() + " prev: " + uri.toString());

            Snackbar sb = Snackbar.make(parentLayout, "Shared Image Added to Drawer of " + subjectName, Snackbar.LENGTH_LONG);
            sb.show();
//            Toast.makeText(context, "Shared Image Added to Drawer of " + subjectName, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "No Image to Add", Toast.LENGTH_SHORT).show();
        }
    }

    public void handleSharedMultipleImages (String subjectName) {
        ArrayList<Uri> imageUriList = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (imageUriList != null) {
            int count = imageUriList.size();
            for (int i = 0; i < count; i++) {
                Uri uri = (Uri) imageUriList.get(i);
                String path = getPath(context, uri);
                String fileName = getFileName(uri);
                File file = new File(path);
                Uri uri1 = Uri.fromFile(file);
                db.addMedia(subjectName, uri1.toString());

                Log.d("attman", "Path: " + path + " Name: " + fileName);
                Log.d("attman", "Uri: " + uri1.toString() + " prev: " + uri.toString());
            }

            Snackbar sb = Snackbar.make(parentLayout, "Shared Images Added to Drawer of " + subjectName, Snackbar.LENGTH_LONG);
            sb.show();
//            Toast.makeText(context, "Shared Images Added to Drawer of " + subjectName, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "No Images to Add", Toast.LENGTH_SHORT).show();
        }
    }

    public void handleSharedSingleDoc (String subjectName) {
        Uri srcUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (srcUri != null) {
//            Log.d("attman", "srcUri: " + srcUri.toString());
            addDocToDrawer(srcUri, subjectName);

            Snackbar sb = Snackbar.make(parentLayout, "Shared Document Added to Drawer of " + subjectName, Snackbar.LENGTH_LONG);
            sb.show();
//            Toast.makeText(context, "Shared Document Added to Drawer of " + subjectName, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "No Document to Add", Toast.LENGTH_SHORT).show();
        }
    }

    public void handleSharedMultipleDocs (String subjectName) {
        ArrayList<Uri> docUriList = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (docUriList != null) {
            int count = docUriList.size();
            for (int i= 0; i<count; i++) {
                Uri srcUri = (Uri) docUriList.get(i);
                addDocToDrawer(srcUri, subjectName);
            }

            Snackbar sb = Snackbar.make(parentLayout, "Shared Documents Added to Drawer of " + subjectName, Snackbar.LENGTH_LONG);
            sb.show();
//            Toast.makeText(context, "Shared Documents Added to Drawer of " + subjectName, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "No Documents to Add", Toast.LENGTH_SHORT).show();
        }
    }

    private void addDocToDrawer (Uri srcUri, String subjectName) {
        boolean srcFilePathFlag = false;
        String srcFilepath = "";
        String srcFileExt = getFileExt(srcUri);
        String srcFileName = getFileName(srcUri) + "." + srcFileExt;
        Log.d("attman", "srcFileName: " + srcFileName);

        try {
            srcFilepath = getExactPath(context, srcUri, srcFileName);
            Log.d("attman", "srcFilePath: " + srcFilepath);
            srcFilePathFlag = true;
        } catch (Exception e) {
            Log.d("attman", "srcFilePath Error " + e.getMessage());
            Toast.makeText(context, "File Path Error", Toast.LENGTH_SHORT).show();
        }

        if (srcFilePathFlag) {
            try {
                String desFilePath = saveFileInternal(srcFilepath, srcFileName, srcFileExt);
                File file = new File(desFilePath);
                Uri toSave = Uri.fromFile(file);
                db.addDocPath(subjectName, toSave.toString());
                db.addDocName(subjectName, srcFileName);
            } catch (Exception e) {
                Log.d("attman", "Save File Error");
                Toast.makeText(context, "Save File Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView recSubName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            recSubName = itemView.findViewById(R.id.recSubName);
        }
    }

    public String getPath(Context context, Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return null;
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
//                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    result = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.TITLE));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private String getFileExt(Uri uri) {
        ContentResolver c = context.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(c.getType(uri));
    }

    private String saveFileInternal(String srcFilePath, String srcFileName, String srcFileExt) throws IOException {
        String desFilePath;
        File srcFile = new File(srcFilePath);

        String prefix = srcFileName.replace("." + srcFileExt, "");
        File storageDir = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            storageDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        } else {
            storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        }
        File desFile = File.createTempFile(
                prefix,                               /* prefix */
                "." + srcFileExt,               /* suffix */
                storageDir                           /* directory */
        );

        FileOutputStream fos = new FileOutputStream(desFile);
        fos.write(getBytesFromFile(srcFile));

        desFilePath = desFile.getAbsolutePath();
        Log.d("attman", "SAVED FILE: " + desFilePath );
        fos.close();

        return desFilePath;
    }

    private byte[] getBytesFromFile(File file) throws IOException {
        byte[] data = FileUtils.readFileToByteArray(file);
        return data;
    }

    public static String getExactPath(final Context context, final Uri uri, final String fileName) {
        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                }

                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {

                    if (fileName != null) {
                        return Environment.getExternalStorageDirectory().toString() + "/Download/" + fileName;
                    }

                    String id = DocumentsContract.getDocumentId(uri);

                    if (id.startsWith("raw:")) {
                        id = id.replaceFirst("raw:", "");
                        File file = new File(id);
                        if (file.exists())
                            return id;
                    }

                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{
                            split[1]
                    };

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }

            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {
                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}