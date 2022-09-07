package com.karru.landing.profile.edit_email;

import com.karru.dagger.ActivityScoped;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

import static com.karru.utility.Constants.EDIT_EMAIL;

/**
 * <h1>EditPhoneUtilModule</h1>
 * This class is used to provide the utils needed for login
 * @author 3Embed
 * @since on 11-12-2017.
 */
@Module
public class EditEmailUtilModule
{
    @Provides
    @Named(EDIT_EMAIL)
    @ActivityScoped
    CompositeDisposable compositeDisposable(){return new CompositeDisposable();}
}
