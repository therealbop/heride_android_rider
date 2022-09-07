package com.karru.authentication.change_password;


import com.karru.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ChangePasswordActivityDaggerModule
{
    @ActivityScoped
    @Binds
    abstract ChangePasswordActivityContract.View provideChangePasswordView(ChangePasswordActivity activity);

    @ActivityScoped
    @Binds
    abstract ChangePasswordActivityContract.Presenter providePresenter(ChangePasswordPresenter changePasswordPresenter);
}
