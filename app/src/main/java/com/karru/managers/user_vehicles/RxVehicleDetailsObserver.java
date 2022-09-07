package com.karru.managers.user_vehicles;

import com.karru.landing.home.model.VehicleTypesDetails;

import java.util.ArrayList;
import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * <h1>RxRoutePathObserver</h1>
 * This class is used to observe the vehicle types
 * @author 3Embed
 * @since on 03-01-2018.
 */
public class RxVehicleDetailsObserver extends Observable<ArrayList<VehicleTypesDetails>>
{
    private Observer<?super ArrayList<VehicleTypesDetails>> observer;

    @Override
    protected void subscribeActual(Observer<? super ArrayList<VehicleTypesDetails>> observer)
    {
        this.observer = observer;
    }

    /**
     * <h2>publishData</h2>
     * This method is used to publish the data for network
     * @param data data to be pushed
     */
    public void publishVehicleTypes(ArrayList<VehicleTypesDetails> data)
    {
        if(observer!=null)
        {
            observer.onNext(data);
            observer.onComplete();
        }
    }
}
