package com.karru.landing.support;

import com.karru.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

/**
 * <h1>SupportDaggerModule</h1>
 * used to provide models to dagger
 * @author 3Embed
 * @since on 21-03-2018.
 */
@Module
public abstract class SupportDaggerModule
{
    @Binds
    @ActivityScoped
    abstract SupportActivityContract.Presenter providePresenter(SupportActivityPresenter supportActivityPresenter);

    @Binds
    @ActivityScoped
    abstract SupportActivityContract.View provideView(SupportActivity supportActivity);
}
