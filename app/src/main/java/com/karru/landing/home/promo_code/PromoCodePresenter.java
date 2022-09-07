package com.karru.landing.home.promo_code;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.karru.ApplicationClass;
import com.karru.api.NetworkService;
import com.karru.booking_flow.invoice.model.ReceiptDetails;
import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.data.source.local.sqlite.SQLiteDataSource;
import com.karru.landing.home.model.HomeActivityModel;
import com.karru.landing.home.model.fare_estimate_model.FareEstimateModel;
import com.karru.landing.home.model.promo_code_model.PromoCodeDataModel;
import com.karru.landing.home.model.promo_code_model.PromoCodeModel;
import com.karru.managers.network.NetworkStateHolder;
import com.karru.managers.user_vehicles.MQTTManager;
import com.karru.util.DataParser;
import com.karru.util.ExpireSession;
import com.karru.utility.Utility;
import com.heride.rider.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
import static com.karru.utility.Constants.PROMO_ACTIVITY;

public class PromoCodePresenter implements PromoCodeContract.Presenter {


    public static final String TAG = "PromoCodePresenter";
    @Inject NetworkStateHolder networkStateHolder;
    @Inject NetworkService networkService;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject HomeActivityModel mHomActivityModel;
    @Inject Context mContext;
    @Inject PromoCodeActivity mActivity;
    @Inject MQTTManager mqttManager;
    @Inject SQLiteDataSource addressDataSource;
    @Inject Gson gson;
    @Inject PromoCodeContract.View promoCodeView;
    @Inject ArrayList<PromoCodeDataModel> promoCodeDataModels;
    @Inject com.karru.util.Utility utility;
    private com.karru.landing.home.model.promo_code_model.PromoCodeModel promoCodeModel;
    private ArrayList<PromoCodeDataModel> promoCodeDataModel;
    @Inject @Named(PROMO_ACTIVITY) CompositeDisposable compositeDisposable;
    private ArrayList<ReceiptDetails> listOfBreakDown;
    private FareEstimateModel fareEstimateModel;

    @Inject
    public PromoCodePresenter() {
        fareEstimateModel = FareEstimateModel.getInstance();
    }

    @Override
    public void checkRTLConversion() {
        RtlConversion(mActivity,preferenceHelperDataSource.getLanguageSettings().getCode());
    }


    //
    @Override
    public void getPromoCodeData() {
        if(networkStateHolder.isConnected())
        {
            promoCodeView.showProgressDialog();
            Observable<Response<ResponseBody>> request = networkService.getPromoCodeData(((ApplicationClass)mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                            preferenceHelperDataSource.getLanguageSettings().getCode(), Double.parseDouble(preferenceHelperDataSource.getCurrLatitude()),Double.parseDouble(preferenceHelperDataSource.getCurrLongitude()));
            request.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<ResponseBody>>() {
                        @Override
                        public void onSubscribe(Disposable d)
                        {
                            compositeDisposable.add(d);
                        }
                        @Override
                        public void onNext(retrofit2.Response<ResponseBody> result)
                        {
                           // Utility.printLog(TAG+" addFavAddress onNext"+result.code());
                            switch (result.code())
                            {
                                case 200:

                                    promoCodeDataModels.clear();
                                    Gson gson = new Gson();
                                    try {
                                        String promoData =DataParser.fetchSuccessResponse(result);
                                        //DataParser.fetchSuccessResponse(result);
                                        promoCodeModel = gson.fromJson(promoData, PromoCodeModel.class);
                                        promoCodeDataModel = promoCodeModel.getData();
                                        if (promoCodeDataModel.size() > 0)
                                        {
                                            promoCodeDataModels.addAll(promoCodeDataModel);
                                            promoCodeView.setPromoCodeList(promoCodeDataModel);
                                            //Utility.printLog("activeOrderData" + promoCodeDataModel.get(0));
                                        }
                                    } catch ( NullPointerException | JsonSyntaxException e) {
                                        e.printStackTrace();
                                    }
                                    break;

                                case 502:
                                    promoCodeView.showToast(mContext.getString(R.string.bad_gateway));
                                    break;

                                default:
                                    promoCodeView.showToast(DataParser.fetchErrorMessage(result));
                                    break;
                            }
                        }
                        @Override
                        public void onError(Throwable errorMsg)
                        {
                           // Utility.printLog(TAG + " addFavAddress onAddCardError " + errorMsg);
                            promoCodeView.dismissProgressDialog();
                            promoCodeView.showToast(mContext.getString(R.string.network_problem));
                        }
                        @Override
                        public void onComplete()
                        {
                            promoCodeView.dismissProgressDialog();
                           // Utility.printLog(TAG + " addFavAddress onComplete ");
                        }
                    });
        }
        else
            promoCodeView.showToast(mContext.getString(R.string.network_problem));

    }



