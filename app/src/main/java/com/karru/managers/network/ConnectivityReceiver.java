package com.karru.managers.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.karru.ApplicationClass;

/**
 * <h1>ConnectivityReceiver</h1>
 * This class was added to handle network reachable
 * @author Akbar
 * @since 22-10-2018
 */
public class ConnectivityReceiver extends BroadcastReceiver
{
    public static ConnectivityReceiverListener connectivityReceiverListener;
 
    public ConnectivityReceiver()
    {
        super();
    }
 
    @Override
    public void onReceive(Context context, Intent arg1) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
 
        if (connectivityReceiverListener != null) {
            connectivityReceiverListener.onNetworkConnectionChanged(isConnected);
        }
    }
 
    public static boolean isConnected() {
        ConnectivityManager
                cm = (ConnectivityManager) ApplicationClass.getInstance().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }
 
 
    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }
}