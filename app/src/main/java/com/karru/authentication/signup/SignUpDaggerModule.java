package com.karru.authentication.signup;

import android.app.Activity;

import com.karru.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

/**
 * <h1>SignUpDaggerModule</h1>
 * This class is used to provide the activity level scope
 * @author 3Embed
 * @since on 25-11-2017.
 */
@Module
public abstract class SignUpDaggerModule
{
    @Binds
    @ActivityScoped
    abstract Activity provideActivity(SignUpActivity signUpActivity);

    @Binds
    @ActivityScoped
    abstract SignUpContract.Presenter provideSignUpPresenter(SignUpPresenter signUpPresenter);

    @Binds
    @ActivityScoped
    abstract SignUpContract.View provideView(SignUpActivity signUpPresenter);
}
