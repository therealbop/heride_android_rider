package com.karru.managers.location;

import android.location.Location;
import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * <h1>RxLocationObserver</h1>
 * This class is created to observe the location change of the user
 * @author 3Embed
 * @since on 11-01-2018.
 */
public class RxLocationObserver extends Observable<Location>
{
    private static final String TAG = "RxLocationObserver";
    private Observer<?super Location> observer;

    @Override
    protected void subscribeActual(Observer<? super Location> observer)
    {
        this.observer = observer;
    }

    /**
     * <h2>publishData</h2>
     * This method is used to publish the data for network
     * @param location data to be pushed
     */
    void publishData(Location location)
    {
        if(observer!=null)
        {
            observer.onNext(location);
            observer.onComplete();
        }
    }
}
