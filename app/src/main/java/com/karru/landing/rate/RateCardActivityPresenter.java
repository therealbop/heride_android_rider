package com.karru.landing.rate;

import android.content.Context;

import com.karru.api.NetworkService;
import com.heride.rider.R;
import com.google.gson.Gson;
import com.karru.ApplicationClass;
import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.data.source.local.sqlite.SQLiteDataSource;
import com.karru.landing.rate.model.RateCardCityDetails;
import com.karru.landing.rate.model.RateCardDetail;
import com.karru.managers.network.NetworkStateHolder;
import com.karru.managers.network.RxNetworkObserver;
import com.karru.managers.user_vehicles.MQTTManager;
import com.karru.util.DataParser;
import com.karru.util.ExpireSession;
import com.karru.utility.Utility;
import java.util.ArrayList;

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
import static com.karru.utility.Constants.RATE_CARD;

public class RateCardActivityPresenter implements RateCardActivityContract.RateCardPresenter
{
    @Inject NetworkService networkService;
    @Inject NetworkStateHolder networkStateHolder;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject MQTTManager mqttManager;
    @Inject SQLiteDataSource addressDataSource;
    @Inject @Named(RATE_CARD) CompositeDisposable compositeDisposable;
    @Inject Context mContext;
    @Inject RateCardActivity mActivity;
    @Inject Gson gson;
    @Inject RateCardActivityContract.RateCardView rateCardView;

    private ArrayList<String> cities = new ArrayList<>();

    @Inject
    RateCardActivityPresenter() {}

    @Override
    public void checkRTLConversion() {
        RtlConversion(mActivity,preferenceHelperDataSource.getLanguageSettings().getCode());
    }


    @Override
    public void disposeObservable()
    {
        compositeDisposable.clear();
    }

    @Override
    public void getRateCard()
    {
        if(networkStateHolder.isConnected())
        {
            rateCardView.showProgress();
            Observable<Response<ResponseBody>> request = networkService.getRateCard(
                    ((ApplicationClass) mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                    preferenceHelperDataSource.getLanguageSettings().getCode());

            request.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<ResponseBody>>() {
                        @Override
                        public void onSubscribe(Disposable d)
                        {
                            compositeDisposable.add(d);
                        }

                        @Override
                        public void onNext(Response<ResponseBody> value)
                        {
                            Utility.printLog(" getRateCard: onNext " + value.code());
                            switch (value.code())
                            {
                                case 200:
                                    String result = DataParser.fetchSuccessResponse(value);
                                    RateCardDetail rateCardDetail = gson.fromJson(result, RateCardDetail.class);
                                    handleResponse(rateCardDetail);
                                    Utility.printLog(" getRateCard: onNext" + result);
                                    break;

                                case 401:
                                    ExpireSession.refreshApplication(mContext,mqttManager,preferenceHelperDataSource,addressDataSource);
                                    break;

                                case 502:
                                    rateCardView.showToast(mContext.getString(R.string.bad_gateway));
                                    break;

                                default:
                                    rateCardView.showToast(DataParser.fetchErrorMessage(value));
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable e)
                        {
                            Utility.printLog(" getRateCard onError: " + e.getMessage());
                            rateCardView.hideProgress();
                            rateCardView.showToast(mContext.getString(R.string.network_problem));
                        }

                        @Override
                        public void onComplete()
                        {
                            rateCardView.hideProgress();
                        }
                    });
        }
        else
            rateCardView.showToast(mContext.getString(R.string.network_problem));
    }

    /**
     * <h>Handle Server Response</h>
     * <p>this method is using to handle server response </p>
     * @param rateCardDetail server response
     */
    private void handleResponse(RateCardDetail rateCardDetail) {
        if (rateCardDetail.getData().size() > 0)
        {
            ArrayList<RateCardCityDetails> rateCardCityDetails = rateCardDetail.getData();
            for (int i = 0; i < rateCardCityDetails.size(); i++) {
                cities.add(rateCardCityDetails.get(i).getCityName());
            }
            rateCardView.initCities(cities, rateCardDetail);
        }
    }
}
