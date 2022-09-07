package com.karru.splash.second;

/**
 * <h1>SecondSplashContract</h1>
 * This interface is used for broker between view , presenter and model
 * @author 3Embed
 * @since on 22-12-2017.
 */
class SecondSplashContract
{
    interface View
    {
        /**
         * <h2>startMainActivity</h2>
         * This method is used to start the main activity
         */
        void startMainActivity();
        /**
         * <h2>showWaveAnimation</h2>
         * This method is used to start the wave animation
         */
        void showWaveAnimation();
        /**
         * <h2>hideWaveAnimation</h2>
         * This method is used to hide the wave animation
         */
        void hideWaveAnimation();

        /**
         * <h2>showToast</h2>
         * used to show the toast
         * @param message message to be shown
         */
        void showToast(String message);
    }
    interface Presenter
    {
        /**
         * <h2>getVehicleDetails</h2>
         * This method is used to get the vehicle details
         */
        void getVehicleDetails();

        /**
         * <h2>locationDisposable</h2>
         * used to dispose observables
         */
        void disposeObservables();

        void checkRTLConversion();
    }
}
