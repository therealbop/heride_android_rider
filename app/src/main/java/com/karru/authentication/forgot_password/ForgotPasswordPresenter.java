package com.karru.authentication.forgot_password;

import android.content.Context;

import com.karru.api.NetworkService;
import com.karru.util.TextUtil;
import com.heride.rider.R;
import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.managers.network.NetworkStateHolder;
import com.karru.util.DataParser;
import com.karru.util.Validation;
import com.karru.utility.Utility;
import com.karru.utility.Validator;
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
import static com.karru.utility.Constants.FORGOT_PASS;

public class ForgotPasswordPresenter implements ForgotPasswordContract.ForgotPasswdPresenter
{
    private String TAG = "ForgotPasswordPresenter";

    @Inject Context context;
    @Inject ForgotPasswordActivity mActivity;
    @Inject NetworkStateHolder networkStateHolder;
    @Inject NetworkService networkService;
    @Inject @Named(FORGOT_PASS) CompositeDisposable compositeDisposable;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject ForgotPasswordContract.ForgotPasswordView forgotPasswordView;

    @Inject
    ForgotPasswordPresenter()
    {
    }

    @Override
    public void checkRTLConversion() {
        RtlConversion(mActivity,preferenceHelperDataSource.getLanguageSettings().getCode());
    }

    @Override
    public void isEmailValidation(boolean isEmailValidation, boolean isPhoneNoValid)
    {
        if (isEmailValidation)
            forgotPasswordView.setMailHeaderText();
        else
            forgotPasswordView.setPhNumberHeaderText();
    }

    @Override
    public void disposeObservable()
    {
        compositeDisposable.clear();
    }

    @Override
    public void isValid(boolean isEmailValidation, String mailTxt, String phNumTxt,String countryCode)
    {
        if (isEmailValidation)
            validatePhoneEmail(mailTxt,countryCode);
        else
            forgotPasswordView.checkVersionTOContinue(phNumTxt);
    }

    @Override
    public void validatePhoneEmail(String phone_Mail, String countryCode) {
        int mobile_email_type = 0;
        int opt = Validation.validatePhoneEmail(phone_Mail);

        switch (opt) {
            case 1:
                forgotPasswordView.showEmptyPhNumberAlert();
                mobile_email_type = 0;
                break;
            case 2:
                mobile_email_type = 2;
                break;
            case 3:
                mobile_email_type = 1;
                break;
            case 4:
                forgotPasswordView.showWrongPhNumberAlert();
                mobile_email_type = 0;
                break;
            case 5:
                mobile_email_type = 0;
                forgotPasswordView.showAlert(context.getString(R.string.email_invalid));
                break;
        }
        forgotPasswordView.showProgress();
        getVerificationCode(phone_Mail, mobile_email_type,countryCode);
    }


    /**
     * This method will send the Verification code on our given Mobile No.
     * @param phone_Mail        is using to send to server
     * @param mobile_email_type is using to send to server
     */
    private void getVerificationCode(final String phone_Mail, final int mobile_email_type,
                                     String countryCode)
    {
        if(networkStateHolder.isConnected())
        {
            forgotPasswordView.showProgress();
            Observable<Response<ResponseBody>> request = networkService.forgotPassword(preferenceHelperDataSource.getLanguageSettings().getCode() ,
                    phone_Mail,
                    countryCode, mobile_email_type);
            request.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<ResponseBody>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            compositeDisposable.add(d);
                        }

                        @Override
                        public void onNext(Response<ResponseBody> value) {
                            Utility.printLog(TAG+ "forgot pass code  "+value.code());
                            switch (value.code())
                            {
                                case 200:
                                    switch (mobile_email_type)
                                    {
                                        case 1:
                                            String[] registerData = DataParser.fetchSidFromData(value);
                                            preferenceHelperDataSource.setSid(registerData[0]);
                                            forgotPasswordView.startOtpClass(phone_Mail,countryCode,registerData[1]);
                                            break;
                                        case 2:
                                            forgotPasswordView.openMessageDialog(DataParser.fetchSuccessMessage(value));
                                            break;
                                    }
                                    break;

                                case 502:
                                    forgotPasswordView.showToast(context.getString(R.string.bad_gateway));
                                    break;

                                default:
                                    forgotPasswordView.showAlert(DataParser.fetchErrorMessage(value));
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable e)
                        {
                            forgotPasswordView.dismissProgressDialog();
                            forgotPasswordView.onError(context.getString(R.string.network_problem));
                        }

                        @Override
                        public void onComplete()
                        {
                            forgotPasswordView.dismissProgressDialog();
                        }
                    });
        }
        else
            forgotPasswordView.onError(context.getString(R.string.network_problem));
    }

    /**
     * <h2>phoneMailValidation</h2>
     * <p>this method is using to validate the Phone</p>
     *
     * @param isMail: if its an eamil id
     * @param countryCode:   contains / email id or phone no
     */
    @Override
    public void phoneMailValidation(boolean isMail, String phone,String countryCode)
    {
        if (phone.isEmpty() || phone.trim().isEmpty())
            forgotPasswordView.errorMandatoryNotifier();
        else if (isMail)
        {
            if (!new Validator().emailValidation(phone.trim()))
                forgotPasswordView.emailErrorInvalidNotifier();
            else
                forgotPasswordView.onValidMail();
        }
        else if (!TextUtil.phoneNumberLengthValidation(phone,countryCode))
            forgotPasswordView.phoneErrorInvalidNotifier();
        else
            forgotPasswordView.phoneValidNotifier();
    }
}
