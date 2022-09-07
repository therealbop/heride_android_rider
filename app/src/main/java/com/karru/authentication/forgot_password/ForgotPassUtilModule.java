package com.karru.authentication.forgot_password;

import com.karru.dagger.ActivityScoped;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

import static com.karru.utility.Constants.FORGOT_PASS;

/**
 * <h1>ForgotPassUtilModule</h1>
 * This class is used to provide the utils needed for login
 * @author 3Embed
 * @since on 11-12-2017.
 */
@Module
public class ForgotPassUtilModule
{
    @Provides
    @Named(FORGOT_PASS)
    @ActivityScoped
    CompositeDisposable compositeDisposable(){return new CompositeDisposable();}

}
