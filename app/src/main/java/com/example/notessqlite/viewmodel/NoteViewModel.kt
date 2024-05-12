package com.example.notessqlite.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.notessqlite.model.Note
import com.example.notessqlite.repository.NoteRepsitory
import kotlinx.coroutines.launch

class NoteViewModel(app:Application,private val noteRepsitory: NoteRepsitory):AndroidViewModel(app) {

    fun addNote(note: Note) =
        viewModelScope.launch {
            noteRepsitory.insertNote(note)
        }
    fun deleteNote(note: Note) =
        viewModelScope.launch {
            noteRepsitory.deleteNote(note)
        }
    fun updateNote(note: Note) =
        viewModelScope.launch {
            noteRepsitory.updateNote(note)
        }

    fun getAllNotes() = noteRepsitory.getAllNotes()

    fun searchNote(query:String?) =
        noteRepsitory.searchNote(query)
}