package com.kursatmemis.mynotes.database_tables

class MyNotesTable {
    companion object {
        val TABLE_NAME = "MyNotesTableM"
        val VERSION = 1
        val TABLE_CREATE_CODE = "CREATE TABLE \"MyNotesTableM\" (\n" +
                "\t\"id\"\tINTEGER,\n" +
                "\t\"title\"\tTEXT,\n" +
                "\t\"detail\"\tTEXT,\n" +
                "\t\"date\"\tTEXT,\n" +
                "\tPRIMARY KEY(\"id\" AUTOINCREMENT)\n" +
                ");"
        val COLUMN_ID = "id"
        val COLUMN_TITLE = "title"
        val COLUMN_DETAIL = "detail"
        val COLUMN_DATE = "date"
    }
}