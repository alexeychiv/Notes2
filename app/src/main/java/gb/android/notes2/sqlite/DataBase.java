package gb.android.notes2.sqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import gb.android.notes2.model.NoteListItem;
import gb.android.notes2.utils.NoteDate;

public class DataBase extends SQLiteOpenHelper {

    public static final String TABLE_NOTE_LINES = "TABLE_NOTE_LINES";
    public static final String TABLE_NOTE_TEXTS = "TABLE_NOTE_TEXTS";

    public static final String COLUMN_NOTE_ID = "NOTE_ID";
    public static final String COLUMN_NOTE_TITLE = "NOTE_TITLE";
    public static final String COLUMN_NOTE_DATE = "NOTE_DATE";
    public static final String COLUMN_NOTE_TEXT = "NOTE_TEXT";

    //===================================================================================================
    // CONSTRUCTOR

    public DataBase(Context context) {
        super(context, "notes.db", null, 1);
    }

    // ===================================================================================================
    // SQLiteOpenHelper
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(String.format(
                "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT)",
                TABLE_NOTE_LINES,
                COLUMN_NOTE_ID,
                COLUMN_NOTE_TITLE,
                COLUMN_NOTE_DATE));
        db.execSQL(String.format(
                "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT)",
                TABLE_NOTE_TEXTS,
                COLUMN_NOTE_ID,
                COLUMN_NOTE_TEXT));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // ===================================================================================================
    // PUBLIC METHODS

    public boolean addNote() {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cvLine = new ContentValues();

        cvLine.put(COLUMN_NOTE_TITLE, "New Note");
        cvLine.put(COLUMN_NOTE_DATE, NoteDate.getCurrentDateString());

        if (db.insert(TABLE_NOTE_LINES, null, cvLine) < 0)
            return false;

        ContentValues cvText = new ContentValues();

        cvText.put(COLUMN_NOTE_TEXT, "");

        return db.insert(TABLE_NOTE_TEXTS, null, cvText) != -1;
    }

    public boolean updateNoteItem(int id, String title, String date) {
        @SuppressLint("DefaultLocale") String query = String.format(
                "UPDATE %s SET %s = '%s', %s = '%s' WHERE %s = %d",
                TABLE_NOTE_LINES,
                COLUMN_NOTE_TITLE, title,
                COLUMN_NOTE_DATE, date,
                COLUMN_NOTE_ID, id);

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            cursor.close();
            db.close();
            return true;
        }

        cursor.close();
        db.close();
        return false;
    }

    public boolean updateNoteText(int id, String text) {
        @SuppressLint("DefaultLocale") String query = String.format(
                "UPDATE %s SET %s = '%s' WHERE %s = %d",
                TABLE_NOTE_TEXTS,
                COLUMN_NOTE_TEXT, text,
                COLUMN_NOTE_ID, id);

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            cursor.close();
            db.close();
            return true;
        }

        cursor.close();
        db.close();
        return false;
    }


    @SuppressLint("DefaultLocale")
    public boolean deleteNote(int id) {
        @SuppressLint("DefaultLocale") String query = String.format(
                "DELETE FROM %s WHERE %s = %d",
                TABLE_NOTE_LINES,
                COLUMN_NOTE_ID, id);

        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (!cursor.moveToFirst())
            return false;

        query = String.format(
                "DELETE FROM %s WHERE %s = %d",
                TABLE_NOTE_TEXTS,
                COLUMN_NOTE_ID, id);

        cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            cursor.close();
            db.close();
            return true;
        }

        cursor.close();
        db.close();
        return false;
    }

    @SuppressLint("DefaultLocale")
    public boolean deleteAll() {
        @SuppressLint("DefaultLocale") String query = String.format(
                "DELETE FROM %s WHERE EXISTS (SELECT * FROM %s)",
                TABLE_NOTE_LINES,
                TABLE_NOTE_LINES);

        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (!cursor.moveToFirst())
            return false;

        query = String.format(
                "DELETE FROM %s WHERE EXISTS (SELECT * FROM %s)",
                TABLE_NOTE_TEXTS,
                TABLE_NOTE_TEXTS);

        cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            cursor.close();
            db.close();
            return true;
        }

        cursor.close();
        db.close();
        return false;
    }

    public List<NoteListItem> getNotesList() {
        List<NoteListItem> returnList = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_NOTE_LINES;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
            do {
                returnList.add(new NoteListItem(
                        String.valueOf(cursor.getInt(0)),
                        cursor.getString(1),
                        cursor.getString(2))
                );

            } while (cursor.moveToNext());

        cursor.close();
        db.close();

        return returnList;
    }

    public NoteListItem getNoteListItem(int id) {
        NoteListItem noteListItem = null;

        @SuppressLint("DefaultLocale") String query = String.format(
                "SELECT * FROM %s WHERE %s = %d",
                TABLE_NOTE_LINES,
                COLUMN_NOTE_ID, id);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
            noteListItem = new NoteListItem(
                    String.valueOf(cursor.getInt(0)),
                    cursor.getString(1),
                    cursor.getString(2)
            );

        cursor.close();
        db.close();

        return noteListItem;
    }

    public String getNoteText(int id) {
        String result = null;

        @SuppressLint("DefaultLocale") String query = String.format(
                "SELECT * FROM %s WHERE %s = %d",
                TABLE_NOTE_TEXTS,
                COLUMN_NOTE_ID, id);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
            result = cursor.getString(1);

        cursor.close();
        db.close();

        return result;
    }
}
