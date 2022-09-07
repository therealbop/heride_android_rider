package com.karru.landing.history;

import com.karru.dagger.ActivityScoped;
import com.karru.landing.history.view.BookingHistoryActivity;

import dagger.Binds;
import dagger.Module;

/**
 * <h1>BookingHistoryDaggerModule</h1>
 * used to provide models to dagger
 * @author 3Embed
 * @since on 21-03-2018.
 */
@Module
public abstract class BookingHistoryDaggerModule
{
    @Binds
    @ActivityScoped
    abstract BookingHistoryContract.Presenter provideHistoryPresenter(BookingHistoryPresenter presenter);

    @Binds
    @ActivityScoped
    abstract BookingHistoryContract.View provideView(BookingHistoryActivity bookingHistoryActivity);
}
