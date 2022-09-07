package com.karru.splash.first;

import android.location.Location;

import com.google.android.gms.common.api.Status;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.karru.ApplicationClass;
import com.karru.ApplicationVersion;
import com.karru.RxAppVersionObserver;
import com.karru.api.NetworkService;
import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.data.source.local.sqlite.SQLiteDataSource;
import com.karru.landing.home.model.MQTTResponseDataModel;
import com.karru.managers.location.LocationProvider;
import com.karru.managers.location.RxLocationObserver;
import com.karru.managers.user_vehicles.MQTTManager;
import com.karru.util.DataParser;
import com.karru.util.ExpireSession;
import com.karru.util.Validation;
import com.karru.utility.Utility;
import com.heride.rider.R;

import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static com.karru.util.Utility.RtlConversion;
import static com.karru.utility.Constants.APP_VERSION;
import static com.karru.utility.Constants.GMT_CURRENT_LAT;
import static com.karru.utility.Constants.GMT_CURRENT_LNG;
import static com.karru.utility.Constants.LANGUAGE;
import static com.karru.utility.Constants.RESPONSE_IN_API;
import static com.karru.utility.Constants.SPLASH;
import static com.karru.utility.Constants.isToUpdateAlertVisible;

/**
 * <h1>SplashPresenter</h1>
 * This class is used as presenter for splash screen
 * @author Akbar.
 * @since 07-11-2017
 */
public class SplashPresenter implements SplashContract.Presenter
{
    private static final String TAG = "SplashPresenter";

    @Inject SplashActivity mContext;
    @Inject NetworkService networkService;
    @Inject SplashContract.View splashView;
    @Inject @Named(SPLASH) CompositeDisposable compositeDisposable;
    @Inject LocationProvider locationProvider;
    @Inject MQTTManager mqttManager;
    @Inject @Named(LANGUAGE) ArrayList<LanguagesList> languagesLists = new ArrayList<>();
    @Inject SQLiteDataSource addressDataSource;
    @Inject PreferenceHelperDataSource preferencesHelperData;

    private ApplicationVersion applicationVersion;
    private RxLocationObserver rxLocationObserver;
    private RxAppVersionObserver rxAppVersionObserver;
    private Disposable locationDisposable,appVersionDispose;

    /**
     * <h2>SplashPresenter</h2>
     * This is contructor injected from dagger with
     * This is contructor injected from dagger with
     */
    @Inject
    SplashPresenter(SplashActivity mContext,RxLocationObserver rxLocationObserver,
                    RxAppVersionObserver rxAppVersionObserver,ApplicationVersion applicationVersion)
    {
        this.mContext = mContext;
        this.mContext = mContext;
        this.applicationVersion = applicationVersion;
        this.rxAppVersionObserver = rxAppVersionObserver;
        this.rxLocationObserver = rxLocationObserver;
        subscribeLocationChange();
        Utility.printLog(TAG+"context check "+mContext.getString(R.string.email));
    }

    @Override
    public void checkRTLConversion() {
        RtlConversion(mContext,preferencesHelperData.getLanguageSettings().getCode());
    }

    /**
     * <h2>subscribeLocationChange</h2>
     * This method is used to subscribe for the location change
     */
    private void subscribeLocationChange()
    {
        rxLocationObserver.subscribeOn(Schedulers.io());
        rxLocationObserver.observeOn(AndroidSchedulers.mainThread());
        locationDisposable = rxLocationObserver.subscribeWith( new DisposableObserver<Location>()
        {
            @Override
            public void onNext(Location location)
            {
                GMT_CURRENT_LAT = location.getLatitude()+"";
                GMT_CURRENT_LNG = location.getLongitude()+"";
                Utility.printLog(TAG+" onUpdateLocation success lat: "+location.getLatitude()+
                        "   " + "lng: "+location.getLongitude());
                callAPIIfLocationUpdate(location);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Utility.printLog(TAG+" onAddCardError location oberved  "+e);
            }

            @Override
            public void onComplete()
            {
                compositeDisposable.add(locationDisposable);
            }
        });
    }

    @Override
    public void generateFCMPushToken()
    {
        String token = FirebaseInstanceId.getInstance().getToken();
        preferencesHelperData.setFCMRegistrationId(token);
    }

    /**
     * <h2>subscribeAppVersionConfig</h2>
     * This method is used to subscribe to the app verions settings
     */
    private void subscribeAppVersionConfig()
    {
        rxAppVersionObserver.subscribeOn(Schedulers.io());
        rxAppVersionObserver.observeOn(AndroidSchedulers.mainThread());
        appVersionDispose = rxAppVersionObserver.subscribeWith( new DisposableObserver<ApplicationVersion>()
        {
            @Override
            public void onNext(ApplicationVersion applicationVersion)
            {
                Utility.printLog(TAG+" subscribeAppVersionConfig observed  "+
                        applicationVersion.getCurrenctAppVersion()+" "+applicationVersion.isMandatoryUpdateEnable());
                checkForAppVersion();
            }

            @Override
            public void onError(Throwable e)
            {
                e.printStackTrace();
                Utility.printLog(TAG+"  subscribeAppVersionConfig onError  "+e);
            }

            @Override
            public void onComplete() {
                compositeDisposable.add(appVersionDispose);
            }
        });
    }

    /**
     * <h2>checkForAppVersion</h2>
     * used to check for app version and show popup
     */
    private void checkForAppVersion()
    {
        Utility.printLog(TAG+" checkForAppVersion "+applicationVersion.getCurrenctAppVersion()+" "
                + applicationVersion.isMandatoryUpdateEnable());
        if (!isToUpdateAlertVisible && Validation.updateAppVersion
                (APP_VERSION, applicationVersion.getCurrenctAppVersion()))
        {
            isToUpdateAlertVisible = true;
            splashView.showAppUpdateAlert(applicationVersion.isMandatoryUpdateEnable());
        }
        else
            splashView.startLocationService();
    }

