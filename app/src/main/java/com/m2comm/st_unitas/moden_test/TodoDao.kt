package com.m2comm.st_unitas.moden_test

import androidx.room.*

@Dao
interface TodoDao {

    @Query("Select * From Todo")
    fun getAll() : List<Todo>

    @Insert
    fun insert( todo : Todo )

    @Update
    fun update( todo : Todo )

    @Delete
    fun delete( todo : Todo )
}
