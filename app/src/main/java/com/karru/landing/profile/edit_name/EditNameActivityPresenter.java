package com.karru.landing.profile.edit_name;

import android.content.Context;

import com.karru.api.NetworkService;
import com.heride.rider.R;
import com.karru.ApplicationClass;
import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.managers.network.NetworkStateHolder;
import com.karru.util.DataParser;
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
import static com.karru.utility.Constants.EDIT_NAME;

public class EditNameActivityPresenter implements EditNameActivityContract.EditNamePresenter
{
    @Inject Context context;
    @Inject EditNameActivity mActivity;
    @Inject NetworkService networkService;
    @Inject EditNameActivityContract.EditNameView editNameView;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject @Named(EDIT_NAME) CompositeDisposable compositeDisposable;
    @Inject NetworkStateHolder networkStateHolder;

    @Inject
    EditNameActivityPresenter() {}

    @Override
    public void checkRTLConversion() {
        RtlConversion(mActivity,preferenceHelperDataSource.getLanguageSettings().getCode());
    }


    @Override
    public void isNameEmpty(String name) {
        if(TextUtil.isEmpty(name))
            editNameView.onNameEmpty();
        else
            editNameView.onNameNonEmpty();
    }



    @Override
    public void nameUpdateRequest(String editName)
    {
        if (networkStateHolder.isConnected())
        {
            editNameView.showProgressDialog();
            Observable<Response<ResponseBody>> request = networkService.updateProfileEditName
                    (((ApplicationClass) context.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                            preferenceHelperDataSource.getLanguageSettings().getCode(), editName);

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
                            switch (value.code())
                            {
                                case 200:
                                    preferenceHelperDataSource.setUsername(editName);
                                    editNameView.loadingAlert(DataParser.fetchSuccessMessage(value));
                                    break;

                                case 502:
                                    editNameView.onResponseError(context.getString(R.string.bad_gateway));
                                    break;

                                default:
                                    Utility.printLog("EdinameActResponse"+"Error"+value.code());
                                    editNameView.onResponseError(DataParser.fetchErrorMessage(value));
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable e)
                        {
                            editNameView.onResponseError(context.getString(R.string.network_problem));
                            editNameView.dismissProgressDialog();
                        }

                        @Override
                        public void onComplete()
                        {
                            editNameView.dismissProgressDialog();
                        }
                    });
        }
        else
            editNameView.onResponseError(context.getString(R.string.network_problem));
    }

    @Override
    public void disposeObservable() {
        compositeDisposable.clear();
    }
}
