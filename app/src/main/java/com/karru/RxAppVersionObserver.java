package com.karru;

import io.reactivex.Observable;
import io.reactivex.Observer;

public class RxAppVersionObserver extends Observable<ApplicationVersion>
{
    private Observer<?super ApplicationVersion> observer;

    @Override
    protected void subscribeActual(Observer<? super ApplicationVersion> observer) {
        this.observer = observer;
    }

    /**
     * <h2>publishDriverDetails</h2>
     * This method is used to publish the driver details
     * @param data driver details to be pushed
     */
    void publishApplicationVersion(ApplicationVersion data)
    {
        if(observer!=null)
        {
            observer.onNext(data);
            observer.onComplete();
        }
    }
}
