package com.jenzz.demo.di

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jenzz.demo.Database
import com.jenzz.demo.posts.PostsQueries
import com.jenzz.demo.users.UsersQueries
import com.squareup.sqldelight.android.AndroidSqliteDriver
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
    fun provideDatabase(@ApplicationContext context: Context): Database =
        Database(
            AndroidSqliteDriver(
                schema = Database.Schema,
                context = context,
                name = "demo.db",
                callback = object : AndroidSqliteDriver.Callback(Database.Schema) {
                    override fun onConfigure(db: SupportSQLiteDatabase) {
                        db.setForeignKeyConstraintsEnabled(true)
                    }
                }
            )
        )

    @Provides
    fun provideUsersQueries(database: Database): UsersQueries =
        database.usersQueries

    @Provides
    fun providePostsQueries(database: Database): PostsQueries =
        database.postsQueries
}
