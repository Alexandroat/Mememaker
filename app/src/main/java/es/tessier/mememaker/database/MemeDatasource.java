package es.tessier.mememaker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;

import es.tessier.mememaker.models.Meme;
import es.tessier.mememaker.models.MemeAnnotation;
//
/**
 * Created by Evan Anger on 8/17/14.
 */
public class MemeDatasource {

    private Context mContext;
    private MemeSQLiteHelper mMemeSqlLiteHelper;

    public MemeDatasource(Context context) {

        mContext = context;
        mMemeSqlLiteHelper = new MemeSQLiteHelper(mContext);
       // SQLiteDatabase database = mMemeSqlLiteHelper.getReadableDatabase();
    }

    public SQLiteDatabase openWriteable (){
        return mMemeSqlLiteHelper.getWritableDatabase();
    }

    public SQLiteDatabase openReadable (){
        return mMemeSqlLiteHelper.getReadableDatabase();
    }

    public void CloseDB (SQLiteDatabase sqLiteDatabase){
        sqLiteDatabase.close();
    }

    public void create (Meme meme){
        SQLiteDatabase db = openWriteable();
        db.beginTransaction();
        ContentValues memeValues = new ContentValues();
        memeValues.put(DataBaseManager.COLUMN_MEMES_NAME, meme.getName());
        memeValues.put(DataBaseManager.COLUMN_MEMES_ASSET, meme.getAssetLocation());
        memeValues.put(DataBaseManager.COLUMN_CREATE_DATE, new Date().getTime());
        long memeID = db.insert(DataBaseManager.MEMES_TABLE, null, memeValues);

        for (MemeAnnotation memeAnnotation : meme.getAnnotations()){
            ContentValues annotationValues = new ContentValues();
            annotationValues.put(DataBaseManager.COLUMN_ANNOTATIONS_TITLE, memeAnnotation.getTitle());
            annotationValues.put(DataBaseManager.COLUMN_ANNOTATIONS_X, memeAnnotation.getLocationX());
            annotationValues.put(DataBaseManager.COLUMN_ANNOTATIONS_Y, memeAnnotation.getLocationY());
            annotationValues.put(DataBaseManager.COLUMN_ANNOTATIONS_COLOR, memeAnnotation.getColor());
            annotationValues.put(DataBaseManager.COLUMN_ANNOTATIONS_FK, memeID);

            db.insert(DataBaseManager.ANNOTATIONS_TABLE, null, annotationValues);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        CloseDB(db);
    }

    /*public void read(){

    }

    public void readMemes(){
        SQLiteDatabase db = openReadable();
        Cursor cursor = db.query(DataBaseManager.MEMES_TABLE,new String [] {DataBaseManager.COLUMN_MEMES_NAME, DataBaseManager.COLUMN_MEMES_ID, DataBaseManager.COLUMN_MEMES_ASSET},null, null, null, null, null);
        ArrayList <Meme> memes = new ArrayList<Meme>();
        if (cursor.moveToFirst()){
            do{

            }while ();
        }

    }*/
}
