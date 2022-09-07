package com.karru;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.util.Log;
import androidx.appcompat.app.AppCompatDelegate;
import android.widget.Toast;
import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.google.gson.Gson;
import com.karru.api.NetworkService;
import com.karru.dagger.AppComponent;
import com.karru.dagger.DaggerAppComponent;
import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.data.source.local.sqlite.SQLiteDataSource;
import com.karru.managers.account.AccountGeneral;
import com.karru.managers.network.ConnectivityReceiver;
import com.karru.managers.network.NetworkStateHolder;
import com.karru.managers.network.RxNetworkObserver;
import com.karru.managers.user_vehicles.MQTTManager;
import com.karru.splash.first.LanguagesList;
import com.karru.util.Alerts;
import com.karru.util.DataParser;
import com.karru.util.ExpireSession;
import com.karru.utility.IsForeground;
import com.karru.utility.Utility;
import com.heride.rider.R;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import androidx.multidex.MultiDex;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import static com.karru.utility.Constants.DEVICE_TYPE;
import static com.karru.utility.Constants.IS_APP_BACKGROUND;

/**
 * <h>ApplicationClass</h>
 * <p>
 * Class to get the application class to
 * handle application level apis and
 * </P>
 *
 * @since 12/8/16.
 */
public class ApplicationClass extends DaggerApplication
{
    private static final String TAG = "ApplicationClass";
    private static AccountManager mAccountManager;

    @Inject NetworkService apiService;
    @Inject RxConfigCalled rxConfigCalled;
    @Inject Alerts alerts;
    @Inject NetworkStateHolder networkStateHolder;
    @Inject RxNetworkObserver rxNetworkObserver;
    @Inject MQTTManager mqttManager;
    @Inject ApplicationVersion applicationVersion;
    @Inject SQLiteDataSource addressDataSource;
    @Inject RxAppVersionObserver rxAppVersionObserver;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;

    private CompositeDisposable compositeDisposable;
    private static ApplicationClass mInstance;

