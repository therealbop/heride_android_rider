package com.karru.booking_flow.address;

import com.karru.dagger.ActivityScoped;
import com.karru.booking_flow.address.view.AddressSelectionActivity;

import dagger.Binds;
import dagger.Module;

/**
 * <h1>AddressSelectModule</h1>
 * This class is used to provide classed needed for the screen to dagger
 *@author 3Embed
 * @since on 20-01-2018.
 */
@Module
public interface AddressSelectModule
{
    @Binds
    @ActivityScoped
    AddressSelectContract.Presenter providePresenter(AddressSelectPresenter addressSelectPresenter);

    @Binds
    @ActivityScoped
    AddressSelectContract.View provideView(AddressSelectionActivity selectionActivity);
}
