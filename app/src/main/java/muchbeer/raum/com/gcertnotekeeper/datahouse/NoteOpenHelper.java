package muchbeer.raum.com.gcertnotekeeper.datahouse;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class NoteOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "NoteKeeper.db";
    public static final int DATABASE_VERSION = 3;
    public NoteOpenHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(NoteDatabaseContract.CourseInfoEntry.SQL_CREATE_TABLE);
        db.execSQL(NoteDatabaseContract.NoteInfoEntry.SQL_CREATE_TABLE);
        db.execSQL(NoteDatabaseContract.CourseInfoEntry.SQL_CREATE_INDEX1);
        db.execSQL(NoteDatabaseContract.NoteInfoEntry.SQL_CREATE_INDEX1);

        DatabaseDataSample sampleData = new DatabaseDataSample(db);
        sampleData.insertCourses();
        sampleData.insertSampleNotes();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion < 3) {
            db.execSQL(NoteDatabaseContract.CourseInfoEntry.SQL_CREATE_INDEX1);
            db.execSQL(NoteDatabaseContract.NoteInfoEntry.SQL_CREATE_INDEX1);
        }
    }
}
