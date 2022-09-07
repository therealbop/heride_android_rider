package com.karru.managers.location;

import android.location.Address;
import com.karru.utility.Utility;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider;

/**
 * <h1>AddressProviderFromLocation</h1>
 * This class is used to provide the address from given location
 * @author 3Embed
 * @since on 12-01-2018.
 */
public class AddressProviderFromLocation
{
    private static final String TAG = "AddressProviderFromLocation";
    private ReactiveLocationProvider locationProvider;
    private Disposable addressDisposable;
    private Observable<Address> addressObservable;
    private RxAddressObserver rxAddressObserver;

    /**
     * <h2>AddressProviderFromLocation</h2>
     * constructor of the class
     * @param locationProvider location provider object
     * @param rxAddressObserver observer object
     */
    public AddressProviderFromLocation(ReactiveLocationProvider locationProvider,RxAddressObserver rxAddressObserver)
    {
        this.locationProvider = locationProvider;
        this.rxAddressObserver = rxAddressObserver;
    }

    /**
     * <h2>getAddressFromLocation</h2>
     * This method is used to get the address from location
     * @param currentLat current latitude
     * @param currentLng current longitude
     */
    public void getAddressFromLocation(double currentLat,double currentLng,String serverKey)
    {
        Utility.printLog(TAG+" address getAddressFromLocation  "+currentLat+" "+currentLng);
        addressObservable =  locationProvider.getReverseGeocodeObservable(currentLat, currentLng, 1,serverKey)
                .map(addresses -> addresses != null && !addresses.isEmpty() ? addresses.get(0) : null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        subscribeAddressChange();
    }

    /**
     * <h2>subscribeLocationChange</h2>
     * This method is used to subscribe for the location change
     */
    private void subscribeAddressChange()
    {
        Observer<Address> observer = new Observer<Address>()
        {
            @Override
            public void onSubscribe(Disposable d)
            {
                addressDisposable = d;
            }
            @Override
            public void onNext(Address address)
            {
                Utility.printLog(TAG+" onNext address observed  "+address.toString());
                rxAddressObserver.publishAddress(address);
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
        addressObservable.subscribeOn(Schedulers.io());
        addressObservable.observeOn(AndroidSchedulers.mainThread());
        addressObservable.subscribe(observer);
    }

    /**
     * <h2>stopAddressListener</h2>
     * This method is used to stop the address update
     */
    public void stopAddressListener()
    {
        if(addressDisposable!=null)
            addressDisposable.dispose();
    }
}
