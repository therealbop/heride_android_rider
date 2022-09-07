package com.karru.rental;


import com.karru.dagger.ActivityScoped;
import com.karru.rental.view.RentalActivity;

import dagger.Binds;
import dagger.Module;

/**
 * <h1>AdvertiseModule</h1>
 * used to provide models to dagger
 */
@Module
public abstract class RentCarModule {

    @Binds
    @ActivityScoped
    abstract RentCarContract.View provideRentCar(RentalActivity rentCarActivity);

    @Binds
    @ActivityScoped
    abstract RentCarContract.Presenter provideRentCarPresenter(RentCarPresenter rentCarPresenter);
}
