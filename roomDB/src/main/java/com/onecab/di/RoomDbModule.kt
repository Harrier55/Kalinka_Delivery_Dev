package com.onecab.di

import android.content.Context
import androidx.room.Room
import com.onecab.roomdb.dao.OrderDAO
import com.onecab.roomdb.dao.QrCodeDAO
import com.onecab.roomdb.db.MainDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomDbModule {

    private const val DB_NAME = "kalinka.db"

  @Provides
  @Singleton
   internal fun provideMainDb(@ApplicationContext context: Context): MainDataBase {
        return Room.databaseBuilder(
            context = context,
            klass =  MainDataBase::class.java,
            name = DB_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideOrderDao(db:MainDataBase): OrderDAO = db.orderDao()

    @Singleton
    @Provides
    fun provideUserDao(db: MainDataBase): QrCodeDAO = db.qrCodeDao()
}

