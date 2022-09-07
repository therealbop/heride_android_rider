package com.karru.help_center;

import com.karru.dagger.ActivityScoped;
import com.karru.help_center.zendesk_ticket_details.view.HelpIndexAdapter;

import dagger.Module;
import dagger.Provides;

/**
 * <h>ZendeskUtilModule</h>
 * Created by Ali on 2/26/2018.
 */

@Module
public class ZendeskUtilModule
{
    @ActivityScoped
    @Provides
    HelpIndexAdapter provideHelpIndexAdapter()
    {
        return  new HelpIndexAdapter();
    }
}
