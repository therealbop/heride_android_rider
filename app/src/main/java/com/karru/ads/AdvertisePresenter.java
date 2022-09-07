package com.karru.ads;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.karru.ApplicationClass;
import com.karru.api.NetworkService;
import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.data.source.local.sqlite.SQLiteDataSource;
import com.karru.managers.network.NetworkStateHolder;
import com.karru.managers.user_vehicles.MQTTManager;
import com.karru.util.ExpireSession;
import com.karru.utility.Utility;
import com.heride.rider.R;

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
import static com.karru.utility.Constants.ADS;

/**
 * <h1>AdvertisePresenter</h1>
 * This method is used to add the link between view and model and call the API
 * @author 3Embed
 * @since on 25-01-2018.
 */
public class AdvertisePresenter implements AdvertiseContract.Presenter
{
    @Inject Context mContext;
    @Inject AdvertiseActivity mActivity;
    @Inject MQTTManager mqttManager;
    @Inject SQLiteDataSource sqLiteDataSource;
    @Inject NetworkService networkService;
    @Inject NetworkStateHolder networkStateHolder;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject AdvertiseContract.View view;
    @Inject @Named(ADS) CompositeDisposable compositeDisposable;

    public static final String TAG = "AdvertisePresenter";

    @Inject
    AdvertisePresenter() {}

    @Override
    public void disposeObservable() {
        compositeDisposable.clear();
    }

    @Override
    public void updateNotificationStatus(int status, String messageId,String knowMoreUrl,boolean hide)
    {
        if (networkStateHolder.isConnected())
        {
            Utility.printLog(TAG + " updateNotificationStatus status " + status);
            Observable<Response<ResponseBody>> request = networkService.updateNotificationStatus(((ApplicationClass)
                            mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                    preferenceHelperDataSource.getLanguageSettings().getCode(),status,messageId);
            request.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<ResponseBody>>() {

                        @Override
                        public void onSubscribe(Disposable d) {
                            compositeDisposable.add(d);
                        }

                        @Override
                        public void onNext(Response<ResponseBody> result)
                        {
                            Utility.printLog(TAG + " updateNotificationStatus onNext " + result.code());
                            switch (result.code())
                            {
                                case 401:
                                    ExpireSession.refreshApplication(mContext,mqttManager, preferenceHelperDataSource, sqLiteDataSource);
                                    break;

                                case 200:
                                    if(hide)
                                        view.hideAdScreen();
                                    else
                                    {
                                        if(knowMoreUrl != null)
                                            if(!knowMoreUrl.equals(""))
                                            {
                                                view.openBrowser();
                                                return;
                                            }
                                        view.hideAdScreen();
                                    }
                                    break;

                                case 502:
                                    view.showToast(mContext.getString(R.string.bad_gateway));
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable errorMsg)
                        {
                            Utility.printLog(TAG + " updateNotificationStatus onError " + errorMsg);
                        }

                        @Override
                        public void onComplete()
                        {
                            Utility.printLog(TAG + " updateNotificationStatus onComplete ");
                        }
                    });
        }
    }

    @Override
    public void checkRTLConversion() {
        RtlConversion(mActivity,preferenceHelperDataSource.getLanguageSettings().getCode());
    }
}


