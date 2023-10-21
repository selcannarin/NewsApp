package com.example.newsapp.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.newsapp.data.datasource.NewsDataSource
import com.example.newsapp.data.datasource.NewsDataSourceImpl
import com.example.newsapp.data.local.NewsDatabase
import com.example.newsapp.data.remote.NewsApi
import com.example.newsapp.data.repository.NewsRepository
import com.example.newsapp.data.repository.NewsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    fun providesNewsDataSource(newsApi: NewsApi, db: NewsDatabase): NewsDataSource {
        return NewsDataSourceImpl(newsApi, db)
    }

    @Provides
    fun providesNewsRepository(newsDataSource: NewsDataSource): NewsRepository {
        return NewsRepositoryImpl(newsDataSource)
    }

    @Provides
    fun providesNewsDatabase(context: Context): NewsDatabase {
        return Room.databaseBuilder(
            context,
            NewsDatabase::class.java,
            "news_db.db"
        ).build()
    }

    @Provides
    fun provideApplicationContext(application: Application): Context {
        return application.applicationContext
    }

}
