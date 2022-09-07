package com.karru.booking_flow.location_from_map;


import com.karru.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class LocationFromMapDaggerModule
{
    @Binds
    @ActivityScoped
    abstract LocationFromMapContract.LocationFromMapView provideLocationView(LocationFromMapActivity mapView);

    @Binds
    @ActivityScoped
    abstract LocationFromMapContract.LocationFromMapPresenter providPresenter(LocationFromMapPresenter presenter);

}
