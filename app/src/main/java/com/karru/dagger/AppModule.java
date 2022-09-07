package com.karru.dagger;

import android.app.Application;
import android.content.Context;

import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.data.source.local.shared_preference.PreferencesHelper;
import com.karru.data.source.local.sqlite.SQLiteDataSource;
import com.karru.data.source.local.sqlite.SQLiteLocalDataSource;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

/**
 * <h1>AppModule</h1>
 * Used to inject the dependency
 * @author 3Embed
 * @since 03-Nov-17
 */

@Module
interface AppModule
{
    //expose Application as an injectable context
    @Binds
    @Singleton
    Context bindContext(Application application);

    @Binds
    @Singleton
    PreferenceHelperDataSource preferenceHelperDataSource(PreferencesHelper preferencesHelper);

    @Binds
    @Singleton
    SQLiteDataSource provideAddressLocalDataSource(SQLiteLocalDataSource addressLocalDataSource);

}