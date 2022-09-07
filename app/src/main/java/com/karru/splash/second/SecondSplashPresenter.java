package com.karru.splash.second;

import android.content.Context;
import com.google.gson.Gson;
import com.karru.ApplicationClass;
import com.karru.api.NetworkService;
import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.managers.user_vehicles.MQTTManager;
import com.karru.landing.home.model.MQTTResponseDataModel;
import com.karru.util.DataParser;
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
import static com.karru.utility.Constants.RESPONSE_IN_API;
import static com.karru.utility.Constants.SECOND_SPLASH;

/**
 * <h1>SecondSplashPresenter</h1>
 * This method is used to calling network API
 * @author 3Embed
 * @since on 22-12-2017.
 */
public class SecondSplashPresenter implements SecondSplashContract.Presenter
{
    private static final String TAG = "SecondSplashPresenter";
    @Inject NetworkService networkService;
    @Inject MQTTManager mqttManager;
    @Inject SecondSplashActivity mActivity;
    @Inject
    @Named(SECOND_SPLASH)
    CompositeDisposable compositeDisposable;
    @Inject SecondSplashContract.View splashView;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    private Context mContext;

    @Inject
    SecondSplashPresenter(Context context)
    {
        this.mContext=context;
    }

    @Override
    public void checkRTLConversion() {
        RtlConversion(mActivity,preferenceHelperDataSource.getLanguageSettings().getCode());
    }

    @Override
    public void getVehicleDetails()
    {
        preferenceHelperDataSource.setBusinessType(2);
        if(Utility.isNetworkAvailable(mContext))
        {
            Utility.printLog(TAG+" token "+((ApplicationClass)mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid())
                    +" lang "+ preferenceHelperDataSource.getLanguageSettings().getCode()+" lat "+preferenceHelperDataSource.getCurrLatitude()
                    +" long "+preferenceHelperDataSource.getCurrLongitude()+" topic "+preferenceHelperDataSource.getMqttTopic());
            Observable<Response<ResponseBody>> request =
                    networkService.getDrivers(((ApplicationClass)mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                            preferenceHelperDataSource.getLanguageSettings().getCode(), preferenceHelperDataSource.getCurrLatitude(),
                            preferenceHelperDataSource.getCurrLongitude(), preferenceHelperDataSource.getMqttTopic(),
                            RESPONSE_IN_API,preferenceHelperDataSource.getBusinessType());
            request.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<ResponseBody>>() {
                        @Override
                        public void onSubscribe(Disposable d)
                        {
                            compositeDisposable.add(d);
                        }
                        @Override
                        public void onNext(Response<ResponseBody> result)
                        {
                            splashView.hideWaveAnimation();
                            Utility.printLog(TAG+" getVehicleDetails onNext"+result.code());
                            switch (result.code())
                            {
                                case 200:
                                    String dataResponseString =  DataParser.fetchDataObjectString(result);
                                    MQTTResponseDataModel mqttResponseDataModel = new Gson().fromJson(dataResponseString,
                                            MQTTResponseDataModel.class);
                                    Utility.printLog(TAG+" getVehicleDetails response "+dataResponseString);
                                    preferenceHelperDataSource.setVehicleDetailsResponse(dataResponseString);
                                    mqttManager.updateNewVehicleTypesData(mqttResponseDataModel);
                                    splashView.startMainActivity();
                                    break;
                                case 400:
                                    splashView.startMainActivity();
                                    break;
                            }
                        }
                        @Override
                        public void onError(Throwable errorMsg)
                        {
                            Utility.printLog(TAG + " getVehicleDetails onAddCardError " + errorMsg);
                            splashView.showWaveAnimation();
                        }
                        @Override
                        public void onComplete()
                        {
                            Utility.printLog(TAG + " getVehicleDetails onComplete ");
                        }
                    });
        }
        else
            splashView.showToast(mContext.getString(R.string.network_problem));
    }

    @Override
    public void disposeObservables() {
        compositeDisposable.clear();
    }
}
