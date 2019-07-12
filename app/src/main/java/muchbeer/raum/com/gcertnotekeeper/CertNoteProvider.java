package muchbeer.raum.com.gcertnotekeeper;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.ContactsContract;

import muchbeer.raum.com.gcertnotekeeper.data.CourseInfo;
import muchbeer.raum.com.gcertnotekeeper.datahouse.NoteDatabaseContract;
import muchbeer.raum.com.gcertnotekeeper.datahouse.NoteDatabaseContract.CourseInfoEntry;
import muchbeer.raum.com.gcertnotekeeper.datahouse.NoteDatabaseContract.NoteInfoEntry;
import muchbeer.raum.com.gcertnotekeeper.datahouse.NoteOpenHelper;
import muchbeer.raum.com.gcertnotekeeper.datahouse.NoteProviderContract;
import muchbeer.raum.com.gcertnotekeeper.datahouse.NoteProviderContract.Courses;
import muchbeer.raum.com.gcertnotekeeper.datahouse.NoteProviderContract.CoursesIdColumns;
import muchbeer.raum.com.gcertnotekeeper.datahouse.NoteProviderContract.Notes;

import static muchbeer.raum.com.gcertnotekeeper.datahouse.NoteProviderContract.Notes.PATH;
import static muchbeer.raum.com.gcertnotekeeper.datahouse.NoteProviderContract.Notes.PATH_EXPANDED;

public class CertNoteProvider extends ContentProvider {

    private static final String MIME_VENDOR_TYPE = "vnd." + NoteProviderContract.AUTHORITY + ".";

    public CertNoteProvider() {
    }
private static UriMatcher sUriMather = new UriMatcher(UriMatcher.NO_MATCH);

    public static final int COURSE = 0;
    public static final int NOTES = 1;
    public static final int NOTE_EXPANDED = 2;
    public static final int NOTE_ROW = 3;
    private static final int COURSES_ROW = 4;
    private static final int NOTES_EXPANDED_ROW = 5;

    static {
        sUriMather.addURI(NoteProviderContract.AUTHORITY, Courses.PATH, COURSE);
        sUriMather.addURI(NoteProviderContract.AUTHORITY, PATH, NOTES);
        sUriMather.addURI(NoteProviderContract.AUTHORITY, PATH_EXPANDED, NOTE_EXPANDED);

        sUriMather.addURI(NoteProviderContract.AUTHORITY, PATH + "/#", NOTE_ROW);
        sUriMather.addURI(NoteProviderContract.AUTHORITY, Courses.PATH + "/#", COURSES_ROW);
        sUriMather.addURI(NoteProviderContract.AUTHORITY, Notes.PATH_EXPANDED + "/#", NOTES_EXPANDED_ROW);

    }
    private NoteOpenHelper mDbOpenHelper;
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {

        // TODO: Implement this to handle requests for the MIME type of the data
        String mimeType = null;
        int uriMatch = sUriMather.match(uri);
        switch(uriMatch){
            case COURSE:
                // vnd.android.cursor.dir/vnd.com.jwhh.jim.notekeeper.provider.courses
                mimeType = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
                        MIME_VENDOR_TYPE + Courses.PATH;
                break;
            case NOTES:
                mimeType = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + MIME_VENDOR_TYPE + Notes.PATH;
                break;
            case NOTE_EXPANDED:
                mimeType = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + MIME_VENDOR_TYPE + Notes.PATH_EXPANDED;
                break;
            case COURSES_ROW:
                mimeType = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + MIME_VENDOR_TYPE + Courses.PATH;
                break;
            case NOTE_ROW:
                mimeType = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + MIME_VENDOR_TYPE + Notes.PATH;
                break;
            case NOTES_EXPANDED_ROW:
                mimeType = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + MIME_VENDOR_TYPE + Notes.PATH_EXPANDED;
                break;
        }
        return mimeType;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
        long rowId = -1;
        Uri rowUri = null;

        int uriMatch = sUriMather.match(uri);
        switch (uriMatch) {
            case NOTES:
                rowId = db.insert(NoteInfoEntry.TABLE_NAME, null, values);
                // content://com.jwhh.jim.notekeeper.provider/notes/1
                rowUri = ContentUris.withAppendedId(Notes.CONTENT_URI, rowId);
                break;

            case COURSE:
                rowId = db.insert(CourseInfoEntry.TABLE_NAME, null, values);
                rowUri = ContentUris.withAppendedId(Courses.CONTENT_URI, rowId);
                break;

            case NOTE_EXPANDED:
                // throw exception saying that this is a read-only table
                break;
        }

        return rowUri;
    }
    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        mDbOpenHelper = new NoteOpenHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        long rowId = -1;
        String rowSelection = null;
        String[] rowSelectionArgs = null;
        Cursor cursor = null;
        SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();

            //UriMatch . match return an integer
             int chooseUri =   sUriMather.match(uri);

             switch (chooseUri) {

                 case COURSE:
                     cursor = db.query(CourseInfoEntry.TABLE_NAME, projection, selection, selectionArgs,
                             null, null, sortOrder);
                     break;

                 case NOTES:
                     cursor = db.query(NoteInfoEntry.TABLE_NAME, projection, selection, selectionArgs,
                             null, null, sortOrder);
                     break;
                 case NOTE_EXPANDED:
                     cursor = notesExpandedQuery(db, projection, selection, selectionArgs, sortOrder);
                     break;

                 case NOTE_ROW:
                     rowId = ContentUris.parseId(uri);
                     rowSelection = NoteInfoEntry._ID + " = ?";
                     rowSelectionArgs = new String[]{Long.toString(rowId)};

                     cursor = db.query(NoteInfoEntry.TABLE_NAME, projection, rowSelection,
                             rowSelectionArgs, null, null, null);
                     break;
             }


            return cursor;
    }

    private Cursor notesExpandedQuery(SQLiteDatabase db, String[] projection,
                                      String selection, String[] selectionArgs, String sortOrder) {

        String[] columns = new String[projection.length];
        for(int idx=0; idx < projection.length; idx++) {
            columns[idx] = projection[idx].equals(BaseColumns._ID) ||
                    projection[idx].equals(CoursesIdColumns.COLUMN_COURSE_ID) ?
                    NoteInfoEntry.getQName(projection[idx]) : projection[idx];
        }

        String tableWithJoin = NoteInfoEntry.TABLE_NAME +
                " JOIN " + CourseInfoEntry.TABLE_NAME +
                " ON " +    CourseInfoEntry.getQName(CourseInfoEntry.COLUMN_COURSE_ID) +
                " = " +
                            NoteInfoEntry.getQName(NoteInfoEntry.COLUMN_COURSE_ID);

        Cursor returnJoinCursor = db.query(tableWithJoin, columns,selection,selectionArgs,
                null,null,sortOrder);

        return returnJoinCursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
