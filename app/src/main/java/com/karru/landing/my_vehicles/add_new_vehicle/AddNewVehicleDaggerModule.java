package com.karru.landing.my_vehicles.add_new_vehicle;
/**
 * <h1>PromoCodeDaggerModule</h1>
 * used to provide models to dagger
 * @author 3Embed
 * @since on 15-06-2018.
 */

import com.karru.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module(includes = AddNewVehicleUtilModule.class)
public  interface AddNewVehicleDaggerModule {


    @Binds
    @ActivityScoped
    AddNewVehicleContract.Presenter provideAddNewVehiclePresenter(AddNewVehiclePresenter addNewVehiclePresenter);

    @Binds
    @ActivityScoped
    AddNewVehicleContract.View provideAddNewVehicleView(AddNewVehicleActivity addNewVehicleActivity);

}
