package com.example.notessqlite.repository

import com.example.notessqlite.database.NoteDatabase
import com.example.notessqlite.model.Note

class NoteRepsitory(private val db:NoteDatabase) {

    suspend fun insertNote(note: Note)=db.getNoteDao().insertNote(note)
    suspend fun deleteNote(note: Note)=db.getNoteDao().deleteNote(note)
    suspend fun updateNote(note: Note)=db.getNoteDao().updateNote(note)

    fun getAllNotes()=db.getNoteDao().getAllNotes()
    fun searchNote(query:String?)=db.getNoteDao().searchNote(query)
}