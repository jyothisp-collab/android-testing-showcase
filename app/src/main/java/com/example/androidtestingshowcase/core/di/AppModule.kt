package com.example.androidtestingshowcase.core.di

import android.content.Context
import androidx.room.Room
import com.example.androidtestingshowcase.BuildConfig
import com.example.androidtestingshowcase.core.database.ShowcaseDatabase
import com.example.androidtestingshowcase.core.database.ShowcaseItemDao
import com.example.androidtestingshowcase.core.network.DemoBackendInterceptor
import com.example.androidtestingshowcase.core.network.ShowcaseApi
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ShowcaseDatabase {
        return Room.databaseBuilder(
            context,
            ShowcaseDatabase::class.java,
            "showcase.db",
        ).build()
    }

    @Provides
    fun provideItemDao(database: ShowcaseDatabase): ShowcaseItemDao = database.itemDao()

    @Provides
    @Singleton
    fun provideOkHttpClient(
        demoBackendInterceptor: DemoBackendInterceptor,
    ): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BASIC
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(demoBackendInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideShowcaseApi(retrofit: Retrofit): ShowcaseApi {
        return retrofit.create(ShowcaseApi::class.java)
    }
}
