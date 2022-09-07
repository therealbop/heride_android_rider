package com.karru.landing.wallet;

import android.content.Context;
import android.widget.Toast;
import com.karru.api.NetworkService;
import com.heride.rider.R;
import com.google.gson.Gson;
import com.karru.ApplicationClass;
import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.landing.home.model.WalletDataModel;
import com.karru.managers.network.NetworkStateHolder;
import com.karru.managers.network.RxNetworkObserver;
import com.karru.utility.Utility;
import org.json.JSONException;
import org.json.JSONObject;
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
import static com.karru.utility.Constants.WALLET;

public class WalletActivityPresenter implements WalletActivityContract.WalletPresenter
{
    private final String TAG = "WalletDetailsProvider";

    @Inject Gson gson;
    @Inject com.karru.util.Utility utility;
    @Inject WalletActivityContract.WalletView walletView;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject NetworkService networkService;
    @Inject NetworkStateHolder networkStateHolder;
    @Inject @Named(WALLET) CompositeDisposable compositeDisposable;
    @Inject Context mContext;
    @Inject WalletActivity mActivity;

    @Inject
    WalletActivityPresenter() {}

    @Override
    public void checkRTLConversion() {
        RtlConversion(mActivity,preferenceHelperDataSource.getLanguageSettings().getCode());
    }


    @Override
    public void getWalletDetails()
    {
        getWalletDetails(preferenceHelperDataSource.getWalletSettings());
        if(preferenceHelperDataSource.isCardEnable() && preferenceHelperDataSource.getLoginType() == 0)
            walletView.showCardOption();
        else
            walletView.hideCardOption();
    }

    @Override
    public void disposeObservable() {
        compositeDisposable.clear();
    }

    @Override
    public void rechargeWallet(String rechargeWallet)
    {
        if(networkStateHolder.isConnected() && preferenceHelperDataSource.getDefaultCardDetails()!=null)
        {
            walletView.showProgressDialog();
            Observable<Response<ResponseBody>> request = networkService.rechargeWallet(
                    ((ApplicationClass) mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                    preferenceHelperDataSource.getLanguageSettings().getCode(),
                    preferenceHelperDataSource.getDefaultCardDetails().getId(), rechargeWallet);

            request.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<ResponseBody>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            compositeDisposable.add(d);
                        }

                        @Override
                        public void onNext(Response<ResponseBody> value)
                        {
                            Utility.printLog(TAG + " RechargeWallet onNext: " + value.code());
                            String response;
                            try
                            {
                                switch (value.code())
                                {
                                    case 200:
                                        response = value.body().string();
                                        JSONObject object;
                                        try
                                        {
                                            Utility.printLog("data string "+response);
                                            object = new JSONObject(response);
                                            String dataObject =  object.getJSONObject("data").toString();
                                            JSONObject jsonObject = new JSONObject(dataObject);
                                            JSONObject wallet = jsonObject.getJSONObject("walletData");
                                            WalletDataModel walletDataModel = gson.fromJson(wallet.toString(),
                                                    WalletDataModel.class);
                                            preferenceHelperDataSource.setWalletSettings(walletDataModel);
                                            getWalletDetails();

                                            JSONObject jsonResponse = new JSONObject(response);
                                            walletView.showAlert(jsonResponse.getString("message"));
                                        }
                                        catch (JSONException e)
                                        {
                                            e.printStackTrace();
                                        }
                                        break;

                                    case 502:
                                        walletView.showToast(mContext.getString(R.string.bad_gateway),Toast.LENGTH_LONG);
                                        break;

                                    default:
                                        response = value.errorBody().string();
                                        JSONObject jsonResponse = new JSONObject(response);
                                        walletView.showAlert(jsonResponse.getString("message"));
                                        break;
                                }
                            }
                            catch (IOException e)
                            {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Throwable e)
                        {
                            Utility.printLog(TAG + "RechargeWallet error: " + e.getMessage());
                            walletView.hideProgressDialog();
                            walletView.showToast(mContext.getString(R.string.network_problem),Toast.LENGTH_LONG);
                        }

                        @Override
                        public void onComplete() {
                            walletView.hideProgressDialog();
                        }
                    });
        }
        else
            walletView.showToast(mContext.getString(R.string.network_problem),Toast.LENGTH_LONG);
    }

    /**
     * <h2>getWalletDetails</h2>
     * used to handle the wallet details
     * @param walletDataModel wallet details model
     */
    private void getWalletDetails(WalletDataModel walletDataModel)
    {
        int currencyAbbr = walletDataModel.getCurrencyAbbr();
        String currencySymbol = walletDataModel.getCurrencySymbol();
        walletView.setBalanceValues(utility.currencyAdjustment(currencyAbbr,currencySymbol,walletDataModel.getWalletBalance()+""),
                utility.currencyAdjustment(currencyAbbr,currencySymbol,walletDataModel.getHardLimit()+""),
                utility.currencyAdjustment(currencyAbbr,currencySymbol,walletDataModel.getSoftLimit()+""),
                currencySymbol,currencyAbbr);
    }

    @Override
    public void getLastCardNo()
    {
        if(preferenceHelperDataSource.getDefaultCardDetails()!=null)
        {
            Utility.printLog("MyCardNumber"+preferenceHelperDataSource.getDefaultCardDetails().getLast4());
            walletView.setCard(preferenceHelperDataSource.getDefaultCardDetails().getLast4(),
                    preferenceHelperDataSource.getDefaultCardDetails().getBrand());
        }
        else
            walletView.setNoCard();

        switch (preferenceHelperDataSource.getWalletSettings().getCurrencyAbbr())
        {
            case 1:
                walletView.prefixCurrency(preferenceHelperDataSource.getWalletSettings().getCurrencySymbol());
                break;

            case 2:
                walletView.postfixCurrency(preferenceHelperDataSource.getWalletSettings().getCurrencySymbol());
                break;
        }
    }

    @Override
    public void makeCardAsDefault()
    {
        if(preferenceHelperDataSource.getDefaultCardDetails()!=null)
        {
            Utility.printLog("MyCardNumber"+preferenceHelperDataSource.getDefaultCardDetails().getLast4());
            walletView.setCard(preferenceHelperDataSource.getDefaultCardDetails().getLast4(),
                    preferenceHelperDataSource.getDefaultCardDetails().getBrand());
        }
        else
            walletView.setNoCard();
    }
}
