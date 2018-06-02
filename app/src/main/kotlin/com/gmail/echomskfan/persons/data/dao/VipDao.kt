package com.gmail.echomskfan.persons.data.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import com.gmail.echomskfan.persons.data.entity.VipEntity

@Dao
interface VipDao {

    @Query("SELECT * from " + VipEntity.TABLE)
    fun getAll(): List<VipEntity>

    @Insert(onConflict = REPLACE)
    fun insert(vipEntity: VipEntity)

    @Query("DELETE from " + VipEntity.TABLE)
    fun deleteAll()
}