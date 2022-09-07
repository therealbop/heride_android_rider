package com.karru.booking_flow.location_from_map;


import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Location;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.karru.api.NetworkService;
import com.karru.data.source.local.shared_preference.PreferencesHelper;
import com.karru.data.source.local.sqlite.SQLiteDataSource;
import com.karru.managers.location.AddressProviderFromLocation;
import com.karru.managers.location.LocationManagerCallback;
import com.karru.managers.location.LocationProvider;
import com.karru.managers.location.RxAddressObserver;
import com.karru.managers.location.RxLocationObserver;
import com.karru.managers.network.NetworkStateHolder;
import com.karru.booking_flow.address.model.FavAddressDataModel;
import com.karru.landing.home.model.HomeActivityModel;
import com.karru.util.DataParser;
import com.karru.utility.Utility;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.karru.util.Utility.RtlConversion;
import static com.karru.utility.Constants.GMT_CURRENT_LAT;
import static com.karru.utility.Constants.GMT_CURRENT_LNG;
import static com.karru.utility.Constants.LOCATION_FROM_MAP;

public class LocationFromMapPresenter implements LocationFromMapContract.LocationFromMapPresenter,
        LocationManagerCallback.CallBacks
{
    private final String TAG = "LocationFromMapPresenter";

    @Inject Context mContext;
    @Inject LocationFromMapActivity mActivity;
    @Inject LocationFromMapContract.LocationFromMapView locationView;
    @Inject Gson gson;
    @Inject NetworkService networkService;
    @Inject SQLiteDataSource addressDataSource;
    @Inject HomeActivityModel mHomActivityModel;
    @Inject NetworkStateHolder networkStateHolder;
    @Inject FavAddressDataModel favAddressDataModel;
    @Inject
    @Named(LOCATION_FROM_MAP)
    CompositeDisposable compositeDisposable;
    @Inject PreferencesHelper preferenceHelperDataSource;
    @Inject LocationProvider locationProvider;
    @Inject AddressProviderFromLocation addressProvider;

    private RxLocationObserver rxLocationObserver;
    private RxAddressObserver rxAddressObserver;

    @Inject
    LocationFromMapPresenter(RxLocationObserver rxLocationObserver, RxAddressObserver rxAddressObserver) {

        this.rxLocationObserver = rxLocationObserver;
        this.rxAddressObserver = rxAddressObserver;
    }

    @Override
    public void checkRTLConversion() {
        RtlConversion(mActivity,preferenceHelperDataSource.getLanguageSettings().getCode());
    }

    @Override
    public void disposeObservable() {
        compositeDisposable.clear();
    }

    @Override
    public void subscribeLocationChange()
    {
        Observer<Location> observer = new Observer<Location>()
        {
            @Override
            public void onSubscribe(Disposable d)
            {
                compositeDisposable.add(d);
            }
            @Override
            public void onNext(Location location)
            {
                Utility.printLog(TAG+" onNext location oberved  "+location.getLatitude()
                        +" "+location.getLongitude());
                GMT_CURRENT_LAT = location.getLatitude()+"";
                GMT_CURRENT_LNG = location.getLongitude()+"";

                if(location.getLatitude() != 0 && location.getLatitude() != mHomActivityModel.getMapCenterLatitude()
                        && location.getLongitude() != 0 && location.getLongitude() != mHomActivityModel.getMapCenterLongitude())
                {
                    preferenceHelperDataSource.setCurrLatitude(String.valueOf(mHomActivityModel.getMapCenterLatitude()));
                    preferenceHelperDataSource.setCurrLongitude(String.valueOf(mHomActivityModel.getMapCenterLongitude()));
                    if(mHomActivityModel.isMoveToCurrLocation())
                    {
                        mHomActivityModel.setMoveToCurrLocation(false);
                        mHomActivityModel.setLastKnownLocation(location);
                        mHomActivityModel.setMapCenterLatitude( location.getLatitude());
                        mHomActivityModel.setMapCenterLongitude(location.getLongitude());
                        mHomActivityModel.setPrevMapCenterLatitude(mHomActivityModel.getMapCenterLatitude());
                        mHomActivityModel.setPrevMapCenterLongitude( mHomActivityModel.getMapCenterLongitude());
                        locationView.updateCameraPosition(mHomActivityModel.getMapCenterLatitude(), mHomActivityModel.getMapCenterLongitude());
                    }
                }
            }
            @Override
            public void onError(Throwable e)
            {
                e.printStackTrace();
                Utility.printLog(TAG+" onAddCardError location oberved  "+e);
            }
            @Override
            public void onComplete()
            {}
        };
        rxLocationObserver.subscribeOn(Schedulers.io());
        rxLocationObserver.observeOn(AndroidSchedulers.mainThread());
        rxLocationObserver.subscribe(observer);
    }

    @Override
    public void getStoredAddresses()
    {
        locationView.setStoredAddresses(preferenceHelperDataSource.getPickUpAddress(),
                preferenceHelperDataSource.getDropAddress());
    }

    @Override
    public void storePickAddress(String pickAddress) {
        preferenceHelperDataSource.setPickUpAddress(pickAddress);
    }

    @Override
    public void storeDropAddress(String storeDropAddress) {
        preferenceHelperDataSource.setDropAddress(storeDropAddress);
    }

    @Override
    public void subscribeAddressChange()
    {
        Observer<Address> observer = new Observer<Address>()
        {
            @Override
            public void onSubscribe(Disposable d)
            {
                compositeDisposable.add(d);
            }
            @Override
            public void onNext(Address address)
            {
                Utility.printLog(TAG+" onNext address observed  "+address.toString());
                if(address!=null)
                {
                    String full_address = DataParser.getStringAddress(address);
                    Utility.printLog(TAG+ " onNext getAddressFromLocation full_address: " + full_address);
                    favAddressDataModel.setAddress(full_address);
                    preferenceHelperDataSource.setDropAddress(full_address);
                    locationView.updateAddress(full_address);
                }
            }
            @Override
            public void onError(Throwable e)
            {
                e.printStackTrace();
                Utility.printLog(TAG+" onAddCardError address observed  "+e);
            }
            @Override
            public void onComplete()
            {}
        };
        rxAddressObserver.subscribeOn(Schedulers.io());
        rxAddressObserver.observeOn(AndroidSchedulers.mainThread());
        rxAddressObserver.subscribe(observer);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void getCurrentLocation()
    {
        Utility.printLog(TAG+" inside getCurrentLocation ");
        locationProvider.startLocation(this);
    }

    /**
     * <h2>getAddressFromLocation</h2>
     * This method is used to get the address from latlong
     * @param currentLat latitude of the location
     * @param currentLng longitude of the location
     */
    private void getAddressFromLocation(final double currentLat, final double currentLng)
    {
        Utility.printLog(TAG+" address getAddressFromLocation  "+currentLat+" "+currentLng);
        favAddressDataModel.setLatitude(currentLat);
        favAddressDataModel.setLongitude(currentLng);
        preferenceHelperDataSource.setDropLatitude(currentLat+"");
        preferenceHelperDataSource.setDropLongitude(currentLng+"");
        addressProvider.getAddressFromLocation(currentLat,currentLng,preferenceHelperDataSource.getGoogleServerKey());
    }

    @Override
    public void onUpdateLocation(Location location)
    {

    }

    @Override
    public void locationMsg(String error)
    {

    }

    @Override
    public void onLocationServiceDisabled(Status status)
    {

    }
    public void onResumeActivity()
    {
        Utility.printLog(TAG + "isFromOnCreateView out " + mHomActivityModel.isFromOnCreateView());
        if (mHomActivityModel.isFromOnCreateView())
        {
            Utility.printLog(TAG + "isFromOnCreateView inside " + mHomActivityModel.isFromOnCreateView());
            mHomActivityModel.setFromOnCreateView(false);
        }
    }

    public void findCurrentLocation()
    {
        Utility.printLog("findCurrentLocation"+preferenceHelperDataSource.getCurrLatitude());
        if(!preferenceHelperDataSource.getCurrLatitude().equals("0.0") && !preferenceHelperDataSource.getCurrLongitude().equals("0.0") ) {
            Utility.printLog("findCurrentLocation1"+mHomActivityModel.getMapCenterLatitude());
            double lat = Double.parseDouble(preferenceHelperDataSource.getCurrLatitude());
            double lang = Double.parseDouble(preferenceHelperDataSource.getCurrLongitude());
            locationView.moveGoogleMapToLocation(lat,lang);
        }
    }


    public void verifyAndUpdateNewLocation(LatLng centerFromPoint) {
        if (centerFromPoint != null) {
            mHomActivityModel.setMapCenterLatitude(centerFromPoint.latitude);
            mHomActivityModel.setMapCenterLongitude(centerFromPoint.longitude);
        }

        if (mHomActivityModel.getMapCenterLatitude() == 0 || mHomActivityModel.getMapCenterLongitude() == 0 ||
                (mHomActivityModel.getMapCenterLatitude() == mHomActivityModel.getPrevMapCenterLatitude() &&
                        mHomActivityModel.getMapCenterLongitude() == mHomActivityModel.getPrevMapCenterLongitude())) {
            Utility.printLog(TAG + "verifyAndUpdateNewLocation() FALSE");
        }
        else
        {
            preferenceHelperDataSource.setCurrLatitude(String.valueOf(mHomActivityModel.getMapCenterLatitude()));
            preferenceHelperDataSource.setCurrLongitude(String.valueOf(mHomActivityModel.getMapCenterLongitude()));

            Utility.printLog(TAG + "verifyAndUpdateNewLocation() TRUE");
            getAddressFromLocation(mHomActivityModel.getMapCenterLatitude(),
                    mHomActivityModel.getMapCenterLongitude());

            mHomActivityModel.setPrevMapCenterLatitude(mHomActivityModel.getMapCenterLatitude());
            mHomActivityModel.setPrevMapCenterLongitude(mHomActivityModel.getMapCenterLongitude());

        }
    }
}
