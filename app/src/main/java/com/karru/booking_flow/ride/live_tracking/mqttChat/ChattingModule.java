package com.karru.booking_flow.ride.live_tracking.mqttChat;

import android.app.Activity;

import com.karru.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

/**
 * Created by DELL on 27-03-2018.
 */

@Module
public abstract class ChattingModule {

    @ActivityScoped
    @Binds
    abstract Activity getActivity(ChattingActivity activity);

    @Binds
    @ActivityScoped
    abstract  ChattingContract.ViewOperations getView(ChattingActivity view);

    @Binds
    @ActivityScoped
    abstract  ChattingContract.PresenterOperations getPresenter(Presenter presenter);
}
