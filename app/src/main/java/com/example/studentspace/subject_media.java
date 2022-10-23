package com.example.studentspace;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;
import com.example.studentspace.data.MyDbHandler;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class subject_media extends AppCompatActivity {

    public static final int IMAGE_CAPTURE_REQUEST_CODE = 4;
    public static final int IMAGE_PICK_REQUEST_CODE = 3;
    public static final int FILE_PICK_REQUEST_CODE = 5;
    MyDbHandler db;
    TabLayout tabLayout;
    ViewPager tabViewPager;
    String name;
    FloatingActionsMenu fabImages;
    com.getbase.floatingactionbutton.FloatingActionButton fabCamera;
    com.getbase.floatingactionbutton.FloatingActionButton fabGallery;
    FloatingActionButton fabDocs;
    String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_media);

        db = new MyDbHandler(subject_media.this);
        tabLayout = findViewById(R.id.tabLayout);
        tabViewPager = findViewById(R.id.tabViewPager);

        final Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        name = bundle.getString("subjectName");
        setTitle(name + "  -  Drawer");

        setupTabViewPager(tabViewPager);
        tabLayout.setupWithViewPager(tabViewPager);

        fabImages = findViewById(R.id.fabImages);
        fabCamera = findViewById(R.id.fabCamera);
        fabGallery = findViewById(R.id.fabGallery);
        fabDocs = findViewById(R.id.fabDocs);
        fabDocs.setVisibility(View.INVISIBLE);

        tabViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                fabImages.collapse();
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    fabImages.setVisibility(View.VISIBLE);
                    fabDocs.setVisibility(View.INVISIBLE);
                } else if (position == 1) {
                    fabImages.setVisibility(View.INVISIBLE);
                    fabDocs.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                fabImages.collapse();
            }

        });

        fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(v.getContext()).withPermission(Manifest.permission.CAMERA)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                dispatchTakePictureIntent();
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                                if (permissionDeniedResponse.isPermanentlyDenied()) {
                                    Snackbar sb = Snackbar.make(findViewById(R.id.subMediaParentLayout), "Requires Camera Permission to continue.", Snackbar.LENGTH_LONG)
                                            .setAction("SETTINGS", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent();
                                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                                                    intent.setData(uri);
                                                    startActivity(intent);
                                                }
                                            });
                                    sb.show();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });


        fabGallery.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                gallery.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(gallery, IMAGE_PICK_REQUEST_CODE);
            }
        });


        fabDocs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent docs = new Intent(Intent.ACTION_GET_CONTENT);
                docs.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                docs.setType("*/*");
                startActivityForResult(docs, FILE_PICK_REQUEST_CODE);
            }
        });

//        delete = findViewById(R.id.delete);
//        delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                File file = new File(toDelete);
//                boolean deleted = file.delete();
//                Log.d("attman", "DELETE: " + deleted);
//            }
//        });

//        fabMedia.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.Q)
//            @Override
//            public void onClick(final View v) {
//                Intent intent_fe = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                Intent intent_fe = new Intent(Intent.ACTION_PICK, Uri.parse(MediaStore.Files.FileColumns.DATA));
//                Intent intent_fe = new Intent(Intent.ACTION_PICK);
//                Intent intent_fe = new Intent(Intent.ACTION_GET_CONTENT);
//                intent_fe.setType("*/*");
//                startActivityForResult(intent_fe, REQUEST_CODE_SELECT_FILE);

//                    Toast.makeText(v.getContext(), "Select a ", Toast.LENGTH_LONG).show();
//                    Intent browseStorage = new Intent(Intent.ACTION_PICK);
////                    browseStorage.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                    browseStorage.setType("image/*");
////                    browseStorage.addCategory(Intent.CATEGORY_OPENABLE);
//                    startActivityForResult(Intent.createChooser(browseStorage, "Img"), REQUEST_CODE_SELECT_FILE);

//                Intent intent1 = new Intent(Intent.ACTION_PICK);
//                intent1.setType("image/*");
//                startActivityForResult(intent1, REQUEST_CODE_SELECT_FILE);

//                Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);
//                intent1.setAction(Intent.ACTION_GET_CONTENT);
//                intent1.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                intent1.addCategory(Intent.CATEGORY_OPENABLE);
//                intent1.setType("*/*");
//                startActivityForResult(intent1, REQUEST_CODE_SELECT_FILE);
//                startActivityForResult(Intent.createChooser(intent1, "File"), REQUEST_CODE_SELECT_FILE);


//                Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);
//                intent1.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                intent1.setType("application/pdf");
//                intent1.addCategory(Intent.CATEGORY_OPENABLE);
////                intent1.setType("*/*");
//                startActivityForResult(intent1, REQUEST_CODE_SELECT_FILE);


//                startActivityForResult(Intent.createChooser(intent1, "PDFs"), REQUEST_CODE_SELECT_FILE);

