package com.karru.landing.profile.edit_email;


import com.karru.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class EditEmailActivityDaggermodule
{

    @ActivityScoped
    @Binds
    abstract EditEmailActivityContract.EditEmailView bindEditEmailActivity(EditEmailActivity activity);
    @ActivityScoped
    @Binds
    abstract EditEmailActivityContract.EditEmailPresenter bindEditEmailPresenter(EditEmailActivityPresenter presenter);

}
