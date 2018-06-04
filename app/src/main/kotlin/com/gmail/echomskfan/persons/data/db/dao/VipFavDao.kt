package com.gmail.echomskfan.persons.data.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.IGNORE
import android.arch.persistence.room.Query
import com.gmail.echomskfan.persons.data.entity.VipDetailsEntity

@Dao
interface VipFavDao {

    @Query("SELECT * FROM " + VipDetailsEntity.TABLE)
    fun getAll(): List<VipDetailsEntity>

    @Insert(onConflict = IGNORE)
    fun insert(vipDetailsEntity: VipDetailsEntity)

    @Query("SELECT * FROM " + VipDetailsEntity.TABLE + " WHERE " + VipDetailsEntity.FIELD_URL + " = :url")
    fun getByPk(url: String): VipDetailsEntity

    @Query("UPDATE " + VipDetailsEntity.TABLE +
            " SET " + VipDetailsEntity.FIELD_NOTIFICATION + " = :notification" +
            " WHERE " + VipDetailsEntity.FIELD_URL + " = :url")
    fun setNotificationByPk(url: String, notification: Boolean)

    @Query("UPDATE " + VipDetailsEntity.TABLE +
            " SET " + VipDetailsEntity.FIELD_FAV + " = :fav" +
            " WHERE " + VipDetailsEntity.FIELD_URL + " = :url")
    fun setFavByPk(url: String, fav: Boolean)
}