package com.karru.managers.booking;

import com.karru.landing.home.model.OnGoingBookingsModel;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * <h1>RxOnGoingBookingsDetailsObserver</h1>
 * This class is used to publish the ongoing booking details data
 * @author 3Embed
 * @since on 29-01-2018.
 */

public class RxOnGoingBookingsDetailsObserver extends Observable<ArrayList<OnGoingBookingsModel>>
{
    private Observer<? super ArrayList<OnGoingBookingsModel>> observer;

    @Override
    protected void subscribeActual(Observer<? super ArrayList<OnGoingBookingsModel>> observer)
    {
        this.observer = observer;
    }

    /**
     * <h2>publishData</h2>
     * This method is used to publish the data for network
     * @param data data to be pushed
     */
    void publishOnGoingBookingDetails(ArrayList<OnGoingBookingsModel> data)
    {
        if (observer != null) {
            observer.onNext(data);
            observer.onComplete();
        }
    }
}
