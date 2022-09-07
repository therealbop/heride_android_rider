package com.karru.landing.invite;

import android.content.pm.ResolveInfo;
import java.util.List;

public interface InviteActivityContract
{
    interface View
    {
        /**
         * <h>Setting FbIntent</h>
         * <p>this method is using to  set the Intent using to call Facebook</p>
         * @param info parameter is using as a Facebook Intent property
         */
        void setFbIntent(ResolveInfo info);

        /**
         * <h>Network Error</h>
         * <p>this method is using to call alert to show Netwotk Error</p>
         */
        void showNetworkError();

        /**
         * <h2>facebookShare</h2>
         * <p> method to share on facebook </p>
         */
        void launchFacebook();

        /**
         * <h>Facebook on WebView</h>
         * <p>this method is using to call Facebook on WebView</p>
         */
        void callFacebookWebView();

        /**
         * <h2>showToast</h2
         * used to show the toast>
         * @param string string to be shown
         */
        void showToast(String string);

        /**
         * <h2>showReferralCode</h2>
         * used to show referral
         * @param referral referral code
         */
        void showReferralCode(String referral,String title,String shareTextMessage);
    }

    interface Presenter
    {
        /**
         * <h2>facebookShare</h2>
         * <p> method to share on facebook </p>
         */
        void callFacebookShare(List<ResolveInfo> matches);

        /**
         * <h2>disposeObservable</h2>
         * used to dispose
         */
        void disposeObservable();

        /**
         * <h2>fetchReferralCode</h2>
         * used to fetch the referral code
         */
        void fetchReferralCode();

        void checkRTLConversion();
    }
}
