package com.karru.landing.rate;


import com.karru.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class RateCardActivityDaggerModule
{
    @Binds
    @ActivityScoped
    abstract RateCardActivityContract.RateCardView provideRatecardView(RateCardActivity activity);

    @Binds
    @ActivityScoped
    abstract RateCardActivityContract.RateCardPresenter provideRateCardPresenter(RateCardActivityPresenter presenter);

}
