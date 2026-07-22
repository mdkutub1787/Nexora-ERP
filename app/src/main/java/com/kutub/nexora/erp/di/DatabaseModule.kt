package com.kutub.nexora.erp.di

import android.content.Context
import androidx.room.Room
import com.kutub.nexora.erp.data.database.ProductDao
import com.kutub.nexora.erp.data.database.SmartErpDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideSmartErpDatabase(
        @ApplicationContext context: Context
    ): SmartErpDatabase {
        return Room.databaseBuilder(
            context,
            SmartErpDatabase::class.java,
            "smarterp_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideProductDao(database: SmartErpDatabase): ProductDao {
        return database.productDao()
    }
}
