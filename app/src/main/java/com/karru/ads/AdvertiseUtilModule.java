package com.karru.ads;

import javax.inject.Named;
import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

import static com.karru.utility.Constants.ADS;

/**
 * <h1>AdvertiseUtilModule</h1>
 * used to assign the classes to dagger
 *@author 3EMbed
 * @since on 2/13/2018.
 */
@Module
public class AdvertiseUtilModule
{
    @Provides
    @Named(ADS)
    CompositeDisposable provideCompositeDisposable()
    {
        return new CompositeDisposable();
    }
}
