package com.kursatmemis.mynotes.database_helpers

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import com.kursatmemis.mynotes.R
import com.kursatmemis.mynotes.database_tables.MyNotesTable
import com.kursatmemis.mynotes.models.MyNote
import java.util.*

class MyNotesDatabaseHelper(val context: AppCompatActivity) :
    SQLiteOpenHelper(context, MyNotesTable.TABLE_NAME, null, MyNotesTable.VERSION),
    java.io.Serializable {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(MyNotesTable.TABLE_CREATE_CODE)
        db?.execSQL(
            "INSERT INTO ${MyNotesTable.TABLE_NAME} (${MyNotesTable.COLUMN_TITLE}, ${MyNotesTable.COLUMN_DETAIL}, ${MyNotesTable.COLUMN_DATE}) VALUES  ('${context.getString(R.string.example_note_title_1)}', '${context.getString(R.string.example_note_detail_1)}', '${getCurrentDate()}')"
        )
        db?.execSQL(
            "INSERT INTO ${MyNotesTable.TABLE_NAME} (${MyNotesTable.COLUMN_TITLE}, ${MyNotesTable.COLUMN_DETAIL}, ${MyNotesTable.COLUMN_DATE}) VALUES  ('${context.getString(R.string.example_note_title_2)}', '${context.getString(R.string.example_note_detail_2)}', '${getCurrentDate()}')"
        )
        db?.execSQL(
            "INSERT INTO ${MyNotesTable.TABLE_NAME} (${MyNotesTable.COLUMN_TITLE}, ${MyNotesTable.COLUMN_DETAIL}, ${MyNotesTable.COLUMN_DATE}) VALUES  ('${context.getString(R.string.example_note_title_3)}', '${context.getString(R.string.example_note_detail_3)}', '${getCurrentDate()}')"
        )
        db?.execSQL(
            "INSERT INTO ${MyNotesTable.TABLE_NAME} (${MyNotesTable.COLUMN_TITLE}, ${MyNotesTable.COLUMN_DETAIL}, ${MyNotesTable.COLUMN_DATE}) VALUES  ('${context.getString(R.string.example_note_title_4)}', '${context.getString(R.string.example_note_detail_4)}', '${getCurrentDate()}')"
        )
        db?.execSQL(
            "INSERT INTO ${MyNotesTable.TABLE_NAME} (${MyNotesTable.COLUMN_TITLE}, ${MyNotesTable.COLUMN_DETAIL}, ${MyNotesTable.COLUMN_DATE}) VALUES  ('${context.getString(R.string.example_note_title_5)}', '${context.getString(R.string.example_note_detail_5)}', '${getCurrentDate()}')"
        )

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${MyNotesTable.TABLE_NAME}")
        onCreate(db)
    }

    fun addNote(title: String, detail: String, date: String): Long {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(MyNotesTable.COLUMN_TITLE, title)
        contentValues.put(MyNotesTable.COLUMN_DETAIL, detail)
        contentValues.put(MyNotesTable.COLUMN_DATE, date)
        val result = database.insert(MyNotesTable.TABLE_NAME, null, contentValues)
        database.close()
        return result
    }

    fun deleteNote(id: Int): Int {
        val database = this.writableDatabase
        return database.delete(
            MyNotesTable.TABLE_NAME,
            "${MyNotesTable.COLUMN_ID} =?",
            arrayOf(id.toString())
        )
    }

    fun deleteAllNotes() {
        val database = this.writableDatabase
        database.delete(MyNotesTable.TABLE_NAME, null, null)
    }

    fun getAllNotes(): MutableList<MyNote> {
        val notes = mutableListOf<MyNote>()
        val database = this.readableDatabase
        val cursor = database.query(MyNotesTable.TABLE_NAME, null, null, null, null, null, null)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(0)
            val title = cursor.getString(1)
            val detail = cursor.getString(2)
            val date = cursor.getString(3)
            val myNote = MyNote(id, title, detail, date)
            notes.add(myNote)
        }
        cursor.close()
        database.close()
        return notes
    }

    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return "$day/$month/$year"
    }
}