    @Override
    public void validatePromoCode(String promoCode)
    {
        if(networkStateHolder.isConnected())
        {
            promoCodeView.showProgressDialog();
            /*Utility.printLog(TAG+" validatePromoCode payment "+mHomActivityModel.getPaymentType()+" "+
            mHomActivityModel.getPayByWallet();*/
            Observable<retrofit2.Response<ResponseBody>> request =
                    networkService.validatePromo(((ApplicationClass)mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                            preferenceHelperDataSource.getLanguageSettings().getCode(),
                            mHomActivityModel.getMapCenterLatitude(),mHomActivityModel.getMapCenterLongitude(),
                            Double.parseDouble(mHomActivityModel.getAmount()),mHomActivityModel.getSelectedVehicleId(),
                            mHomActivityModel.getPaymentType(),mHomActivityModel.getPayByWallet(), promoCode);
            request.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<retrofit2.Response<ResponseBody>>() {
                        @Override
                        public void onSubscribe(Disposable d)
                        {
                            compositeDisposable.add(d);
                        }
                        @Override
                        public void onNext(retrofit2.Response<ResponseBody> result)
                        {
                            Utility.printLog(TAG+" validatePromoCode onNext"+result.code());
                            switch (result.code())
                            {
                                case 401:
                                    ExpireSession.refreshApplication(mContext,mqttManager,
                                            preferenceHelperDataSource,addressDataSource);
                                    break;

                                case 200:
                                    String response;
                                    try
                                    {
                                        response = result.body().string();
                                        //Utility.printLog(" data string "+response);
                                        JSONObject object = new JSONObject(response);
                                        String successResponse =  object.getJSONObject("data").toString();
                                        String successMessage =  object.getString("message");
                                        com.karru.landing.home.model.PromoCodeModel promoCodeModel = gson.fromJson(successResponse, com.karru.landing.home.model.PromoCodeModel.class);
                                        //Utility.printLog(" validatePromoCode promo id "+promoCodeModel.getPromoId());
                                        mHomActivityModel.setPromoCodeModel(promoCodeModel);
                                        if(fareEstimateModel!=null)
                                        {
                                            fareEstimateModel.setPromoCodeApplied(true);
                                            //Utility.printLog("PromoCodeApplied "+fareEstimateModel.isPromoCodeApplied());
                                            fareEstimateModel.setPromoCodeData(promoCodeModel);
                                            promoCodeView.fareEstimate(true);
                                        }
                                        promoCodeView.validPromo(promoCode);
                                        promoCodeView.showToast(successMessage);
                                    } catch (IOException e)
                                    {
                                        Utility.printLog(" validatePromoCode error "+e);
                                        e.printStackTrace();
                                    } catch (JSONException e)
                                    {
                                        Utility.printLog(" validatePromoCode error "+e);
                                        e.printStackTrace();
                                    }
                                    break;

                                case 502:
                                    mHomActivityModel.setPromoCodeModel(null);
                                    promoCodeView.invalidPromo(mContext.getString(R.string.bad_gateway));
                                    break;

                                default:
                                    mHomActivityModel.setPromoCodeModel(null);
                                    promoCodeView.invalidPromo(DataParser.fetchErrorMessage(result));
                                    break;
                            }
                        }
                        @Override
                        public void onError(Throwable errorMsg)
                        {
                            mHomActivityModel.setPromoCodeModel(null);
                            //Utility.printLog(TAG + " validatePromoCode onError " + errorMsg);
                            promoCodeView.invalidPromo(mContext.getString(R.string.network_problem));
                        }

                        @Override
                        public void onComplete()
                        {
                            promoCodeView.dismissProgressDialog();
                            //Utility.printLog(TAG + " validatePromoCode onComplete ");
                        }
                    });
        }
        else
        {
            promoCodeView.showToast(mContext.getString(R.string.network_problem));
        }

    }



}

