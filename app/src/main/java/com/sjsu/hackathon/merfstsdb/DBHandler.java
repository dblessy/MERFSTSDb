package com.sjsu.hackathon.merfstsdb;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {

    // creating a constant variables for our database.
    // below variable is for our database name.
    private static final String DB_NAME = "hackathon";

    // below int is our database version
    private static final int DB_VERSION = 1;

    // below variable is for our table name.
    private static final String[] TABLE_NAMES = {"gdp", "fdi_inflows", "fdi_outflows", "ie_flow",
            "con_gdp", "credit", "fertilizer", "fertilizer_prod", "reserves", "gni", "debt",
            "gni_cur"};

    private static final String ID_COL = "id";

    // below variable is for our id column.
    private static final String YEAR_COL = "year";

    // below variable is for our course name column
    private static final String DATA_COL = "data";

    // below variable id for our course duration column.
    private static final String COUNTRY_COL = "country";

    // creating a constructor for our database handler.
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // below method is for creating a database by running a sqlite query
    @Override
    public void onCreate(SQLiteDatabase db) {
        // on below line we are creating
        // an sqlite query and we are
        // setting our column names
        // along with their data types.
        for (String tableName : TABLE_NAMES) {
            String query = "CREATE TABLE " + tableName + " ("
                    + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + YEAR_COL + " TEXT, "
                    + DATA_COL + " INTEGER,"
                    + COUNTRY_COL + " TEXT)";

            // at last we are calling a exec sql
            // method to execute above sql query
            db.execSQL(query);
        }
    }

    // this method is use to add new course to our sqlite database.
    public void addNewData(String tableName, String year, long data, String country) {
        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(YEAR_COL, year);
        values.put(DATA_COL, data);
        values.put(COUNTRY_COL, country);

        // after adding all values we are passing
        // content values to our table.
        db.insert(tableName, null, values);

        // at last we are closing our
        // database after adding database.
        db.close();
    }

    public void removeData(String tableName, String startYear, String endYear) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tableName, "year >= ? and year <= ?", new String[]{startYear, endYear});
    }

    public ArrayList<Data> getData(String tableName, String startYear, String endYear, String country) {
        ArrayList<Data> dataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELCT * FROM " + tableName + " WHERE year >= ? AND year <= ? ",
                new String[]{startYear, endYear});
        if (cursor.moveToFirst()) {
            do {
                // on below line we are adding the data from
                // cursor to our array list.
                dataList.add(new Data(
                        cursor.getString(1),
                        cursor.getLong(2),
                        cursor.getString(3)));
            } while (cursor.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursor.close();
        return dataList;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        for (String tableName : TABLE_NAMES) {
            db.execSQL("DROP TABLE IF EXISTS " + tableName);
            onCreate(db);
        }
    }
}
