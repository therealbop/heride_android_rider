package com.karru.splash.first;

import com.karru.dagger.ActivityScoped;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

import static com.karru.utility.Constants.SPLASH;


/**
 * <h1>SplashUtilModule</h1>
 * This class is used to provide the instance required by Splash Activity
 * @author  3Embed
 * @since on 09-12-2017.
 */
@Module
public class SplashUtilModule
{
    @Provides
    @Named(SPLASH)
    @ActivityScoped
    CompositeDisposable compositeDisposable ()
    {
        return new CompositeDisposable();
    }
}
