package com.karru.booking_flow.ride.request;

import com.karru.booking_flow.ride.request.model.RequestingModel;
import com.karru.dagger.ActivityScoped;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

import static com.karru.utility.Constants.REQUEST;

/**
 * <h1>RequestingUtilModule</h1>
 * Used to provide the non static classes
 * @author 3Embed
 * @since on 24-01-2018.
 */
@Module
public class RequestingUtilModule
{
    @Provides
    @ActivityScoped
    RequestingModel provideRequestingModel(){return new RequestingModel();}

    @Provides
    @Named(REQUEST)
    @ActivityScoped
    CompositeDisposable provideCompositeDisposable()
    {
        return new CompositeDisposable();
    }
}
