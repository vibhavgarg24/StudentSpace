package com.example.studentspace.data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.example.studentspace.model.subject;
import com.example.studentspace.params.Params;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class MyDbHandler extends SQLiteOpenHelper {

    private final  Context context;

    public MyDbHandler( Context context) {
        super(context, Params.DB_NAME, null, Params.DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create = "CREATE TABLE " + Params.TABLE_NAME + "(" + Params.KEY_ID + " INTEGER PRIMARY KEY, "
                        + Params.KEY_SUBJECTNAME + " TEXT, " + Params.KEY_PRESENT + " INTEGER, "
                        + Params.KEY_ABSENT + " INTEGER, " + Params.KEY_STACK + " TEXT, " + Params.KEY_NOTES + " TEXT, "
                        + Params.KEY_CRITERIA + " INTEGER, " + Params.KEY_SCRITERIA + " INTEGER, " + Params.KEY_IMGNAMES + " TEXT,"
                        + Params.KEY_IMGPATHS + " TEXT," + Params.KEY_DOCNAMES + " TEXT," + Params.KEY_DOCPATHS + " TEXT" + ")";
        Log.d("attman", create);
        db.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addSubject (String subjectName) {
        //        read attendance criteria from shared preferences
        SharedPreferences shrd = context.getSharedPreferences("newInstall", MODE_PRIVATE);
        final int attendanceCriteriaInt = shrd.getInt("attendanceCriteria", -1);


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Params.KEY_SUBJECTNAME , subjectName);
        values.put(Params.KEY_PRESENT, 0);
        values.put(Params.KEY_ABSENT, 0);
        values.put(Params.KEY_STACK, "");
        values.put(Params.KEY_NOTES, "");
        values.put(Params.KEY_CRITERIA, attendanceCriteriaInt);
        values.put(Params.KEY_SCRITERIA, 0);
        values.put(Params.KEY_IMGNAMES, "");
        values.put(Params.KEY_IMGPATHS, "");
        values.put(Params.KEY_DOCNAMES, "");
        values.put(Params.KEY_DOCPATHS, "");

        db.insert(Params.TABLE_NAME, null, values);
        Log.d("attman","Subject_added_To_DB");
        db.close();
    }

    public void deleteSubject (String subjectName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Params.TABLE_NAME, Params.KEY_SUBJECTNAME + " = \"" + subjectName + "\"", null );
        Log.d("attman","Subject_deleted_from_DB");
        db.close();
    }

    public ArrayList<subject> getSubjects() {
        ArrayList<subject> allSubjects = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String getAll = "SELECT * FROM " + Params.TABLE_NAME;
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(getAll,null);

        if (cursor.moveToLast()) {
            do {
                subject subject = new subject();
                subject.setId(Integer.parseInt(cursor.getString(0)));
                subject.setName(cursor.getString(1));
                subject.setPresent(Integer.parseInt(cursor.getString(2)));
                subject.setAbsent(Integer.parseInt(cursor.getString(3)));
                subject.setCriteria(Integer.parseInt(cursor.getString(6)));
                subject.setScriteria(Integer.parseInt(cursor.getString(7)));

                allSubjects.add(subject);
            }while (cursor.moveToPrevious());
        }
        db.close();
        return allSubjects;
    }

    public ArrayList<String> getSubjectsName() {
        ArrayList<String> allSubjectsName = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String getAll = "SELECT * FROM " + Params.TABLE_NAME;
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(getAll,null);

        if (cursor.moveToFirst()) {
            do {
                String subjectName = (cursor.getString(1));

                allSubjectsName.add(subjectName);
            }while (cursor.moveToNext());
        }
        db.close();
        return allSubjectsName;
    }

    public int getCriteria (String subjectName) {
        SQLiteDatabase db = this.getReadableDatabase();
        int criteria = 0;

        String search = "SELECT * FROM " + Params.TABLE_NAME + " WHERE " + Params.KEY_SUBJECTNAME + " = \""  + subjectName + "\"";
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(search,null);
        if (cursor.moveToFirst()) {
            criteria = cursor.getInt(6);
        }
        db.close();
        return criteria;
    }

    public void setCriteria (String subjectName, int newCriteria) {
        SQLiteDatabase db = this.getWritableDatabase();

        String update = "UPDATE " + Params.TABLE_NAME + " SET " + Params.KEY_CRITERIA + " = " + newCriteria +
                        " WHERE " + Params.KEY_SUBJECTNAME + " = \"" + subjectName + "\"";
        db.execSQL(update);
        db.close();
    }

    public int getScriteria (String subjectName) {
        SQLiteDatabase db = this.getReadableDatabase();
        int scriteria = 0;

        String search = "SELECT * FROM " + Params.TABLE_NAME + " WHERE " + Params.KEY_SUBJECTNAME + " = \""  + subjectName + "\"";
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(search,null);
        if (cursor.moveToFirst()) {
            scriteria = cursor.getInt(7);
        }
        db.close();
        return scriteria;
    }

    public void setScriteria (String subjectName, int newScriteria) {
        SQLiteDatabase db = this.getWritableDatabase();

        String update = "UPDATE " + Params.TABLE_NAME + " SET " + Params.KEY_SCRITERIA + " = " + newScriteria +
                        " WHERE " + Params.KEY_SUBJECTNAME + " = \"" + subjectName + "\"";
        db.execSQL(update);
        db.close();
    }

    public void editCriteria (int attendanceCriteriaInt) {
        SQLiteDatabase db = this.getWritableDatabase();
        String update = "UPDATE " + Params.TABLE_NAME + " SET " + Params.KEY_CRITERIA + " = " + attendanceCriteriaInt +
                        " WHERE " + Params.KEY_SCRITERIA + " = " + 0;
        db.execSQL(update);
        db.close();
    }

    public void editCriteriaAll (int attendanceCriteriaInt) {
        SQLiteDatabase db = this.getWritableDatabase();
        String update = "UPDATE " + Params.TABLE_NAME + " SET " + Params.KEY_CRITERIA + " = " + attendanceCriteriaInt;
        db.execSQL(update);
        db.close();
    }

    public void reset (String subjectName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String update = "UPDATE " + Params.TABLE_NAME + " SET " + Params.KEY_PRESENT + " = " + 0 +
                        ", " + Params.KEY_ABSENT + " = " + 0 + ", " + Params.KEY_STACK + " = \"\"" +
                        " WHERE " + Params.KEY_SUBJECTNAME + " = \"" + subjectName + "\"";
        db.execSQL(update);
        db.close();
    }

    public char toUndo (String subjectName) {
        SQLiteDatabase dbr = this.getReadableDatabase();
        String stack = "";
        char c = 'n';

        String search = "SELECT * FROM " + Params.TABLE_NAME + " WHERE " + Params.KEY_SUBJECTNAME + " = \""  + subjectName + "\"";
        @SuppressLint("Recycle") Cursor cursor = dbr.rawQuery(search,null);
        if (cursor.moveToFirst())
            stack = cursor.getString(4);
        if (stack.length() == 0)
            return c;
        c = stack.charAt(stack.length() - 1);
        dbr.close();
        return c;
    }

    public int addPresent (String subjectName) {
        SQLiteDatabase dbr = this.getReadableDatabase();
        int present = 0;
        String stack = "";

        String search = "SELECT * FROM " + Params.TABLE_NAME + " WHERE " + Params.KEY_SUBJECTNAME + " = \""  + subjectName + "\"";
        @SuppressLint("Recycle") Cursor cursor = dbr.rawQuery(search,null);
        if (cursor.moveToFirst()) {
            present = cursor.getInt(2) + 1;
            stack = cursor.getString(4) + "p";
        }
        dbr.close();

        SQLiteDatabase dbw = this.getWritableDatabase();
        String update = "UPDATE " + Params.TABLE_NAME + " SET " + Params.KEY_PRESENT + " = " + present +
                        ", " + Params.KEY_STACK + " = \"" + stack + "\"" +
                        " WHERE " + Params.KEY_SUBJECTNAME + " = \"" + subjectName + "\"";
        dbw.execSQL(update);
        dbw.close();
        return present;
    }

    public int subtractPresent (String subjectName) {
        SQLiteDatabase dbr = this.getReadableDatabase();
        int present = 0;
        String stack;
        String upstack = "";

        String search = "SELECT * FROM " + Params.TABLE_NAME + " WHERE " + Params.KEY_SUBJECTNAME + " = \""  + subjectName + "\"";
        @SuppressLint("Recycle") Cursor cursor = dbr.rawQuery(search,null);
        if (cursor.moveToFirst()) {
            present = cursor.getInt(2) - 1;
            stack = cursor.getString(4);
            upstack = stack.substring(0, stack.length() - 1);
        }
        dbr.close();

        SQLiteDatabase dbw = this.getWritableDatabase();
        String update = "UPDATE " + Params.TABLE_NAME + " SET " + Params.KEY_PRESENT + " = " + present +
                        ", " + Params.KEY_STACK + " = \"" + upstack + "\"" +
                        " WHERE " + Params.KEY_SUBJECTNAME + " = \"" + subjectName + "\"";
        dbw.execSQL(update);
        dbw.close();
        return present;
    }

    public int addAbsent (String subjectName) {
        SQLiteDatabase dbr = this.getReadableDatabase();
        int absent = 0;
        String stack = "";

        String search = "SELECT * FROM " + Params.TABLE_NAME + " WHERE " + Params.KEY_SUBJECTNAME + " = \""  + subjectName + "\"";
        @SuppressLint("Recycle") Cursor cursor = dbr.rawQuery(search,null);
        if (cursor.moveToFirst()) {
            absent = cursor.getInt(3) + 1;
            stack = cursor.getString(4) + "a";
        }
        dbr.close();

        SQLiteDatabase dbw = this.getWritableDatabase();
        String update = "UPDATE " + Params.TABLE_NAME + " SET " + Params.KEY_ABSENT + " = " + absent +
                        ", " + Params.KEY_STACK + " = \"" + stack + "\"" +
                        " WHERE " + Params.KEY_SUBJECTNAME + " = \"" + subjectName + "\"";
        dbw.execSQL(update);
        dbw.close();
        return absent;
    }

    public int subtractAbsent (String subjectName) {
        SQLiteDatabase dbr = this.getReadableDatabase();
        int absent = 0;
        String stack ;
        String upstack = "";

        String search = "SELECT * FROM " + Params.TABLE_NAME + " WHERE " + Params.KEY_SUBJECTNAME + " = \""  + subjectName + "\"";
        @SuppressLint("Recycle") Cursor cursor = dbr.rawQuery(search,null);
        if (cursor.moveToFirst()) {
            absent = cursor.getInt(3) - 1;
            stack = cursor.getString(4);
            upstack = stack.substring(0, stack.length() - 1);
        }
        dbr.close();

        SQLiteDatabase dbw = this.getWritableDatabase();
        String update = "UPDATE " + Params.TABLE_NAME + " SET " + Params.KEY_ABSENT + " = " + absent +
                        ", " + Params.KEY_STACK + " = \"" + upstack + "\"" +
                        " WHERE " + Params.KEY_SUBJECTNAME + " = \"" + subjectName + "\"";
        dbw.execSQL(update);
        dbw.close();
        return absent;
    }
    
    public String getNotes (String subjectName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String notes = "";
        
        String search = "SELECT * FROM " + Params.TABLE_NAME + " WHERE " + Params.KEY_SUBJECTNAME + " = \""  + subjectName + "\"";
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(search,null);
        if (cursor.moveToFirst()) {
            notes = cursor.getString(5);
        }
        db.close();
        return notes;
    }

    public void addNotes (String subjectName, String notes) {
        SQLiteDatabase db = this.getWritableDatabase();

        String update = "UPDATE " + Params.TABLE_NAME + " SET " + Params.KEY_NOTES + " = \"" + notes + "\"" +
                        " WHERE " + Params.KEY_SUBJECTNAME + " = \"" + subjectName + "\"";
        db.execSQL(update);
        db.close();
    }

    public void deleteNotes (String subjectName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String notes = "";

        String update = "UPDATE " + Params.TABLE_NAME + " SET " + Params.KEY_NOTES + " = \"" + notes + "\"" +
                        " WHERE " + Params.KEY_SUBJECTNAME + " = \"" + subjectName + "\"";
        db.execSQL(update);
        db.close();
    }

    public void updateName (String subjectName, String newName) {
        SQLiteDatabase db = this.getWritableDatabase();

        String update = "UPDATE " + Params.TABLE_NAME + " SET " + Params.KEY_SUBJECTNAME + " = \"" + newName + "\"" +
                        " WHERE " + Params.KEY_SUBJECTNAME + " = \"" + subjectName + "\"";
        db.execSQL(update);
        db.close();
    }

    public void addMedia (String subjectName, String uri) {
        SQLiteDatabase dbr = this.getReadableDatabase();
        String media = "";

        String search = "SELECT * FROM " + Params.TABLE_NAME + " WHERE " + Params.KEY_SUBJECTNAME + " = \""  + subjectName + "\"";
        @SuppressLint("Recycle") Cursor cursor = dbr.rawQuery(search,null);
        if (cursor.moveToFirst()) {
            media = cursor.getString(9);
        }
        dbr.close();

        if (media.equals(""))
            media = ",";

        String[] array = media.split(",");

        ArrayList<String> list = new ArrayList<>();
        for (int i=0; i<array.length; i++) {
            list.add(array[i]);
        }
        list.add(uri);

        String result = "";
        for (int i=0; i<list.size()-1; i++) {
            result = result + list.get(i) + ",";
        }
        result = result + list.get(list.size()-1);

        SQLiteDatabase dbw = this.getWritableDatabase();
        String update = "UPDATE " + Params.TABLE_NAME + " SET " + Params.KEY_IMGPATHS + " = \"" + result + "\"" +
                " WHERE " + Params.KEY_SUBJECTNAME + " = \"" + subjectName + "\"";
        dbw.execSQL(update);
        dbw.close();

    }

    public String[] getMedia (String subjectName) {
        SQLiteDatabase dbr = this.getReadableDatabase();
        String[] array = {""};
        String stringUris = "";

        String search = "SELECT * FROM " + Params.TABLE_NAME + " WHERE " + Params.KEY_SUBJECTNAME + " = \""  + subjectName + "\"";
        @SuppressLint("Recycle") Cursor cursor = dbr.rawQuery(search,null);
        if (cursor.moveToFirst()) {
            stringUris = cursor.getString(9);
        }
        dbr.close();

        if (stringUris.equals(""))
            return array;

        array = stringUris.split(",");

        return array;
    }

    public void deleteElement (String subjectName, int position) {
        SQLiteDatabase dbr = this.getReadableDatabase();
        String media = "";

        String search = "SELECT * FROM " + Params.TABLE_NAME + " WHERE " + Params.KEY_SUBJECTNAME + " = \""  + subjectName + "\"";
        @SuppressLint("Recycle") Cursor cursor = dbr.rawQuery(search,null);
        if (cursor.moveToFirst()) {
            media = cursor.getString(9);
        }
        dbr.close();

        String result = "";
        String[] array = media.split(",");
        if (array.length == 1)
            result = "";
        else {
            ArrayList<String> list = new ArrayList<>();
            for (String element : array) {
                list.add(element);
            }
            if (position < list.size())
                list.remove(position);
            for (int i = 0; i < list.size() - 1; i++) {
                result = result + list.get(i) + ",";
            }
            result = result + list.get(list.size() - 1);
        }

        SQLiteDatabase dbw = this.getWritableDatabase();
        String update = "UPDATE " + Params.TABLE_NAME + " SET " + Params.KEY_IMGPATHS + " = \"" + result + "\"" +
                " WHERE " + Params.KEY_SUBJECTNAME + " = \"" + subjectName + "\"";
        dbw.execSQL(update);
        dbw.close();
    }

    public void addDocPath (String subjectName, String uri) {
        SQLiteDatabase dbr = this.getReadableDatabase();
        String media = "";

        String search = "SELECT * FROM " + Params.TABLE_NAME + " WHERE " + Params.KEY_SUBJECTNAME + " = \""  + subjectName + "\"";
        @SuppressLint("Recycle") Cursor cursor = dbr.rawQuery(search,null);
        if (cursor.moveToFirst()) {
            media = cursor.getString(11);
        }
        dbr.close();

        if (media.equals(""))
            media = ",";

        String[] array = media.split(",");

        ArrayList<String> list = new ArrayList<>();
        for (int i=0; i<array.length; i++) {
            list.add(array[i]);
        }
        list.add(uri);

        String result = "";
        for (int i=0; i<list.size()-1; i++) {
            result = result + list.get(i) + ",";
        }
        result = result + list.get(list.size()-1);

        SQLiteDatabase dbw = this.getWritableDatabase();
        String update = "UPDATE " + Params.TABLE_NAME + " SET " + Params.KEY_DOCPATHS + " = \"" + result + "\"" +
                " WHERE " + Params.KEY_SUBJECTNAME + " = \"" + subjectName + "\"";
        dbw.execSQL(update);
        dbw.close();

    }

    public String[] getDocPaths (String subjectName) {
        SQLiteDatabase dbr = this.getReadableDatabase();
        String[] array = {""};
        String stringUris = "";

        String search = "SELECT * FROM " + Params.TABLE_NAME + " WHERE " + Params.KEY_SUBJECTNAME + " = \""  + subjectName + "\"";
        @SuppressLint("Recycle") Cursor cursor = dbr.rawQuery(search,null);
        if (cursor.moveToFirst()) {
            stringUris = cursor.getString(11);
        }
        dbr.close();

        if (stringUris.equals(""))
            return array;

        array = stringUris.split(",");

        return array;
    }

    public void addDocName (String subjectName, String uri) {
        SQLiteDatabase dbr = this.getReadableDatabase();
        String media = "";

        String search = "SELECT * FROM " + Params.TABLE_NAME + " WHERE " + Params.KEY_SUBJECTNAME + " = \""  + subjectName + "\"";
        @SuppressLint("Recycle") Cursor cursor = dbr.rawQuery(search,null);
        if (cursor.moveToFirst()) {
            media = cursor.getString(10);
        }
        dbr.close();

        if (media.equals(""))
            media = ",";

        String[] array = media.split(",");

        ArrayList<String> list = new ArrayList<>();
        for (int i=0; i<array.length; i++) {
            list.add(array[i]);
        }
        list.add(uri);

        String result = "";
        for (int i=0; i<list.size()-1; i++) {
            result = result + list.get(i) + ",";
        }
        result = result + list.get(list.size()-1);

        SQLiteDatabase dbw = this.getWritableDatabase();
        String update = "UPDATE " + Params.TABLE_NAME + " SET " + Params.KEY_DOCNAMES + " = \"" + result + "\"" +
                " WHERE " + Params.KEY_SUBJECTNAME + " = \"" + subjectName + "\"";
        dbw.execSQL(update);
        dbw.close();

    }

    public String[] getDocNames (String subjectName) {
        SQLiteDatabase dbr = this.getReadableDatabase();
        String[] array = {""};
        String stringUris = "";

        String search = "SELECT * FROM " + Params.TABLE_NAME + " WHERE " + Params.KEY_SUBJECTNAME + " = \"" + subjectName + "\"";
        @SuppressLint("Recycle") Cursor cursor = dbr.rawQuery(search, null);
        if (cursor.moveToFirst()) {
            stringUris = cursor.getString(10);
        }
        dbr.close();

        if (stringUris.equals(""))
            return array;

//        ArrayList<String> list = new ArrayList<>();
        array = stringUris.split(",");

//        list.addAll(Arrays.asList(array));

        return array;
    }

    public void deleteDocPath (String subjectName, int position) {
        SQLiteDatabase dbr = this.getReadableDatabase();
        String media = "";

        String search = "SELECT * FROM " + Params.TABLE_NAME + " WHERE " + Params.KEY_SUBJECTNAME + " = \""  + subjectName + "\"";
        @SuppressLint("Recycle") Cursor cursor = dbr.rawQuery(search,null);
        if (cursor.moveToFirst()) {
            media = cursor.getString(11);
        }
        dbr.close();

        String result = "";
        String[] array = media.split(",");
        if (array.length == 1)
            result = "";
        else {
            ArrayList<String> list = new ArrayList<>();
            for (String element : array) {
                list.add(element);
            }
            if (position < list.size())
                list.remove(position);
            for (int i = 0; i < list.size() - 1; i++) {
                result = result + list.get(i) + ",";
            }
            result = result + list.get(list.size() - 1);
        }

        SQLiteDatabase dbw = this.getWritableDatabase();
        String update = "UPDATE " + Params.TABLE_NAME + " SET " + Params.KEY_DOCPATHS + " = \"" + result + "\"" +
                " WHERE " + Params.KEY_SUBJECTNAME + " = \"" + subjectName + "\"";
        dbw.execSQL(update);
        dbw.close();
    }

    public void deleteDocName (String subjectName, int position) {
        SQLiteDatabase dbr = this.getReadableDatabase();
        String media = "";

        String search = "SELECT * FROM " + Params.TABLE_NAME + " WHERE " + Params.KEY_SUBJECTNAME + " = \""  + subjectName + "\"";
        @SuppressLint("Recycle") Cursor cursor = dbr.rawQuery(search,null);
        if (cursor.moveToFirst()) {
            media = cursor.getString(10);
        }
        dbr.close();

        String result = "";
        String[] array = media.split(",");
        if (array.length == 1)
            result = "";
        else {
            ArrayList<String> list = new ArrayList<>();
            for (String element : array) {
                list.add(element);
            }
            if (position < list.size())
                list.remove(position);
            for (int i = 0; i < list.size() - 1; i++) {
                result = result + list.get(i) + ",";
            }
            result = result + list.get(list.size() - 1);
        }

        SQLiteDatabase dbw = this.getWritableDatabase();
        String update = "UPDATE " + Params.TABLE_NAME + " SET " + Params.KEY_DOCNAMES + " = \"" + result + "\"" +
                " WHERE " + Params.KEY_SUBJECTNAME + " = \"" + subjectName + "\"";
        dbw.execSQL(update);
        dbw.close();
    }

}
