package com.sjsu.hackathon.merfstsdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Timestamp;
import java.util.ArrayList;

public class AnnotationDBHandler extends SQLiteOpenHelper {

    // creating a constant variables for our database.
    // below variable is for our database name.
    private static final String DB_NAME = "annotation";

    // below int is our database version
    private static final int DB_VERSION = 3;

    // below variable is for our table name.
    private static final String TABLE_NAME = "annotation";

    private static final String ID_COL = "id";

    // below variable is for our id column.
    private static final String TIME_COL = "create_time";

    // below variable is for our course name column
    private static final String TITLE_COL = "title";

    // below variable id for our course duration column.
    private static final String BODY_COL = "body";

    // creating a constructor for our database handler.
    public AnnotationDBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // below method is for creating a database by running a sqlite query
    @Override
    public void onCreate(SQLiteDatabase db) {
        // on below line we are creating
        // an sqlite query and we are
        // setting our column names
        // along with their data types.
            String query = "CREATE TABLE " + TABLE_NAME + " ("
                    + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TITLE_COL + " TEXT,"
                    + BODY_COL + " TEXT,"
                    + TIME_COL + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
            // at last we are calling a exec sql
            // method to execute above sql query
            db.execSQL(query);
    }

    // this method is use to add new course to our sqlite database.
    public void addNewData(String title, String body) {
        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(TITLE_COL, title);
        values.put(BODY_COL, body);

        // after adding all values we are passing
        // content values to our table.
        db.insert(TABLE_NAME, null, values);

        // at last we are closing our
        // database after adding database.
        db.close();
    }

    public ArrayList<Annotation> getDataList() {
        ArrayList<Annotation> dataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY id DESC", null);
        if (cursor.moveToFirst()) {
            do {
                // on below line we are adding the data from
                // cursor to our array list.
                dataList.add(new Annotation(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        Timestamp.valueOf(cursor.getString(3))
                        ));
            } while (cursor.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursor.close();
        return dataList;
    }

    public Annotation getDataById(int id) {
        Annotation annotation = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE id = ?", new String[]{Integer.toString(id)});
        if (cursor.moveToFirst()) {
            // on below line we are adding the data from
            // cursor to our array list.
            annotation = new Annotation(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    Timestamp.valueOf(cursor.getString(3))
            );
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursor.close();
        return annotation;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}