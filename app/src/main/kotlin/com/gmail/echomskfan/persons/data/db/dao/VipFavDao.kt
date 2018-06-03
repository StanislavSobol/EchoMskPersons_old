package com.gmail.echomskfan.persons.data.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.IGNORE
import android.arch.persistence.room.Query
import com.gmail.echomskfan.persons.data.entity.VipFavEntity

@Dao
interface VipFavDao {

    @Query("SELECT * FROM " + VipFavEntity.TABLE)
    fun getAll(): List<VipFavEntity>

    @Insert(onConflict = IGNORE)
    fun insert(vipFavEntity: VipFavEntity)

    @Query("SELECT * FROM " + VipFavEntity.TABLE + " WHERE " + VipFavEntity.FIELD_URL + " = :url")
    fun getByPk(url: String): VipFavEntity
}