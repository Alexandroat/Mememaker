package es.tessier.mememaker.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
//
/**
 * Created by Evan Anger on 8/17/14.
 */
public class MemeSQLiteHelper extends SQLiteOpenHelper {
    private static final String ALTER_ADD_CREATE_DATE= DataBaseManager.COLUMN_CREATE_DATE;
    private static final String DATABASE_NAME = "memes.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TAG = MemeSQLiteHelper.class.getName();

    static final String CREATE_TABLE_MEMES =
            "CREATE TABLE " + DataBaseManager.MEMES_TABLE + " ( " +
                    DataBaseManager.COLUMN_MEMES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DataBaseManager.COLUMN_MEMES_ASSET + " TEXT NOT NULL," +
                    DataBaseManager.COLUMN_MEMES_NAME + " TEXT NOT NULL," + DataBaseManager.COLUMN_CREATE_DATE + "INTEGER );";
    static final String CREATE_TABLE_ANNOTATIONS =
            "CREATE TABLE " + DataBaseManager.ANNOTATIONS_TABLE + " ( " +
                    DataBaseManager.COLUMN_ANNOTATIONS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DataBaseManager.COLUMN_ANNOTATIONS_TITLE + " TEXT NOT NULL," +
                    DataBaseManager.COLUMN_ANNOTATIONS_X + " INTEGER NOT NULL, " +
                    DataBaseManager.COLUMN_ANNOTATIONS_Y + " INTEGER NOT NULL, " +
                    DataBaseManager.COLUMN_ANNOTATIONS_COLOR + " INTEGER NOT NULL, " +
                    DataBaseManager.COLUMN_ANNOTATIONS_FK + " INTEGER NOT NULL, " +
                    "FOREIGN KEY ( " +  DataBaseManager.COLUMN_ANNOTATIONS_FK + " ) REFERENCES " +
                    DataBaseManager.MEMES_TABLE + " ( " + DataBaseManager.COLUMN_MEMES_ID + " ) );";

    public MemeSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MEMES);
        db.execSQL(CREATE_TABLE_ANNOTATIONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    switch (oldVersion){
        case 1: db.execSQL(ALTER_ADD_CREATE_DATE);
    }



    }

}
