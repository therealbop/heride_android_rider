package com.karru.help_center.zendesk_ticket_details;

import android.content.Context;
import android.widget.ImageView;

import com.karru.BaseView;
import com.karru.help_center.model.ZendeskDataEvent;
import com.karru.landing.BasePresenter;

import java.util.ArrayList;

/**
 * <h>HelpIndexContract</h>
 * Created by Ali on 2/26/2018.
 */

public interface HelpIndexContract
{
    interface presenter extends BasePresenter
    {

        void onPriorityImage(Context helpIndexTicketDetails, String priority, ImageView ivHelpCenterPriorityPre);

        void callApiToCommentOnTicket(String trim, String zenId);

        void callApiToCreateTicket(String trim, String subject, String priority);

        void callApiToGetTicketInfo(String zenId);
    }
    interface HelpView extends BaseView
    {

        void onTicketInfoSuccess(ArrayList<ZendeskDataEvent> events, String timeToSet, String subject, String priority, String type);

        void onZendeskTicketAdded(String response);
        void onError(String errMsg);
    }
}
