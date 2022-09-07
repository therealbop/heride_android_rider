package com.karru.api;

import android.app.Application;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.heride.rider.BuildConfig;
import com.karru.util.UnsafeOkHttpClient;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * <h1>NetworkModule</h1>
 * This class is used to provide the instance of retrofit API call
 * @author  3Embed on 03-Nov-17.
 */
@Module
public class NetworkModule
{
    private static final String NAME_BASE_URL = "NAME_BASE_URL";
    private static final long CACHE_SIZE = 10 * 1024 * 1024; //10 MB

    @Provides
    @Named(NAME_BASE_URL)
    String provideBaseUrlString() {
        return BuildConfig.BASEURL;
    }

    @Provides
    @Singleton
    Converter.Factory provideGsonConverter() {
        return GsonConverterFactory.create();
    }

    @Provides
    @Singleton
    Cache provideOkhttpCache(Application application) {
        return new Cache(application.getCacheDir(), CACHE_SIZE);
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Cache cache)
    {
        return UnsafeOkHttpClient.getUnsafeOkHttpClient();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Converter.Factory converter, @Named(NAME_BASE_URL) String baseUrl,
                             OkHttpClient okHttpClient)
    {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(converter)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    NetworkService provideNetworkService(Retrofit retrofit) {
        return retrofit.create(NetworkService.class);
    }
}
