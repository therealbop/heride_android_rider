package com.karru.landing.corporate.add_corporate;

import com.karru.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class AddCorporateProfileAccountDaggerModule
{
    @ActivityScoped
    @Binds
    abstract AddCorporateProfileAccountContract.CorporateProfileAddMailPresenter bindPresenter(AddCorporateProfileAccountPresenter presenter);

    @ActivityScoped
    @Binds
    abstract AddCorporateProfileAccountContract.CorporateProfileAddMailView bindView(AddCorporateProfileAccountActivity activity);


}
