package com.karru.landing.my_vehicles;

import android.content.Context;
import android.os.Bundle;
import com.google.gson.Gson;
import com.karru.ApplicationClass;
import com.karru.api.NetworkService;
import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.data.source.local.sqlite.SQLiteDataSource;
import com.karru.managers.network.NetworkStateHolder;
import com.karru.managers.user_vehicles.MQTTManager;
import com.karru.util.DataParser;
import com.karru.util.ExpireSession;
import com.karru.utility.Utility;
import com.heride.rider.R;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
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
import static com.karru.utility.Constants.MY_VEHICLES;
import static com.karru.utility.Constants.VEHICLE_DETAIL;

/**
 * <h1>CorporateProfilePresenter</h1>
 * used to provide link between model and view
 */
public class MyVehiclesPresenter implements MyVehiclesContract.Presenter
{
    private static final String TAG = "CorporateProfilePresenter";
    @Inject Gson gson;
    @Inject Context mContext;
    @Inject MyVehiclesActivity mActivity;
    @Inject MQTTManager mqttManager;
    @Inject SQLiteDataSource addressDataSource;
    @Inject NetworkService networkService;
    @Inject NetworkStateHolder networkStateHolder;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject ArrayList<MyVehiclesDataModel> myVehiclesDataModels;
    @Inject MyVehiclesContract.View myVehicleView;
    @Inject @Named(MY_VEHICLES) CompositeDisposable compositeDisposable;

    @Inject
    MyVehiclesPresenter() { }

    @Override
    public void checkRTLConversion() {
        RtlConversion(mActivity,preferenceHelperDataSource.getLanguageSettings().getCode());
    }

    @Override
    public void fetchMyVehicles()
    {
        if(networkStateHolder.isConnected())
        {
            Observable<Response<ResponseBody>> request = networkService.fetchMyVehicles(
                    ((ApplicationClass) mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                    preferenceHelperDataSource.getLanguageSettings().getCode());

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
                            Utility.printLog(TAG+ " fetchMyVehicles onNext: "+value.code());
                            switch (value.code())
                            {
                                case 200:
                                    try
                                    {
                                        String response = value.body().string();
                                        MyVehiclesModel myVehiclesModel = gson.fromJson(response,MyVehiclesModel.class);
                                        myVehiclesDataModels.clear();
                                        if(myVehiclesModel.getData().size()>0)
                                        {
                                            if(preferenceHelperDataSource.getDefaultVehicle() != null)
                                            {
                                                for(MyVehiclesDataModel myVehiclesDataModel:myVehiclesModel.getData())
                                                {
                                                    if(preferenceHelperDataSource.getDefaultVehicle().getId().equals(myVehiclesDataModel.getId()))
                                                    {
                                                        myVehiclesDataModel.setVehicleDefault(true);
                                                        myVehiclesDataModels.add(myVehiclesDataModel);
                                                    }
                                                    else
                                                    {
                                                        myVehiclesDataModel.setVehicleDefault(false);
                                                        myVehiclesDataModels.add(myVehiclesDataModel);
                                                    }
                                                }
                                            }
                                            else
                                            {
                                                myVehiclesModel.getData().get(0).setVehicleDefault(true);
                                                preferenceHelperDataSource.setDefaultVehicle(myVehiclesModel.getData().get(0));
                                                myVehiclesDataModels.addAll(myVehiclesModel.getData());
                                            }
                                            myVehicleView.showVehiclesList();
                                        }
                                        else
                                        {
                                            preferenceHelperDataSource.setDefaultVehicle(null);
                                            myVehicleView.showNoVehicles();
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    break;

                                case 401:
                                    ExpireSession.refreshApplication(mContext,mqttManager,
                                            preferenceHelperDataSource,addressDataSource);
                                    break;

                                case 502:
                                    myVehicleView.showToast(mContext.getString(R.string.bad_gateway));
                                    break;

                                default:
                                    myVehicleView.showToast(DataParser.fetchErrorMessage(value));
                                    break;
                            }
                        }
                        @Override
                        public void onError(Throwable e)
                        {
                            Utility.printLog(TAG+ " fetchMyVehicles error: "+e.getMessage());
                        }

                        @Override
                        public void onComplete()
                        {
                        }
                    });
        }
    }

    @Override
    public void deleteVehicle(int position)
    {
        if(networkStateHolder.isConnected())
        {
            myVehicleView.showProgressDialog();
            Observable<Response<ResponseBody>> request = networkService.deleteVehicle(
                    ((ApplicationClass) mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                    preferenceHelperDataSource.getLanguageSettings().getCode(),myVehiclesDataModels.get(position).getId());

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
                            Utility.printLog(TAG+ " fetchMyVehicles onNext: "+value.code());
                            switch (value.code())
                            {
                                case 200:
                                    myVehicleView.showToast(DataParser.fetchSuccessMessage(value));
                                    if(myVehiclesDataModels.get(position).isDefault() && myVehiclesDataModels.size()>1)
                                        preferenceHelperDataSource.setDefaultVehicle(null);
                                    myVehiclesDataModels.remove(position);
                                    if(myVehiclesDataModels.size()>0)
                                    {
                                        if(preferenceHelperDataSource.getDefaultVehicle() == null)
                                        {
                                            preferenceHelperDataSource.setDefaultVehicle(myVehiclesDataModels.get(0));
                                            myVehiclesDataModels.get(0).setVehicleDefault(true);
                                        }
                                        myVehicleView.showVehiclesList();
                                    }
                                    else
                                    {
                                        preferenceHelperDataSource.setDefaultVehicle(null);
                                        myVehicleView.showNoVehicles();
                                    }
                                    break;

                                case 401:
                                    ExpireSession.refreshApplication(mContext,mqttManager,
                                            preferenceHelperDataSource,addressDataSource);
                                    break;

                                case 502:
                                    myVehicleView.showToast(mContext.getString(R.string.bad_gateway));
                                    break;

                                default:
                                    myVehicleView.showToast(DataParser.fetchErrorMessage(value));
                                    break;
                            }
                        }
                        @Override
                        public void onError(Throwable e)
                        {
                            myVehicleView.dismissProgressDialog();
                            Utility.printLog(TAG+ " fetchMyVehicles error: "+e.getMessage());
                        }

                        @Override
                        public void onComplete()
                        {
                            myVehicleView.dismissProgressDialog();
                        }
                    });
        }
    }

    @Override
    public void handleDefaultVehicle(MyVehiclesDataModel myVehiclesDataModel,int comingFrom) {
        preferenceHelperDataSource.setDefaultVehicle(myVehiclesDataModel);
        if(comingFrom == 1)
            myVehicleView.setDefaultVehicle(myVehiclesDataModel.toString());
    }

    @Override
    public void addVehicle(String vehicleData) {
        try {
            JSONObject jsonObject = new JSONObject(vehicleData);
            JSONObject dataObject = jsonObject.getJSONObject("data");
            myVehicleView.showToast(jsonObject.getString("message"));

            MyVehiclesDataModel myVehiclesDataModel = gson.fromJson(dataObject.toString(),MyVehiclesDataModel.class);
            if(preferenceHelperDataSource.getDefaultVehicle() == null)
            {
                preferenceHelperDataSource.setDefaultVehicle(myVehiclesDataModel);
                myVehiclesDataModel.setVehicleDefault(true);
            }
            myVehiclesDataModels.add(myVehiclesDataModel);
            if(myVehiclesDataModels.size()>0)
                myVehicleView.showVehiclesList();
            else
                myVehicleView.showNoVehicles();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disposeObservable() {
        compositeDisposable.clear();
    }
}
