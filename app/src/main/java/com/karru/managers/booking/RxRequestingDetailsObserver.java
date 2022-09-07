package com.karru.managers.booking;

import com.karru.booking_flow.ride.request.model.RequestBookingDetails;

import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * <h1>RxDriverDetailsObserver</h1>
 * This class is used to observe the driver details
 * @author 3Embed
 * @since on 01-02-2018.
 */
public class RxRequestingDetailsObserver extends Observable<RequestBookingDetails>
{
    private Observer<?super RequestBookingDetails> observer;

    @Override
    protected void subscribeActual(Observer<? super RequestBookingDetails> observer) {
        this.observer = observer;
    }

    /**
     * <h2>publishDriverDetails</h2>
     * This method is used to publish the driver details
     * @param data driver details to be pushed
     */
    void publishBookingDetails(RequestBookingDetails data)
    {
        if(observer!=null)
        {
            observer.onNext(data);
            observer.onComplete();
        }
    }
}

