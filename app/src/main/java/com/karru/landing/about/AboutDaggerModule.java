package com.karru.landing.about;

import com.karru.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

/**
 * <h1>AboutDaggerModule</h1>
 * used to provide models to dagger
 * @author 3Embed
 * @since on 21-03-2018.
 */
@Module
public abstract class AboutDaggerModule
{
    @Binds
    @ActivityScoped
    abstract AboutActivityContract.View provideView(AboutActivity aboutActivity);

    @Binds
    @ActivityScoped
    abstract AboutActivityContract.AboutPresenter provideAboutFragPresenter(AboutActivityPresenter presenter);
}
