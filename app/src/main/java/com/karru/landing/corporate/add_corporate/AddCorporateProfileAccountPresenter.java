package com.karru.landing.corporate.add_corporate;

import android.content.Context;
import com.karru.api.NetworkService;
import com.heride.rider.R;
import com.karru.ApplicationClass;
import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.data.source.local.sqlite.SQLiteDataSource;
import com.karru.managers.network.NetworkStateHolder;
import com.karru.managers.network.RxNetworkObserver;
import com.karru.managers.user_vehicles.MQTTManager;
import com.karru.util.DataParser;
import com.karru.util.ExpireSession;
import com.karru.util.TextUtil;
import com.karru.utility.Utility;
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
import static com.karru.utility.Constants.CORPORATE_ADD_PROFILE;

public class AddCorporateProfileAccountPresenter implements
        AddCorporateProfileAccountContract.CorporateProfileAddMailPresenter
{
    @Inject Context mContext;
    @Inject AddCorporateProfileAccountActivity mActivity;
    @Inject NetworkService networkService;
    @Inject NetworkStateHolder networkStateHolder;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject SQLiteDataSource sqLiteDataSource;
    @Inject MQTTManager mqttManager;
    @Inject @Named(CORPORATE_ADD_PROFILE) CompositeDisposable compositeDisposable;
    @Inject AddCorporateProfileAccountContract.CorporateProfileAddMailView addMailView;

    @Inject
    AddCorporateProfileAccountPresenter() {}

    @Override
    public void checkRTLConversion() {
        RtlConversion(mActivity,preferenceHelperDataSource.getLanguageSettings().getCode());
    }

    @Override
    public void checkIsMailEmpty(String mail)
    {
        if(TextUtil.isEmpty(mail))
            addMailView.mailEmptyError();
        else if(TextUtil.emailValidation(mail))
            callAPIToAddAccount(mail);
        else
            addMailView.setInvalidMailError(mContext.getString(R.string.email_invalid));
    }

    /**
     * <h2>callAPIToAddAccount</h2>
     * used to call the add corporate profile API
     */
    private void callAPIToAddAccount(String emailId)
    {
        if (networkStateHolder.isConnected())
        {
            addMailView.showProgressDialog();
            Observable<Response<ResponseBody>> request = networkService.addCorporateProfile(
                    ((ApplicationClass) mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                    preferenceHelperDataSource.getLanguageSettings().getCode(),emailId);
            request.subscribeOn(Schedulers.io())
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
                            Utility.printLog(" onNext callAPIToAddAccount" + value.code());
                            switch (value.code())
                            {
                                case 200:
                                    addMailView.showAlert(DataParser.fetchSuccessMessage(value));
                                    break;

                                case 401:
                                    ExpireSession.refreshApplication(mContext,mqttManager,preferenceHelperDataSource,sqLiteDataSource);
                                    break;

                                case 502:
                                    addMailView.showToast(mContext.getString(R.string.bad_gateway));
                                    break;

                                default:
                                    addMailView.setInvalidMailError(DataParser.fetchErrorMessage(value));
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable e)
                        {
                            Utility.printLog(" onError callAPIToAddAccount" +e);
                            addMailView.dismissProgressDialog();
                            addMailView.showToast(mContext.getString(R.string.network_problem));
                        }

                        @Override
                        public void onComplete() {
                            addMailView.dismissProgressDialog();
                        }
                    });
        }
        else
            addMailView.showToast(mContext.getString(R.string.network_problem));
    }

    @Override
    public void disposeObservable() {
        compositeDisposable.clear();
    }
}
