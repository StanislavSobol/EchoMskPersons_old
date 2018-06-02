package com.gmail.echomskfan.persons.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import android.support.annotation.VisibleForTesting
import com.gmail.echomskfan.persons.data.dao.VipDao
import com.gmail.echomskfan.persons.data.entity.VipEntity

@Database(entities = arrayOf(VipEntity::class), version = 1)
abstract class PersonsDatabase : RoomDatabase() {

    abstract fun getVipDao(): VipDao

    companion object {
        private const val DB_NAME = "db"

        private var INSTANCE: PersonsDatabase? = null

        fun getInstance(appContext: Context): PersonsDatabase {
            if (INSTANCE == null) {
                synchronized(PersonsDatabase::class) {
                    INSTANCE = Room
                            .databaseBuilder(appContext, PersonsDatabase::class.java, DB_NAME)
                            .build()
                }
            }
            return INSTANCE!!
        }

        @VisibleForTesting
        fun getTestInstance(appContext: Context): PersonsDatabase {
            if (INSTANCE == null) {
                synchronized(PersonsDatabase::class) {
                    INSTANCE = Room
                            .inMemoryDatabaseBuilder(appContext, PersonsDatabase::class.java)
                            .build()
                }
            }
            return INSTANCE!!
        }
    }
}