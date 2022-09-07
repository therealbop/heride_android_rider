package com.karru.landing.history;

import com.karru.dagger.ActivityScoped;
import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.landing.history.view.BookingHistoryActivity;
import com.karru.landing.history.view.BookingHistoryPagerAdapter;
import com.karru.network.NetworkReachableActivity;
import com.karru.util.AppTypeface;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;
import static com.karru.utility.Constants.HISTORY;

/**
 * <h1>HistoryDaggerUtilModule</h1>
 * used to provide models to dagger
 * @author 3Embed
 * @since on 21-03-2018.
 */
@Module
public class HistoryDaggerUtilModule
{
    @Provides
    @Named(HISTORY)
    @ActivityScoped
    CompositeDisposable provideCompositeDisposable()
    {
        return new CompositeDisposable();
    }

    @Provides
    @ActivityScoped
    BookingHistoryPagerAdapter bookingHistoryPagerAdapter(BookingHistoryActivity bookingHistoryActivity, PreferenceHelperDataSource preferenceHelperDataSource)
    {
        return new BookingHistoryPagerAdapter(bookingHistoryActivity.getSupportFragmentManager(),bookingHistoryActivity,preferenceHelperDataSource);
    }
}