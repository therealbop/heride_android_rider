package com.karru.booking_flow.address;

import com.karru.booking_flow.address.view.AddressSelectionActivity;
import com.karru.dagger.ActivityScoped;
import com.karru.network.NetworkReachableActivity;
import com.karru.util.AppTypeface;

import javax.inject.Named;
import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

import static com.karru.utility.Constants.ADDRESS_SELECTION;

/**
 * <h1>AddressSelectionUtilModule</h1>
 * This class is used to provide the module for main activity
 * @author 3Embed
 * @since on 06-01-2018.
 */
@Module
public class AddressSelectionUtilModule
{
    @Provides
    @ActivityScoped
    @Named(ADDRESS_SELECTION)
    CompositeDisposable provideCompositeDisposable()
    {
        return new CompositeDisposable();
    }
}
