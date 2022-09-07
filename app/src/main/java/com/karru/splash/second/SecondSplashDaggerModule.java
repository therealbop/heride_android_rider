package com.karru.splash.second;

import android.content.Context;

import com.karru.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

/**
 * <h1>SecondSplashDaggerModule</h1>
 * This class is used to provide the data to activity
 * @author 3Embed
 * @since on 22-12-2017.
 */
@Module
public abstract class SecondSplashDaggerModule
{
    @Binds
    @ActivityScoped
    abstract SecondSplashContract.Presenter providePresenter(SecondSplashPresenter secondSplashPresenter);

    @Binds
    @ActivityScoped
    abstract SecondSplashContract.View provideView(SecondSplashActivity secondSplashActivity);
}
