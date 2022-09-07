package com.karru.landing.corporate;


import com.karru.dagger.ActivityScoped;
import com.karru.landing.corporate.add_corporate.AddCorporateProfileAccountContract;
import com.karru.landing.corporate.view.CorporateProfileActivity;
import com.karru.landing.corporate.view.CorporateProfileAdapter;
import com.karru.util.AppTypeface;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class CorporateProfileDaggerModule
{
    @Binds
    @ActivityScoped
    abstract CorporateProfileContract.Presenter addProfilePresenter(CorporateProfilePresenter corporateProfilePresenter);

    @Binds
    @ActivityScoped
    abstract CorporateProfileContract.View addProfileView(CorporateProfileActivity corporateProfileActivity);
}
