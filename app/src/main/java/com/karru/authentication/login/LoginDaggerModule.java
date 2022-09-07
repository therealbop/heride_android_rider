package com.karru.authentication.login;

import android.app.Activity;
import com.karru.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

/**
 * <h1>LoginDaggerModule</h1>
 * This class is used to provide the LoginPresenter object
 * @author 3Embed .
 * @since 21-11-2017
 */
@Module
public abstract class LoginDaggerModule
{
    @Binds
    @ActivityScoped
    abstract Activity provideLoginActivity(LoginActivity loginActivity);

    @Binds
    @ActivityScoped
    abstract LoginContract.Presenter provideLoginPresenter(LoginPresenter loginPresenter);

    @Binds
    @ActivityScoped
    abstract LoginContract.View provideLoginView(LoginActivity loginActivity);
}
