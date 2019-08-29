package id.medigo.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import id.medigo.local.conventer.Converters
import id.medigo.local.dao.ProfileDao
import id.medigo.model.Profile

@Database(entities = [Profile::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun profileDao(): ProfileDao

    companion object {

        fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "AppDatabase.db")
                .build()
    }
}