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

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(cast: ItemDTO)

    @Query("SELECT * FROM " + ItemDTO.TABLE +
            " WHERE " + ItemDTO.FIELD_VIP_URL + " = :vipUrl AND " + ItemDTO.FIELD_PAGE_NUM + " = :pageNum")
    fun getForVipAndPage(vipUrl: String, pageNum: Int): List<ItemDTO>

    @Query("DELETE FROM " + ItemDTO.TABLE +
            " WHERE " + ItemDTO.FIELD_VIP_URL + " = :castsUrl AND " + ItemDTO.FIELD_PAGE_NUM + " = :pageNum")
    fun clearForVipAndPage(castsUrl: String, pageNum: Int)

}