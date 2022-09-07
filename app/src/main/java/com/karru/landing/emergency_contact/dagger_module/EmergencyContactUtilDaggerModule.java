package com.karru.landing.emergency_contact.dagger_module;

import com.karru.dagger.ActivityScoped;
import com.karru.landing.emergency_contact.EmergencyContactActivity;
import com.karru.landing.emergency_contact.EmergencyContactAdapter;
import com.karru.network.NetworkReachableActivity;
import com.karru.util.AppTypeface;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

import static com.karru.utility.Constants.EMERGENCY;

@Module
public  class EmergencyContactUtilDaggerModule
{
    @Provides
    @ActivityScoped
    EmergencyContactAdapter provideEmergencyContactAdapter(AppTypeface appTypeface)
    {
        return new EmergencyContactAdapter(appTypeface);
    }

    @Provides
    @Named(EMERGENCY)
    CompositeDisposable provideCompositeDisposable()
    {
        return new CompositeDisposable();
    }
}
