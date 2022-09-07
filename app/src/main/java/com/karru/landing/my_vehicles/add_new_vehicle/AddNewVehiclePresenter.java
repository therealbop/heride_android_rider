package com.karru.landing.my_vehicles.add_new_vehicle;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.karru.ApplicationClass;
import com.karru.api.NetworkService;
import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.data.source.local.sqlite.SQLiteDataSource;
import com.karru.managers.network.NetworkStateHolder;
import com.karru.managers.user_vehicles.MQTTManager;
import com.karru.util.DataParser;
import com.karru.util.TextUtil;
import com.karru.utility.Utility;
import com.heride.rider.R;
import org.json.JSONArray;
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
import static com.karru.utility.Constants.ADD_NEW_VEHICLE;

public class AddNewVehiclePresenter implements AddNewVehicleContract.Presenter {
    public static final String TAG = "PromoCodePresenter";
    @Inject NetworkStateHolder networkStateHolder;
    @Inject NetworkService networkService;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject Context mContext;
    @Inject AddNewVehicleActivity mActivity;
    @Inject MQTTManager mqttManager;
    @Inject SQLiteDataSource addressDataSource;
    @Inject Gson gson;
    @Inject AddNewVehicleContract.View addVehicleView;
    @Inject com.karru.util.Utility utility;
    private ArrayList<String> yearData;
    private ArrayList<MakesModelsDataModel> makesData;
    private ArrayList<MakesModelsDataModel> modelsData;
    @Inject @Named(ADD_NEW_VEHICLE) CompositeDisposable compositeDisposable;
    private int yearIndex,  makeIndex, modelIndex;
    private String color;

    @Inject AddNewVehiclePresenter() { }

    @Override
    public void checkRTLConversion() {
        RtlConversion(mActivity,preferenceHelperDataSource.getLanguageSettings().getCode());
    }

