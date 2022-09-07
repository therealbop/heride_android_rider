package com.karru.landing.invite;

import android.content.Context;
import android.content.pm.ResolveInfo;

import com.karru.ApplicationClass;
import com.karru.api.NetworkService;
import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.data.source.local.sqlite.SQLiteDataSource;
import com.karru.managers.network.NetworkStateHolder;
import com.karru.managers.network.RxNetworkObserver;
import com.karru.managers.user_vehicles.MQTTManager;
import com.karru.util.DataParser;
import com.karru.util.ExpireSession;
import com.karru.utility.Utility;
import com.heride.rider.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static com.karru.util.Utility.RtlConversion;
import static com.karru.utility.Constants.INVITE;

/**
 * <h1>CorporateProfilePresenter</h1>
 * used to provide link between model and view
 */
public class InviteActivityPresenter implements InviteActivityContract.Presenter
{
    private static final String TAG = "CorporateProfilePresenter";
    @Inject Context mContext;
    @Inject InviteActivity mActivity;
    @Inject MQTTManager mqttManager;
    @Inject SQLiteDataSource addressDataSource;
    @Inject NetworkService networkService;
    @Inject NetworkStateHolder networkStateHolder;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject InviteActivityContract.View inviteView;
    @Inject @Named(INVITE) CompositeDisposable compositeDisposable;

    @Inject
    InviteActivityPresenter() { }

    @Override
    public void checkRTLConversion() {
        RtlConversion(mActivity,preferenceHelperDataSource.getLanguageSettings().getCode());
    }

    @Override
    public void callFacebookShare(List<ResolveInfo> matches)
    {
        boolean facebookAppFound=false;
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith("com.facebook"))
            {
                inviteView.setFbIntent(info);
                facebookAppFound = true;
                break;
            }
        }
        if(facebookAppFound)
            inviteView.launchFacebook();
        else
        {
            if (networkStateHolder.isConnected())
                inviteView.callFacebookWebView();
            else
                inviteView.showNetworkError();
        }
    }

    @Override
    public void fetchReferralCode()
    {
        if(networkStateHolder.isConnected())
        {
            Observable<Response<ResponseBody>> request = networkService.fetchReferral(
                    ((ApplicationClass) mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                    preferenceHelperDataSource.getLanguageSettings().getCode());

            request.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<ResponseBody>>()
                    {
                        @Override
                        public void onSubscribe(Disposable d) {
                            compositeDisposable.add(d);
                        }

                        @Override
                        public void onNext(Response<ResponseBody> value)
                        {
                            Utility.printLog(TAG+ " fetchReferralCode onNext: "+value.code());
                            switch (value.code())
                            {
                                case 200:
                                    String dataObject = DataParser.fetchDataObjectString(value);
                                    try
                                    {
                                        JSONObject referralCodeObj = new JSONObject(dataObject);
                                        String referralCode = referralCodeObj.getString("referralCode");
                                        String title = referralCodeObj.getString("title");
                                        String shareTextMessage = referralCodeObj.getString("shareTextMessage");
                                        inviteView.showReferralCode(referralCode,title,shareTextMessage);
                                    }
                                    catch (JSONException e) {
                                        Utility.printLog(TAG+ " fetchReferralCode JSONException: "+e);
                                        e.printStackTrace();
                                    }
                                    break;

                                case 401:
                                    ExpireSession.refreshApplication(mContext,mqttManager,
                                            preferenceHelperDataSource,addressDataSource);
                                    break;

                                case 502:
                                    inviteView.showToast(mContext.getString(R.string.bad_gateway));
                                    break;

                                default:
                                    inviteView.showToast(DataParser.fetchErrorMessage(value));
                                    break;
                            }
                        }
                        @Override
                        public void onError(Throwable e)
                        {
                            Utility.printLog(TAG+ " fetchReferralCode error: "+e.getMessage());
                        }

                        @Override
                        public void onComplete()
                        {
                        }
                    });
        }
    }

    @Override
    public void disposeObservable() {
        compositeDisposable.clear();
    }
}