//                Dexter.withContext(v.getContext())
//                        .withPermission(Manifest.permission.INTERNET)
//                        .withListener(new PermissionListener() {
//                            @Override
//                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
//                                Toast.makeText(v.getContext(), "Internet Permission Granted", Toast.LENGTH_SHORT).show();
//                                Intent intent1 = new Intent(v.getContext(), ViewPdf.class);
//                                intent1.putExtra("subjectName", name);
//                                startActivity(intent1);
//                            }
//
//                            @Override
//                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
//                                Toast.makeText(v.getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
//                                permissionToken.continuePermissionRequest();
//                            }
//                        }).check();


//                Intent intent1 = new Intent(Intent.ACTION_VIEW);


//            }
//        });

//        showFile.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//            @Override
//            public void onClick(View v) {
//                String uriText = db.getMedia(name);
//
//                if (uriText.equals(""))
//                    Toast.makeText(v.getContext(), "No File Added", Toast.LENGTH_SHORT).show();
//                else {
//                    showPdfFromUri(uriText);
//                    pdfViewActivity.showPdfFromUri(Uri.parse(uriText));
//                    mediaTv.setText(uriText);
//                    Intent intent1 = new Intent(v.getContext(), ViewPdf.class);
//                    intent1.putExtra("showFile", true);
//                    intent1.putExtra("uri", uriText);
//                    startActivity(intent1);
//                }
//            }
//        });
    }

    public void setupTabViewPager(ViewPager viewPager) {
        tabViewPagerAdapter tabViewPagerAdapter = new tabViewPagerAdapter(getSupportFragmentManager());
        tabViewPagerAdapter.addFragment(new ImageFragment(name), "IMAGES");
        tabViewPagerAdapter.addFragment(new DocFragment(name), "DOCUMENTS");
        viewPager.setAdapter(tabViewPagerAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
// IMAGE PICK
            if (requestCode == IMAGE_PICK_REQUEST_CODE && resultCode == RESULT_OK) {

                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        Uri uri = clipData.getItemAt(i).getUri();
                        String path = getPath(this, uri);
                        File file = new File(path);
                        Uri uri1 = Uri.fromFile(file);
                        db.addMedia(name, uri1.toString());
                        Log.d("attman", "Uri: " + uri1.toString() + " prev: " + uri.toString());
                    }
                } else {
                    Uri uri = data.getData();
                    if (uri != null) {
                        String path = getPath(this, uri);
                        String fileName = getFileName(uri);
                        File file = new File(path);
                        Uri uri1 = Uri.fromFile(file);
                        db.addMedia(name, uri1.toString());

                        Log.d("attman", "Path: " + path + " Name: " + fileName);
                        Log.d("attman", "Uri: " + uri1.toString() + " prev: " + uri.toString());
                    } else {
                        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            }

// FILE PICK
            else if (requestCode == FILE_PICK_REQUEST_CODE && resultCode == RESULT_OK) {
                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    int count = clipData.getItemCount();
                    for (int i=0; i<count; i++) {
                        Uri srcUri = clipData.getItemAt(i).getUri();
                        addDocToDrawer(srcUri);
                    }
                } else {
                    Uri srcUri = data.getData();
                    if (srcUri != null) {
//                        Log.d("attman", "srcUri: " + srcUri.toString());
                        addDocToDrawer(srcUri);
                    } else {
                        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

// IMAGE CAPTURE
        else if (requestCode == IMAGE_CAPTURE_REQUEST_CODE && resultCode == RESULT_OK) {
            File file = new File(currentPhotoPath);
            Uri uri = Uri.fromFile(file);
            db.addMedia(name, uri.toString());

            Log.d("attman", "Captured: " + uri.toString());
        }

        else {
            Toast.makeText(this, "No File Selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void addDocToDrawer (Uri srcUri) {
        boolean srcFilePathFlag = false;
        String srcFilepath = "";
        String srcFileName = getFileName(srcUri);
        String srcFileExt = getFileExt(srcUri);
        Log.d("attman", "srcFileName: " + srcFileName);

        try {
            srcFilepath = getExactPath(this, srcUri, srcFileName);
            Log.d("attman", "srcFilePath: " + srcFilepath);
            srcFilePathFlag = true;
        } catch (Exception e) {
            Log.d("attman", "srcFilePath Error " + e.getMessage());
            Toast.makeText(this, "File Path Error", Toast.LENGTH_SHORT).show();
        }

        if (srcFilePathFlag) {
            try {
                String desFilePath = saveFileInternal(srcFilepath, srcFileName, srcFileExt);
                File file = new File(desFilePath);
                Uri toSave = Uri.fromFile(file);
                db.addDocPath(name, toSave.toString());
                db.addDocName(name, srcFileName);
            } catch (Exception e) {
                Log.d("attman", "Save File Error");
                Toast.makeText(this, "Save File Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String saveFileInternal(String srcFilePath, String srcFileName, String srcFileExt) throws IOException {
        String desFilePath;
        File srcFile = new File(srcFilePath);

        String prefix = srcFileName.replace("." + srcFileExt, "");
        File storageDir = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            storageDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        } else {
            storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
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
        ContentResolver c = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(c.getType(uri));
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        @SuppressLint("SimpleDateFormat")
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,        /* prefix */
                ".jpg",         /* suffix */
                storageDir            /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(this, "Error: " + ex.getMessage(), Toast.LENGTH_LONG).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, IMAGE_CAPTURE_REQUEST_CODE);
            }
        }
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
}