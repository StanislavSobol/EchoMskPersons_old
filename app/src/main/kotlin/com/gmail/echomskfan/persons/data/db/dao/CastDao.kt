package com.gmail.echomskfan.persons.data.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.gmail.echomskfan.persons.data.ItemDTO

@Dao
interface CastDao {

    @Query("SELECT * FROM " + ItemDTO.TABLE)
    fun getAll(): List<ItemDTO>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cast: ItemDTO)

}