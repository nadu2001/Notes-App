package com.example.notessqlite.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.notessqlite.repository.NoteRepsitory

class NoteViewModelFactory(val app:Application,private val noteRepsitory: NoteRepsitory):ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NoteViewModel(app, noteRepsitory) as T
    }
}