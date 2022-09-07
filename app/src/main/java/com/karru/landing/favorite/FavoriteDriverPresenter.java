package com.karru.landing.favorite;

import android.content.Context;

import com.google.gson.Gson;
import com.karru.ApplicationClass;
import com.karru.api.NetworkService;
import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.data.source.local.sqlite.SQLiteDataSource;
import com.karru.landing.favorite.model.FavoritesDriversDetails;
import com.karru.landing.favorite.model.FavoritesModel;
import com.karru.landing.favorite.view.FavoriteDriversActivity;
import com.karru.managers.network.NetworkStateHolder;
import com.karru.managers.user_vehicles.MQTTManager;
import com.karru.util.DataParser;
import com.karru.util.ExpireSession;
import com.karru.utility.Utility;
import com.heride.rider.R;
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
import static com.karru.utility.Constants.FAVORITE_DRIVER;
import static com.karru.utility.Constants.FAVORITE_OFFLINE_DRIVER;
import static com.karru.utility.Constants.FAVORITE_ONLINE_DRIVER;

/**
 * <h1>FavoriteDriverPresenter</h1>
 * used to provide link between model and view
 */
public class FavoriteDriverPresenter implements FavoriteDriversContract.Presenter
{
    private static final String TAG = "FavoriteDriverPresenter";

    @Inject Gson gson;
    @Inject Context mContext;
    @Inject FavoriteDriversActivity mActivity;
    @Inject NetworkService networkService;
    @Inject NetworkStateHolder networkStateHolder;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject FavoriteDriversContract.View favoriteView;
    @Inject MQTTManager mqttManager;
    @Inject SQLiteDataSource sqLiteDataSource;
    @Inject @Named(FAVORITE_DRIVER) CompositeDisposable compositeDisposable;
    @Inject @Named(FAVORITE_ONLINE_DRIVER) ArrayList<FavoritesDriversDetails> favoriteOnlineDriversDetails;
    @Inject @Named(FAVORITE_OFFLINE_DRIVER) ArrayList<FavoritesDriversDetails> favoriteOfflineDriversDetails;

    @Inject FavoriteDriverPresenter() { }

    @Override
    public void checkRTLConversion() {
        RtlConversion(mActivity,preferenceHelperDataSource.getLanguageSettings().getCode());
    }

    @Override
    public void getFavoriteDrivers()
    {
        if (networkStateHolder.isConnected())
        {
            favoriteView.showProgressDialog();
            Observable<Response<ResponseBody>> request = networkService.getFavorites(
                    ((ApplicationClass) mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
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
                            switch (value.code())
                            {
                                case 200:
                                    Utility.printLog(" onNext getFavoriteDrivers" + value.code());
                                    try
                                    {
                                        String responseString = value.body().string();
                                        Utility.printLog(TAG+" onNext FavoritesModel size "+responseString);
                                        FavoritesModel favoritesModel = gson.fromJson(responseString,
                                                FavoritesModel.class);
                                        favoriteOnlineDriversDetails.clear();
                                        favoriteOfflineDriversDetails.clear();
                                        for(FavoritesDriversDetails favoritesDriversDetails : favoritesModel.getData())
                                        {
                                            if (favoritesDriversDetails.isOnline())
                                                favoriteOnlineDriversDetails.add(favoritesDriversDetails);
                                            else
                                                favoriteOfflineDriversDetails.add(favoritesDriversDetails);
                                        }
                                        if(favoritesModel.getData().size()>0)
                                        {
                                            if(favoriteOnlineDriversDetails.size() == 0)
                                                favoriteView.hideOnlineLayout();
                                            if(favoriteOfflineDriversDetails.size() == 0)
                                                favoriteView.hideOfflineLayout();
                                            favoriteView.hideEmptyScreen();
                                        }
                                        else
                                            favoriteView.showEmptyScreen();
                                    }
                                    catch (IOException e)
                                    {
                                        e.printStackTrace();
                                    }
                                    break;

                                case 401:
                                    ExpireSession.refreshApplication(mContext,mqttManager,preferenceHelperDataSource,sqLiteDataSource);
                                    break;

                                case 502:
                                    favoriteView.showToast(mContext.getString(R.string.bad_gateway));
                                    break;

                                default:
                                    favoriteView.showToast(DataParser.fetchErrorMessage(value));
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable e)
                        {
                            Utility.printLog(" onError getFavoriteDrivers" + e);
                            favoriteView.dismissProgressDialog();
                            favoriteView.showToast(mContext.getString(R.string.network_problem));
                        }

                        @Override
                        public void onComplete() {
                            favoriteView.dismissProgressDialog();
                        }
                    });
        }
        else
            favoriteView.showToast(mContext.getString(R.string.network_problem));
    }

    @Override
    public void deleteDriverFromFav(String driverId)
    {
        if (networkStateHolder.isConnected())
        {
            favoriteView.showProgressDialog();
            Observable<Response<ResponseBody>> request = networkService.deleteDriverFromFav(
                    ((ApplicationClass) mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                    preferenceHelperDataSource.getLanguageSettings().getCode(),driverId);
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
                                    Utility.printLog(" onNext deleteDriverFromFav" + value.code()+
                                    " "+driverId);
                                    favoriteView.showToast(DataParser.fetchSuccessMessage(value));
                                    for (int i=0; i<favoriteOfflineDriversDetails.size(); i++)
                                    {
                                        if(favoriteOfflineDriversDetails.get(i).getDriverId().equals(driverId))
                                        {
                                            favoriteOfflineDriversDetails.remove(i);
                                            break;
                                        }
                                    }
                                    for (int i=0; i<favoriteOnlineDriversDetails.size(); i++)
                                    {
                                        if(favoriteOnlineDriversDetails.get(i).getDriverId().equals(driverId))
                                        {
                                            favoriteOnlineDriversDetails.remove(i);
                                            break;
                                        }
                                    }

                                    if(favoriteOfflineDriversDetails.size()>0 || favoriteOnlineDriversDetails.size()>0)
                                    {
                                        if(favoriteOnlineDriversDetails.size() == 0)
                                            favoriteView.hideOnlineLayout();
                                        if(favoriteOfflineDriversDetails.size() == 0)
                                            favoriteView.hideOfflineLayout();
                                        favoriteView.hideEmptyScreen();
                                    }
                                    else
                                        favoriteView.showEmptyScreen();
                                    break;

                                case 401:
                                    ExpireSession.refreshApplication(mContext,mqttManager,preferenceHelperDataSource,sqLiteDataSource);
                                    break;

                                case 502:
                                    favoriteView.showToast(mContext.getString(R.string.bad_gateway));
                                    break;

                                default:
                                    favoriteView.showToast(DataParser.fetchErrorMessage(value));
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable e)
                        {
                            Utility.printLog(" onError deleteDriverFromFav" + e);
                            favoriteView.dismissProgressDialog();
                            favoriteView.showToast(mContext.getString(R.string.network_problem));
                        }

                        @Override
                        public void onComplete() {
                            favoriteView.dismissProgressDialog();
                        }
                    });
        }
        else
            favoriteView.showToast(mContext.getString(R.string.network_problem));
    }

    @Override
    public void handleDeleteFavDriver(String driverId)
    {
        favoriteView.showConfirmationAlert(driverId);
    }

    @Override
    public void disposeObservable() {
        compositeDisposable.clear();
    }
}
