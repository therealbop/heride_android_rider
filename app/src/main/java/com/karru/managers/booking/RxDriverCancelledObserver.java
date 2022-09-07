package com.karru.managers.booking;

import com.karru.landing.home.model.DriverCancellationModel;

import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * <h1>RxDriverCancelledObserver</h1>
 * used to push the driver cancellation model
 * @author TechOps
 * @since on 2/13/2018.
 */

public class RxDriverCancelledObserver extends Observable<DriverCancellationModel>
{
    private Observer<?super DriverCancellationModel> observer;

    @Override
    protected void subscribeActual(Observer<? super DriverCancellationModel> observer) {
        this.observer = observer;
    }

    /**
     * <h2>publishDriverDetails</h2>
     * This method is used to publish the driver details
     * @param data driver details to be pushed
     */
    void publishDriverCancelDetails(DriverCancellationModel data)
    {
        if(observer!=null)
        {
            observer.onNext(data);
            observer.onComplete();
        }
    }
}
