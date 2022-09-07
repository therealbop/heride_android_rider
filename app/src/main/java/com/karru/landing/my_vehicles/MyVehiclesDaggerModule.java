package com.karru.landing.my_vehicles;

import com.karru.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

/**
 * <h1>CorporateProfileDaggerModule</h1>
 * used to provide models to dagger
 * @author 3Embed
 * @since on 21-03-2018.
 */
@Module
public abstract class MyVehiclesDaggerModule
{
    @Binds
    @ActivityScoped
    abstract MyVehiclesContract.Presenter providePresenter(MyVehiclesPresenter presenter);

    @Binds
    @ActivityScoped
    abstract MyVehiclesContract.View provideView(MyVehiclesActivity inviteActivity);
}
