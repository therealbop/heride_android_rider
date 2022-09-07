package com.karru.landing.profile.edit_phone_number;


import com.karru.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class EditPhoneNumberDaggermodule
{
    @ActivityScoped
    @Binds
    abstract EditPhoneNumberActivityContract.EditPhoneNumberView provideEditPhoneNumberView(EditPhoneNumberActivity activity);
    @ActivityScoped
    @Binds
    abstract EditPhoneNumberActivityContract.EditPhoneNuberPresenter provideEditNumberPresenter(EditPhoneNumberPresenter presenter);

}