    /**
     * <h2>getVehicleDetails</h2>
     * This method is used to call the API to get vehicle details
     */
    private void getVehicleDetails()
    {
        preferencesHelperData.setBusinessType(2);
        if (preferencesHelperData.isLoggedIn())
        {
            Observable<Response<ResponseBody>> request =
                    networkService.getDrivers(((ApplicationClass)mContext.getApplicationContext()).getAuthToken(preferencesHelperData.getSid()),
                            preferencesHelperData.getLanguageSettings().getCode(), preferencesHelperData.getCurrLatitude(),
                            preferencesHelperData.getCurrLongitude(),preferencesHelperData.getMqttTopic(),
                            RESPONSE_IN_API,preferencesHelperData.getBusinessType());

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
                            Utility.printLog(TAG+" getVehicleDetails "+result.code());
                            switch (result.code())
                            {
                                case 200:
                                    String dataResponseString =  DataParser.fetchDataObjectString(result);
                                    MQTTResponseDataModel mqttResponseDataModel = new Gson().fromJson(dataResponseString,
                                            MQTTResponseDataModel.class);
                                    Utility.printLog(TAG+" getVehicleDetails response "+dataResponseString);
                                    preferencesHelperData.setVehicleDetailsResponse(dataResponseString);
                                    mqttManager.updateNewVehicleTypesData(mqttResponseDataModel);
                                    splashView.onGettingOfVehicleDetails();
                                    break;

                                case 401:
                                    ExpireSession.refreshApplication(mContext,mqttManager,preferencesHelperData,addressDataSource);
                                    break;
                            }
                        }
                        @Override
                        public void onError(Throwable errorMsg)
                        {
                            Utility.printLog(TAG + " getVehicleDetails onAddCardError " + errorMsg);
                            splashView.showToast(mContext.getString(R.string.network_problem));
                        }
                        @Override
                        public void onComplete()
                        {
                            Utility.printLog(TAG + " getVehicleDetails onComplete ");
                        }
                    });
        }
    }

    @Override
    public void getLanguages()
    {
        Observable<Response<LanguagesListModel>> request = networkService.getLanguages();
        request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<LanguagesListModel>>() {
                    @Override
                    public void onSubscribe(Disposable d)
                    {
                        compositeDisposable.add(d);
                    }
                    @Override
                    public void onNext(Response<LanguagesListModel> result)
                    {
                        Utility.printLog(TAG+" getLanguages "+result.code());
                        switch (result.code())
                        {
                            case 200:
                                LanguagesListModel languagesListModel = result.body();
                                languagesLists.clear();
                                languagesLists.addAll(languagesListModel.getData());
                                boolean isLanguage = false;
                                for(LanguagesList languagesList : languagesLists)
                                {
                                    if(preferencesHelperData.getLanguageSettings().getCode().equals(languagesList.getCode()))
                                    {
                                        isLanguage = true;
                                        splashView.showLanguagesDialog(languagesLists.indexOf(languagesList));
                                        break;
                                    }
                                }
                                if(!isLanguage)
                                    splashView.showLanguagesDialog(-1);
                                break;

                            case 502:
                                splashView.showToast(mContext.getString(R.string.bad_gateway));
                                break;

                            default:
                                try
                                {
                                    splashView.showToast(DataParser.fetchErrorMessage1(result.errorBody().string()));
                                } catch (IOException e)
                                {
                                    e.printStackTrace();
                                }
                                break;
                        }
                    }
                    @Override
                    public void onError(Throwable errorMsg)
                    {
                        Utility.printLog(TAG + " getLanguages onError " + errorMsg);
                        splashView.showToast(mContext.getString(R.string.network_problem));
                    }
                    @Override
                    public void onComplete()
                    {
                        Utility.printLog(TAG + " getLanguages onComplete ");
                    }
                });
    }

    @Override
    public int checkForDirection()
    {
        return preferencesHelperData.getLanguageSettings().getLangDirection();
    }

    @Override
    public void changeLanguage(String langCode,String langName, int isRTL)
    {
        preferencesHelperData.setLanguageSettings(new LanguagesList(langCode,langName,isRTL));
        splashView.setLanguage(langName,true);
    }

    @Override
    public void callAPIIfLocationUpdate(Location location)
    {
        preferencesHelperData.setCurrLatitude(String.valueOf(location.getLatitude()));
        preferencesHelperData.setCurrLongitude(String.valueOf(location.getLongitude()));

        if(Utility.isNetworkAvailable(mContext))
        {
            Utility.printLog(TAG+" user logged in "+preferencesHelperData.isLoggedIn());
            getVehicleDetails();
        }
        else
            splashView.showToast(mContext.getString(R.string.network_problem));
    }

    @Override
    public void checkForUserLoggedIn()
    {
        splashView.setLanguage(preferencesHelperData.getLanguageSettings().getName(),false);
        if(preferencesHelperData.isLoggedIn())
            subscribeAppVersionConfig();
        else
            splashView.ifUserNotRegistered();
    }

    @Override
    public void startLocationService()
    {
        locationProvider.startLocation(this);
    }

    @Override
    public void stopLocationUpdate()
    {
        locationProvider.stopLocationUpdates();
    }

    @Override
    public void disposeObservables()
    {
        Utility.printLog(TAG+" dispose observable ");
        stopLocationUpdate();
        compositeDisposable.clear();
    }

    @Override
    public void onLocationServiceDisabled(Status status) {
        splashView.promptUserWithLocationAlert(status);
    }
}
