package com.karru.booking_flow.ride.request;

import com.karru.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

/**
 * <h1>RequestingModule</h1>
 * This class is used to provide the classes to dagger
 * @author 3Embed
 * @since on 24-01-2018.
 */
@Module
public abstract class RequestingModule
{
    @Binds
    @ActivityScoped
    abstract RequestingContract.Presenter providePresenter(RequestingPresenter requestingPresenter);

    @Binds
    @ActivityScoped
    abstract RequestingContract.View provideView(RequestingActivity requestingActivity);
}
