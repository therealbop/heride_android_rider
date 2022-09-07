package com.karru.network;

import com.karru.dagger.ActivityScoped;
import javax.inject.Named;
import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

import static com.karru.utility.Constants.NETWORK;

/**
 * <h1>AddCardDaggerUtilModule</h1>
 * used to provide models to dagger
 * @author 3Embed
 * @since on 21-03-2018.
 */
@Module
public class NetworkReachableUtilModule
{
    @Provides
    @ActivityScoped
    @Named(NETWORK)
    CompositeDisposable provideCompositeDisposable()
    {
        return new CompositeDisposable();
    }
}