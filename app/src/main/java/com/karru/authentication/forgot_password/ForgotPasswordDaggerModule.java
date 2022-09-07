package com.karru.authentication.forgot_password;


import android.app.Activity;

import com.karru.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

/**
 * <h1>ForgotPasswordDaggerModule</h1>
 * This class is used to provide the utils needed for login
 * @author 3Embed
 * @since on 11-12-2017.
 */
@Module
public abstract class ForgotPasswordDaggerModule
{
    @Binds
    @ActivityScoped
    abstract Activity provideMainActivity(ForgotPasswordActivity activity);

    @Binds
    @ActivityScoped
    abstract ForgotPasswordContract.ForgotPasswordView provideForgotPasswordView(ForgotPasswordActivity forgotPasswordActivity);

    @ActivityScoped
    @Binds
    abstract ForgotPasswordContract.ForgotPasswdPresenter provideForgotPasswordActivity
            (ForgotPasswordPresenter presenter);
}
