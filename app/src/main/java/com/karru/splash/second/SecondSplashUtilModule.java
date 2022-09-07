package com.karru.splash.second;

import com.karru.dagger.ActivityScoped;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

import static com.karru.utility.Constants.SECOND_SPLASH;

/**
 * <h1>SecondSplashUtilModule</h1>
 * This class is used to provide the data to activity
 * @author 3Embed
 * @since on 22-12-2017.
 */
@Module
public class SecondSplashUtilModule
{
    @Provides
    @Named(SECOND_SPLASH)
    @ActivityScoped
    CompositeDisposable compositeDisposable ()
    {
        return new CompositeDisposable();
    }
}
