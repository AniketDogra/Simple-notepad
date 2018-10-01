//Database
package com.example.android.n_otepad;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "note.db";
    private static final int DATABASE_VERSION = 1;
    public static String NOTES_TABLE = "note";
    public static String COLUMN_ID = "_id";
    public static String COLUMN_TITLE = "title";
    public static String COLUMN_CONTENT = "content";
    //public static final String COLUMN_MODIFIED_TIME = "modified_time";
    //public static final String COLUMN_CREATED_TIME = "created_time";
    private static final String[] COLUMNS = {COLUMN_ID,COLUMN_TITLE,COLUMN_CONTENT};

    public DatabaseHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_NOTE = "create table "
                + NOTES_TABLE
                + "("
                + COLUMN_ID + " integer primary key autoincrement, "
                + COLUMN_TITLE + " text not null, "
                + COLUMN_CONTENT + " text not null);";
        db.execSQL(CREATE_TABLE_NOTE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ NOTES_TABLE);
        onCreate(db);
    }

    public void insertNote(Note note)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE,note.getTitle());
        values.put(COLUMN_CONTENT,note.getContent());

        db.insert(NOTES_TABLE,null,values);
    }

    public void deleteNote(Note note)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(NOTES_TABLE,COLUMN_ID + " = ?", new String[]{String.valueOf(note.getId()
        )});
    }

    public void updateNote(Note note)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE,note.getTitle());
        values.put(COLUMN_CONTENT,note.getContent());

        db.update(NOTES_TABLE,values,COLUMN_ID + " = ?",new String[]{String.valueOf(note.getId())});
    }

    public List<Note> getAllNotes()
    {
        String sql = "SELECT * FROM " + NOTES_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        List<Note> storeNote = new ArrayList<Note>();
        Cursor cursor = db.rawQuery(sql,null);
        if(cursor.moveToFirst())
        {
            do
            {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String content = cursor.getString(2);
                storeNote.add(new Note(id,title,content));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return storeNote;
    }

    public Note getSingleNote(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Note note = null;
        Cursor cursor = db.rawQuery("SELECT * FROM " + NOTES_TABLE + " WHERE " + COLUMN_ID + "=?" ,new String[]{String.valueOf(id)});
        if(cursor!=null && cursor.moveToFirst())
        {
            int id1 = cursor.getInt(0);
            String title = cursor.getString(1);
            String content = cursor.getString(2);
            note = new Note(id1,title,content);
        }
        cursor.close();

        return note;
    }

    public int getNotesCount()
    {
        String query = "SELECT * FROM " + NOTES_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public List<Note> searchNote(String searchQuery)
    {
        String sql = "SELECT * FROM "+NOTES_TABLE + " WHERE " + COLUMN_TITLE + " LIKE %" + searchQuery + "%";
        SQLiteDatabase db = this.getReadableDatabase();
        List<Note> searchNote = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + NOTES_TABLE + " WHERE " +COLUMN_TITLE + " LIKE ?",new String[]{'%' + searchQuery + '%'} );
        if(cursor.moveToFirst())
        {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String content = cursor.getString(2);
                searchNote.add(new Note(id,title,content));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return searchNote;
    }
}
