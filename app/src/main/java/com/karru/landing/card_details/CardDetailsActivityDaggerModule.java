package com.karru.landing.card_details;


import com.karru.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class CardDetailsActivityDaggerModule
{
    @ActivityScoped
    @Binds
    abstract CardDetailsActivityContract.View provideDeleteCardView(CardDetailsActivity activity);

    @ActivityScoped
    @Binds
    abstract CardDetailsActivityContract.DeleteCardPresenter provideDeleteCardPresenter(CardDetailsActivityPresenter presenter);

}
