package com.karru.landing.profile;

import com.karru.dagger.ActivityScoped;
import javax.inject.Named;
import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

import static com.karru.utility.Constants.PROFILE;

/**
 * <h1>AddCardDaggerUtilModule</h1>
 * used to provide models to dagger
 * @author 3Embed
 * @since on 21-03-2018.
 */
@Module
public class ProfileDaggerUtilModule
{
    @Provides
    @ActivityScoped
    @Named(PROFILE)
    CompositeDisposable provideCompositeDisposable()
    {
        return new CompositeDisposable();
    }
}
