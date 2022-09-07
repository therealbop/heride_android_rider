package com.karru.help_center;

import com.karru.BaseView;
import com.karru.help_center.model.OpenClose;
import com.karru.landing.BasePresenter;


/**
 * <h>ZendeskHelpContract</h>
 * Created by Ali on 2/26/2018.
 */

public interface ZendeskHelpContract
{
    interface Presenter extends BasePresenter
    {
        void onToGetZendeskTicket();
    }
    interface  ZendeskView extends BaseView
    {
        void onGetTicketSuccess();

        void onEmptyTicket();

        void onTicketStatus(OpenClose openClose, int openCloseSize, boolean isOpenClose);

        void onNotifyData();
        void onError(String error);
    }
}
