package com.karru.landing.card_details;

import android.content.Context;
import com.karru.api.NetworkService;
import com.heride.rider.R;
import com.karru.ApplicationClass;
import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.data.source.local.sqlite.SQLiteDataSource;
import com.karru.landing.payment.model.CardDetails;
import com.karru.managers.network.NetworkStateHolder;
import com.karru.managers.network.RxNetworkObserver;
import com.karru.util.DataParser;
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
import static com.karru.utility.Constants.CARD_DETAILS;

public class CardDetailsActivityPresenter implements CardDetailsActivityContract.DeleteCardPresenter
{
    @Inject Context context;
    @Inject CardDetailsActivity mActivity;
    @Inject CardDetailsActivityContract.View view;
    @Inject NetworkStateHolder networkStateHolder;
    @Inject @Named(CARD_DETAILS) CompositeDisposable compositeDisposable;
    @Inject NetworkService networkService;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject SQLiteDataSource sqLiteDataSource;

    @Inject
    CardDetailsActivityPresenter() {}

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
    public void makeCardDefault(String cardId)
    {
        if (networkStateHolder.isConnected())
        {
            view.showProgressBar();
            Observable<Response<ResponseBody>> request = networkService.makeDefaultCard(
                    ((ApplicationClass) context.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                    preferenceHelperDataSource.getLanguageSettings().getCode(), cardId);
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

                            Utility.printLog(" makeCardDefault "+value.code());
                            switch (value.code())
                            {
                                case 200:
                                    ArrayList<CardDetails> cardsDetailsList = sqLiteDataSource.extractAllCardDetailsStored();
                                    for(CardDetails cardsDetailsList1:cardsDetailsList)
                                    {
                                        if(cardsDetailsList1.getDefault())
                                        {
                                            cardsDetailsList1.setDefault(false);
                                            sqLiteDataSource.updateCardDetails(cardsDetailsList1.getId(), false);
                                            break;
                                        }
                                    }

                                    sqLiteDataSource.updateCardDetails(cardId,true);
                                    view.onResponse(DataParser.fetchSuccessMessage(value));
                                    break;

                                case 502:
                                    view.onError(context.getString(R.string.bad_gateway));
                                    break;

                                default:
                                    view.onResponse(DataParser.fetchErrorMessage(value));
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable e)
                        {
                            view.hideProgressBar();
                            view.onError(context.getString(R.string.network_problem));
                        }

                        @Override
                        public void onComplete()
                        {
                            view.hideProgressBar();
                        }
                    });
        }
    }
}
