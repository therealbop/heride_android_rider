package com.karru.authentication.login;

import android.location.Location;
import androidx.annotation.NonNull;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.gson.Gson;
import com.karru.ApplicationClass;
import com.karru.api.NetworkService;
import com.karru.managers.user_vehicles.MQTTManager;
import com.heride.rider.R;
import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.data.source.local.sqlite.SQLiteDataSource;
import com.karru.managers.location.LocationProvider;
import com.karru.managers.location.RxLocationObserver;
import com.karru.authentication.UserDetailsDataModel;
import com.karru.util.DataParser;
import com.karru.util.TextUtil;
import com.karru.utility.Constants;
import com.karru.utility.Utility;
import org.json.JSONObject;
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
import static com.karru.utility.Constants.GMT_CURRENT_LAT;
import static com.karru.utility.Constants.GMT_CURRENT_LNG;
import static com.karru.utility.Constants.LOGIN;
import static com.karru.utility.Constants.NORMAL_LOGIN;
import static com.karru.utility.Constants.PARTNER_LOGIN;

/**
 * <h1>LoginPresenter</h1>
 * This class is used to communicate between login activity and model
 * @author 3Embed
 * @since on 15/8/17.
 */
public class LoginPresenter implements LoginContract.Presenter,
        GoogleApiClient.OnConnectionFailedListener
{
    private static final String TAG = "LoginPresenter" ;

    private LoginActivity mContext;
    @Inject LoginModel loginModel;
    @Inject Gson gson;
    @Inject com.karru.util.Utility utility;
    @Inject @Named(LOGIN) CompositeDisposable compositeDisposable;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject SQLiteDataSource sqLiteDataSource;
    @Inject MQTTManager mqttManager;
    @Inject FacebookLoginHelper facebookLoginHelper;
    @Inject NetworkService networkService;
    @Inject LoginContract.View mLoginView;
    @Inject LocationProvider locationProvider;
    @Inject UserDetailsDataModel userDetailsDataModel;

    private String socialMediaId ,firstName , profilePic;
    private CallbackManager callbackManager;
    private Disposable locationDisposable;
    private GoogleApiClient mGoogleApiClient;
    private String emailId, password;
    private RxLocationObserver rxLocationObserver;

    @Inject
    LoginPresenter(LoginActivity mContext,RxLocationObserver rxLocationObserver)
    {
        this.mContext = mContext;
        this.rxLocationObserver = rxLocationObserver;

        subscribeLocationChange();
    }

    @Override
    public void checkRTLConversion() {
        RtlConversion(mContext,preferenceHelperDataSource.getLanguageSettings().getCode());
    }

    @Override
    public void normalLogin()
    {
        if(Utility.isNetworkAvailable(mContext))
        {
            userDetailsDataModel.setLoginType(1);
            loginAPI(emailId, password );
        }
        else
            mLoginView.showToast(mContext.getString(R.string.network_problem));
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
                mLoginView.hideProgressDialog();
                locationDisposable.dispose();
                compositeDisposable.add(locationDisposable);
                locationProvider.stopLocationUpdates();
                Utility.printLog(TAG+" onNext location observed  "+location.getLatitude()
                        +" "+location.getLongitude());
                preferenceHelperDataSource.setCurrLatitude(String.valueOf(location.getLatitude()));
                preferenceHelperDataSource.setCurrLongitude(String.valueOf(location.getLongitude()));
                checkForLoginType();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Utility.printLog(TAG+" onAddCardError location oberved  "+e);
            }

            @Override
            public void onComplete()
            {
            }
        });
    }

    /**
     * <h2>checkForLoginType</h2>
     * used to check login type and call API
     */
    private void checkForLoginType()
    {
        switch (loginModel.getLoginType())
        {
            case 1:
                normalLogin();
                break;

            case 2:
                handleResultFromFB(callbackManager);
                break;

            case 3:
                googleLogin();
                break;
        }
    }

    @Override
    public void checkIfLocationEnabled(String mailId, String password )
    {
        this.emailId = mailId;
        this.password = password;
        if(!preferenceHelperDataSource.getCurrLatitude().equals("") &&
                !preferenceHelperDataSource.getCurrLongitude().equals(""))
            checkForLoginType();
        else
            mLoginView.checkLocationPermission();
    }

    @Override
    public void startLocationService(String emailId,String password)
    {
        this.emailId = emailId;
        this.password =  password;
        locationProvider.startLocation(this);
    }

    @Override
    public void handleResultFromGoogle(GoogleSignInResult result)
    {
        if (result.isSuccess())
        {
            String personPhoto = "";
            GoogleSignInAccount account = result.getSignInAccount();
            if (account.getPhotoUrl() != null)
                personPhoto = account.getPhotoUrl().toString();

            userDetailsDataModel.setLoginType (3);
            userDetailsDataModel.setEmailOrPhone(account.getEmail());
            firstName = account.getDisplayName();
            socialMediaId = account.getId();
            profilePic = personPhoto;
            loginAPI(account.getEmail(), account.getId());
        }
    }

    @Override
    public void handleResultFromFB(CallbackManager callbackManager)
    {
        if(Utility.isNetworkAvailable(mContext))
        {
            facebookLoginHelper.refreshToken();
            facebookLoginHelper.facebookLogin(callbackManager, facebookLoginHelper.createFacebook_requestData(),
                    new FacebookLoginHelper.Facebook_callback() {
                        @Override
                        public void success(JSONObject json) {
                            FacebookLoginModel facebookLoginModel = gson.fromJson(json.toString(), FacebookLoginModel.class);
                            userDetailsDataModel.setLoginType(2);
                            userDetailsDataModel.setEmailOrPhone(facebookLoginModel.getEmail());
                            socialMediaId = facebookLoginModel.getId();
                            Utility.printLog("HASHID"+socialMediaId);
                            firstName = facebookLoginModel.getName();
                            profilePic = "https://graph.facebook.com/"+facebookLoginModel.getId()+"/picture?type=large";

                            loginAPI(facebookLoginModel.getEmail(), facebookLoginModel.getId());
                        }

                        @Override
                        public void error(String error) {
                            Utility.printLog("result facebook error: "+error);
                        }

                        @Override
                        public void cancel(String cancel) {
                            Utility.printLog("result facebook cancel: "+cancel);
                        }
                    });
        }
        else
            mLoginView.showToast(mContext.getString(R.string.network_problem));
    }

    @Override
    public void checkDefaultUser()
    {
        if(!preferenceHelperDataSource.getCustomerEmail().equals(""))
            mLoginView.saveUserCreds( preferenceHelperDataSource.getCustomerEmail() ,
                    preferenceHelperDataSource.getPassword());
    }

    @Override
    public void validateUserCreds(String userName, String password)
    {
        Utility.printLog(TAG+" userName "+TextUtil.isEmpty(userName)+"password"+
                TextUtil.isEmpty(password));
        if (!TextUtil.isEmpty(userName) && !TextUtil.isEmpty(password) && loginModel.isValidEmail())
            mLoginView.enableLoginBtn();
        else
            mLoginView.disableLoginBtn();
    }

    /**
     * <h2>loginAPI</h2>
     * <p> Calling login service and if success storing values in session manager and start main activity </p>
     * login_type -> 1- normal login, 2- Fb , 3-google
     * @param emailID: email id of user
     * @param password: password to login
     */
    private void loginAPI( final String emailID, final String password)
    {
        if(Utility.isNetworkAvailable(mContext))
        {
            mLoginView.showProgressDialog();
            userDetailsDataModel.setEmailOrPhone(emailID);
            userDetailsDataModel.setPassword(password);
            userDetailsDataModel.setDeviceId(utility.getDeviceId(mContext));
            userDetailsDataModel.setAppVersion(Constants.APP_VERSION);
            userDetailsDataModel.setDevMake(Constants.DEVICE_MAKER);
            userDetailsDataModel.setDevModel(Constants.DEVICE_MODEL);
            userDetailsDataModel.setDevType(Constants.DEVICE_TYPE);
            userDetailsDataModel.setLatitude(preferenceHelperDataSource.getCurrLatitude());
            userDetailsDataModel.setLongitude(preferenceHelperDataSource.getCurrLongitude());
            userDetailsDataModel.setIpAddress(utility.getLocalIpAddress());

            Utility.printLog(TAG+" model class "+userDetailsDataModel.getEmailOrPhone()+" pass"+
                    userDetailsDataModel.getPassword());
            switch (userDetailsDataModel.getLoginType())
            {
                case 2:
                    userDetailsDataModel.setFacebookId(socialMediaId);
                    break;
                case 3:
                    userDetailsDataModel.setGoogleId(socialMediaId);
                    break;
            }
            Observable<Response<ResponseBody>> request = networkService.loginAPI(preferenceHelperDataSource.getLanguageSettings().getCode(),
                    userDetailsDataModel);
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
                            Utility.printLog(TAG + " login response onNext " + result.code());
                            switch (result.code())
                            {
                                case 200:
                                    DataParser.storeUserData(result,mContext,userDetailsDataModel.getPassword(),
                                            preferenceHelperDataSource,sqLiteDataSource,mqttManager);
                                    switch (userDetailsDataModel.getLoginType())
                                    {
                                        case 1:
                                            preferenceHelperDataSource.setCustomerEmail(emailID);
                                            preferenceHelperDataSource.setPassword(password);
                                            break;
                                    }
                                    ((ApplicationClass)mContext.getApplicationContext()).getConfigurations(false);

                                    mLoginView.onLoginSuccessful();
                                    break;

                                case 404:
                                    switch (userDetailsDataModel.getLoginType())
                                    {
                                        case 1:
                                            mLoginView.showErrorFromAPI(DataParser.fetchErrorMessage(result));
                                            break;

                                        default:
                                            mLoginView.onUserNotLoggedIn(firstName,
                                                    userDetailsDataModel.getEmailOrPhone(), profilePic,
                                                    userDetailsDataModel.getLoginType(), socialMediaId);
                                            socialMediaId = "";
                                            userDetailsDataModel.setLoginType(1);
                                            break;
                                    }
                                    break;

                                case 502:
                                    mLoginView.showToast(mContext.getString(R.string.bad_gateway));
                                    break;

                                default:
                                    mLoginView.showErrorFromAPI(DataParser.fetchErrorMessage(result));
                                    break;
                            }
                        }
                        @Override
                        public void onError(Throwable errorMsg)
                        {
                            Utility.printLog(TAG + " login response onAddCardError " + errorMsg);
                            mLoginView.hideProgressDialog();
                            mLoginView.showToast(mContext.getString(R.string.network_problem));
                        }

                        @Override
                        public void onComplete()
                        {
                            Utility.printLog(TAG + " login response onComplete ");
                            mLoginView.hideProgressDialog();
                        }
                    });
        }
        else
            mLoginView.showToast(mContext.getString(R.string.network_problem));
    }

    @Override
    public void disposeObservable()
    {
        compositeDisposable.clear();
    }

    @Override
    public void checkLoginType(int loginType) {
        switch (loginType)
        {
            case PARTNER_LOGIN:
//                userDetailsDataModel.setIsPartnerUser("1");
                mLoginView.showPartnerUI();
                break;

            case NORMAL_LOGIN:
//                userDetailsDataModel.setIsPartnerUser("0");
                break;
        }
    }

    @Override
    public void googleLogin()
    {
        if(mGoogleApiClient.isConnected())
        {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(status -> {});
            mLoginView.openGoogleActivity(Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient));
        }
    }

    @Override
    public void initializeFBGoogle(CallbackManager callbackManager)
    {
        this.callbackManager = callbackManager;
        FacebookSdk.sdkInitialize(mContext.getApplicationContext());
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder
                (GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .enableAutoManage(mContext, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();
    }

    public void isEmailError(String userName)
    {
        loginModel.validateUserName(userName);
    }
    @Override
    public void validateUserName(String userName) {
        switch (loginModel.validateUserName(userName))
        {
            case 1:
                mLoginView.invalidPhoneNumber();
                break;
            case 2:
                mLoginView.invalidEmailId();
                break;
        }
    }

    @Override
    public void validateNonEmptyPass(String password)
    {
        if(TextUtil.isEmpty(password))
            mLoginView.emptyPassword();
        else
            mLoginView.nonEmptyPassword();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {
        Utility.printLog(TAG+ " onConnectionFailed:" + connectionResult);
    }

    @Override
    public void initializeFacebook()
    {
        facebookLoginHelper.initializeFacebookSdk(mContext);
    }

    @Override
    public void storeLoginType(int loginType)
    {
        loginModel.setLoginType(loginType);
    }

    @Override
    public void onLocationServiceDisabled(Status status) {
        mLoginView.promptUserWithLocationAlert(status);
    }
}
