package com.karru.booking_flow.ride.live_tracking.mqttChat;

import com.karru.dagger.ActivityScoped;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

import static com.karru.utility.Constants.CHAT;

@Module
public class ChatUtilModule
{
    @Provides
    @Named(CHAT)
    @ActivityScoped
    CompositeDisposable compositeDisposable()
    {
        return new CompositeDisposable();
    }
}
