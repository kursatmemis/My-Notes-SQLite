package com.kursatmemis.mynotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class NoteDetailActivity : AppCompatActivity() {
    private lateinit var detailNoteHeaderTextView: TextView
    private lateinit var idTextView: TextView
    private lateinit var titleTextView: TextView
    private lateinit var detailTextView: TextView
    private lateinit var dateTextView: TextView
    private lateinit var deleteNoteButton: Button

    companion object {
        var isDeleted: Boolean = false // Kullanıcının item'ı silip silmediği bilgisini tutar.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_detail)
        setViews() // XML dosyasındaki view'ları, kotlin objelerine set eder.

        setDetailTexts() // Detail sayfasına gidilen item'ın bilgilerini set eder.

        // Eğer 'Notu Sil' butonuna tıklanırsa, not, veritabanından silinir ve bu activity kapanır.
        // Silinen notun, adapter'dan silinmesi işlemi ise MainActivity - onRestart methodunda
        // gerçekleştirilmiştir.
        deleteNoteButton.setOnClickListener {
            MainActivity.dbHelper.deleteNote(MainActivity.deletedNote!!.id)
            isDeleted = true
            finish()
        }
    }

    // Detail sayfasına gidilen item'ın bilgilerini set eder.
    private fun setDetailTexts() {
        idTextView.text = MainActivity.deletedNote?.id.toString()
        titleTextView.text = MainActivity.deletedNote?.title
        detailTextView.text = MainActivity.deletedNote?.detail
        dateTextView.text = MainActivity.deletedNote?.date
    }

    // XML dosyasındaki view'ları, kotlin objelerine set eder.
    private fun setViews() {
        detailNoteHeaderTextView = findViewById(R.id.detailNoteHeaderTextView)
        idTextView = findViewById(R.id.idTextView)
        titleTextView = findViewById(R.id.titleTextView)
        detailTextView = findViewById(R.id.detailTextView)
        dateTextView = findViewById(R.id.dateTextView)
        deleteNoteButton = findViewById(R.id.deleteNoteButton)
    }
}