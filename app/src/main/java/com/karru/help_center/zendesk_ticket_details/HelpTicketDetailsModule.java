package com.karru.help_center.zendesk_ticket_details;

import android.app.Activity;

import com.karru.dagger.ActivityScoped;
import com.karru.help_center.zendesk_ticket_details.view.HelpIndexTicketDetailsActivity;

import dagger.Binds;
import dagger.Module;

/**
 * <h>HelpTicketDetailsModule</h>
 * Created by Ali on 2/26/2018.
 */
@Module
public interface HelpTicketDetailsModule
{
    /**
     * <h>get activity</h>
     * <P>This method provides activity reference</P>
     * @return activity.
     */
    @ActivityScoped
    @Binds
    Activity getActivity(HelpIndexTicketDetailsActivity activity);

    @ActivityScoped
    @Binds
    HelpIndexContract.presenter providePresenter(HelpIndexPresenter helpIndexContract);

    @ActivityScoped
    @Binds
    HelpIndexContract.HelpView provideView(HelpIndexTicketDetailsActivity helpIndexTicketDetails);
}
