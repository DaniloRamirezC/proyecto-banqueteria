package com.danilo.proyectobanqueteria.data.local.entity.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.danilo.proyectobanqueteria.data.local.entity.EventoEntity
import com.danilo.proyectobanqueteria.data.local.entity.dao.EventoDao
import kotlin.concurrent.Volatile

@Database(entities = [EventoEntity::class], version = 1, exportSchema = false)
abstract class EventDatabase: RoomDatabase() {

    abstract fun EventoDao(): EventoDao
    companion object{

        @Volatile
        private var INSTANCE: EventDatabase? = null

        fun getEventDatabase(context: Context): EventDatabase{
            return INSTANCE?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EventDatabase::class.java,
                    "event_database.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}