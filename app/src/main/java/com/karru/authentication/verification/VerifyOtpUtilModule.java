package com.karru.authentication.verification;


import com.karru.dagger.ActivityScoped;
import com.karru.network.NetworkReachableActivity;
import com.karru.util.AppTypeface;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

import static com.karru.utility.Constants.VERIFY;

@Module
public class VerifyOtpUtilModule
{
    @ActivityScoped
    @Provides
    VerifyOTPModel provideVerifyOtpModel()
    {
        return new VerifyOTPModel();
    }

    @ActivityScoped
    @Named(VERIFY)
    @Provides
    CompositeDisposable compositeDisposable()
    {
        return new CompositeDisposable();
    }
}
