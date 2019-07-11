package muchbeer.raum.com.gcertnotekeeper.data;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import muchbeer.raum.com.gcertnotekeeper.NoteActivity;
import muchbeer.raum.com.gcertnotekeeper.R;
import muchbeer.raum.com.gcertnotekeeper.datahouse.NoteDatabaseContract;
import muchbeer.raum.com.gcertnotekeeper.datahouse.NoteDatabaseContract.NoteInfoEntry;

/**
 * Created by Jim.
 */

public class NoteRecyclerAdapter extends RecyclerView.Adapter<NoteRecyclerAdapter.ViewHolder> {

    private final Context mContext;
    private Cursor mCursor;
    private int mCoursePos;
    private int mNoteTitlePos;
    private int mIdPos;
    private final LayoutInflater mLayoutInflater;

  /*  public NoteRecyclerAdapter(Context context, List<NoteInfo> notes) {
        mContext = context;
        mNotes = notes;

        mLayoutInflater = LayoutInflater.from(mContext);
    }*/

    public NoteRecyclerAdapter(Context context, Cursor cursor) {

        mContext = context;
        mCursor = cursor;

        mLayoutInflater = LayoutInflater.from(mContext);
        populateColumnPositions();

    }

    public void changeCursor(Cursor cursor) {
        if(mCursor != null)
            mCursor.close();
        mCursor = cursor;
        populateColumnPositions();
        notifyDataSetChanged();
    }

    private void populateColumnPositions() {
        if(mCursor == null)
            return;
        // Get column indexes from mCursor
        mCoursePos = mCursor.getColumnIndex(NoteDatabaseContract.CourseInfoEntry.COLUMN_COURSE_TITLE);
        mNoteTitlePos = mCursor.getColumnIndex(NoteInfoEntry.COLUMN_NOTE_TITLE);
        mIdPos = mCursor.getColumnIndex(NoteInfoEntry._ID);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_note_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
     //   NoteInfo note = mNotes.get(position);
       /* holder.mTextCourse.setText(note.getCourse().getTitle());
        holder.mTextTitle.setText(note.getTitle());
        holder.mCurrentPosition = position;*/

        String course = mCursor.getString(mCoursePos);
        String noteTitle = mCursor.getString(mNoteTitlePos);
        int id = mCursor.getInt(mIdPos);

        holder.mTextCourse.setText(course);
        holder.mTextTitle.setText(noteTitle);
        holder.mId = id;
    }

    @Override
    public int getItemCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView mTextCourse;
        public final TextView mTextTitle;
      //  public int mCurrentPosition;
        public int mId;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextCourse = (TextView) itemView.findViewById(R.id.text_course);
            mTextTitle = (TextView) itemView.findViewById(R.id.text_title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, NoteActivity.class);
                    intent.putExtra(NoteActivity.NOTE_ID, mId);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}







