package com.karru.authentication.signup;

import android.content.Context;
import android.location.Location;
import android.net.Uri;
import androidx.core.content.ContextCompat;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import com.google.android.gms.common.api.Status;
import com.karru.api.NetworkService;
import com.karru.authentication.UserDetailsDataModel;
import com.karru.countrypic.Country;
import com.karru.countrypic.CountryPicker;
import com.heride.rider.R;
import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.util.image_upload.ImageOperationInterface;
import com.karru.util.image_upload.MediaInterface;
import com.karru.util.image_upload.ResultInterface;
import com.karru.managers.location.LocationProvider;
import com.karru.managers.location.RxLocationObserver;
import com.karru.util.image_upload.AmazonUpload;
import com.karru.util.DataParser;
import com.karru.util.image_upload.ImageOperation;
import com.karru.util.TextUtil;
import com.karru.utility.Constants;
import com.karru.utility.Utility;
import org.json.JSONObject;
import java.io.File;

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

import static android.app.Activity.RESULT_OK;
import static com.karru.util.DataParser.stringToJsonAndPublish;
import static com.karru.util.Utility.RtlConversion;
import static com.karru.utility.Constants.AMAZON_PROFILE_FOLDER;
import static com.karru.utility.Constants.AMAZON_PROFILE_PATH;
import static com.karru.utility.Constants.REGISTER;

/**
 * <h1>SignUpPresenter</h1>
 * <h4>This is a Controller class for SignUp Activity</h4>
 * This class is used for performing the business logic for our Activity and
 * this class is getting called from SignUp Activity and give a call to SignUpPresenter class.
 * @version 1.0
 * @since 17/08/17
 */

public class SignUpPresenter implements SignUpContract.Presenter
{
    private static final String TAG = "SignUpPresenter";

    @Inject SignUpActivity mActivity;
    @Inject SignUpModel mSignUpModel;
    @Inject com.karru.util.Utility utility;
    @Inject NetworkService networkService;
    @Inject ImageOperation mImageOperation;
    @Inject @Named(REGISTER) CompositeDisposable compositeDisposable;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject SignUpContract.View signUpView;
    @Inject UserDetailsDataModel userDetailsDataModel ;
    @Inject Context mContext;
    @Inject LocationProvider locationProvider;
    @Inject AmazonUpload amazonImageUpload;

    private String amazonFileName;
    private Disposable locationDisposable,networkDisposable;
    private RxLocationObserver rxLocationObserver;
    private boolean isBuisness,isIndividual;

    @Inject
    SignUpPresenter(SignUpActivity signUpActivity,RxLocationObserver rxLocationObserver)
    {
        mActivity=signUpActivity;
        this.rxLocationObserver=rxLocationObserver;
        subscribeLocationChange();
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
                signUpView.hideProgressDialog();
                locationDisposable.dispose();
                locationProvider.stopLocationUpdates();
                Utility.printLog(TAG+" onNext location observed  "+location.getLatitude()
                        +" "+location.getLongitude());
                /**
                 * JIRA id : KAC-77
                 * @author Akbar
                 * @since 24-10-2018
                 */
                if(preferenceHelperDataSource.getCurrLatitude().equals("") &&
                        preferenceHelperDataSource.getCurrLongitude().equals(""))
                {
                    preferenceHelperDataSource.setCurrLatitude(String.valueOf(location.getLatitude()));
                    preferenceHelperDataSource.setCurrLongitude(String.valueOf(location.getLongitude()));
                    registerUser();
                }
            }

            @Override
            public void onError(Throwable e)
            {
                e.printStackTrace();
                Utility.printLog(TAG+" onAddCardError location oberved  "+e);
            }

