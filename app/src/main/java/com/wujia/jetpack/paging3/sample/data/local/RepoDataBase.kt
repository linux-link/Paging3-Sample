package com.wujia.jetpack.paging3.sample.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.wujia.jetpack.paging3.sample.model.Repo

@Database(
    entities = [Repo::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class RepoDataBase : RoomDatabase() {

    abstract fun reposDao(): RepoDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var INSTANCE: RepoDataBase? = null

        fun getInstance(context: Context): RepoDataBase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildDataBase(context).also {
                INSTANCE = it
            }
        }

        private fun buildDataBase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            RepoDataBase::class.java, "GitHub.db"
        ).build()
    }
}