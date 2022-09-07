package com.karru.landing.profile;

import com.karru.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

/**
 * <h1>ProfileDaggerModule</h1>
 * used to provide models to dagger
 * @author 3Embed
 * @since on 21-03-2018.
 */
@Module
public abstract class ProfileDaggerModule
{
    @Binds
    @ActivityScoped
    abstract ProfileContract.Presenter provideProfileFragPresenter(ProfileFragmentPresenter presenter);

    @Binds
    @ActivityScoped
    abstract ProfileContract.ProfileView provideProfileView(ProfileActivity profileActivity);
}
