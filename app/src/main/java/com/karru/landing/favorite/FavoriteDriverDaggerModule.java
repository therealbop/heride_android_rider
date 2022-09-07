package com.karru.landing.favorite;

import com.karru.dagger.ActivityScoped;
import com.karru.landing.favorite.view.FavoriteDriversActivity;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class FavoriteDriverDaggerModule
{
    @Binds
    @ActivityScoped
    abstract FavoriteDriversContract.Presenter presenter(FavoriteDriverPresenter favoriteDriverPresenter);

    @Binds
    @ActivityScoped
    abstract FavoriteDriversContract.View view(FavoriteDriversActivity favoriteDriversActivity);
}