    @Override
    public void onCreate()
    {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        super.onCreate();
        mInstance = this;
        compositeDisposable = new CompositeDisposable();
        mAccountManager = AccountManager.get(getApplicationContext());
        IsForeground.init(this);

        AppsFlyerConversionListener conversionDataListener =
                new AppsFlyerConversionListener()
                {

                    @Override
                    public void onInstallConversionDataLoaded(Map<String, String> map)
                    {
                        for (String attrName : map.keySet()) {
                            Utility.printLog(AppsFlyerLib.LOG_TAG+ "attribute: " + attrName + " = " + map.get(attrName));
                        }
                    }

                    @Override
                    public void onInstallConversionFailure(String s) {
                        Utility.printLog(AppsFlyerLib.LOG_TAG+ "error getting conversion data: " + s);
                    }

                    @Override
                    public void onAppOpenAttribution(Map<String, String> map) {

                    }

                    @Override
                    public void onAttributionFailure(String s) {
                        Utility.printLog(AppsFlyerLib.LOG_TAG+ "error onAttributionFailure : " + s);
                    }
                };
        AppsFlyerLib.getInstance().init(getString(R.string.AF_DEV_KEY), conversionDataListener, getApplicationContext());
        AppsFlyerLib.getInstance().startTracking(this);

        Utility.printLog(TAG+" preferenceHelperDataSource.getLanguageSettings() "+
                preferenceHelperDataSource.getLanguageSettings());
        if (preferenceHelperDataSource.getLanguageSettings() == null)
            preferenceHelperDataSource.setLanguageSettings(new LanguagesList("en","English", 0));
        changeLangConfig();

        IsForeground.get(this).addListener(new IsForeground.Listener()
        {
            @Override
            public void onBecameForeground()
            {
                IS_APP_BACKGROUND = false;
                if (preferenceHelperDataSource.isLoggedIn())
                {
                    mqttManager.createMQttConnection(new com.karru.util.Utility().getDeviceId(getApplicationContext())+"_"+
                            preferenceHelperDataSource.getSid());
                    getConfigurations(true);
                }
            }

            @Override
            public void onBecameBackground()
            {
                IS_APP_BACKGROUND = true;
                if(mqttManager.isMQTTConnected())
                {
                    mqttManager.unSubscribeToTopic(preferenceHelperDataSource.getMqttTopic());
                    mqttManager.disconnect();
                }
                compositeDisposable.clear();
            }
        });
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener)
    {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

    /**
     * <h2>changeLangConfig</h2>
     * used to change the language configuration
     */
    public void changeLangConfig()
    {
        com.karru.util.Utility.changeLanguageConfig(preferenceHelperDataSource.getLanguageSettings()
                .getCode(),this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Utility.printLog(TAG+" langguage change "+newConfig.locale);
    }

    public static synchronized ApplicationClass getInstance() {
        return mInstance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        AppComponent appComponent = DaggerAppComponent.builder().application(this).build();
        appComponent.inject(this);
        return appComponent;
    }

    /**
     * <h2>addAccount</h2>
     * This method is used to set the auth token by creating the account manager with the account
     *
     * @param emailID email ID to be added
     */
    public void setAuthToken(String emailID, String password, String authToken) {
        Account account = new Account(emailID, AccountGeneral.ACCOUNT_TYPE);
        mAccountManager.addAccountExplicitly(account, password, null);
        mAccountManager.setAuthToken(account, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS, authToken);
    }

    /**
     * <h2>getAuthToken</h2>
     * This method is used to get the auth token from the created account
     *
     * @return auth token stored
     */
    public String getAuthToken(String emailID) {
        Account[] account = mAccountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE);
        List<Account> accounts = Arrays.asList(account);
        Utility.printLog(TAG + "auth token from size " + accounts.size() + " " + emailID);
        if (accounts.size() > 0) {
            for (int i = 0; i < accounts.size(); i++) {
                Utility.printLog(TAG + "auth token from size " + accounts.get(i).name);
                if (accounts.get(i).name.equals(emailID))
                    return mAccountManager.peekAuthToken(accounts.get(i), AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS);
                else
                    removeAccount(accounts.get(i).name);
            }
        }
        return null;
    }

    /**
     * <h2>removeAccount</h2>
     * This method is used to remove the account stored
     *
     * @param emailID email ID of the account
     */
    public void removeAccount(String emailID)
    {
        Account[] account = mAccountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE);
        List<Account> accounts = Arrays.asList(account);
        Utility.printLog(TAG + "auth token from size " + accounts.size() + " " + emailID);
        if (accounts.size() > 0) {
            for (int i = 0; i < accounts.size(); i++)
            {
                if (accounts.get(i).name.equals(emailID))
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1)
                        Utility.printLog("account removed " + mAccountManager.removeAccountExplicitly(accounts.get(i)));
                }
            }
        }
    }

    /**
     * <h2>getConfigurations</h2>
     * This method is used to call API to get the configurations
     *
     * @param isToRestartMQTT boolean is to whether to restart MQTT
     */
    public void getConfigurations(final boolean isToRestartMQTT)
    {
        Observable<Response<ConfigResponseModel>> request = apiService.getConfigurations(
                getAuthToken(preferenceHelperDataSource.getSid()),
                preferenceHelperDataSource.getLanguageSettings().getCode(), DEVICE_TYPE);
        request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ConfigResponseModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Response<ConfigResponseModel> result)
                    {
                        switch (result.code())
                        {
                            case 401:
                            case 404:
                            case 500:
                                try
                                {
                                    Toast.makeText(getApplicationContext(), DataParser.fetchErrorMessage1(result.errorBody().string()), Toast.LENGTH_LONG).show();
                                    ExpireSession.refreshApplication(getApplicationContext(),mqttManager,
                                            preferenceHelperDataSource,addressDataSource);
                                }
                                catch (IOException e)
                                {
                                    e.printStackTrace();
                                }
                                break;

                            case 200:
                                ConfigurationDataModel configurationDataModel = result.body().getData();
                                Log.d("Response","Resonse : "+new Gson().toJson(result.body().toString()));
                                if(configurationDataModel.getCustomerApiInterval()>0)
                                    preferenceHelperDataSource.setCustomerApiInterval(configurationDataModel.getCustomerApiInterval());
                                preferenceHelperDataSource.setLaterBookingBufferTime(configurationDataModel.getRideLaterBookingAppBuffer());
                                preferenceHelperDataSource.setStripeKey(configurationDataModel.getStripePublishKey());
                                preferenceHelperDataSource.setGoogleServerKeys(configurationDataModel.getCustomerGooglePlaceKeys());
                                preferenceHelperDataSource.setEmergencyContactLimit(configurationDataModel.getCustomerEmergencyContactLimit());
                                preferenceHelperDataSource.setTWILIOCallEnable(configurationDataModel.isTwillioCallingEnable());
                                preferenceHelperDataSource.setReferralCodeEnabled(configurationDataModel.isReferralCodeEnable());
                                preferenceHelperDataSource.setChatModuleEnable(configurationDataModel.isChatModuleEnable());
                                preferenceHelperDataSource.setHelpModuleEnable(configurationDataModel.getHelpCenterEnable());

                                //   if (configurationDataModel.getCustomerGooglePlaceKeys().size() > 0)
                                // preferenceHelperDataSource.setGoogleServerKey(configurationDataModel.getCustomerGooglePlaceKeys().get(0));
                                preferenceHelperDataSource.setGoogleServerKey(configurationDataModel.getGoogleMapKey());
                                // Log.d("Test","Google Key From Server: "+configurationDataModel.getGoogleMapKey());
                                Utility.printLog(TAG+ " ETA TEST current API key  " + preferenceHelperDataSource.getGoogleServerKey());

                                applicationVersion.setCurrenctAppVersion(configurationDataModel.getAppVersion());


                                applicationVersion.setCurrenctAppVersion(configurationDataModel.getAppVersion());
                                applicationVersion.setMandatoryUpdateEnable(configurationDataModel.isMandatoryUpdateEnable());
                                if(isToRestartMQTT)
                                    rxAppVersionObserver.publishApplicationVersion(applicationVersion);
                                rxConfigCalled.publishStatusOfConfig(true);
                                break;

                            case 502:
                                Toast.makeText(getApplicationContext(), ApplicationClass.this.getString(R.string.bad_gateway), Toast.LENGTH_LONG).show();
                                break;

                            default:
                                try
                                {
                                    Toast.makeText(getApplicationContext(), DataParser.fetchErrorMessage1(result.errorBody().string()), Toast.LENGTH_LONG).show();
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
                        Utility.printLog(TAG + " getConfigurations onAddCardError " + errorMsg);
                    }

                    @Override
                    public void onComplete()
                    {
                        Utility.printLog(TAG + " getConfigurations onComplete ");
                    }
                });
    }
}
