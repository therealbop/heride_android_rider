package com.karru.splash.first;

import android.app.Activity;
import com.karru.dagger.ActivityScoped;
import com.karru.dagger.FragmentScoped;
import com.karru.splash.fragments.LandingFirstFragment;
import com.karru.splash.fragments.LandingFiveFragment;
import com.karru.splash.fragments.LandingFourthFragment;
import com.karru.splash.fragments.LandingSecondFragment;
import com.karru.splash.fragments.LandingSixFragment;
import com.karru.splash.fragments.LandingThirdFragment;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * <h1>SplashDaggerModule</h1>
 * This class is used to provide the instance required by Splash Activity
 * @author  3Embed
 * @since on 09-12-2017.
 */
@Module
public abstract class SplashDaggerModule
{
    @ActivityScoped
    @Binds
    abstract Activity provideSplashActivity(SplashActivity splashActivity);

    @ActivityScoped
    @Binds
    abstract SplashContract.Presenter provideSplashPresenter(SplashPresenter presenter);

    @Binds
    @ActivityScoped
    abstract SplashContract.View provideView(SplashActivity splashActivity);

    @FragmentScoped
    @ContributesAndroidInjector
    abstract LandingFirstFragment provideLandingFirstFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract LandingSecondFragment provideLandingSecondFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract LandingThirdFragment proLandingThirdFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract LandingFourthFragment proLandingFourthFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract LandingFiveFragment landingFiveFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract LandingSixFragment landingSixFragment();
}
