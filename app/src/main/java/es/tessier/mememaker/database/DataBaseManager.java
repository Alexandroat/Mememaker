package es.tessier.mememaker.database;

/**
 * Created by alejandro on 11/02/15.
 */
public class DataBaseManager {
    //MEME table
    public static final String MEMES_TABLE = "MEMES";
    public static final String COLUMN_MEMES_ASSET = "ASSET";
    public static final String COLUMN_MEMES_NAME = "NAME";
    public static final String COLUMN_MEMES_ID = "_ID";

    //MEME table Annotations

    public static final String ANNOTATIONS_TABLE = "ANNOTATIONS";
    public static final String COLUMN_ANNOTATIONS_NAME = "NAME";
    public static final String COLUMN_ANNOTATIONS_ID = "_ID";
    public static final String COLUMN_ANNOTATIONS_FK = "FK_MEME_ID";

}