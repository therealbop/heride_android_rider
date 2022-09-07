package com.karru.authentication.verification;

import android.app.Activity;

import com.karru.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

/**
 * <h1>VerifyOTPDaggerModule</h1>
 * This class is used to provide instances required in this activity
 * @author  3Embed
 * @since on 02-12-2017.
 */
@Module
public abstract class VerifyOTPDaggerModule
{
    @Binds
    @ActivityScoped
    abstract Activity provideLoginActivity(VerifyOTPActivity verifyOTPActivity);

    @Binds
    @ActivityScoped
    abstract VerifyOTPContract.Presenter provideVerifyOtpPresenter(VerifyOTPPresenter presenter);

    @Binds
    @ActivityScoped
    abstract VerifyOTPContract.View provideView(VerifyOTPActivity verifyOTPActivity);
}
