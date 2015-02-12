package es.tessier.mememaker.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Evan Anger on 8/17/14.
 */
public class MemeSQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ContactDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TAG = MemeSQLiteHelper.class.getName();

    static final String CREATE_TABLE_MEMES =
            "CREATE TABLE " + DataBaseManager.MEMES_TABLE + "( " +
                    DataBaseManager.COLUMN_MEMES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                    DataBaseManager.COLUMN_MEMES_ASSET + " TEXT NOT NULL," +
                    DataBaseManager.COLUMN_MEMES_NAME + " TEXT NOT NULL );";

    public MemeSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MEMES);
        //db.execSQL(CREATE_TABLE_ANNOTATIONS); APARTADO 11
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
