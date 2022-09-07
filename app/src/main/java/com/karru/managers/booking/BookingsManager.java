package com.karru.managers.booking;

import com.google.gson.Gson;
import com.karru.booking_flow.ride.request.model.RequestBookingDetails;
import com.karru.landing.home.model.BookingDetailsDataModel;
import com.karru.landing.home.model.DriverCancellationModel;
import com.karru.landing.home.model.DriverDetailsModel;
import com.karru.landing.home.model.InvoiceDetailsModel;
import com.karru.landing.home.model.OnGoingBookingsModel;
import com.karru.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * <h1>BookingsManager</h1>
 * This class is used to manager the booking details of user
 * @author 3Embed
 * @since on 02-02-2018.
 */
public class BookingsManager
{
    private static final String TAG = "BookingsManager";
    private Gson gson;
    private RxDriverDetailsObserver rxDriverDetailsObserver;
    private RxRequestingDetailsObserver rxRequestingDetailsObserver;
    private RxLiveBookingDetailsObserver rxLiveBookingDetailsObserver;
    private RxInvoiceDetailsObserver rxInvoiceDetailsObserver;
    private RxDriverCancelledObserver rxDriverCancelledObserver;
    private RxOnGoingBookingsDetailsObserver rxOnGoingBookingDetailsObserver;

    public BookingsManager(RxDriverDetailsObserver rxDriverDetailsObserver,
                           RxLiveBookingDetailsObserver rxLiveBookingDetailsObserver,
                           RxRequestingDetailsObserver rxRequestingDetailsObserver,
                           RxOnGoingBookingsDetailsObserver rxOnGoingBookingDetailsObserver,Gson gson,
                           RxInvoiceDetailsObserver rxInvoiceDetailsObserver,RxDriverCancelledObserver rxDriverCancelledObserver)
    {
        this.gson = gson;
        this.rxDriverDetailsObserver = rxDriverDetailsObserver;
        this.rxLiveBookingDetailsObserver = rxLiveBookingDetailsObserver;
        this.rxInvoiceDetailsObserver = rxInvoiceDetailsObserver;
        this.rxDriverCancelledObserver = rxDriverCancelledObserver;
        this.rxRequestingDetailsObserver = rxRequestingDetailsObserver;
        this.rxOnGoingBookingDetailsObserver = rxOnGoingBookingDetailsObserver;
    }

    /**
     * <h2>handleBookingsStatus</h2>
     * This method is used to handle the user bookings details
     * @param bookingDetails booking details to be handled
     */
    public void handleBookingsStatus(String bookingDetails)
    {
        try
        {
            JSONObject object = new JSONObject(bookingDetails);
            if(object.has("status"))
            {
                switch (object.getInt("status"))
                {
                    //id user requested and kill the app so user is still in booking
                    case 1:
                        if(object.has("bookingRequestData"))
                        {
                            JSONObject bookingRequestData = object.getJSONObject("bookingRequestData");
                            RequestBookingDetails requestingDetailsModel = gson.fromJson(bookingRequestData.toString(),
                                    RequestBookingDetails.class);
                            rxRequestingDetailsObserver.publishBookingDetails(requestingDetailsModel);
                        }
                        break;

                    //driver sends his status
                    case 14:
                        DriverDetailsModel driverDetailsModel = gson.fromJson(bookingDetails,
                                DriverDetailsModel.class);
                        rxDriverDetailsObserver.publishDriverDetails(driverDetailsModel);
                        break;

                        //driver cancelled the booking
                    case 5:
                        DriverCancellationModel driverCancellationModel = gson.fromJson(bookingDetails,
                                DriverCancellationModel.class);
                        rxDriverCancelledObserver.publishDriverCancelDetails(driverCancellationModel);
                        break;

                        //booking status 6 :on the way 7: arrived 9:journey started
                    case 6:
                    case 7:
                    case 9:
                        BookingDetailsDataModel bookingDetailsDataModel = gson.fromJson(bookingDetails,
                                BookingDetailsDataModel.class);
                        rxLiveBookingDetailsObserver.publishBookingDetails(bookingDetailsDataModel);
                        break;

                        //12 invoice for the booking
                    case 12:
                        if(object .has("invoice"))
                        {
                            String invoiceObj = object.getString("invoice");
                            String bookingId = object.getString("bookingId");
                            Utility.printLog(TAG+" invoice status "+invoiceObj);
                            InvoiceDetailsModel invoiceDetailsModel = gson.fromJson(invoiceObj,
                                    InvoiceDetailsModel.class);
                            invoiceDetailsModel.setCallAPi(false);
                            invoiceDetailsModel.setBookingId(bookingId);
                            rxInvoiceDetailsObserver.publishInvoiceDetails(invoiceDetailsModel);
                        }
                        break;
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * <h2>handleOnGoingBookings</h2>
     * This method is used to handle the ongoing bookings list
     * @param onGoingBookingsModels on going bookings list with the details
     */
    public void handleOnGoingBookings(ArrayList<OnGoingBookingsModel> onGoingBookingsModels)
    {
        ArrayList<OnGoingBookingsModel> onGoingBookingList = new ArrayList<>();
        for(int onGoingBookingCount=0; onGoingBookingCount<onGoingBookingsModels.size() ;
            onGoingBookingCount++)
        {
            switch (onGoingBookingsModels.get(onGoingBookingCount).getBookingStatus())
            {
                case 12:
                    InvoiceDetailsModel invoiceDetailsModel = new InvoiceDetailsModel();
                    invoiceDetailsModel.setBookingId(onGoingBookingsModels.get(onGoingBookingCount).getBookingId());
                    invoiceDetailsModel.setCallAPi(true);
                    Utility.printLog(TAG+" invoice details observed  "+
                            invoiceDetailsModel.getBookingId());
                    rxInvoiceDetailsObserver.publishInvoiceDetails(invoiceDetailsModel);
                    break;

                default:
                    onGoingBookingList.add(onGoingBookingsModels.get(onGoingBookingCount));
                    break;
            }
        }
        rxOnGoingBookingDetailsObserver.publishOnGoingBookingDetails(onGoingBookingList);
    }
}
