package com.kursatmemis.mynotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.kursatmemis.mynotes.adapters.NotesAdapter
import com.kursatmemis.mynotes.database_helpers.MyNotesDatabaseHelper
import com.kursatmemis.mynotes.models.MyNote
import com.shashank.sony.fancytoastlib.FancyToast
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var addNoteButton: Button
    private lateinit var deleteAllNotesButton: Button
    private lateinit var notesListView: ListView
    private lateinit var adapter: ArrayAdapter<MyNote>
    private var notes = mutableListOf<MyNote>()

    companion object {
        lateinit var dbHelper: MyNotesDatabaseHelper
        var deletedNote: MyNote? = null // Kullanıcının silmek istediği notu tutacak.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setViews() // XML dosyasındaki view'ları, kotlin objelerine set eder.
        dbHelper = MyNotesDatabaseHelper(this)
        adapter = NotesAdapter(this, notes)
        notesListView.adapter = adapter
        loadDatabase() // Uygulama açıldığında veritabanındaki verileri, adapter için veri kaynağı olan list'e set eder.


        // Listview üzerinde bir item'a tıklanıldığında, detail activity'e geçiş için bir intent
        // gerçekleştirilir. Eğer detail activity'de, ilgili not silinirse, silinme işlemi
        // 'deletedNote' değişkeni üzerinden yapılacaktır.
        notesListView.setOnItemClickListener { adapterView, view, i, l ->
            val intent = Intent(this@MainActivity, NoteDetailActivity::class.java)
            deletedNote = notes[i]
            startActivity(intent)
        }

        // Kullanıcı bir not eklemek istediğinde, bir alertdialog kullanılarak not bilgileri
        // kullanıcıdan alınır.
        // (Notun veritabanına kaydedilmesi işlemi, alertdialog-positive button içinde gerçekleştirilmiştir.)
        addNoteButton.setOnClickListener {
            val alertDialog = createAlertDialog("addNote")
            alertDialog.show()
        }

        // Kullanıcı tüm notları silmek istediğinde, alertdialog ile kullanıcıdan tekrar bir
        // onay alınır.
        // (Tüm notların veritabanından silinmesi işlemi alertdialog-positive button içinde gerçekleştirilmiştir.)
        deleteAllNotesButton.setOnClickListener {
            val alertDialog = createAlertDialog("deleteNote")
            alertDialog.show()
        }
    }


    /**
     * typeAlertDialog: Not ekleme-Tüm Notları Silme işlemlerinden hangisi için alertdialog'un
     * oluşturulacağını belirleyen parametredir.
     *
     * Not eklemek için oluşturulacak olan alertdialog, custom bir view'la oluşturulmuştur.
     * Bu custom view ile, not title'ı ve not detail'i kullanıcıdan alınır.
     * Note date'i ise, sistemin mevcut tarihine göre set edilir.
     * Bu bilgilere göre eklenmek istenen not, veritabanına kaydedilir.
     *
     * Tüm notları silmek için oluşturulacak olan alertdialog, android'in default alertdialog
     * temasıyla oluşturulmuştur. Eğer kullanıcı positive butona tıklarsa, tüm notlar veritabanından
     * silinecektir.
     */
    private fun createAlertDialog(typeAlertDialog: String): AlertDialog {
        val builder = AlertDialog.Builder(this@MainActivity)
        when (typeAlertDialog) {
            "addNote" -> {
                val customAlertDialog = layoutInflater.inflate(R.layout.custom_alert_dialog, null)
                builder.setView(customAlertDialog)
                builder.setTitle("Yeni Not Ekle")
                builder.setPositiveButton("Notu Kaydet") { dialogInterface, i ->
                    val noteTitleEditText =
                        customAlertDialog.findViewById<EditText>(R.id.noteTitleEditText)
                    val noteDetailEditText =
                        customAlertDialog.findViewById<EditText>(R.id.noteDetailEditText)
                    val title = noteTitleEditText.text.toString()
                    val detail = noteDetailEditText.text.toString()
                    val date = getCurrentDate()
                    addNote(title, detail, date)
                }
                builder.setNegativeButton("Vazgeç") { dialogInterface, i ->
                    FancyToast.makeText(
                        this,
                        "Not Kaydetme İşleminden Vazgeçildi!",
                        FancyToast.LENGTH_LONG,
                        FancyToast.DEFAULT,
                        false
                    ).show()
                }
            }
            "deleteNote" -> {
                builder.setTitle("Tüm Notları Sil")
                builder.setMessage("Tüm notları silmek istediğinizden emin misiniz?")
                builder.setPositiveButton("Evet") { dialogInterface, i ->
                    dbHelper.deleteAllNotes()
                    notes = mutableListOf()
                    adapter.clear()
                    adapter.addAll(notes)
                    adapter.notifyDataSetChanged()
                }
                builder.setNegativeButton("Vazgeç") { dialogInterface, i ->
                    FancyToast.makeText(
                        this@MainActivity,
                        "Tüm notları silme işleminden vazgeçtiniz!",
                        FancyToast.LENGTH_LONG,
                        FancyToast.INFO,
                        false
                    ).show()
                }
            }
        }
        return builder.create()
    }

    // Veritabanına not ekler.
    private fun addNote(title: String, detail: String, date: String) {
        val result = dbHelper.addNote(title, detail, date)
        if (result > 0) {
            updateAdapter() // Veri eklendi ise, listview'ın adapter'ı için kullanılan datasource'u güncellener.
            showMessage("addNote") // Not ekleme işleminin bilgisini toast mesaj ile kullanıcıya gösterir.
        } else {
            showMessage("failAddNote") // Not ekleme işleminin bilgisini toast mesaj ile kullanıcıya gösterir.
        }
        dbHelper.close()
    }

    // Listview'ın adapter'ı için kullanılan datasource'u günceller.
    private fun updateAdapter() {
        notes = dbHelper.getAllNotes()
        adapter.clear()
        adapter.addAll(notes)
        adapter.notifyDataSetChanged()
    }

    // Gönderilen argüman'a göre not ekleme işleminin bilgisini kullanıcıya toast mesaj olarak gösterir.
    private fun showMessage(typeMessage: String) {
        when (typeMessage) {
            "deleteNote" -> {
                FancyToast.makeText(
                    this@MainActivity,
                    "Not Başarıyla Silindi!",
                    FancyToast.LENGTH_LONG,
                    FancyToast.SUCCESS,
                    false
                ).show()
            }
            "addNote" -> {
                FancyToast.makeText(
                    this@MainActivity,
                    "Not Başarıyla Kaydedildi!",
                    FancyToast.LENGTH_LONG,
                    FancyToast.SUCCESS,
                    false
                ).show()
            }
            "failAddNote" -> {
                FancyToast.makeText(
                    this@MainActivity,
                    "Not Kaydedilmesi Başarısız Oldu!",
                    FancyToast.LENGTH_LONG,
                    FancyToast.ERROR,
                    false
                ).show()
            }
        }
    }

    // DetailActivity'den, MainActivity'e geri dönüşte çalışır. Eğer detail'ine gidilen not silinmiş
    // ise, ilgili not veritabanından kaldırılır ve listview'ın adapter'ı için kullanılan datasource'u
    // günceller.
    override fun onRestart() {
        super.onRestart()
        val isDeleted = NoteDetailActivity.isDeleted
        if (isDeleted) {
            notes.remove(deletedNote)
            updateAdapter()
            showMessage("deleteNote")
        }
        NoteDetailActivity.isDeleted = false
    }

    // Sistemin mevcut tarihini return eder.
    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return "$day/$month/$year"
    }

    // Uygulama açıldığında veritabanındaki verileri, adapter için veri kaynağı olan list'e set eder.
    private fun loadDatabase() {
        notes = dbHelper.getAllNotes()
        updateAdapter()
        dbHelper.close()
    }

    // XML dosyasındaki view'ları, kotlin objelerine set eder.
    private fun setViews() {
        addNoteButton = findViewById(R.id.addNoteButton)
        notesListView = findViewById(R.id.myNotesListView)
        deleteAllNotesButton = findViewById(R.id.deleteAllNotesButton)
    }
}