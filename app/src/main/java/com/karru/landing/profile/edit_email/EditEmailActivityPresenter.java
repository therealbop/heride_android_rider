package com.karru.landing.profile.edit_email;

import android.content.Context;
import android.util.Patterns;

import com.karru.api.NetworkService;
import com.heride.rider.R;
import com.karru.ApplicationClass;
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
import static com.karru.utility.Constants.EDIT_EMAIL;

public class EditEmailActivityPresenter implements EditEmailActivityContract.EditEmailPresenter
{
    private static final String TAG = "EditEmailActivityPresenter";
    @Inject NetworkService networkService;
    @Inject NetworkStateHolder networkStateHolder;
    @Inject @Named(EDIT_EMAIL) CompositeDisposable compositeDisposable;
    @Inject EditEmailActivityContract.EditEmailView editEmailView;
    @Inject Context context;
    @Inject EditEmailActivity mActivity;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;

    @Inject
    EditEmailActivityPresenter() {
    }

    @Override
    public void checkRTLConversion() {
        RtlConversion(mActivity,preferenceHelperDataSource.getLanguageSettings().getCode());
    }


    @Override
    public void disposeObservable()
    {
        compositeDisposable.clear();
    }

    @Override
    public void EditEmailService(final String email)
    {
        if(networkStateHolder.isConnected())
        {
            editEmailView.showProgressDialog();
            Observable<Response<ResponseBody>> request = networkService.verifyEmail(preferenceHelperDataSource.getLanguageSettings().getCode(),
                    email);
            request.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<ResponseBody>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            compositeDisposable.add(d);
                        }

                        @Override
                        public void onNext(Response<ResponseBody> value)
                        {
                            Utility.printLog("EmailChangeResponse" + value.code());
                            switch (value.code())
                            {
                                case 200:
                                    callUpdateProfile(email);
                                    break;

                                case 502:
                                    editEmailView.showToast(context.getString(R.string.bad_gateway));
                                    break;

                                default:
                                    editEmailView.loadingAlert(DataParser.fetchErrorMessage(value));
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable e)
                        {
                            editEmailView.dismissProgressDialog();
                            editEmailView.showToast(context.getString(R.string.network_problem));
                        }

                        @Override
                        public void onComplete()
                        {
                            editEmailView.dismissProgressDialog();
                        }
                    });
        }
        else
            editEmailView.showToast(context.getString(R.string.network_problem));
    }

    /**
     * Updating email of profile.
     *
     * @param email is new email to update
     */
    private void callUpdateProfile(final String email)
    {
        if(networkStateHolder.isConnected())
        {
            editEmailView.showProgressDialog();
            Observable<Response<ResponseBody>> request = networkService.changeEmailAPI(((ApplicationClass) context.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                    preferenceHelperDataSource.getLanguageSettings().getCode(), email);
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
                            Utility.printLog(TAG+" callUpdateProfile " + value.code());
                            switch (value.code())
                            {
                                case 200:
                                    preferenceHelperDataSource.setUserEmail(email);
                                    editEmailView.loadingAlert(DataParser.fetchSuccessMessage(value));
                                    break;

                                case 502:
                                    editEmailView.showToast(context.getString(R.string.bad_gateway));
                                    break;

                                default:
                                    editEmailView.loadingAlert(DataParser.fetchErrorMessage(value));
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable e)
                        {
                            editEmailView.dismissProgressDialog();
                            editEmailView.showToast(context.getString(R.string.network_problem));
                        }

                        @Override
                        public void onComplete()
                        {
                            editEmailView.dismissProgressDialog();
                        }
                    });
        }
        else
            editEmailView.showToast(context.getString(R.string.network_problem));
    }

    @Override
    public void emailValidator(boolean hasFocus, String temp) {
        if (!hasFocus) {
            if (temp.length() == 0) {
                editEmailView.setEmailEmptyError();
            } else if (temp.length() < 3) {
                editEmailView.setInvalidEmailError();
            } else if (!new Validator().emailValidation(temp)) {
                editEmailView.wrongMailFormat();
            } else {
                editEmailView.clearMailError();
            }
        }
    }

    public  void isValidEmail(CharSequence target) {
        if (Patterns.EMAIL_ADDRESS.matcher(target).matches())
            editEmailView.onNameNonEmpty();
        else
            editEmailView.onNameEmpty();

    }
}