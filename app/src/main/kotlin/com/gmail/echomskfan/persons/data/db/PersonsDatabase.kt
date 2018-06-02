package com.gmail.echomskfan.persons.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import android.support.annotation.VisibleForTesting
import com.gmail.echomskfan.persons.data.db.dao.VipDao
import com.gmail.echomskfan.persons.data.entity.VipEntity

@Database(entities = arrayOf(VipEntity::class), version = 1)
abstract class PersonsDatabase : RoomDatabase() {

    abstract fun getVipDao(): VipDao

    companion object {
        private const val DB_NAME = "db"

        private var instance: PersonsDatabase? = null

        fun getInstance(appContext: Context): PersonsDatabase {
            if (instance == null) {
                synchronized(PersonsDatabase::class) {
                    instance = Room
                            .databaseBuilder(appContext, PersonsDatabase::class.java, DB_NAME)
                            .build()
                }
            }
            return instance!!
        }

        @VisibleForTesting
        fun getTestInstance(appContext: Context): PersonsDatabase {
            if (instance == null) {
                synchronized(PersonsDatabase::class) {
                    instance = Room
                            .inMemoryDatabaseBuilder(appContext, PersonsDatabase::class.java)
                            .build()
                }
            }
            return instance!!
        }
    }
}