            @Override
            public void onComplete()
            {
            }
        });
    }

    @Override
    public void validateAllFieldsFlags()
    {
        if(mSignUpModel.isFullNameValid() && mSignUpModel.isPhoneValid() && mSignUpModel.isEmailValid()
                && mSignUpModel.isPasswordValid())
        {
            if (!mSignUpModel.isBusinessAccount())
            {
                if(isIndividual)
                    signUpView.setIndividualAwitch();
                signUpView.agreeToSignUpAccount();
                return;
            }

            if (mSignUpModel.isBusinessAccount() && mSignUpModel.isCompanyNameValid() &&
                    mSignUpModel.isCompanyAddressValid()) {
                signUpView.agreeToSignUpAccountBuisness();
            }
            else {
                signUpView.disAgreeToSignUpAccountBuisness();
            }
        }
        else{
            signUpView.disAgreeToSignUpAccountBuisness();
            signUpView.disAgreeToSignUpAccount();
        }
    }

    @Override
    public void changeAccountType(boolean isItBusinessAccount)
    {
        signUpView.hideSoftKeyboard();
        signUpView.clearFocus();
        mSignUpModel.setBusinessAccount(isItBusinessAccount);

        if(mSignUpModel.isBusinessAccount())
            signUpView.switchToBusinessAccount();
        else
            signUpView.switchToIndividualAccount();
    }

    @Override
    public void handleSignUpBtnStateEnabling(boolean isTermsAndCondsAccepted ,boolean isTermsAndCondsEnabled,boolean state)
    {
        Utility.printLog("ButtonHandle"+isTermsAndCondsAccepted+"-"+state);
        if (isTermsAndCondsAccepted && isTermsAndCondsAccepted)
        {
            signUpView.enableSignUpButton();
        }
        else
            signUpView.disableSignUpButton();
    }

    public void isIndividual(boolean isTermsAndCondsAccepted,boolean state)
    {
        if(state && isTermsAndCondsAccepted) {
            isIndividual=true;
        }
        else if(!state && isTermsAndCondsAccepted){
            isBuisness=true;}
        else if (!state && !isTermsAndCondsAccepted)
        {
            isBuisness=false;
        }
        else if(state && !isTermsAndCondsAccepted)
            isIndividual=false;
    }
    @Override
    public void getSpannableString(String termsCondsString)
    {
        SpannableString termsCond=new SpannableString(termsCondsString);
        ClickableSpan terms=new ClickableSpan()
        {
            @Override
            public void updateDrawState(TextPaint ds)
            {
                super.updateDrawState(ds);
                ds.setColor(ContextCompat.getColor(mActivity, R.color.tandc1898e2));
            }
            @Override
            public void onClick(android.view.View view)
            {
                String termsLink = mContext.getString(R.string.PRIVACY_BASE)+
                        preferenceHelperDataSource.getLanguageSettings().getCode()+mContext.getString(R.string.TERMS_KEY);
                signUpView.openWebView(termsLink, mActivity.getString(R.string.terms_and_conditions));
            }
        };
        ClickableSpan privacyPolicy=new ClickableSpan()
        {
            @Override
            public void updateDrawState(TextPaint ds)
            {
                super.updateDrawState(ds);
                ds.setColor(ContextCompat.getColor(mActivity,R.color.tandc1898e2));
            }
            @Override
            public void onClick(android.view.View view)
            {
                String privacyLink = mContext.getString(R.string.PRIVACY_BASE)+
                        preferenceHelperDataSource.getLanguageSettings().getCode()+mContext.getString(R.string.PRIVACY_KEY);
                signUpView.openWebView (privacyLink, mActivity.getString(R.string.privacy_policy));
            }
        };
        termsCond.setSpan(terms,9,15,0);
        termsCond.setSpan(privacyPolicy ,17 ,termsCond.length(),0);
        signUpView.setSpannableString(termsCond);
    }

    @Override
    public void validatePassword(String password)
    {
        if(TextUtil.isEmpty(password))
        {
            mSignUpModel.setPasswordValid(false);
            signUpView.onInValidPassword();
        }
        else
        {
            mSignUpModel.setPasswordValid(true);
            signUpView.onValidPassword();
        }
    }

    @Override
    public void validateFullName(String fullName)
    {
        if(TextUtil.isEmpty(fullName))
        {
            mSignUpModel.setFullNameValid(false);
            signUpView.onInValidFullName();
        }
        else
        {
            mSignUpModel.setFullNameValid(true);
            signUpView.onValidFullName();
        }
    }

    @Override
    public void validateCompanyAddress(String companyAddress)
    {
        if(TextUtil.isEmpty(companyAddress))
        {
            mSignUpModel.setCompanyAddressValid(false);
            signUpView.onInValidCompanyAddress();
        }
        else
        {
            mSignUpModel.setCompanyAddressValid(true);
            signUpView.onValidCompanyAddress(companyAddress);
        }
        validateAllFieldsFlags();
    }

    @Override
    public void restoreAccountType(boolean accountType ,int loginType)
    {
        Utility.printLog(TAG+" ");
        userDetailsDataModel.setLoginType(loginType);
        mSignUpModel.setUserDetailsDataModel(userDetailsDataModel);
        mSignUpModel.setBusinessAccount(accountType);

        if(accountType)
            signUpView.switchToBusinessAccount();
        else
            signUpView.switchToIndividualAccount();
    }

    @Override
    public void validateCompanyName(String companyName)
    {
        if(TextUtil.isEmpty(companyName))
        {
            mSignUpModel.setCompanyNameValid(false);
            signUpView.onInValidCompanyName();
        }
        else
        {
            mSignUpModel.setCompanyNameValid(true);
            signUpView.onValidCompanyName();
        }
    }

    @Override
    public void validateEmailFormat(String emailId,boolean isToCallAPI)
    {
        if (TextUtil.isEmpty(emailId))
        {
            mSignUpModel.setEmailValid(false);
            signUpView.onInvalidEmail(mActivity.getString(R.string.mandatory));
        }
        else if (!TextUtil.emailValidation(emailId.trim()))
        {
            mSignUpModel.setEmailValid(false);
            signUpView.onInvalidEmail(mActivity.getString(R.string.email_invalid));
        }
        else
        {
            mSignUpModel.setEmailValid(true);
            signUpView.onValidEmail();
            if(mSignUpModel.isPhoneValid() && isToCallAPI)
                validateEmailAvailability(emailId);
            else
                validateAllFieldsFlags();
        }
    }

    @Override
    public void validateMobileFormat(String mobileNumber,String countryCode)
    {
        if (TextUtil.isEmpty(mobileNumber))
        {
            mSignUpModel.setPhoneValid(false);
            signUpView.onInvalidMobileFromDB(mActivity.getString(R.string.mandatory));
        }
        else if (TextUtil.phoneNumberLengthValidation(mobileNumber,countryCode))
        {
            mSignUpModel.setPhoneValid(true);
            signUpView.onValidMobileFromDB();
        }
        else
        {
            mSignUpModel.setPhoneValid(false);
            signUpView.onInvalidMobileFromDB(mActivity.getString(R.string.phone_invalid));
        }
    }

    @Override
    public void validateMobileField(String mobileNumber ,String countryCode , boolean isToCallAPI)
    {
        validateMobileFormat(mobileNumber ,countryCode);

        if(mSignUpModel.isPhoneValid() && isToCallAPI)
            validateMobileAvailability(mobileNumber,countryCode);
        else
            validateAllFieldsFlags();
    }

    @Override
    public void verifyReferralEntered(String referralCode)
    {
        if (TextUtil.isEmpty(referralCode))
        {
            mSignUpModel.setReferralCodeEntered(false);
            signUpView.onReferralEmpty();
            mSignUpModel.setReferralCode(null);
        }
        else
        {
            mSignUpModel.setReferralCode(referralCode);
            mSignUpModel.setReferralCodeEntered(true);
            signUpView.onReferralEntered();
        }
    }

    @Override
    public void getCountryInfo(CountryPicker countryPicker)
    {
        Country country = countryPicker.getUserCountryInfo(mActivity);
        mSignUpModel.setCountryCodeMinLength(country.getMinDigits());
        mSignUpModel.setCountryCodeMaxLength(country.getMaxDigits());
        signUpView.onGettingOfCountryInfo(country.getFlag(),country.getDialCode(),country.getMaxDigits()
                , true);
    }

    @Override
    public void addListenerForCountry(CountryPicker countryPicker)
    {
        countryPicker.setListener((name, code, dialCode, flagDrawableResID, min, max) ->
        {
            mSignUpModel.setCountryCodeMinLength(min);
            mSignUpModel.setCountryCodeMaxLength(max);
            signUpView.onGettingOfCountryInfo(flagDrawableResID,dialCode,max ,false);
        });
    }

    @Override
    public void validateMobileAvailability(final String phone,String countryCode)
    {
        if(Utility.isNetworkAvailable(mContext))
        {
            signUpView.showProgressDialog(mActivity.getString(R.string.phone_validating));
            Observable<Response<ResponseBody>> request = networkService.validateMobileAPI(
                    preferenceHelperDataSource.getLanguageSettings().getCode(), countryCode,phone);
            request.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<ResponseBody>>()
                    {
                        @Override
                        public void onSubscribe(Disposable d)
                        {
                            compositeDisposable.add(d);
                        }
                        @Override
                        public void onNext(Response<ResponseBody> response)
                        {
                            Utility.printLog(TAG + " validateMobileAvailability onAddCardError " + response.code());
                            switch (response.code())
                            {
                                case 200:
                                    mSignUpModel.setPhoneValid(true);
                                    preferenceHelperDataSource.setMobileNo(phone);
                                    signUpView.onValidMobileFromDB();
                                    break;

                                case 406:
                                case 412:
                                case 500:
                                    mSignUpModel.setPhoneValid(false);
                                    signUpView.onInvalidMobileFromDB(DataParser.fetchErrorMessage(response));
                                    break;

                                case 502:
                                    signUpView.showToast(mActivity.getString(R.string.bad_gateway));
                                    break;

                                default:
                                    signUpView.showToast(DataParser.fetchErrorMessage(response));
                                    break;
                            }
                        }
                        @Override
                        public void onError(Throwable errorMsg)
                        {
                            signUpView.hideProgressDialog();
                            signUpView.showToast(mContext.getString(R.string.network_problem));
                            Utility.printLog(TAG + " validateMobileAvailability onAddCardError " + errorMsg);
                        }
                        @Override
                        public void onComplete()
                        {
                            signUpView.hideProgressDialog();
                            Utility.printLog(TAG + " validateMobileAvailability onComplete ");
                        }
                    });
        }
        else
            signUpView.showToast(mContext.getString(R.string.network_problem));
    }

    @Override
    public void disposeObservable()
    {
        compositeDisposable.clear();
    }

    @Override
    public void setImageUrl(String url)
    {
        preferenceHelperDataSource.setImageUrl(url);
    }

    @Override
    public void validateEmailAvailability(final String emailId)
    {
        if(Utility.isNetworkAvailable(mContext))
        {
            Utility.printLog("Tracker"+"validateEmailAvailability");
            signUpView.showProgressDialog(mActivity.getString(R.string.mail_validating));
            Observable<Response<ResponseBody>> request = networkService.validateEmailAPI
                    ( preferenceHelperDataSource.getLanguageSettings().getCode(), emailId);
            request.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<ResponseBody>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            compositeDisposable.add(d);
                        }
                        @Override
                        public void onNext(Response<ResponseBody> response)
                        {
                            Utility.printLog(TAG+" validateEmailAvailability code "+response.code());
                            Utility.printLog("Tracker"+response.code());
                            switch (response.code())
                            {
                                case 200:
                                    mSignUpModel.setEmailValid(true);
                                    preferenceHelperDataSource.setUserEmail(emailId);
                                    signUpView.onValidEmailIdFromDB();
                                    break;

                                case 412:
                                case 500:
                                    mSignUpModel.setEmailValid(false);
                                    signUpView.onInvalidEmailIdFromDB(DataParser.fetchErrorMessage(response));
                                    break;

                                case 502:
                                    signUpView.showToast(mActivity.getString(R.string.bad_gateway));
                                    break;

                                default:
                                    signUpView.showToast(DataParser.fetchErrorMessage(response));
                                    break;
                            }
                        }
                        @Override
                        public void onError(Throwable errorMsg)
                        {
                            Utility.printLog(TAG + " validateEmailAvailability onAddCardError " + errorMsg);
                            signUpView.hideProgressDialog();
                            signUpView.showToast(mContext.getString(R.string.network_problem));
                        }
                        @Override
                        public void onComplete() {
                            Utility.printLog(TAG + " validateEmailAvailability onComplete ");
                            signUpView.hideProgressDialog();
                        }
                    });
        }
        else
            signUpView.showToast(mContext.getString(R.string.network_problem));
    }

    @Override
    public void onBackPressed()
    {
        preferenceHelperDataSource.setImageUrl("");
        preferenceHelperDataSource.setUserEmail("");
        preferenceHelperDataSource.setMobileNo("");
    }

    @Override
    public void startLocationService()
    {
        locationProvider.startLocation(this);
    }

    @Override
    public void storeSocialMediaID(String socialMediaID)
    {
        mSignUpModel.setSocialMediaId(socialMediaID);
        mSignUpModel.setUserDetailsDataModel(userDetailsDataModel);
    }

    @Override
    public boolean fetchAccountType()
    {
        return mSignUpModel.isBusinessAccount();
    }

    @Override
    public int fetchLoginType()
    {
        return  mSignUpModel.getUserDetailsDataModel().getLoginType();
    }

    @Override
    public void storeProfilePicUrl(String url)
    {
        userDetailsDataModel.setProfilePic(url);
        mSignUpModel.setUserDetailsDataModel(userDetailsDataModel);
    }

    @Override
    public String fetchProfilePicUrl() {
        return preferenceHelperDataSource.getImageUrl();

    }

    @Override
    public String fetchCompanyAddress() {
        return mSignUpModel.getUserDetailsDataModel().getBillingAddress1();
    }

    @Override
    public void storeCompanyAddress(String address) {
        userDetailsDataModel.setBillingAddress1(address);
        mSignUpModel.setUserDetailsDataModel(userDetailsDataModel);
    }

    @Override
    public void performImageOperation()
    {
        mImageOperation.doImageOperation(new ResultInterface()
        {
            @Override
            public void errorMandatoryNotifier()
            {
                signUpView.captureImage();
            }
            @Override
            public void errorInvalidNotifier()
            {
                signUpView.removeImageCaptured();
            }
        });
    }

    @Override
    public void hideKeyboardAndClearFocus() {
        signUpView.hideSoftKeyboard();
        signUpView.clearFocus();
    }

    @Override
    public void getUserCompanyAddress()
    {
        signUpView.openAddressActivity();
    }

    @Override
    public void storeIfProfilePicSelected(boolean isSelected) {
        mSignUpModel.setProfilePicSelected(isSelected);
    }

    @Override
    public void uploadImageToAmazon(File fileName)
    {
        mImageOperation.uploadToAmazon(fileName,AMAZON_PROFILE_FOLDER,
                new ImageOperationInterface() {
                    @Override
                    public void onSuccess(String fileName)
                    {
                        signUpView.onSuccessOfImageUpload(AMAZON_PROFILE_PATH+fileName);
                    }
                    @Override
                    public void onFailure() {
                        signUpView.onFailureOfImageUpload();
                    }
                });
    }

    @Override
    public void storeIfImageUploaded(boolean isSelected) {
        mSignUpModel.setImageUploaded(isSelected);
    }

    @Override
    public void storeIfAllPermissionsGranted(boolean isGranted) {
        mSignUpModel.setAllPermissionsGranted(isGranted);
    }

    @Override
    public boolean fetchIfAllPermissionsGranted() {
        return mSignUpModel.isAllPermissionsGranted();
    }

    @Override
    public void handleResult(int requestCode, int resultCode ,String address, Uri imageUri)
    {
        switch (requestCode)
        {
            case Constants.COMPANY_ADDR_ID:
                switch (resultCode)
                {
                    case RESULT_OK:
                        signUpView.onGettingOfCompanyAddress(address);
                        break;
                }
                break;
        }
    }

    @Override
    public void uploadToAmazon(Uri imageUri){
        MediaInterface callbacksAmazon = new MediaInterface() {
            @Override
            public void onSuccessUpload(JSONObject message) {
                Utility.printLog(TAG+"image uploaded "+message.toString());
                String profilePicUrl= AMAZON_PROFILE_PATH+amazonFileName;
                signUpView.onImageUploadSuccess(profilePicUrl);
            }
            @Override
            public void onUploadError(JSONObject message)
            {
                Utility.printLog(TAG+"image onUploadError "+message.toString());
            }
            @Override
            public void onSuccessDownload(String fileName, byte[] stream, JSONObject object) {
            }
            @Override
            public void onDownloadFailure(JSONObject object)
            {
                Utility.printLog(TAG+"image onUploadError "+object.toString());
            }
            @Override
            public void onSuccessPreview(String fileName, byte[] stream, JSONObject object) {
            }
        };
        amazonFileName = System.currentTimeMillis()+".jpg";
        JSONObject message = stringToJsonAndPublish(mContext.getString(R.string.app_name) +"/"+ amazonFileName, imageUri);
        amazonImageUpload.uploadMedia(mActivity, imageUri, mActivity.getString(R.string.AMAZON_PICTURE_BUCKET), AMAZON_PROFILE_FOLDER + amazonFileName, callbacksAmazon, message);
    }

    @Override
    public void storeUserDetails(String... params)
    {
        userDetailsDataModel.setFirstName(params[0]);
        userDetailsDataModel.setEmail(params[1]);
        userDetailsDataModel.setPassword(params[2]);
        userDetailsDataModel.setCountryCode(params[3]);
        userDetailsDataModel.setPhone(params[4]);
        userDetailsDataModel.setReferralCode(params[5]);
        userDetailsDataModel.setBusinessName(params[6]);
        mSignUpModel.setUserDetailsDataModel(userDetailsDataModel);
    }

    @Override
    public void checkIfLocationEnabled()
    {
        if(!preferenceHelperDataSource.getCurrLatitude().equals("") &&
                !preferenceHelperDataSource.getCurrLongitude().equals(""))
            registerUser();
        else
            signUpView.checkForPermissionAndSendOTP();
    }

    /**
     * <h2>verifyReferralCode</h2>
     * used to verify the referral code entered
     */
    private void verifyReferralCode()
    {
        Utility.printLog(TAG+" verifyReferralCode");
        if (Utility.isNetworkAvailable(mContext))
        {
            signUpView.showProgressDialog(mActivity.getString(R.string.verifying_referral));
            Observable<Response<ResponseBody>> request = networkService.validateReferralCode
                    (preferenceHelperDataSource.getLanguageSettings().getCode(),mSignUpModel.getReferralCode());
            request.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<ResponseBody>>() {
                        @Override
                        public void onSubscribe(Disposable d)
                        {
                            compositeDisposable.add(d);
                        }
                        @Override
                        public void onNext(Response<ResponseBody> response)
                        {
                            switch (response.code())
                            {
                                case 200:
                                    signUpAPI();
                                    break;

                                case 502:
                                    signUpView.showToast(mActivity.getString(R.string.bad_gateway));
                                    break;

                                default:
                                    signUpView.showToast(DataParser.fetchErrorMessage(response));
                                    break;
                            }
                        }
                        @Override
                        public void onError(Throwable errormsg)
                        {
                            Utility.printLog(TAG+" onAddCardError  showToast " + errormsg);
                            signUpView.hideProgressDialog();
                            signUpView.showToast(mContext.getString(R.string.network_problem));
                        }
                        @Override
                        public void onComplete()
                        {
                            signUpView.hideProgressDialog();
                        }
                    });
        }
        else
            signUpView.showToast(mContext.getString(R.string.network_problem));
    }

    @Override
    public void registerUser()
    {
        if(mSignUpModel.isReferralCodeEntered())
            verifyReferralCode();
        else
            signUpAPI();
    }

    /**
     * <h2>signUpAPI</h2>
     * API to register User
     */
    private void signUpAPI()
    {
        Utility.printLog(TAG+" signUpAPI");
        switch (mSignUpModel.getUserDetailsDataModel().getLoginType())
        {
            case 2:
                userDetailsDataModel.setFacebookId(mSignUpModel.getSocialMediaId());
                break;

            case 3:
                userDetailsDataModel.setGoogleId(mSignUpModel.getSocialMediaId());
                break;
        }
        userDetailsDataModel.setLatitude(preferenceHelperDataSource.getCurrLatitude());
        userDetailsDataModel.setLongitude(preferenceHelperDataSource.getCurrLongitude());
        userDetailsDataModel.setDevType(Constants.DEVICE_TYPE);
        userDetailsDataModel.setDeviceId(utility.getDeviceId(mActivity));
        userDetailsDataModel.setAppVersion(Constants.APP_VERSION);
        userDetailsDataModel.setDevMake(Constants.DEVICE_MAKER);
        userDetailsDataModel.setDevModel(Constants.DEVICE_MODEL);
        userDetailsDataModel.setIpAddress(utility.getLocalIpAddress());
        userDetailsDataModel.setProfilePic(preferenceHelperDataSource.getImageUrl());
//        userDetailsDataModel.setIsPartnerUser(null);

        if (Utility.isNetworkAvailable(mContext))
        {
            signUpView.showProgressDialog(mActivity.getString(R.string.registering));
            Observable<Response<ResponseBody>> request = networkService.userSignUp
                    (preferenceHelperDataSource.getLanguageSettings().getCode(),userDetailsDataModel);
            request.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<ResponseBody>>() {
                        @Override
                        public void onSubscribe(Disposable d)
                        {
                            compositeDisposable.add(d);
                        }
                        @Override
                        public void onNext(Response<ResponseBody> response)
                        {
                            switch (response.code())
                            {
                                case 200:
                                    String[] registerData = DataParser.fetchSidFromData(response);
                                    Utility.printLog(TAG+" sid from response "+registerData[0]);
                                    preferenceHelperDataSource.setSid(registerData[0]);
                                    signUpView.onSuccessOfGettingOTP(registerData[1]);
                                    break;

                                case 409:
                                    signUpView.showAlertWithMsg(DataParser.fetchErrorMessage(response));
                                    break;

                                case 502:
                                    signUpView.showToast(mActivity.getString(R.string.bad_gateway));
                                    break;

                                default:
                                    signUpView.showToast(DataParser.fetchErrorMessage(response));
                                    break;
                            }
                        }
                        @Override
                        public void onError(Throwable errorMsg)
                        {
                            Utility.printLog(TAG+" onAddCardError  showToast " + errorMsg);
                            signUpView.hideProgressDialog();
                            signUpView.showToast(mContext.getString(R.string.network_problem));
                        }
                        @Override
                        public void onComplete()
                        {
                            signUpView.hideProgressDialog();
                        }
                    });
        }
        else
            signUpView.showToast(mContext.getString(R.string.network_problem));
    }

    @Override
    public void checkRTLConversion() {
        RtlConversion(mActivity,preferenceHelperDataSource.getLanguageSettings().getCode());
    }

    @Override
    public void onLocationServiceDisabled(Status status)
    {
        signUpView.promptUserWithLocationAlert(status);
    }
}
