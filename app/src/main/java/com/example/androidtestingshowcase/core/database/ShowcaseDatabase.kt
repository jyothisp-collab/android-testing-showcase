package com.example.androidtestingshowcase.core.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ShowcaseItemEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class ShowcaseDatabase : RoomDatabase() {
    abstract fun itemDao(): ShowcaseItemDao
}
