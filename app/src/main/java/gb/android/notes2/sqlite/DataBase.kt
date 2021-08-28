package gb.android.notes2.sqlite

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import gb.android.notes2.model.NoteListItem
import gb.android.notes2.utils.NoteDate.currentDateString
import java.util.*

class DataBase(context: Context?) : SQLiteOpenHelper(context, "notes.db", null, 1) {

    companion object {
        const val TABLE_NOTE_LINES = "TABLE_NOTE_LINES"
        const val TABLE_NOTE_TEXTS = "TABLE_NOTE_TEXTS"
        const val COLUMN_NOTE_ID = "NOTE_ID"
        const val COLUMN_NOTE_TITLE = "NOTE_TITLE"
        const val COLUMN_NOTE_DATE = "NOTE_DATE"
        const val COLUMN_NOTE_TEXT = "NOTE_TEXT"
    }


    // ===================================================================================================
    // SQLiteOpenHelper

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            String.format(
                "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT)",
                TABLE_NOTE_LINES,
                COLUMN_NOTE_ID,
                COLUMN_NOTE_TITLE,
                COLUMN_NOTE_DATE
            )
        )
        db.execSQL(
            String.format(
                "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT)",
                TABLE_NOTE_TEXTS,
                COLUMN_NOTE_ID,
                COLUMN_NOTE_TEXT
            )
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}


    // ===================================================================================================
    // PUBLIC METHODS

    fun addNote(): Boolean {
        val db = writableDatabase
        val cvLine = ContentValues()
        cvLine.put(COLUMN_NOTE_TITLE, "New Note")
        cvLine.put(COLUMN_NOTE_DATE, currentDateString)
        if (db.insert(TABLE_NOTE_LINES, null, cvLine) < 0) return false
        val cvText = ContentValues()
        cvText.put(COLUMN_NOTE_TEXT, "")
        return db.insert(TABLE_NOTE_TEXTS, null, cvText) != -1L
    }

    fun updateNoteItem(id: Int, title: String?, date: String?): Boolean {
        @SuppressLint("DefaultLocale") val query = String.format(
            "UPDATE %s SET %s = '%s', %s = '%s' WHERE %s = %d",
            TABLE_NOTE_LINES,
            COLUMN_NOTE_TITLE, title,
            COLUMN_NOTE_DATE, date,
            COLUMN_NOTE_ID, id
        )
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            cursor.close()
            db.close()
            return true
        }
        cursor.close()
        db.close()
        return false
    }

    fun updateNoteText(id: Int, text: String?): Boolean {
        @SuppressLint("DefaultLocale") val query = String.format(
            "UPDATE %s SET %s = '%s' WHERE %s = %d",
            TABLE_NOTE_TEXTS,
            COLUMN_NOTE_TEXT, text,
            COLUMN_NOTE_ID, id
        )
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            cursor.close()
            db.close()
            return true
        }
        cursor.close()
        db.close()
        return false
    }

    @SuppressLint("DefaultLocale")
    fun deleteNote(id: Int): Boolean {
        @SuppressLint("DefaultLocale") var query = String.format(
            "DELETE FROM %s WHERE %s = %d",
            TABLE_NOTE_LINES,
            COLUMN_NOTE_ID, id
        )
        val db = writableDatabase
        var cursor = db.rawQuery(query, null)
        if (!cursor.moveToFirst()) return false
        query = String.format(
            "DELETE FROM %s WHERE %s = %d",
            TABLE_NOTE_TEXTS,
            COLUMN_NOTE_ID, id
        )
        cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            cursor.close()
            db.close()
            return true
        }
        cursor.close()
        db.close()
        return false
    }

    @SuppressLint("DefaultLocale")
    fun deleteAll(): Boolean {
        @SuppressLint("DefaultLocale") var query = String.format(
            "DELETE FROM %s WHERE EXISTS (SELECT * FROM %s)",
            TABLE_NOTE_LINES,
            TABLE_NOTE_LINES
        )
        val db = writableDatabase
        var cursor = db.rawQuery(query, null)
        if (!cursor.moveToFirst()) return false
        query = String.format(
            "DELETE FROM %s WHERE EXISTS (SELECT * FROM %s)",
            TABLE_NOTE_TEXTS,
            TABLE_NOTE_TEXTS
        )
        cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            cursor.close()
            db.close()
            return true
        }
        cursor.close()
        db.close()
        return false
    }

    val notesList: MutableList<NoteListItem>?
        get() {
            val returnList: MutableList<NoteListItem> = ArrayList()
            val query = "SELECT * FROM $TABLE_NOTE_LINES"
            val db = this.readableDatabase
            val cursor = db.rawQuery(query, null)
            if (cursor.moveToFirst()) do {
                returnList.add(
                    NoteListItem(
                        cursor.getInt(0).toString(),
                        cursor.getString(1),
                        cursor.getString(2)
                    )
                )
            } while (cursor.moveToNext())
            cursor.close()
            db.close()
            return returnList
        }

    fun getNoteListItem(id: Int): NoteListItem? {
        var noteListItem: NoteListItem? = null
        @SuppressLint("DefaultLocale") val query = String.format(
            "SELECT * FROM %s WHERE %s = %d",
            TABLE_NOTE_LINES,
            COLUMN_NOTE_ID, id
        )
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) noteListItem = NoteListItem(
            cursor.getInt(0).toString(),
            cursor.getString(1),
            cursor.getString(2)
        )
        cursor.close()
        db.close()
        return noteListItem
    }

    fun getNoteText(id: Int): String? {
        var result: String? = null
        @SuppressLint("DefaultLocale") val query = String.format(
            "SELECT * FROM %s WHERE %s = %d",
            TABLE_NOTE_TEXTS,
            COLUMN_NOTE_ID, id
        )
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) result = cursor.getString(1)
        cursor.close()
        db.close()
        return result
    }
}