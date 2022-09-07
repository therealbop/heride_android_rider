package com.karru.landing.corporate.add_corporate;

import com.karru.dagger.ActivityScoped;
import com.karru.network.NetworkReachableActivity;
import com.karru.util.AppTypeface;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

import static com.karru.utility.Constants.CORPORATE_ADD_PROFILE;

/**
 * <h1>AddCorporateProfileUtilModule</h1>
 * used to provide models to dagger
 * @author 3Embed
 * @since on 21-03-2018.
 */
@Module
public class AddCorporateProfileUtilModule
{
    @Provides
    @Named(CORPORATE_ADD_PROFILE)
    @ActivityScoped
    CompositeDisposable compositeDisposable()
    {
        return new CompositeDisposable();
    }
}
