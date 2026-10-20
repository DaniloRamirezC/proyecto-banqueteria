package com.danilo.proyectobanqueteria.data.local.entity.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.danilo.proyectobanqueteria.data.local.entity.UsuarioEntity
import com.danilo.proyectobanqueteria.data.local.entity.dao.UsuarioDao
import kotlin.concurrent.Volatile

@Database(entities = [UsuarioEntity::class], version = 1, exportSchema = false)
abstract class UserDatabase: RoomDatabase() {

    abstract fun UsuarioDao(): UsuarioDao
    companion object{

        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase{
            return INSTANCE?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "user_database.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}