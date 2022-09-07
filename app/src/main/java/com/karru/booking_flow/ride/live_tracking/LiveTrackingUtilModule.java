package com.karru.booking_flow.ride.live_tracking;

import android.content.Context;

import com.karru.booking_flow.ride.live_tracking.view.CancelBookingAdapter;
import com.karru.booking_flow.ride.live_tracking.view.CancelBookingReasonsDialog;
import com.karru.booking_flow.ride.live_tracking.view.LiveTrackingActivity;
import com.karru.dagger.ActivityScoped;
import com.karru.util.AppTypeface;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

import static com.karru.utility.Constants.LIVE;

/**
 * <h1>LiveTrackingUtilModule</h1>
 * used to assign the classes to dagger
 *@author 3EMbed
 * @since on 2/13/2018.
 */
@Module
public class LiveTrackingUtilModule
{
    @Provides
    @ActivityScoped
    CancelBookingAdapter provideCancelBookingAdapter(Context context, AppTypeface appTypeface,
                                                     LiveTrackingContract.View view)
    {
        return new CancelBookingAdapter(context,appTypeface,view);
    }

    @Provides
    @ActivityScoped
    CancelBookingReasonsDialog provideCancelBookingReasonsDialog(LiveTrackingActivity liveTrackingActivity,
                                                                 AppTypeface appTypeface,
                                                                 CancelBookingAdapter cancelBookingAdapter,
                                                                 LiveTrackingContract.View view)
    {
        return new CancelBookingReasonsDialog(liveTrackingActivity,appTypeface,cancelBookingAdapter,view);
    }

    @Provides
    @Named(LIVE)
    CompositeDisposable provideCompositeDisposable()
    {
        return new CompositeDisposable();
    }
}
