package com.karru.landing.emergency_contact;

import android.content.Context;

import com.karru.api.NetworkService;
import com.heride.rider.R;
import com.google.gson.Gson;
import com.karru.ApplicationClass;
import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.data.source.local.sqlite.SQLiteDataSource;
import com.karru.landing.emergency_contact.model.EmergencyContactDetail;
import com.karru.landing.emergency_contact.model.UserContactInfo;
import com.karru.managers.network.NetworkStateHolder;
import com.karru.managers.network.RxNetworkObserver;
import com.karru.managers.user_vehicles.MQTTManager;
import com.karru.util.DataParser;
import com.karru.util.ExpireSession;
import com.karru.util.Validation;
import com.karru.utility.Utility;
import java.util.ArrayList;

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
import static com.karru.utility.Constants.EMERGENCY;

public class EmergencyContactPresenter implements EmergencyContactContract.EmergencyContactPresenter
{
    private static final String TAG = "EmergencyContactPresenter";
    private ArrayList<String> listOfContacts =new ArrayList<>();

    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject NetworkService networkService;
    @Inject MQTTManager mqttManager;
    @Inject SQLiteDataSource sqLiteDataSource;
    @Inject Context context;
    @Inject @Named(EMERGENCY) CompositeDisposable compositeDisposable;
    @Inject Gson gson;
    @Inject EmergencyContactContract.EmergencyContactView contactView;
    @Inject NetworkStateHolder networkStateHolder;
    @Inject EmergencyContactActivity activity;

    @Inject
    EmergencyContactPresenter() {}

    @Override
    public void checkRTLConversion() {
        RtlConversion(activity,preferenceHelperDataSource.getLanguageSettings().getCode());
    }

