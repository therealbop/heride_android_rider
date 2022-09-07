package com.karru.landing.about;

import android.content.Context;
import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.managers.network.NetworkStateHolder;
import com.karru.managers.network.RxNetworkObserver;
import com.karru.utility.Utility;
import com.heride.rider.R;

import javax.inject.Inject;
import javax.inject.Named;
import io.reactivex.disposables.CompositeDisposable;

import static com.karru.util.Utility.RtlConversion;
import static com.karru.utility.Constants.ABOUT;

public class AboutActivityPresenter implements AboutActivityContract.AboutPresenter
{
    private static final String TAG = "AboutActivityPresenter";
    @Inject Context mContext;
    @Inject AboutActivity mActivity;
    @Inject AboutActivityContract.View aboutView;
    @Inject NetworkStateHolder networkStateHolder;
    @Inject @Named(ABOUT) CompositeDisposable compositeDisposable;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;

    @Inject
    AboutActivityPresenter(){ }

    @Override
    public void checkRTLConversion() {
        RtlConversion(mActivity,preferenceHelperDataSource.getLanguageSettings().getCode());
    }

    @Override
    public void checkNetworkForGoogleConnect()
    {
        if(networkStateHolder.isConnected())
            aboutView.connectToWeb();
        else
            aboutView.showNetworkErrorMsg();
    }

    @Override
    public void checkNetworkForFacebookConnect()
    {
        if(networkStateHolder.isConnected())
            aboutView.connectToFacebook();
        else
            aboutView.showNetworkErrorMsg();
    }

    @Override
    public void disposeObservable() {
        compositeDisposable.clear();
    }

    @Override
    public void checkTermsPrivacyLinks()
    {
        String termsLink = mContext.getString(R.string.PRIVACY_BASE)+
                preferenceHelperDataSource.getLanguageSettings().getCode()+mContext.getString(R.string.TERMS_KEY);
        String privacyLink = mContext.getString(R.string.PRIVACY_BASE)+
                preferenceHelperDataSource.getLanguageSettings().getCode()+mContext.getString(R.string.PRIVACY_KEY);
        Utility.printLog(TAG+" privacy links "+termsLink+"\n"+privacyLink);
        String links[] = {termsLink,privacyLink};
        aboutView.openTermsScreen(links);
    }
}
