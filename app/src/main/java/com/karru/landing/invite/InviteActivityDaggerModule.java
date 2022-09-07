package com.karru.landing.invite;

import com.karru.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

/**
 * <h1>CorporateProfileDaggerModule</h1>
 * used to provide models to dagger
 * @author 3Embed
 * @since on 21-03-2018.
 */
@Module
public abstract class InviteActivityDaggerModule
{
    @Binds
    @ActivityScoped
    abstract InviteActivityContract.Presenter provideInviteFragPresenter(InviteActivityPresenter presenter);

    @Binds
    @ActivityScoped
    abstract InviteActivityContract.View provideView(InviteActivity inviteActivity);
}
