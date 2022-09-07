package com.karru.landing.profile.edit_name;

import com.karru.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

@Module
public abstract class EditNameActivityDaggerModule
{
    @ActivityScoped
    @Binds
    abstract EditNameActivityContract.EditNamePresenter provideEditNameActivityPresenter(EditNameActivityPresenter presenter);

    @Binds
    @ActivityScoped
    abstract EditNameActivityContract.EditNameView provideEditNameView(EditNameActivity editNameActivity);
}
