package com.karru.booking_flow.location_from_map;

import com.karru.dagger.ActivityScoped;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

import static com.karru.utility.Constants.LOCATION_FROM_MAP;

/**
 * <h1>AddCardDaggerUtilModule</h1>
 * used to provide models to dagger
 * @author 3Embed
 * @since on 21-03-2018.
 */
@Module
public class LocationMapDaggerUtilModule
{
    @Provides
    @ActivityScoped
    @Named(LOCATION_FROM_MAP)
    CompositeDisposable provideCompositeDisposable()
    {
        return new CompositeDisposable();
    }
}
