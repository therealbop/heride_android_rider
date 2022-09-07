package com.karru.landing.emergency_contact.dagger_module;


import com.karru.dagger.ActivityScoped;
import com.karru.landing.emergency_contact.EmergencyContactActivity;
import com.karru.landing.emergency_contact.EmergencyContactContract;
import com.karru.landing.emergency_contact.EmergencyContactPresenter;

import dagger.Binds;
import dagger.Module;

@Module(includes = EmergencyContactUtilDaggerModule.class)
public abstract class EmergencyContactDaggerModule
{
    @ActivityScoped
    @Binds
    abstract EmergencyContactContract.EmergencyContactView provideEmergencyview(EmergencyContactActivity activity);

    @ActivityScoped
    @Binds
    abstract EmergencyContactContract.EmergencyContactPresenter provideEmergencyContactPresenter(EmergencyContactPresenter presenter);

}
