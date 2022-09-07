package com.karru.landing.wallet.wallet_transaction;

import android.content.Context;
import android.widget.Toast;

import com.karru.api.NetworkService;
import com.karru.landing.wallet.wallet_transaction.view.WalletTransactionActivity;
import com.heride.rider.R;
import com.google.gson.Gson;
import com.karru.ApplicationClass;
import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.landing.wallet.wallet_transaction.model.WalletTransactionsModel;
import com.karru.managers.network.NetworkStateHolder;
import com.karru.managers.network.RxNetworkObserver;
import com.karru.util.DataParser;
import com.karru.utility.Utility;
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
import static com.karru.utility.Constants.WALLET_TRANSACTION;

public class WalletTransactionPresenter implements WalletTransactionContract.WalletTransactionPresenter
{
    private final String TAG = "WalletTransProvider";

    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject Context mContext;
    @Inject WalletTransactionActivity mActivity;
    @Inject WalletTransactionContract.View trasactionView;
    @Inject NetworkService networkService;
    @Inject NetworkStateHolder networkStateHolder;
    @Inject Gson gson;
    @Inject @Named(WALLET_TRANSACTION) CompositeDisposable compositeDisposable;

    @Inject
    WalletTransactionPresenter() {}

    @Override
    public void checkRTLConversion() {
        RtlConversion(mActivity,preferenceHelperDataSource.getLanguageSettings().getCode());
    }


    /**
     * <h2>initLoadTransactions</h2>
     * <p> method to init the getTransactionsHistory() api call if network connectivity is there </p>
     */
    public void initLoadTransactions()
    {
        if( networkStateHolder.isConnected())
        {
            trasactionView.showProgressDialog(mContext.getString(R.string.pleaseWait));
            getTransactionHistory();
        }
        else
            trasactionView.showToast(mContext.getString(R.string.network_problem), Toast.LENGTH_LONG);
    }


    @Override
    public void disposeObservable() {
        compositeDisposable.clear();
    }

    /**
     * <h>get Wallet History</h>
     * <p>this method is using to get the Wallet history data</p>
     */
    private void getTransactionHistory()
    {
        if(networkStateHolder.isConnected())
        {
            Observable<Response<ResponseBody>> request = networkService.getWalletTransaction(
                    ((ApplicationClass) mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                    preferenceHelperDataSource.getLanguageSettings().getCode(),"0");

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
                            Utility.printLog(TAG+ " getWalletTrans onNext: "+value.code());
                            switch (value.code())
                            {
                                case 200:
                                    String response=DataParser.fetchSuccessResponse(value);
                                    Utility.printLog(TAG+ " getWalletTrans onNext: "+response);
                                    handleResponse(response);
                                    break;

                                case 502:
                                    trasactionView.showToast(mContext.getString(R.string.bad_gateway), Toast.LENGTH_LONG);
                                    break;

                                default:
                                    trasactionView.showToast(DataParser.fetchErrorMessage(value), Toast.LENGTH_LONG);
                                    break;
                            }
                        }
                        @Override
                        public void onError(Throwable e)
                        {
                            Utility.printLog(TAG+ "getWalletTrans error: "+e.getMessage());
                            trasactionView.hideProgressDialog();
                            trasactionView.showToast(mContext.getString(R.string.network_problem), Toast.LENGTH_LONG);
                        }

                        @Override
                        public void onComplete()
                        {
                            trasactionView.hideProgressDialog();
                        }
                    });
        }
        else
            trasactionView.showToast(mContext.getString(R.string.network_problem), Toast.LENGTH_LONG);
    }

    /**
     * <h>Response Handler</h>
     * <p>this method is using to  handle the Server Response</p>
     * @param response server response
     */
    private void handleResponse(String response)
    {
        WalletTransactionsModel walletTransactionsModel = gson.fromJson(response, WalletTransactionsModel.class);
        trasactionView.setAllTransactionsAL(walletTransactionsModel.getData().getCreditDebitArr());
        trasactionView.setCreditTransactionsAL(walletTransactionsModel.getData().getCreditArr());
        trasactionView.setDebitTransactionsAL(walletTransactionsModel.getData().getDebitArr());
        trasactionView.walletTransactionsApiSuccessViewNotifier();
    }
}
