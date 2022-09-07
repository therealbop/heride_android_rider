package com.karru.authentication.verification;

import android.app.Activity;
import android.os.CountDownTimer;
import com.karru.ApplicationClass;
import com.karru.api.NetworkService;
import com.karru.managers.user_vehicles.MQTTManager;
import com.heride.rider.R;
import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.data.source.local.sqlite.SQLiteDataSource;
import com.karru.util.DataParser;
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
import static com.karru.utility.Constants.EDIT_PHONE_NUMBER;
import static com.karru.utility.Constants.FORGOT_PASS_TYPE;
import static com.karru.utility.Constants.VERIFY;

/**
 * <h1>VerifyOTPPresenter</h1>
 * <h4>This is a Controller class for VerifyOTPActivity Activity</h4>
 * This class is used for performing the business logic for our Activity and
 * this class is getting called from VerifyOTPActivity Activity and give a call to VerifyOTPModel class.
 * @version 1.0
 * @author 3Embed
 * @since 17/08/17
 */
public class VerifyOTPPresenter implements VerifyOTPContract.Presenter
{
    private static final String TAG = "VerifyOTPPresenter";

    @Inject VerifyOTPModel verifyOTPModel;
    @Inject NetworkService networkService;
    @Inject VerifyOTPContract.View viewCallBack;
    @Inject @Named(VERIFY) CompositeDisposable compositeDisposable;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject SQLiteDataSource sqLiteDataSource;
    @Inject MQTTManager mqttManager;
    @Inject VerifyOTPActivity mActivity;

    @Inject
    VerifyOTPPresenter() { }

    @Override
    public void checkRTLConversion() {
        RtlConversion(mActivity,preferenceHelperDataSource.getLanguageSettings().getCode());
    }

    @Override
    public void extractAndStoreData(String ... params)
    {
        if (params[1] != null)
            verifyOTPModel.setComingFrom(params[1]);
        verifyOTPModel.setMobileNumber( params[0]);
        verifyOTPModel.setCountryCode(params[2]);
        verifyOTPModel.setPassword(params[3]);
        verifyOTPModel.setOtpTime(params[4]);
        Utility.printLog(TAG+" otp timer "+verifyOTPModel.getOtpTime());
        viewCallBack.setTitleForScreen(params[2]+params[0]);
    }

    @Override
    public void handleResendOTP()
    {
        long timer = 60;
        if(verifyOTPModel.getOtpTime() !=null)
        {
            Utility.printLog(TAG+" otp timer 1 "+verifyOTPModel.getOtpTime());
            timer = Long.parseLong(verifyOTPModel.getOtpTime());
            if(timer > 0)
                timer(timer);
            else
                viewCallBack.enableResendButton();
        }
        else
            timer(timer);
    }

    /**
     * <h2>timer</h2>
     * used to add the timer for resend OTP
     * @param timer timer value
     */
    private void timer(long timer)
    {
        new CountDownTimer(timer *1000, 1000)
        {
            @Override
            public void onTick(long millisUntilFinished)
            {
                Utility.printLog(TAG+" millisUntilFinished "+millisUntilFinished);
                String minutes = String.format("%02d",(millisUntilFinished / 1000) / 60);
                String seconds = String.format("%02d",(millisUntilFinished / 1000) % 60);
                Utility.printLog(TAG+"%d Milliseconds = %d minutes and %d seconds."+ millisUntilFinished+" "+ minutes+" "+ seconds);
                viewCallBack.setElapsedTime(minutes + ":" + seconds);
            }
            @Override
            public void onFinish()
            {
                viewCallBack.enableResendButton();
            }
        }.start();
    }

    @Override
    public void verifyOTPApi(final String phone, final String otp, final String comingFrom)
    {
        if (Utility.isNetworkAvailable(mActivity))
        {
            Observable<Response<ResponseBody>> request;
            switch (comingFrom)
            {
                case "SignUp":
                    request = networkService.verifyPhoneNumber(preferenceHelperDataSource.getLanguageSettings().getCode(),
                            Double.parseDouble(otp), preferenceHelperDataSource.getSid());
                    requestInitializer(request,otp,phone,comingFrom);
                    break;

                case "forgotPassword":
                    request = networkService.verifyOTPCode(preferenceHelperDataSource.getLanguageSettings().getCode(),
                            Double.parseDouble(otp), preferenceHelperDataSource.getSid(),FORGOT_PASS_TYPE);
                    requestInitializer(request,otp,phone,comingFrom);
                    break;

                case "EditPhoneNumberActivity":
                    request=networkService.verifyOTPCode(preferenceHelperDataSource.getLanguageSettings().getCode(),
                            Double.parseDouble(otp), preferenceHelperDataSource.getSid(),EDIT_PHONE_NUMBER);
                    requestInitializer(request,otp,phone,comingFrom);
                    break;
            }
        }
        else
            viewCallBack.showToast(mActivity.getString(R.string.network_problem));
    }

