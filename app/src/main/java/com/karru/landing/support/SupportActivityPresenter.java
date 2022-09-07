package com.karru.landing.support;

import android.content.Context;

import com.karru.api.NetworkService;
import com.heride.rider.R;
import com.google.gson.Gson;
import com.karru.ApplicationClass;
import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.landing.support.model.SupportModel;
import com.karru.managers.network.NetworkStateHolder;
import com.karru.managers.network.RxNetworkObserver;
import com.karru.util.DataParser;
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
import static com.karru.utility.Constants.SUPPORT;

public class SupportActivityPresenter implements SupportActivityContract.Presenter
{
    private static final String TAG = "SupportActivityPresenter";
    @Inject Context mContext;
    @Inject SupportActivity mActivity;
    @Inject NetworkService networkService;
    @Inject @Named(SUPPORT) CompositeDisposable compositeDisposable;
    @Inject NetworkStateHolder networkStateHolder;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject SupportActivityContract.View supportView;

    private SupportModel supportModel;

    @Inject
    SupportActivityPresenter() { }

    @Override
    public void checkRTLConversion() {
        RtlConversion(mActivity,preferenceHelperDataSource.getLanguageSettings().getCode());
    }


    @Override
    public void callSupportAPI()
    {
        if (networkStateHolder.isConnected())
        {
            supportView.showProgress();
            Observable<Response<ResponseBody>> request = networkService.supportAPI(
                    preferenceHelperDataSource.getLanguageSettings().getCode(),
                    ((ApplicationClass) mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()));
            request.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<ResponseBody>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            compositeDisposable.add(d);
                        }

                        @Override
                        public void onNext(Response<ResponseBody> value) {
                            String responseString;
                            switch (value.code())
                            {
                                case 200:
                                    try
                                    {
                                        responseString = value.body().string();
                                        Utility.printLog("support response onNext "+responseString);
                                        Gson gson = new Gson();
                                        supportModel = gson.fromJson(responseString, SupportModel.class);
                                        supportView.onSupportSuccess(supportModel);
                                    }
                                    catch (IOException e)
                                    {
                                        Utility.printLog("support response IOException "+e);
                                        e.printStackTrace();
                                    }
                                    break;

                                case 502:
                                    supportView.onError(mContext.getString(R.string.bad_gateway));
                                    break;

                                default:
                                    supportView.onError(DataParser.fetchErrorMessage(value));
                                    break;
                            }
                        }
                        @Override
                        public void onError(Throwable e) {

                            supportView.onError(mContext.getString(R.string.network_problem));
                            supportView.hideProgress();
                            supportView.onError(mContext.getString(R.string.network_problem));
                        }

                        @Override
                        public void onComplete() {
                            supportView.hideProgress();
                        }
                    });
        }
        else
            supportView.onError(mContext.getString(R.string.network_problem));
    }

    @Override
    public void isLinkNull(String link, int position)
    {
        if (!link.equals(""))
            supportView.startWebView(position);
        else
            supportView.onError(mContext.getString(R.string.network_problem));
    }

    @Override
    public void disposeObservables()
    {
        compositeDisposable.clear();
    }
}
