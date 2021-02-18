package com.m2comm.st_unitas.moden_test

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Todo (
    @ColumnInfo(name = "title")var title : String?) {

    @PrimaryKey(autoGenerate = true) var id : Int = 0
}