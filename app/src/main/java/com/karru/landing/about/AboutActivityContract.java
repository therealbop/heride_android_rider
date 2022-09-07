package com.karru.landing.about;

public interface AboutActivityContract
{
    interface View
    {
        /**
         * <h>Connect to PlayStore</h>
         * <p> this method is using to connect to google PlayStore <p>
         */
        void connectToWeb();

        /**
         * <h>Facebook Connection</h>
         * <p>this method is using to connect to Facebook login</p>
         */
        void connectToFacebook();

        /**
         * <h>Network Error Alert</h>
         * <p>this methos is using to display network Error message</p>
         */
        void showNetworkErrorMsg();

        /**
         * <h2>openTermsScreen</h2>
         * used to open terms privacy screen
         */
        void openTermsScreen(String[] links);
    }
    interface AboutPresenter
    {
        /**
         * <h>Check Internet</h>
         * <p>this methd is using to check internet connection before google playStore connection</p>
         */
        void checkNetworkForGoogleConnect();

        /**
         * <h>Check Internet</h>
         * <p>this methd is using to check internet connection before Facebook connection</p>
         */
        void checkNetworkForFacebookConnect();

        /**
         * <h2>disposeObservable</h2>
         * used to dispose
         */
        void disposeObservable();

        /**
         * <h2>checkTermsPrivacyLinks</h2>
         * used to get the terms and privacy links
         */
        void checkTermsPrivacyLinks();

        void checkRTLConversion();
    }
}
