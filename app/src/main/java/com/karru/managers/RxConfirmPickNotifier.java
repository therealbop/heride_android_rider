package com.karru.managers;

import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * <h1>RxRoutePathObserver</h1>
 * This class is used to observe the vehicle types
 * @author 3Embed
 * @since on 03-01-2018.
 */
public class RxConfirmPickNotifier extends Observable<Boolean>
{
    private Observer<?super Boolean> observer;

    @Override
    protected void subscribeActual(Observer<? super Boolean> observer)
    {
        this.observer = observer;
    }

    /**
     * <h2>publishData</h2>
     * This method is used to publish the data for network
     * @param data data to be pushed
     */
    public void notifyConfirmClick(Boolean data)
    {
        if(observer!=null)
        {
            observer.onNext(data);
            observer.onComplete();
        }
    }
}
