package es.tessier.mememaker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;

import es.tessier.mememaker.models.Meme;
import es.tessier.mememaker.models.MemeAnnotation;
//g

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

    public SQLiteDatabase openWriteable() {
        return mMemeSqlLiteHelper.getWritableDatabase();
    }

    public SQLiteDatabase openReadable() {
        return mMemeSqlLiteHelper.getReadableDatabase();
    }

    public void CloseDB(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.close();
    }

    public void create(Meme meme) {
        SQLiteDatabase db = openWriteable();
        db.beginTransaction();
        ContentValues memeValues = new ContentValues();
        memeValues.put(DataBaseManager.COLUMN_MEMES_NAME, meme.getName());
        memeValues.put(DataBaseManager.COLUMN_MEMES_ASSET, meme.getAssetLocation());
        memeValues.put(DataBaseManager.COLUMN_MEMES_CREATE_DATE, new Date().getTime());
        long memeID = db.insert(DataBaseManager.MEMES_TABLE, null, memeValues);

        for (MemeAnnotation memeAnnotation : meme.getAnnotations()) {
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

    public ArrayList<Meme> read() {
        ArrayList<Meme> memes = readMemes();
        addMemeAnnotations(memes);
        return memes;
    }


    // Obteniendo Datos
    public ArrayList<Meme> readMemes() {
        SQLiteDatabase db = openReadable();
        Cursor cursor = db.query(DataBaseManager.MEMES_TABLE, new String[]{DataBaseManager.COLUMN_MEMES_NAME, DataBaseManager.COLUMN_MEMES_ID, DataBaseManager.COLUMN_MEMES_ASSET}, null, null, null, null, DataBaseManager.COLUMN_MEMES_CREATE_DATE + " DESC");
        ArrayList<Meme> memes = new ArrayList<Meme>();
        if (cursor.moveToFirst()) {
            do {
                Meme meme = new Meme(getIntFromColumnName(cursor, DataBaseManager.COLUMN_MEMES_ID),
                        getStringFromColumnName(cursor, DataBaseManager.COLUMN_MEMES_ASSET),
                        getStringFromColumnName(cursor, DataBaseManager.COLUMN_MEMES_NAME), null);
                memes.add(meme);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return memes;
    }

    private int getIntFromColumnName(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName);
        return cursor.getInt(columnIndex);
    }

    private String getStringFromColumnName(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName);
        return cursor.getString(columnIndex);
    }
// Obteniendo Frases

    public void addMemeAnnotations(ArrayList<Meme> memes) {
        SQLiteDatabase db = openReadable();
        ArrayList<MemeAnnotation> annotations;
        Cursor cursor;
        MemeAnnotation annotation;


        for (Meme meme : memes) {
            annotations = new ArrayList<MemeAnnotation>();
            cursor = db.rawQuery("SELECT * FROM " + DataBaseManager.MEMES_TABLE + " WHERE " + DataBaseManager.COLUMN_ANNOTATIONS_FK +
                    " = " + meme.getId(), null);

            if (cursor.moveToFirst()) {
                do {
                    annotation = new MemeAnnotation(getIntFromColumnName(cursor, DataBaseManager.COLUMN_ANNOTATIONS_ID),
                            getStringFromColumnName(cursor, DataBaseManager.COLUMN_ANNOTATIONS_COLOR),
                            getStringFromColumnName(cursor, DataBaseManager.COLUMN_ANNOTATIONS_TITLE),
                            getIntFromColumnName(cursor, DataBaseManager.COLUMN_ANNOTATIONS_Y),
                            getIntFromColumnName(cursor, DataBaseManager.COLUMN_ANNOTATIONS_X)
                    );


                    annotations.add(annotation);

                } while (cursor.moveToNext());

                meme.setAnnotations(annotations);
                cursor.close();

            }

            db.close();
        }
    }


    public void update(Meme meme) {
        SQLiteDatabase database = openWriteable();
        database.beginTransaction();

        ContentValues updateMemeValues = new ContentValues();
        updateMemeValues.put(DataBaseManager.COLUMN_MEMES_NAME, meme.getName());

        database.update(DataBaseManager.MEMES_TABLE,
                updateMemeValues,
                String.format("%s=%d", DataBaseManager.COLUMN_MEMES_ID, meme.getId()),
                null);

        for (MemeAnnotation memeAnnotation : meme.getAnnotations()) {
            ContentValues updateAnnotation = new ContentValues();
            updateAnnotation.put(DataBaseManager.COLUMN_ANNOTATIONS_TITLE, memeAnnotation.getTitle());
            updateAnnotation.put(DataBaseManager.COLUMN_ANNOTATIONS_X, memeAnnotation.getLocationX());
            updateAnnotation.put(DataBaseManager.COLUMN_ANNOTATIONS_Y, memeAnnotation.getLocationY());
            updateAnnotation.put(DataBaseManager.COLUMN_ANNOTATIONS_COLOR, memeAnnotation.getColor());
            updateAnnotation.put(DataBaseManager.COLUMN_ANNOTATIONS_FK, meme.getId());
            if (memeAnnotation.hasBeenSaved()) {
                database.update(DataBaseManager.MEMES_TABLE,
                        updateAnnotation,
                        String.format("%s=%d", DataBaseManager.COLUMN_ANNOTATIONS_FK, memeAnnotation.getId()),
                        null);
            } else {
                database.insert(DataBaseManager.MEMES_TABLE, null, updateAnnotation);
            }
        }

        database.setTransactionSuccessful();
        database.endTransaction();

        CloseDB(database);
    }


    public void delete(int memeId) {
        SQLiteDatabase database = openWriteable();
        database.beginTransaction();

        database.delete(DataBaseManager.MEMES_TABLE,
                String.format("%s=%d", DataBaseManager.COLUMN_ANNOTATIONS_FK, memeId),
                null);
        database.delete(DataBaseManager.MEMES_TABLE,
                String.format("%s=%d", DataBaseManager.COLUMN_MEMES_ID, memeId),
                null);

        database.setTransactionSuccessful();
        database.endTransaction();
        CloseDB(database);
    }


}
