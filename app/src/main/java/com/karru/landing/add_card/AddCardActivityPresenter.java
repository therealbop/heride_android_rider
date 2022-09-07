package com.karru.landing.add_card;

import android.content.Context;
import androidx.core.content.ContextCompat;
import com.karru.api.NetworkService;
import com.heride.rider.R;
import com.google.gson.Gson;
import com.karru.ApplicationClass;
import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.data.source.local.sqlite.SQLiteDataSource;
import com.karru.landing.payment.model.CardsDetailsModel;
import com.karru.managers.network.NetworkStateHolder;
import com.karru.managers.user_vehicles.MQTTManager;
import com.karru.util.DataParser;
import com.karru.util.ExpireSession;
import com.karru.utility.Utility;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
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
import static com.karru.utility.Constants.ADD_CARD;

public class AddCardActivityPresenter implements AddCardActivityContract.AddCardPresenter
{
    @Inject Gson gson;
    @Inject MQTTManager mqttManager;
    @Inject NetworkStateHolder networkStateHolder;
    @Inject NetworkService networkService;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject SQLiteDataSource sqLiteDataSource;
    @Inject @Named(ADD_CARD) CompositeDisposable compositeDisposable;
    @Inject Context context;
    @Inject AddCardActivity mActivity;
    @Inject AddCardActivityContract.AddCardView addCardView;

    @Inject AddCardActivityPresenter() {}

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
    public void addCardAPI(String cardToken)
    {
        Utility.printLog(" card token generated "+cardToken);
        if (networkStateHolder.isConnected())
        {
            Observable<Response<ResponseBody>> request = networkService.addCard(
                    ((ApplicationClass) context.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid())
                    , preferenceHelperDataSource.getLanguageSettings().getCode(), preferenceHelperDataSource.getUserEmail(), cardToken);
            request.subscribeOn(Schedulers.io())
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
                            switch (value.code())
                            {
                                case 200:
                                    String getCardsPOJO = DataParser.fetchDataObjectString(value);
                                    CardsDetailsModel getCardsPOJOObject = gson.fromJson(getCardsPOJO, CardsDetailsModel.class);
                                    if(getCardsPOJOObject.getCards().length == 1)
                                        sqLiteDataSource.insertCard(getCardsPOJOObject.getCards()[0]);
                                    else
                                        sqLiteDataSource.insertCard(getCardsPOJOObject.getCards()[1]);

                                    addCardView.onCardAdded();
                                    break;

                                case 401:
                                    ExpireSession.refreshApplication(context,mqttManager,
                                            preferenceHelperDataSource,sqLiteDataSource);

                                case 502:
                                    addCardView.onAddCardError(context.getString(R.string.bad_gateway));
                                    break;

                                default:
                                    addCardView.onAddCardError(DataParser.fetchErrorMessage(value));
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable e)
                        {
                            addCardView.dismissProgressDialog();
                            addCardView.onAddCardError(context.getString(R.string.network_problem));
                        }

                        @Override
                        public void onComplete()
                        {
                            addCardView.dismissProgressDialog();
                        }
                    });
        }
    }

    @Override
    public void generateStripeToken(Card card)
    {
        addCardView.showProgressDialog();
        new Stripe(context).createToken(card, preferenceHelperDataSource.getStripeKey(), new TokenCallback()
        {
            @Override
            public void onError(Exception error)
            {
                addCardView.dismissProgressDialog();
                addCardView.updateUI(true,ContextCompat.getColor(context,R.color.colorPrimaryDark),
                        ContextCompat.getColor(context,R.color.text_color));
            }
            @Override
            public void onSuccess(com.stripe.android.model.Token token)
            {
                addCardAPI(token.getId() );
            }
        });
    }

    @Override
    public void validateCardDetails(Card card)
    {
        if(card==null)
            addCardView.onInvalidOfCard();
        else
            addCardView.onValidOfCard();
    }

    @Override
    public String stripeKeyGetter()
    {
        return preferenceHelperDataSource.getStripeKey();
    }
}