    /**
     * <h2>requestInitializer</h2>
     * used to call API to verify OTP
     * @param request request params
     * @param otp otp to be verified
     * @param phone phone number
     * @param comingFrom coming from tag
     */
    private void requestInitializer(Observable<Response<ResponseBody>> request,String otp,String phone,String comingFrom)
    {
        viewCallBack.showProgressDialog(mActivity.getResources().getString(R.string.verifying_code));
        Utility.printLog(" OTPVERIFYRESPONSE called ");
        request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }
                    @Override
                    public void onNext(Response<ResponseBody> response)
                    {
                        Utility.printLog("OTPVERIFYRESPONSE"+response.code());
                        switch (response.code())
                        {
                            case 200:
                                preferenceHelperDataSource.setOTP(otp);
                                preferenceHelperDataSource.setMobileNo(phone);
                                Utility.printLog("OTPVERIFYRESPONSE"+comingFrom);
                                switch (comingFrom)
                                {
                                    case "forgotPassword":
                                        viewCallBack.openChangePasswordActivity(otp,phone,comingFrom);
                                        break;
                                    case "changePassword":
                                        viewCallBack.openMainActivityWithoutClear();
                                        break;
                                    case "EditPhoneNumberActivity":
                                        preferenceHelperDataSource.setMobileNo(phone);
                                        viewCallBack.loadingAlert(DataParser.fetchSuccessMessage(response));
                                        break;
                                    case "SignUp":
                                        DataParser.storeUserData(response,mActivity,verifyOTPModel.getPassword(),
                                                preferenceHelperDataSource,sqLiteDataSource,mqttManager);
                                        ((ApplicationClass)mActivity.getApplication()).getConfigurations(false);
                                        viewCallBack.openSecondSplashScreen();
                                        break;
                                }
                                break;

                            case 502:
                                viewCallBack.showToast(mActivity.getString(R.string.bad_gateway));
                                break;

                            default:
                                viewCallBack.showAlertWithMsg(DataParser.fetchErrorMessage(response));
                                viewCallBack.clearFields();
                                break;
                        }
                    }
                    @Override
                    public void onError(Throwable errormsg)
                    {
                        Utility.printLog(TAG + " verifyOTPApi onAddCardError " + errormsg);
                        viewCallBack.showToast(mActivity.getString(R.string.network_problem));
                        viewCallBack.hideProgressDialog();
                        viewCallBack.showToast(mActivity.getString(R.string.network_problem));
                    }
                    @Override
                    public void onComplete()
                    {
                        viewCallBack.hideProgressDialog();
                    }
                });
    }

    @Override
    public void disposeObservable()
    {
        compositeDisposable.clear();
    }

    @Override
    public void getVerificationCode()
    {
        int trigger = 0;
        switch (verifyOTPModel.getComingFrom())
        {
            //1 - Register,2 - Forgot Password,3-change number, 4-login with phone OTP
            case "SignUp":
                trigger = 1;
                break;

            case "forgotPassword":
                trigger = 2;
                break;

            case "EditPhoneNumberActivity":
                trigger = 3;
                break;
        }
        viewCallBack.showProgressDialog(mActivity.getResources().getString(R.string.resending_code));
        Observable<Response<ResponseBody>> request = networkService.getVerificationCode
                (preferenceHelperDataSource.getLanguageSettings().getCode(),preferenceHelperDataSource.getSid(),trigger);
        request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }
                    @Override
                    public void onNext(Response<ResponseBody> response)
                    {
                        Utility.printLog(TAG+" send OTP onNext "+response.code());
                        switch (response.code())
                        {
                            case 200:
                                String responseToBeParsed;
                                try {
                                    responseToBeParsed = response.body().string();
                                    JSONObject jsonResponse = new JSONObject(responseToBeParsed);
                                    Utility.printLog(" response from otp resend "+responseToBeParsed);
                                    JSONObject dataObject = jsonResponse.getJSONObject("data");
                                    viewCallBack.showToast(jsonResponse.getString("message"));
                                    verifyOTPModel.setOtpTime(dataObject.getString("expireOtp"));
                                    viewCallBack.disableResend();
                                } catch (IOException e) {
                                    Utility.printLog(" response from otp IOException "+e);
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    Utility.printLog(" response from otp JSONException "+e);
                                    e.printStackTrace();
                                }
                                break;

                            case 502:
                                viewCallBack.showToast(mActivity.getString(R.string.bad_gateway));
                                break;

                            default:
                                viewCallBack.showToast(DataParser.fetchErrorMessage(response));
                                break;
                        }
                    }
                    @Override
                    public void onError(Throwable errorMsg)
                    {
                        Utility.printLog(TAG + " validateReferralCodeAPI onError " + errorMsg);
                        viewCallBack.showToast(mActivity.getString(R.string.network_problem));
                        viewCallBack.hideProgressDialog();
                    }
                    @Override
                    public void onComplete()
                    {
                        viewCallBack.hideProgressDialog();
                    }
                });
    }

    @Override
    public void addDataAndCallAPI(String otp)
    {
        if (otp.length() == 4)
        {
            verifyOTPApi(verifyOTPModel.getCountryCode() + verifyOTPModel.getMobileNumber()
                    ,otp, verifyOTPModel.getComingFrom());
        }
        else
            viewCallBack.showToast(mActivity.getString(R.string.otp_mandatory));
    }

    @Override
    public void validateEnteredDigit(String currDigit, int nextDigitPos, int prevDigitPos ,boolean callAPI)
    {
        switch (currDigit.length())
        {
            case 1:
                viewCallBack.requestFocus(nextDigitPos);
                break;

            case 0:
                viewCallBack.requestFocus(prevDigitPos);
                break;
        }
    }
}
