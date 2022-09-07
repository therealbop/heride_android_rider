package com.karru.landing.history.history_details;

import com.karru.dagger.ActivityScoped;
import com.karru.landing.history.history_details.view.HistoryDetailsActivity;

import dagger.Binds;
import dagger.Module;

/**
 * <h1>HistoryDetailsModule</h1>
 * used to provide models to dagger
 * @author 3EMbed
 * @since on 2/23/2018.
 */
@Module
public abstract class HistoryDetailsModule
{
    @Binds
    @ActivityScoped
    abstract HistoryDetailsContract.Presenter providePresenter(HistoryDetailsPresenter historyDetailsPresenter);

    @Binds
    @ActivityScoped
    abstract HistoryDetailsContract.View provideView(HistoryDetailsActivity historyDetailsActivity);
}
