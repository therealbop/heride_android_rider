package com.karru.booking_flow.scheduled_booking;

import android.content.Context;

import com.karru.dagger.ActivityScoped;
import com.karru.booking_flow.ride.live_tracking.view.CancelBookingAdapter;
import com.karru.booking_flow.ride.live_tracking.view.CancelBookingReasonsDialog;
import com.karru.util.AppTypeface;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

import static com.karru.utility.Constants.SCHEDULE;

/**
 * <h1>AdvertiseUtilModule</h1>
 * used to assign the classes to dagger
 *@author 3EMbed
 * @since on 2/13/2018.
 */
@Module
public class ScheduleUtilModule
{
    @Provides
    @ActivityScoped
    CancelBookingAdapter provideCancelBookingAdapter(Context context, AppTypeface appTypeface,
                                                     ScheduledBookingContract.View view)
    {
        return new CancelBookingAdapter(context,appTypeface,view);
    }

    @Provides
    @ActivityScoped
    CancelBookingReasonsDialog provideCancelBookingReasonsDialog(ScheduledBookingActivity scheduledBookingActivity,
                                                                 AppTypeface appTypeface,
                                                                 CancelBookingAdapter cancelBookingAdapter,
                                                                 ScheduledBookingContract.View view)
    {
        return new CancelBookingReasonsDialog(scheduledBookingActivity,appTypeface,cancelBookingAdapter,view);
    }

    @Provides
    @Named(SCHEDULE)
    CompositeDisposable provideCompositeDisposable()
    {
        return new CompositeDisposable();
    }
}
