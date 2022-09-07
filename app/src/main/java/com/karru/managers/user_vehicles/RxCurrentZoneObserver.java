package com.karru.managers.user_vehicles;

import com.karru.landing.home.model.AreaZoneDetails;
import io.reactivex.Observer;

/**
 * <h1>RxCurrentZoneObserver</h1>
 * This class is used to observe the user current zone
 * @author 3Embed
 * @since on 03-01-2018.
 */
public class RxCurrentZoneObserver extends io.reactivex.Observable<AreaZoneDetails> {
    private Observer<?super AreaZoneDetails> observer;

    @Override
    protected void subscribeActual(Observer<? super AreaZoneDetails> observer) {
        this.observer = observer;
    }

    /**
     * <h2>publishData</h2>
     * This method is used to publish the data for network
     * @param data data to be pushed
     */
    public void publishAreaZones(AreaZoneDetails data)
    {
        if(observer!=null)
        {
            observer.onNext(data);
            observer.onComplete();
        }
    }
}
