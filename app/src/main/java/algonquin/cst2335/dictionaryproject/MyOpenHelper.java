package algonquin.cst2335.dictionaryproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyOpenHelper extends SQLiteOpenHelper {
    public static final String filename = "TheDataBase";
    public static final int version = 3;
    public static final String TABLE_NAME = "MyDictionary";
    public static final String COL_ID = "_id";
    public static final String COL_DEFINITION = "WordDefinitions";
    public static final String COL_WORD = "Search";


    public MyOpenHelper(Context context) {
        super(context, filename, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "Create table " + TABLE_NAME + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_WORD + " TEXT, " + COL_DEFINITION + " TEXT);";

        db.execSQL(query);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("Drop table if exists " + TABLE_NAME);
        this.onCreate(db);

    }
}
