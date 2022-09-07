package com.karru.managers.booking;

import com.karru.landing.home.model.BookingDetailsDataModel;
import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * <h1>RxLiveBookingDetailsObserver</h1>
 * This class is used to subscribe the booking details
 * @author 3Embed
 * @since on 24-01-2018.
 */
public class RxLiveBookingDetailsObserver extends Observable<BookingDetailsDataModel>
{
    private Observer<? super BookingDetailsDataModel> observer;
    private static RxLiveBookingDetailsObserver instance;

    public RxLiveBookingDetailsObserver()
    {
        instance = this ;
    }

    //returns singleton object
    public static RxLiveBookingDetailsObserver getInstance()
    {
        // Return the instance
        return instance;
    }

    @Override
    protected void subscribeActual(Observer<? super BookingDetailsDataModel> observer)
    {
        this.observer = observer;
    }

    /**
     * <h2>publishData</h2>
     * This method is used to publish the data for network
     * @param data data to be pushed
     */
    public void publishBookingDetails(BookingDetailsDataModel data) {
        if (observer != null) {
            observer.onNext(data);
            observer.onComplete();
        }
    }
}