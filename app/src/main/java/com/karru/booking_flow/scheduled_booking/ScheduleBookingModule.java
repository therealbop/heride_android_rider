package com.karru.booking_flow.scheduled_booking;

import com.karru.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

/**
 * <h1>AdvertiseModule</h1>
 * used to provide models to dagger
 * @author 3Embed
 * @since on 05-03-2018.
 */
@Module
public abstract class ScheduleBookingModule
{
    @Binds
    @ActivityScoped
    abstract ScheduledBookingContract.View provideScheduleBooking(ScheduledBookingActivity scheduledBookingActivity);

    @Binds
    @ActivityScoped
    abstract ScheduledBookingContract.Presenter providePresenter(ScheduledBookingPresenter scheduledBookingPresenter);

}