    @Override
    public void addContact(String name, String number)
    {
        if(!listOfContacts.contains(number))
        {
            if (networkStateHolder.isConnected())
            {
                Utility.printLog("AuthMsg"+((ApplicationClass) context.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()));
                contactView.showProgressDialog();
                Observable<Response<ResponseBody>> request = networkService.saveEmergencyContact(
                        ((ApplicationClass) context.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                        preferenceHelperDataSource.getLanguageSettings().getCode(), name, number);
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
                                Utility.printLog("SaveContact" + value.code());
                                switch (value.code())
                                {
                                    case 200:
                                        Utility.printLog("SaveContact" + DataParser.fetchSuccessMessage(value));
                                        getEmergencyContact();
                                        break;

                                    case 401:
                                        ExpireSession.refreshApplication(context,mqttManager,
                                                preferenceHelperDataSource,sqLiteDataSource);
                                        break;

                                    case 502:
                                        contactView.showToast(context.getString(R.string.bad_gateway));
                                        break;

                                    default:
                                        contactView.showToast(DataParser.fetchErrorMessage(value));
                                        break;
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                contactView.dismissProgressDialog();
                                contactView.showToast(context.getString(R.string.network_problem));
                            }

                            @Override
                            public void onComplete() {
                                contactView.dismissProgressDialog();
                            }
                        });
            }
            else
                contactView.showToast(context.getString(R.string.network_problem));
        }
        else
            contactView.showAlertForMultipleNumber();
    }

    @Override
    public void isArrayEmpty(ArrayList<UserContactInfo> contactDetailsList)
    {
        if(contactDetailsList.size()>0)
            contactView.hideProfileBackground();
        else
            contactView.showProfileBackground();
    }

    @Override
    public void deleteContact(String id, int position)
    {
        if (networkStateHolder.isConnected())
        {
            contactView.showProgressDialog();
            Observable<Response<ResponseBody>> request = networkService.deleteContact(
                    ((ApplicationClass) context.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                    preferenceHelperDataSource.getLanguageSettings().getCode(), id);
            request.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<ResponseBody>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            compositeDisposable.add(d);
                        }

                        @Override
                        public void onNext(Response<ResponseBody> value) {

                            Utility.printLog(" delete contact " + value.code());
                            switch (value.code())
                            {
                                case 200:
                                    Utility.printLog(" delete contact" + DataParser.fetchSuccessMessage(value));

                                    contactView.onDeleteSuccess(position);
                                    contactView.enableAddContactButton();
                                    break;

                                case 401:
                                    ExpireSession.refreshApplication(context,mqttManager,
                                            preferenceHelperDataSource,sqLiteDataSource);
                                    break;

                                case 502:
                                    contactView.showToast(context.getString(R.string.bad_gateway));
                                    break;

                                default:
                                    contactView.showToast(DataParser.fetchErrorMessage(value));
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable e)
                        {
                            contactView.dismissProgressDialog();
                            contactView.showToast(context.getString(R.string.network_problem));
                        }

                        @Override
                        public void onComplete() {
                            contactView.dismissProgressDialog();
                        }
                    });
        }
        else
            contactView.showToast(context.getString(R.string.network_problem));
    }

    @Override
    public void validateMobileNumber(String number,String name)
    {
        Utility.printLog(TAG+" is number valid "+Validation.isValidMobile(number));
        if(Validation.isValidMobile(number))
        {
            if(!listOfContacts.contains(number))
                contactView.addContact(name,number);
            else
                contactView.showAlertForMultipleNumber();
        }
        else
            contactView.showAlertForInvalidNumber();
    }

    @Override
    public void getEmergencyContact()
    {
        if (networkStateHolder.isConnected())
        {
            contactView.showProgressDialog();
            Observable<Response<ResponseBody>> request = networkService.getSavedEmergencyContact(
                    ((ApplicationClass) context.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                    preferenceHelperDataSource.getLanguageSettings().getCode());
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
                            Utility.printLog(" get contact " + value.code());
                            contactView.setLimit(preferenceHelperDataSource.getEmergencyContactLimit());
                            switch (value.code())
                            {
                                case 200:
                                    EmergencyContactDetail contactDetail = gson.fromJson(DataParser.fetchSuccessResponse(value),
                                            EmergencyContactDetail.class);
                                    if(contactDetail.getData().size()< preferenceHelperDataSource.getEmergencyContactLimit())
                                        contactView.enableAddContactButton();
                                    else
                                        contactView.disableAddContactButton();
                                    if(contactDetail.getData().size()>0)
                                    {
                                        contactView.setList(contactDetail.getData());
                                        listOfContacts.clear();

                                        for(UserContactInfo userContactInfo :contactDetail.getData())
                                            listOfContacts.add(userContactInfo.getContactNumber());
                                    }
                                    break;

                                case 401:
                                    ExpireSession.refreshApplication(context,mqttManager,
                                            preferenceHelperDataSource,sqLiteDataSource);
                                    break;

                                case 502:
                                    contactView.showToast(context.getString(R.string.bad_gateway));
                                    break;

                                default:
                                    contactView.showToast(DataParser.fetchErrorMessage(value));
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable e)
                        {
                            contactView.dismissProgressDialog();
                            contactView.showToast(context.getString(R.string.network_problem));
                        }

                        @Override
                        public void onComplete() {
                            contactView.dismissProgressDialog();
                        }
                    });
        }
        else
            contactView.showToast(context.getString(R.string.network_problem));
    }

    @Override
    public void updateContact(String id, int status)
    {
        if (networkStateHolder.isConnected())
        {
            contactView.showProgressDialog();
            Observable<Response<ResponseBody>> request = networkService.updateContactStatus(
                    ((ApplicationClass) context.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                    preferenceHelperDataSource.getLanguageSettings().getCode(), id, status);
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

                            Utility.printLog("SaveContactUpdate" + value.code());
                            switch (value.code())
                            {
                                case 200:
                                    Utility.printLog("SaveContactUpdate" + DataParser.fetchSuccessMessage(value));
                                    break;

                                case 401:
                                    ExpireSession.refreshApplication(context,mqttManager,
                                            preferenceHelperDataSource,sqLiteDataSource);
                                    break;

                                case 502:
                                    contactView.showToast(context.getString(R.string.bad_gateway));
                                    break;

                                default:
                                    contactView.showToast(DataParser.fetchErrorMessage(value));
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable e)
                        {
                            contactView.dismissProgressDialog();
                            contactView.showToast(context.getString(R.string.network_problem));
                        }

                        @Override
                        public void onComplete() {
                            contactView.dismissProgressDialog();
                        }
                    });
        }
        else
            contactView.showToast(context.getString(R.string.network_problem));
    }

    @Override
    public void clearDisposable()
    {
        compositeDisposable.clear();
    }
}