    @Override
    public void fetchVehicleDetailList(int type,int year,int make) {
        if(networkStateHolder.isConnected())
        {
            addVehicleView.showProgressDialog();
            Observable<Response<ResponseBody>> request = null;
            switch (type)
            {
                case 1:
                    yearData = new ArrayList<>();
                    request = networkService.getYearData(((ApplicationClass)mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                            preferenceHelperDataSource.getLanguageSettings().getCode());
                    break;

                case 2:
                    makesData = new ArrayList<>();
                    request = networkService.getMakeData(((ApplicationClass)mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                            preferenceHelperDataSource.getLanguageSettings().getCode(),yearData.get(year));
                    break;

                case 3:
                    modelsData = new ArrayList<>();
                    request = networkService.getModelData(((ApplicationClass)mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                            preferenceHelperDataSource.getLanguageSettings().getCode(),yearData.get(year),makesData.get(make).getMakeId());
                    break;
            }
            request.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<ResponseBody>>() {
                        @Override
                        public void onSubscribe(Disposable d)
                        {
                            compositeDisposable.add(d);
                        }
                        @Override
                        public void onNext(Response<ResponseBody> result)
                        {
                            Utility.printLog(TAG+" fetchVehicleDetailList onNext"+result.code());
                            switch (result.code())
                            {
                                case 200:
                                    try
                                    {
                                        JSONArray yearStringData =DataParser.fetchStringArrayFromData(result);
                                        for(int i=0;i<yearStringData.length();i++)
                                        {
                                            switch (type)
                                            {
                                                case 1:
                                                    yearData.add(yearStringData.getString(i));
                                                    break;

                                                case 2:
                                                    JSONObject jsonObject = yearStringData.getJSONObject(i);
                                                    MakesModelsDataModel makesDataModel = new MakesModelsDataModel();
                                                    makesDataModel.setMakeId(jsonObject.getString("id"));
                                                    makesDataModel.setMakeName(jsonObject.getString("name"));
                                                    makesData.add(makesDataModel);
                                                    break;

                                                case 3:
                                                    JSONObject jsonObjectModel = yearStringData.getJSONObject(i);
                                                    MakesModelsDataModel modelsDataModel = new MakesModelsDataModel();
                                                    modelsDataModel.setModelId(jsonObjectModel.getString("id"));
                                                    modelsDataModel.setModelName(jsonObjectModel.getString("name"));
                                                    modelsData.add(modelsDataModel);
                                                    break;
                                            }
                                        }
                                        switch (type)
                                        {
                                            case 1:
                                                addVehicleView.populateYearsSpinner(yearData);
                                                break;

                                            case 2:
                                                ArrayList<String> makesList = new ArrayList<>();
                                                for(MakesModelsDataModel makesDataModel :makesData)
                                                    makesList.add(makesDataModel.getMakeName());
                                                addVehicleView.populateMakesSpinner(makesList);
                                                break;

                                            case 3:
                                                ArrayList<String> modelList = new ArrayList<>();
                                                for(MakesModelsDataModel makesDataModel :modelsData)
                                                    modelList.add(makesDataModel.getModelName());
                                                addVehicleView.populateModelsSpinner(modelList);
                                                break;
                                        }
                                    } catch ( NullPointerException | JsonSyntaxException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    break;

                                case 502:
                                    addVehicleView.showToast(mContext.getString(R.string.bad_gateway));
                                    break;

                                default:
                                    addVehicleView.showToast(DataParser.fetchErrorMessage(result));
                                    break;
                            }
                        }
                        @Override
                        public void onError(Throwable errorMsg)
                        {
                            addVehicleView.dismissProgressDialog();
                            addVehicleView.showToast(mContext.getString(R.string.network_problem));
                        }
                        @Override
                        public void onComplete()
                        {
                            addVehicleView.dismissProgressDialog();
                        }
                    });
        }
        else
            addVehicleView.showToast(mContext.getString(R.string.network_problem));
    }

    @Override
    public void saveVehicleDetails() {
        if(networkStateHolder.isConnected())
        {
            addVehicleView.showProgressDialog();
            Observable<Response<ResponseBody>> request = networkService.saveVehicleDetails(((ApplicationClass)mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                    preferenceHelperDataSource.getLanguageSettings().getCode(),yearData.get(yearIndex),
                    makesData.get(makeIndex).getMakeName(),modelsData.get(modelIndex).getModelName(),color);
            request.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<ResponseBody>>() {
                        @Override
                        public void onSubscribe(Disposable d)
                        {
                            compositeDisposable.add(d);
                        }
                        @Override
                        public void onNext(Response<ResponseBody> result)
                        {
                            Utility.printLog(TAG+" saveVehicleDetails onNext"+result.code());
                            switch (result.code())
                            {
                                case 200:
                                    try {
                                        String response = result.body().string();
                                        addVehicleView.finishActivity(response);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    break;

                                case 502:
                                    addVehicleView.showToast(mContext.getString(R.string.bad_gateway));
                                    break;

                                default:
                                    addVehicleView.showToast(DataParser.fetchErrorMessage(result));
                                    break;
                            }
                        }
                        @Override
                        public void onError(Throwable errorMsg)
                        {
                            addVehicleView.dismissProgressDialog();
                            addVehicleView.showToast(mContext.getString(R.string.network_problem));
                        }
                        @Override
                        public void onComplete()
                        {
                            addVehicleView.dismissProgressDialog();
                        }
                    });
        }
        else
            addVehicleView.showToast(mContext.getString(R.string.network_problem));
    }

    @Override
    public void validateSavingVehicle(int yearIndex, int makeIndex, int modelIndex, String colorText) {
        if(yearData.size()>0 && makesData.size()>0 && modelsData.size()>0)
        {
            this.yearIndex = yearIndex;
            this.makeIndex = makeIndex;
            this.modelIndex = modelIndex;
            this.color = colorText;
            if(!TextUtil.isEmpty(modelsData.get(modelIndex).getModelId()) && !TextUtil.isEmpty(colorText))
                addVehicleView.enableSaveOption();
            else
                addVehicleView.disableSaveOption();
        }
        else
            addVehicleView.disableSaveOption();
    }
}

