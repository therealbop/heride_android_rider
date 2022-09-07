package com.karru.landing.support;

import com.karru.landing.BasePresenter;
import com.karru.landing.support.model.SupportDataModel;
import com.karru.landing.support.model.SupportModel;

public interface SupportActivityContract
{
    interface View
    {
        /**
         * <h>Activity Starter</h>
         * <p>this method using to start the WebViewActivity if all the pre defined conditions met</p>
         * @param position paramater is using to get the link and title at the specified position
         */
        void startWebView(int position);

        /**
         * <h>Successfull API call</h>
         * @param support_pojo paramater contains Network response from server
         */
        void onSupportSuccess(SupportModel support_pojo);

        /**
         * <h>Error in API call</h>
         * <p>this method is using to disaply Error message to the user</p>
         */
        void onError(String message);

        /**
         * <h2>showProgress</h2>
         * used to show the progress bar
         */
        void showProgress();

        /**
         * <h2>hideProgress</h2>
         * used to hide the progress bar
         */
        void hideProgress();
    }

    interface Presenter
    {
        /**
         * <h>Call Api</h>
         * <p>this method is using to make Api call to server</p>
         */
        void callSupportAPI();

        /**
         * <H>Null Checker</H>
         * <p>this method is using to check whether the link is null or not</p>
         * @param link parameter contains the string link value
         * @param position parameter is using for the callback purpose
         */
        void  isLinkNull(String link, int position);

        /**
         * <h2>disposeObservables</h2>
         * used o dispose
         */
        void disposeObservables();

        void checkRTLConversion();
    }
}
