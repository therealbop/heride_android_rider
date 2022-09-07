package com.karru.authentication.change_password;

import android.content.Context;
import com.karru.ApplicationClass;
import com.karru.api.NetworkService;
import com.heride.rider.R;
import com.karru.dagger.ActivityScoped;
import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.managers.network.NetworkStateHolder;
import com.karru.util.DataParser;
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
import static com.karru.utility.Constants.CHANGE_PASS;

@ActivityScoped
class ChangePasswordPresenter implements ChangePasswordActivityContract.Presenter
{
    @Inject NetworkService networkService;
    @Inject @Named(CHANGE_PASS) CompositeDisposable compositeDisposable;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject ChangePasswordActivityContract.View changepasswordView;
    @Inject Context context;
    @Inject ChangePasswordActivity mActivity;
    @Inject NetworkStateHolder networkStateHolder;

    private String comingFrom;
    private boolean passwdFlag = false, rePasswdFlag = false, oldPasswdFlag = false;

    @Inject
    ChangePasswordPresenter() {
    }

    public void checkSource(String comingFrom) {
        this.comingFrom = comingFrom;
        if (comingFrom.equals("Profile"))
            changepasswordView.enableOldPasswod();
        else
            changepasswordView.setForgotPasswordViews();
    }

    @Override
    public void checkRTLConversion() {
        RtlConversion(mActivity,preferenceHelperDataSource.getLanguageSettings().getCode());
    }

    @Override
    public void validateInputValue(String number) {
        if (number.length() == 0) {
            passwdFlag = false;
            changepasswordView.newPasswordDataEmpty();
        } else if (!Validator.isPasswordValid(number)) {
            passwdFlag = false;
            changepasswordView.newPasswordDataNotValid();
        } else {
            passwdFlag = true;
            changepasswordView.newPasswordDataValid();
        }
    }

    public void validateOldPasswordInputValue(String number) {
        if (number.length() == 0) {
            oldPasswdFlag = false;
            changepasswordView.oldPasswordDataEmpty();
        } else {
            oldPasswdFlag = true;
            changepasswordView.oldPasswordDataValid();
        }
    }

    @Override
    public void validateReEnterInputValue(String number) {
        if (number.length() == 0) {
            rePasswdFlag = false;
            changepasswordView.reEnterPasswordDataEmpty();
        } else if (!Validator.isPasswordValid(number)) {
            rePasswdFlag = false;
            changepasswordView.reEnterPasswordDataNotValid();
        } else {
            rePasswdFlag = true;
            changepasswordView.reEnterPasswordDataValid();
        }
    }


    private void initPasswordChangeApi(String firstPassword, String secondPassword) {
        if (Validator.checkReEnteredPass(firstPassword, secondPassword)) {
            updatePassword(firstPassword);
        } else {
            changepasswordView.loadErrorAlert(context.getString(R.string.password_not_matched));
        }
    }

    /**
     * This method is used to call an API, used to update the password.
     */
    private void updatePassword(final String password)
    {
        if(networkStateHolder.isConnected())
        {
            Observable<Response<ResponseBody>> request = networkService.changePassword(preferenceHelperDataSource.getLanguageSettings().getCode(),
                    password, preferenceHelperDataSource.getSid());
            request.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<ResponseBody>>()
                    {
                        @Override
                        public void onSubscribe(Disposable d)
                        {
                            compositeDisposable.add(d);
                        }

                        @Override
                        public void onNext(Response<ResponseBody> value)
                        {
                            switch (value.code())
                            {
                                case 200:
                                    preferenceHelperDataSource.setPassword(password);
                                    changepasswordView.loadAlert();
                                    break;

                                case 502:
                                    changepasswordView.showToast(context.getString(R.string.bad_gateway));
                                    break;

                                default:
                                    changepasswordView.loadErrorAlert(DataParser.fetchErrorMessage(value));
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable e)
                        {
                        }
                        @Override
                        public void onComplete() {
                        }
                    });
        }
        else
            changepasswordView.showToast(context.getString(R.string.network_problem));
    }

    @Override
    public void isValidPassword(String comingFrom) {
        if (comingFrom.equals("Profile")) {
            if (passwdFlag && rePasswdFlag && oldPasswdFlag)
                changepasswordView.getPasswordForProfile();
        } else if (passwdFlag && rePasswdFlag)
            changepasswordView.getPassword();

    }

    @Override
    public void disposeObservable()
    {
        compositeDisposable.clear();
    }

    @Override
    public void checkPasswordMatch(String newPassword, String reEnteredPassword) {
        if (!Validator.checkReEnteredPass(newPassword, reEnteredPassword)) {
            changepasswordView.missMatchError();
        } else
            initPasswordChangeApi(newPassword.trim(), reEnteredPassword.trim());
    }

    public void checkPasswordMatchforProfile(String newPassword, String reEnteredPassword, String oldPassword) {
        if (!Validator.checkReEnteredPass(newPassword, reEnteredPassword)) {
            changepasswordView.missMatchError();
        } else
            changePassword(oldPassword.trim(),newPassword.trim());
    }

    /**
     * This metod will check that user entered password is correct or not.
     *
     * @param oldPassword .
     */
    private void changePassword(String oldPassword, String newPassword) {

        if(networkStateHolder.isConnected())
        {
            changepasswordView.showProgressDialog();
            Observable<Response<ResponseBody>> request = networkService.getProfileCheckPassword
                    (((ApplicationClass) context.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                            preferenceHelperDataSource.getLanguageSettings().getCode(), oldPassword, newPassword);
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
                            Utility.printLog(" changePassword onNext" + value.code());

                            switch (value.code()) {
                                case 200:
                                    if (comingFrom.equals("Profile"))
                                    {
                                        preferenceHelperDataSource.setPassword(newPassword);
                                        changepasswordView.loadErrorAlert(DataParser.fetchSuccessMessage(value));
                                    }
                                    else
                                        changepasswordView.loadAlert();
                                    changepasswordView.hideProgressDialog();
                                    break;

                                case 401:
                                    changepasswordView.setOldPaswdError(DataParser.fetchErrorMessage(value));
                                    break;

                                case 403:
                                    changepasswordView.setSamePasswordError();
                                    break;

                                case 402:
                                    changepasswordView.setOldPaswdError(DataParser.fetchErrorMessage(value));
                                    break;

                                case 502:
                                    changepasswordView.showToast(context.getString(R.string.bad_gateway));
                                    break;

                                default:
                                    changepasswordView.loadErrorAlert(DataParser.fetchErrorMessage(value));
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable e)
                        {
                            changepasswordView.showToast(context.getString(R.string.network_problem));
                            changepasswordView.hideProgressDialog();
                        }

                        @Override
                        public void onComplete()
                        {
                            changepasswordView.hideProgressDialog();
                        }
                    });
        }
        else
            changepasswordView.showToast(context.getString(R.string.network_problem));
    }
}
