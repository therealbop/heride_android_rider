package com.karru.help_center.zendesk_ticket_details;

import com.karru.dagger.ActivityScoped;
import com.karru.help_center.zendesk_ticket_details.view.HelpIndexRecyclerAdapter;

import dagger.Module;
import dagger.Provides;

/**
 * <h>HelpIndexAdapterModule</h>
 * Created by Ali on 2/26/2018.
 */
@Module
public class HelpIndexAdapterModule
{
    @ActivityScoped
    @Provides
    HelpIndexRecyclerAdapter provideHelpAdapter()
    {
     return new HelpIndexRecyclerAdapter();
    }
}
