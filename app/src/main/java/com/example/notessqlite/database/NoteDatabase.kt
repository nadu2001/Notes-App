package com.example.notessqlite.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notessqlite.model.Note

// Define a Room Database with Note entity and version 1
@Database(entities = [Note::class],version = 1)
abstract class NoteDatabase :RoomDatabase() {

    // Abstract function to get NoteDao
    abstract fun getNoteDao():NoteDao

    // Companion object to hold singleton instance of the database
    companion object{

        // Volatile to ensure instance is always up-to-date across threads
        @Volatile
        private var instance:NoteDatabase?=null
        // Lock object to synchronize instance creation
        private var LOCK =Any()

        // Function to get instance of the database, creating it if necessary
        operator fun invoke(context:Context)=instance ?:
        synchronized(LOCK){
            instance ?:
            createDatabase(context).also{
                instance =it
            }
        }

        // Function to create the database
        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                NoteDatabase::class.java,
                "note_db"
            ).build()
    }


}