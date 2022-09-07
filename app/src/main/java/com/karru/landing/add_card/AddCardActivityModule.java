package com.karru.landing.add_card;


import com.karru.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class AddCardActivityModule
{

    @ActivityScoped
    @Binds
    abstract AddCardActivityContract.AddCardView provideAddCardview(AddCardActivity activity);

    @ActivityScoped
    @Binds
    abstract AddCardActivityContract.AddCardPresenter provideAddCardPresenter(AddCardActivityPresenter presenter);
}
