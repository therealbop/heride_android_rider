package com.karru.landing.payment;

import android.content.Context;

import com.karru.api.NetworkService;
import com.heride.rider.R;
import com.karru.ApplicationClass;
import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.data.source.local.sqlite.SQLiteDataSource;
import com.karru.landing.payment.model.CardDetails;
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
import static com.karru.utility.Constants.PAYMENT;

/**
 * <h>PaymentFragmentModel</h>
 * <p>service call for the payment to get the all card </p>
 */
public class PaymentPresenter implements PaymentActivityContract.Presenter
{
    private static final String TAG = "PaymentPresenter";
    @Inject Context mContext;
    @Inject PaymentActivity mActivity;
    @Inject MQTTManager mqttManager;
    @Inject NetworkService networkService;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject @Named(PAYMENT) CompositeDisposable compositeDisposable;
    @Inject NetworkStateHolder networkStateHolder;
    @Inject PaymentActivityContract.View paymentView;
    @Inject SQLiteDataSource sqLiteDataSource;

    private ArrayList<CardDetails> cardDetailsListDataModel;

    @Inject
    PaymentPresenter() {}

    @Override
    public void checkRTLConversion() {
        RtlConversion(mActivity,preferenceHelperDataSource.getLanguageSettings().getCode());
    }

    @Override
    public void extractSavedCards()
    {
        cardDetailsListDataModel = sqLiteDataSource.extractAllCardDetailsStored();
        paymentView.savedCardsDetails(cardDetailsListDataModel);
    }

    @Override
    public void saveDefaultCard(CardDetails cardDetails)
    {
        preferenceHelperDataSource.setDefaultCardDetails(cardDetails);
        Utility.printLog(TAG+" default card object "+preferenceHelperDataSource.getDefaultCardDetails());
    }

    @Override
    public void makeCardDefault(CardDetails cardDetails)
    {
        if (networkStateHolder.isConnected())
        {
            paymentView.showProgressDialog();
            Observable<Response<ResponseBody>> request = networkService.makeDefaultCard(
                    ((ApplicationClass) mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                    preferenceHelperDataSource.getLanguageSettings().getCode(), cardDetails.getId());
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

                                    sqLiteDataSource.updateCardDetails(cardDetails.getId(),true);
                                    saveDefaultCard(cardDetails);
                                    paymentView.onSelectOfCard();
                                    break;

                                case 401:
                                    ExpireSession.refreshApplication(mContext,mqttManager,preferenceHelperDataSource,sqLiteDataSource);
                                    break;

                                default:
                                    Utility.printLog("AddCardResponseCodePayment1"+ DataParser.fetchErrorMessage(value));
                                    paymentView.errorResponse(DataParser.fetchErrorMessage(value));
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable e)
                        {
                            paymentView.dismissProgressDialog();
                            paymentView.errorResponse(mContext.getString(R.string.network_problem));
                        }

                        @Override
                        public void onComplete() {
                            paymentView.dismissProgressDialog();
                        }
                    });
        }
        else
            paymentView.errorResponse(mContext.getString(R.string.network_problem));
    }

    @Override
    public void checkForCardSelect(CardDetails cardDetails, boolean isFromBooking, boolean isDefault)
    {
        if(!isFromBooking)
            paymentView.onClickOfItem(cardDetails);
        else
        {
            if(isDefault)
                paymentView.onSelectOfCard();
            else
                makeCardDefault(cardDetails);
        }
    }

    @Override
    public void disposeObservables()
    {
        compositeDisposable.clear();
    }

    @Override
    public void deleteCard(String cardId,int position)
    {
        if (networkStateHolder.isConnected())
        {
            Observable<Response<ResponseBody>> request = networkService.deleteCard(
                    ((ApplicationClass) mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                    preferenceHelperDataSource.getLanguageSettings().getCode(),cardId);
            request.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<ResponseBody>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            compositeDisposable.add(d);
                        }

                        @Override
                        public void onNext(Response<ResponseBody> value) {
                            Utility.printLog(" AddCardResponse CodePayment1 "+value.code());
                            switch (value.code())
                            {
                                case 200:
                                    if(cardDetailsListDataModel.get(position).getDefault())
                                    {
                                        if(position != cardDetailsListDataModel.size()-1)
                                        {
                                            cardDetailsListDataModel.get(cardDetailsListDataModel.size()-1).setDefault(true);
                                            sqLiteDataSource.updateCardDetails(cardDetailsListDataModel.get(cardDetailsListDataModel.size()-1).getId(),
                                                    true);
                                            paymentView.deleteItemData(position,cardDetailsListDataModel);
                                        }
                                        else
                                        {
                                            cardDetailsListDataModel.get(0).setDefault(true);
                                            sqLiteDataSource.updateCardDetails(cardDetailsListDataModel.get(0).getId(), true);
                                            paymentView.deleteItemData(position,cardDetailsListDataModel);
                                        }
                                    }
                                    else
                                        paymentView.deleteItemData(position,cardDetailsListDataModel);

                                    //to make saved card as null
                                    Utility.printLog(" card list zise "+cardDetailsListDataModel.size());
                                    if(cardDetailsListDataModel.size() == 0)
                                        preferenceHelperDataSource.setDefaultCardDetails(null);
                                    Utility.printLog(TAG+" default card object 2 "+preferenceHelperDataSource.getDefaultCardDetails());
                                    sqLiteDataSource.deleteCard(cardId);
                                    Utility.printLog(" AddCardResponse CodePayment1"+ DataParser.fetchSuccessMessage(value));
                                    break;

                                case 500:
                                case 509:
                                    paymentView.badGateWayError();
                                    break;

                                case 401:
                                    ExpireSession.refreshApplication(mContext,mqttManager,preferenceHelperDataSource,sqLiteDataSource);
                                    break;

                                case 502:
                                    paymentView.errorResponse(mContext.getString(R.string.bad_gateway));
                                    break;

                                default:
                                    paymentView.errorResponse(DataParser.fetchErrorMessage(value));
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable e)
                        {
                            paymentView.dismissProgressDialog();
                            paymentView.errorResponse(mContext.getString(R.string.network_problem));
                        }

                        @Override
                        public void onComplete()
                        {
                            paymentView.dismissProgressDialog();
                        }
                    });
        }
        else
            paymentView.errorResponse(mContext.getString(R.string.network_problem));
    }
}
