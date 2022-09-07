package com.karru.landing.corporate;

import android.content.Context;

import com.karru.api.NetworkService;
import com.karru.landing.corporate.view.CorporateProfileActivity;
import com.heride.rider.R;
import com.google.gson.Gson;
import com.karru.ApplicationClass;
import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.data.source.local.sqlite.SQLiteDataSource;
import com.karru.managers.network.NetworkStateHolder;
import com.karru.managers.network.RxNetworkObserver;
import com.karru.managers.user_vehicles.MQTTManager;
import com.karru.util.DataParser;
import com.karru.util.ExpireSession;
import com.karru.utility.Utility;
import java.io.IOException;

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
import static com.karru.utility.Constants.CORPORATE_PROFILE;

/**
 * <h1>CorporateProfilePresenter</h1>
 * used to provide link between model and view
 */
public class CorporateProfilePresenter implements CorporateProfileContract.Presenter
{
    private static final String TAG = "CorporateProfilePresenter";
    @Inject Gson gson;
    @Inject Context mContext;
    @Inject CorporateProfileActivity mActivity;
    @Inject NetworkService networkService;
    @Inject MQTTManager mqttManager;
    @Inject NetworkStateHolder networkStateHolder;
    @Inject SQLiteDataSource sqLiteDataSource;
    @Inject CorporateProfileContract.View corporateView;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject @Named(CORPORATE_PROFILE) CompositeDisposable compositeDisposable;

    @Inject CorporateProfilePresenter() {}

    @Override
    public void checkRTLConversion() {
        RtlConversion(mActivity,preferenceHelperDataSource.getLanguageSettings().getCode());
    }

    @Override
    public void getCorporateProfiles()
    {
        if (networkStateHolder.isConnected())
        {
            Utility.printLog("AuthMsg"+((ApplicationClass) mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()));
            corporateView.showProgressDialog();
            Observable<Response<ResponseBody>> request = networkService.getCorporateProfiles(
                    ((ApplicationClass) mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                    preferenceHelperDataSource.getLanguageSettings().getCode());
            request.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<ResponseBody>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            compositeDisposable.add(d);
                        }

                        @Override
                        public void onNext(Response<ResponseBody> value)
                        {
                            Utility.printLog(" onNext corporateProfileModel" + value.code());
                            switch (value.code())
                            {
                                case 200:
                                    try
                                    {
                                        CorporateProfileModel corporateProfileModel = gson.fromJson(value.body().string(),
                                                CorporateProfileModel.class);
                                        Utility.printLog(TAG+" corporateProfileModel size "+corporateProfileModel.getData().size());
                                        corporateView.populateListWithProfiles(corporateProfileModel.getData());
                                        if(corporateProfileModel.getData().size()>0)
                                            corporateView.hideEmptyScreen();
                                        else
                                            corporateView.showEmptyScreen();
                                    }
                                    catch (IOException e)
                                    {
                                        e.printStackTrace();
                                    }
                                    break;

                                case 401:
                                    ExpireSession.refreshApplication(mContext,mqttManager,preferenceHelperDataSource,sqLiteDataSource);
                                    break;

                                case 502:
                                    corporateView.showToast(mContext.getString(R.string.bad_gateway));
                                    break;

                                default:
                                    corporateView.showToast(DataParser.fetchErrorMessage(value));
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable e)
                        {
                            corporateView.dismissProgressDialog();
                            corporateView.showToast(mContext.getString(R.string.network_problem));
                        }

                        @Override
                        public void onComplete() {
                            corporateView.dismissProgressDialog();
                        }
                    });
        }
        else
            corporateView.showToast(mContext.getString(R.string.network_problem));
    }

    @Override
    public void disposeObservable() {
        compositeDisposable.clear();
    }
}
