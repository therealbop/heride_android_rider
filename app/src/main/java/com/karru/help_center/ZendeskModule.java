package com.karru.help_center;

import com.karru.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

/**
 * <h>ZendeskModule</h>
 * Created by Ali on 2/26/2018.
 */
@Module
public interface ZendeskModule
{
    @ActivityScoped
    @Binds
    ZendeskHelpContract.Presenter providePresenter(ZendeskHelpPresenter zendeskHelpIndex);

    @ActivityScoped
    @Binds
    ZendeskHelpContract.ZendeskView provideView(ZendeskHelpActivity zendeskHelpIndex);
}
