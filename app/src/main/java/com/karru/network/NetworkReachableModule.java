package com.karru.network;

import com.karru.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

@Module
public abstract class NetworkReachableModule
{
    @ActivityScoped
    @Binds
    abstract NetworkReachableContract.View networkView(NetworkReachableActivity activity);

    @ActivityScoped
    @Binds
    abstract NetworkReachableContract.Presenter networkPresenter(NetworkReachablePresenter presenter);

}
