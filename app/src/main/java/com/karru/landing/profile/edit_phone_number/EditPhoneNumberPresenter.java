package com.karru.landing.profile.edit_phone_number;


import android.content.Context;

import com.karru.api.NetworkService;
import com.karru.util.TextUtil;
import com.heride.rider.R;
import com.karru.ApplicationClass;
import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.managers.network.NetworkStateHolder;
import com.karru.util.DataParser;
import com.karru.utility.Utility;

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
import static com.karru.utility.Constants.EDIT_PHONE;

public class EditPhoneNumberPresenter implements EditPhoneNumberActivityContract.EditPhoneNuberPresenter
{
    private static final String TAG = "EditPhoneNumberPresenter";
    @Inject NetworkService networkService;
    @Inject @Named(EDIT_PHONE) CompositeDisposable compositeDisposable;
    @Inject Context context;
    @Inject EditPhoneNumberActivity mActivity;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject EditPhoneNumberActivityContract.EditPhoneNumberView editPhoneNumberView;
    @Inject NetworkStateHolder networkStateHolder;

    @Inject
    EditPhoneNumberPresenter() {}

    @Override
    public void checkRTLConversion() {
        RtlConversion(mActivity,preferenceHelperDataSource.getLanguageSettings().getCode());
    }


    /**
     * <h2>phoneValidationRequest</h2>
     * <p> checking phone number already registered or not calling phone number validation service using okhttp </p>
     */
    private void phoneValidationRequest(String phoneNumber, String countryCode)
    {
        if (networkStateHolder.isConnected())
        {
            editPhoneNumberView.setProgressDialog();
            Utility.printLog(TAG+" TESTING TWICE API phoneValidationRequest called ");
            Observable<Response<ResponseBody>> request = networkService.verifyPhoneNumber(preferenceHelperDataSource.getLanguageSettings().getCode(),
                    countryCode, phoneNumber);
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
                            Utility.printLog(TAG+"  TESTING TWICE API phoneValidationRequest" + value.code());
                            switch (value.code())
                            {
                                case 200:
                                    getVerificationCode(phoneNumber, countryCode);
                                    break;

                                case 502:
                                    editPhoneNumberView.errorMessage(context.getString(R.string.bad_gateway));
                                    break;

                                default:
                                    editPhoneNumberView.loadingAlert(DataParser.fetchErrorMessage(value), false);
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable e)
                        {
                            editPhoneNumberView.dismissDialog();
                            editPhoneNumberView.errorMessage(context.getString(R.string.network_problem));
                        }

                        @Override
                        public void onComplete()
                        {
                            editPhoneNumberView.dismissDialog();
                        }
                    });
        }
        else
            editPhoneNumberView.errorMessage(context.getString(R.string.network_problem));
    }

    @Override
    public void disposeObservable()
    {
        compositeDisposable.clear();
    }

    /**
     * <h2>getVerificationCode</h2>
     * <p>
     * This method will send the Verification code on our given Mobile No.
     * </p>
     */
    private void getVerificationCode(String EditPhone, String countryCode) {

        if (networkStateHolder.isConnected())
        {
            editPhoneNumberView.setProgressDialog();
            Observable<Response<ResponseBody>> request = networkService.changePhoneNumber(
                    ((ApplicationClass) context.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                    preferenceHelperDataSource.getLanguageSettings().getCode(), countryCode, EditPhone);
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
                            Utility.printLog(TAG+"  TESTING TWICE API getVerificationCode code "+response.code() );
                            switch (response.code())
                            {
                                case 200:
                                    String[] registerData = DataParser.fetchSidFromData(response);
                                    editPhoneNumberView.dismissDialog();
                                    editPhoneNumberView.startVerifyOTPActivity(registerData[1]);
                                    break;

                                case 502:
                                    editPhoneNumberView.errorMessage(context.getString(R.string.bad_gateway));
                                    break;

                                default:
                                    editPhoneNumberView.loadingAlert(DataParser.fetchErrorMessage(response), false);
                                    break;
                            }
                        }
                        @Override
                        public void onError(Throwable e)
                        {
                            editPhoneNumberView.dismissDialog();
                            editPhoneNumberView.errorMessage(context.getString(R.string.network_problem));
                        }

                        @Override
                        public void onComplete()
                        {
                            editPhoneNumberView.dismissDialog();
                        }
                    });
        }
        else
            editPhoneNumberView.errorMessage(context.getString(R.string.network_problem));
    }

    @Override
    public void checkForMobileValidation(String phoneNumber,String countryCode)
    {
        if(!TextUtil.phoneNumberLengthValidation(phoneNumber,countryCode))
            editPhoneNumberView.setInvalidPhoneNumberError();
        else
            phoneValidationRequest(phoneNumber,countryCode);
    }
}
