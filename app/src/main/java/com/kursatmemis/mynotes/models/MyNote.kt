package com.kursatmemis.mynotes.models

import java.io.Serializable

data class MyNote(
    val id: Int = 0,
    val title: String,
    val detail: String,
    val date: String
) : Serializable {
    override fun toString(): String {
        return title
    }
}
