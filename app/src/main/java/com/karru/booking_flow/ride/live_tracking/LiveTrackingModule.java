package com.karru.booking_flow.ride.live_tracking;

import com.karru.booking_flow.ride.live_tracking.view.LiveTrackingActivity;
import com.karru.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

/**
 * <h1>LiveTrackingModule</h1>
 * This class is used to provide the classes injection to dagger
 * @author 3Embed
 * @since on 25-01-2018.
 */
@Module
public interface LiveTrackingModule
{
    @Binds
    @ActivityScoped
    LiveTrackingContract.Presenter providePresenter(LiveTrackingPresenter liveTrackingPresenter);

    @Binds
    @ActivityScoped
    LiveTrackingContract.View provideView(LiveTrackingActivity liveTrackingActivity);
}
