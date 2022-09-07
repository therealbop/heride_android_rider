package com.karru.landing;

import com.karru.dagger.ActivityScoped;
import com.karru.dagger.FragmentScoped;
import com.karru.landing.home.HomeFragmentContract;
import com.karru.landing.home.HomeFragmentPresenter;
import com.karru.landing.home.view.HomeFragment;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

@Module (includes = MainActivityUtilModule.class)
public abstract class MainActivityDaggerModule
{
    @ActivityScoped
    @Binds
    abstract MainActivityContract.MainActPresenter provideMainPresenter(MainActivityPresenter presenter);

    @ActivityScoped
    @Binds
    abstract MainActivityContract.MainActView provideView(MainActivity mainActivity);

    @Provides
    @ActivityScoped
    static androidx.fragment.app.FragmentManager provideFragmentManager(MainActivity activity) {
        return activity.getSupportFragmentManager();
    }

    /************************************************************************************************/
    @FragmentScoped
    @ContributesAndroidInjector
    abstract HomeFragment provideHomeActivity();

    @Binds
    @ActivityScoped
    abstract HomeFragmentContract.Presenter provideHomePresenter(HomeFragmentPresenter homeActivityPresenter);


}
