package com.kursatmemis.mynotes.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.kursatmemis.mynotes.R
import com.kursatmemis.mynotes.models.MyNote

class NotesAdapter(val context: AppCompatActivity, val notes: MutableList<MyNote>) :
    ArrayAdapter<MyNote>(context, R.layout.custom_text_view, notes) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater = context.layoutInflater
        val customView = layoutInflater.inflate(R.layout.custom_text_view, null)
        val noteTitleTextView = customView.findViewById<TextView>(R.id.noteTitleTextView)
        noteTitleTextView.text = notes[position].title
        val color: Int = when(position % 5) {
            0 -> ContextCompat.getColor(context, R.color.light_blue)
            1 -> ContextCompat.getColor(context, R.color.light_pink)
            2 -> ContextCompat.getColor(context, R.color.light_green)
            3 -> ContextCompat.getColor(context, R.color.light_purple)
            4 -> ContextCompat.getColor(context, R.color.light_yellow)
            else -> ContextCompat.getColor(context, R.color.black)
        }
        customView.setBackgroundColor(color)
        return customView
    }
}