package com.karru.managers.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import androidx.annotation.Nullable;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.karru.utility.Utility;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider;

/**
 * <h2>LocationProvider</h2>
 * This method is used to provide the current location
 * @author 3Embed
 * @since on 11-01-2018.
 */
public class LocationProvider
{
    private static final String TAG = "LocationProvider";
    private ReactiveLocationProvider locationProvider;
    private LocationRequest locationRequest;
    private Disposable updatableLocationDisposable;
    private RxLocationObserver rxLocationObserver;
    @Nullable
    private Observable<Location> locationUpdatesObservable;

    /**
     * <h2>LocationProvider</h2>
     * @param context context of the application
     * @param rxLocationObserver observer to be published
     */
    public LocationProvider(Context context,RxLocationObserver rxLocationObserver)
    {
        locationProvider = new ReactiveLocationProvider(context);
        this.rxLocationObserver= rxLocationObserver;
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5000);
    }

    /**
     * <h2>startLocation</h2>
     * This method is used to start the location update
     */
    @SuppressLint("MissingPermission")
    public void startLocation(LocationCallBack locationCallBack)
    {
        locationUpdatesObservable = locationProvider
                .checkLocationSettings(new LocationSettingsRequest.Builder()
                        .addLocationRequest(locationRequest)
                        .setAlwaysShow(true)  //Refrence: http://stackoverflow.com/questions/29824408/google-play-services-locationservices-api-new-option-never
                        .build())
                .doOnNext(locationSettingsResult ->
                {
                    Status status = locationSettingsResult.getStatus();
                    if (status.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                        Utility.printLog(TAG+ "opening startResolutionForResult.");
                        locationCallBack.onLocationServiceDisabled(status);
                    }
                })
                .flatMap(locationSettingsResult ->
                        locationProvider.getUpdatedLocation(locationRequest))
                .observeOn(AndroidSchedulers.mainThread());

        subscribeLocationChange();
    }

    /**
     * <h2>subscribeLocationChange</h2>
     * This method is used to subscribe for the location change
     */
    private void subscribeLocationChange()
    {
        Observer<Location> observer = new Observer<Location>()
        {
            @Override
            public void onSubscribe(Disposable d)
            {
                updatableLocationDisposable=d;
            }
            @Override
            public void onNext(Location location)
            {
                rxLocationObserver.publishData(location);
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
        locationUpdatesObservable.subscribeOn(Schedulers.io());
        locationUpdatesObservable.observeOn(AndroidSchedulers.mainThread());
        locationUpdatesObservable.subscribe(observer);
    }

    /**
     * <h2>stopLocationUpdates</h2>
     * This method is used to stop the location updates
     */
    public void stopLocationUpdates()
    {
        locationUpdatesObservable = null;
        if(updatableLocationDisposable!=null)
            updatableLocationDisposable.dispose();
    }
}
