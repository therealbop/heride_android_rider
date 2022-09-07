package com.karru.managers.booking;

import com.karru.landing.home.model.DriverDetailsModel;
import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * <h1>RxDriverDetailsObserver</h1>
 * This class is used to observe the driver details
 * @author 3Embed
 * @since on 01-02-2018.
 */
public class RxDriverDetailsObserver extends Observable<DriverDetailsModel>
{
    private Observer<?super DriverDetailsModel> observer;

    @Override
    protected void subscribeActual(Observer<? super DriverDetailsModel> observer) {
        this.observer = observer;
    }

    /**
     * <h2>publishDriverDetails</h2>
     * This method is used to publish the driver details
     * @param data driver details to be pushed
     */
    void publishDriverDetails(DriverDetailsModel data)
    {
        if(observer!=null)
        {
            observer.onNext(data);
            observer.onComplete();
        }
    }
}
