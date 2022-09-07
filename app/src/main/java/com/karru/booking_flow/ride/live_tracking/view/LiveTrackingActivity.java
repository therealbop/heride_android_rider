package com.karru.booking_flow.ride.live_tracking.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatDelegate;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.karru.booking_flow.ride.live_tracking.LiveTrackingContract;
import com.karru.booking_flow.ride.live_tracking.mqttChat.ChattingActivity;
import com.karru.util.path_plot.LatLongBounds;
import com.heride.rider.R;
import com.karru.booking_flow.address.view.AddressSelectionActivity;
import com.karru.twilio_call.ClientActivity;
import com.karru.util.Alerts;
import com.karru.util.AppTypeface;
import com.karru.utility.Scaler;
import com.karru.utility.Utility;
import org.json.JSONArray;

import javax.inject.Inject;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

import static com.karru.utility.Constants.BOOKING_ID;
import static com.karru.utility.Constants.DRIVER_HEIGHT;
import static com.karru.utility.Constants.DRIVER_WIDTH;
import static com.karru.utility.Constants.DROP_ADDRESS_REQUEST;
import static com.karru.utility.Constants.DROP_ADDRESS_SCREEN;
import static com.karru.utility.Constants.LIVE_TRACKING_OPEN;
import static com.karru.utility.Constants.PHONE_IMAGE_URL;
import static com.karru.utility.Constants.PHONE_NUMBER;

public class LiveTrackingActivity extends DaggerAppCompatActivity implements LiveTrackingContract.View,
        OnMapReadyCallback
{
    private static final String TAG = "LiveTrackingActivity";

    @Inject Alerts alerts;
    @Inject Context mContext;
    @Inject AppTypeface appTypeface;
    @Inject com.karru.util.Utility utility;
    @Inject CancelBookingReasonsDialog cancelBookingReasonsDialog;
    @Inject LiveTrackingContract.Presenter liveTrackingPresenter;

    @BindView(R.id.tv_all_tool_bar_title) TextView tv_all_tool_bar_title;
    @BindView(R.id.tv_pick_address) TextView tv_live_tracking_pick;
    @BindView(R.id.tv_drop_address) TextView tv_live_tracking_drop;
    @BindView(R.id.tv_live_tracking_driver_name) TextView tv_live_tracking_driver_name;
    @BindView(R.id.tv_live_tracking_driver_rate) TextView tv_live_tracking_driver_rate;
    @BindView(R.id.tv_live_tracking_car_plate) TextView tv_live_tracking_car_plate;
    @BindView(R.id.tv_live_tracking_car_name) TextView tv_live_tracking_car_name;
    @BindView(R.id.tv_live_tracking_call) TextView tv_live_tracking_call;
    @BindView(R.id.tv_live_tracking_share) TextView tv_live_tracking_share;
    @BindView(R.id.tv_live_tracking_cancel) TextView tv_live_tracking_cancel;
    @BindView(R.id.tv_drop_address_change) TextView tv_live_tracking_change;
    @BindView(R.id.tv_live_tracking_bid) TextView tv_live_tracking_bid;
    @BindView(R.id.tv_live_tracking_type) TextView tv_live_tracking_type;
    @BindView(R.id.tv_live_tracking_chat) TextView tv_live_tracking_chat;
    @BindView(R.id.pb_live_tracking_driver_pic) ProgressBar pb_live_tracking_driver_pic;
    @BindView(R.id.pb_live_tracking_vehicle_pic) ProgressBar pb_live_tracking_vehicle_pic;
    @BindView(R.id.iv_live_tracking_driver_pic) ImageView iv_live_tracking_driver_pic;
    @BindView(R.id.iv_live_tracking_vehicle_pic) ImageView iv_live_tracking_vehicle_pic;
    @BindView(R.id.iv_live_tracking_curr_location) ImageView iv_live_tracking_curr_location;
    @BindView(R.id.tv_rental_detail) TextView tv_rental_details;
    @BindView(R.id.tv_rental_details_info) TextView tv_rental_details_info;
    @BindDrawable(R.drawable.signup_profile_default_image) Drawable signup_profile_default_image;
    @BindView(R.id.ll_address_drop_change) LinearLayout ll_live_tracking_change;
    @BindView(R.id.ll_live_tracking_bottom) LinearLayout ll_live_tracking_bottom;
    @BindString(R.string.on_the_way_pick) String on_the_way;
    @BindString(R.string.id) String id;
    @BindString(R.string.cancel_booking) String cancel_booking;
    @BindString(R.string.call) String call;
    @BindString(R.string.call_driver) String call_driver;
    @BindString(R.string.call_1) String call_1;
    @BindString(R.string.yes_alert) String yes_alert;
    @BindString(R.string.away) String away;
    @BindString(R.string.on_the_way_pick) String on_the_way_pick;
    @BindString(R.string.on_the_way_drop) String on_the_way_drop;
    @BindString(R.string.your_driver) String your_driver;
    @BindString(R.string.has) String has;

    private LatLngBounds latLngBounds;
    private Polyline confirmPathPlotLine;
    private ProgressDialog progressDialog;
    private GoogleMap googleMap;
    private String bookingId;
    private Marker driverMarker;
    private Marker pickUpMarker,dropMarker;
    private SupportMapFragment supportmapfragment;
    private Dialog userPromptWithTwoButtons;
    private String reason;
    private String duration;
    private int padding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_tracking);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        initialize();
        initializeMap();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        liveTrackingPresenter.checkRTLConversion();
        supportmapfragment.getMapAsync(this);
        liveTrackingPresenter.handleResume();
    }

    @Override
    public void hideCancelOption(int childs)
    {
        runOnUiThread(() -> {
            if(userPromptWithTwoButtons.isShowing() && !isFinishing())
                userPromptWithTwoButtons.dismiss();
            if(cancelBookingReasonsDialog.isShowing() && !isFinishing())
                cancelBookingReasonsDialog.dismiss();
            ll_live_tracking_bottom.setWeightSum(childs);
            tv_live_tracking_cancel.setVisibility(View.GONE);
        });
    }

    @Override
    public void enableChatModule()
    {
        ll_live_tracking_bottom.setWeightSum(4);
        tv_live_tracking_chat.setVisibility(View.VISIBLE);
    }

    @Override
    public void disableChatModule()
    {
        ll_live_tracking_bottom.setWeightSum(3);
        tv_live_tracking_chat.setVisibility(View.GONE);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void hidePath(String driverName)
    {
        runOnUiThread(() ->
        {
            if(confirmPathPlotLine != null)
                confirmPathPlotLine.remove();

            if(driverName!=null)
                tv_all_tool_bar_title.setText(your_driver+" "+driverName+" " +has);
            CameraPosition cameraPosition;
            if(driverMarker != null)
                cameraPosition = new CameraPosition.Builder().target(driverMarker.getPosition()).zoom(16.00f).build();
            else
                cameraPosition = new CameraPosition.Builder().target(pickUpMarker.getPosition()).zoom(16.00f).build();
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        });
    }

    /**
     * <h2>initialize</h2>
     * This method is used to initialize with the fonts
     */
    @SuppressLint("SetTextI18n")
    private void initialize()
    {
        ButterKnife.bind(this);
        progressDialog = alerts.getProcessDialog(this);
        progressDialog.setCancelable(false);
        bookingId = getIntent().getStringExtra(BOOKING_ID);
        tv_all_tool_bar_title.setText(on_the_way);
        tv_all_tool_bar_title.setTextSize(15);
        userPromptWithTwoButtons = alerts.userPromptWithTwoButtons(this);
        LIVE_TRACKING_OPEN = true;
        tv_live_tracking_bid.setText(id+" "+bookingId);
        double scale[] ;
        scale = Scaler.getScalingFactor(mContext);
        padding  = (int) scale[0]* 90;

        tv_all_tool_bar_title.setTypeface(appTypeface.getPro_narMedium());
        tv_live_tracking_driver_name.setTypeface(appTypeface.getPro_narMedium());
        tv_live_tracking_driver_rate.setTypeface(appTypeface.getPro_narMedium());
        tv_live_tracking_car_plate.setTypeface(appTypeface.getPro_narMedium());
        tv_rental_details.setTypeface(appTypeface.getPro_narMedium());
        tv_rental_details_info.setTypeface(appTypeface.getPro_narMedium());
        tv_live_tracking_pick.setTypeface(appTypeface.getPro_News());
        tv_live_tracking_drop.setTypeface(appTypeface.getPro_News());
        tv_live_tracking_car_name.setTypeface(appTypeface.getPro_News());
        tv_live_tracking_change.setTypeface(appTypeface.getPro_News());
        tv_live_tracking_call.setTypeface(appTypeface.getPro_News());
        tv_live_tracking_bid.setTypeface(appTypeface.getPro_News());
        tv_live_tracking_type.setTypeface(appTypeface.getPro_News());
        tv_live_tracking_share.setTypeface(appTypeface.getPro_News());
        tv_live_tracking_cancel.setTypeface(appTypeface.getPro_News());
        tv_live_tracking_chat.setTypeface(appTypeface.getPro_News());
    }

    /**
     * <h1>initializeMap</h1>
     * This method is used to initialize google Map
     */
    private void initializeMap()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.frag_live_tracking_map);
        supportmapfragment = (SupportMapFragment)fragment;
    }

    @Override
    public void showProgressDialog()
    {
        if(progressDialog!=null && !progressDialog.isShowing() && !isFinishing())
            progressDialog.show();
    }

    @Override
    public void dismissProgressDialog()
    {
        if(progressDialog!=null && progressDialog.isShowing() && !isFinishing())
            progressDialog.dismiss();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setBookingUI(double rating, LatLng pickLatLong, LatLng dropLatLong, LatLng driverLatLong,
                             boolean isRental, String... params)
    {
        runOnUiThread(() ->
        {
            tv_live_tracking_driver_name.setText(params[0]);
            tv_live_tracking_pick.setText(params[1]);
            tv_live_tracking_drop.setText(params[2]);
            if(params[2].equals(""))
                tv_live_tracking_change.setText(getString(R.string.add));

            tv_live_tracking_car_plate.setText( params[3]);

            if(null != params[8])
            {
                if(!params[8].equals("") && params[8]!=null )
                    tv_live_tracking_car_name.setText( params[8]+", "+params[7]);
                else
                    tv_live_tracking_car_name.setText(params[7]);
            }
            else
                tv_live_tracking_car_name.setText(params[7]);

            String driverProfileImage = params[4];
            String vehicleName = params[5].toUpperCase();
            String driverMarkerImage = params[6];
            String vehicleImage = params[9];

            if(isRental && !params[10].isEmpty())
            {
                tv_rental_details.setVisibility(View.VISIBLE);
                tv_rental_details_info.setVisibility(View.VISIBLE);
                tv_rental_details_info.setText(params[10]);
            }
            tv_live_tracking_driver_rate.setText(rating+"");
            tv_live_tracking_type.setText(vehicleName);

            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.placeholder(signup_profile_default_image);
            requestOptions = requestOptions.optionalCircleCrop();
            if(!driverProfileImage.equals(""))
            {
                Glide.with(mContext)
                        .load(driverProfileImage)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                pb_live_tracking_driver_pic.setVisibility(View.GONE);
                                return false;
                            }
                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                pb_live_tracking_driver_pic.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .apply(requestOptions)
                        .into(iv_live_tracking_driver_pic);
            }

            if(null != vehicleImage)
            {
                if(!vehicleImage.equals(""))
                {
                    Glide.with(mContext)
                            .load(vehicleImage)
                            .listener(new RequestListener<Drawable>()
                            {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, com.bumptech.glide.request.target.Target<Drawable> target, boolean isFirstResource) {
                                    pb_live_tracking_vehicle_pic.setVisibility(View.GONE);
                                    return false;
                                }
                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, com.bumptech.glide.request.target.Target<Drawable> target, DataSource dataSource, boolean isFirstResource)
                                {
                                    pb_live_tracking_vehicle_pic.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .apply(requestOptions)
                            .into(iv_live_tracking_vehicle_pic);
                }
            }

            if(pickUpMarker!=null)
                pickUpMarker.remove();

            if(dropMarker!=null)
                dropMarker.remove();

            if(driverMarker!=null)
                driverMarker.remove();

            Utility.printLog(TAG+" live track location "+pickLatLong.latitude+" "+pickLatLong.longitude);
            pickUpMarker = googleMap.addMarker(new MarkerOptions()
                    .position(pickLatLong)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.confirmation_pickup_pin_icon)));

            if(dropLatLong.latitude !=0 && dropLatLong.longitude!=0)
            {
                dropMarker = googleMap.addMarker(new MarkerOptions()
                        .position(dropLatLong)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.confirmation_drop_pin_icon)));
            }

            double[] size = Scaler.getScalingFactor(mContext);
            double markerDimenWidth = size[0] * DRIVER_WIDTH;
            double markerDimenHeight = size[1] * DRIVER_HEIGHT;
            if(!driverMarkerImage.equals(""))
            {
                Utility.printLog(TAG+" live track driver "+driverLatLong.latitude+" "+driverLatLong.longitude);
                loadCarMapImage(markerDimenWidth,markerDimenHeight,driverLatLong,driverMarkerImage);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void pickLatLongBounds(LatLng driverLatLng)
    {
        if(pickUpMarker!=null && driverLatLng!=null)
        {
            runOnUiThread(() ->
            {
                liveTrackingPresenter.plotPathRoute(driverLatLng,pickUpMarker.getPosition());

                if(latLngBounds!=null)
                {
                    if(duration != null)
                        tv_all_tool_bar_title.setText(on_the_way_pick+" "+ duration +" "+mContext.getString(R.string.away));

                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(latLngBounds, padding);
                    // Move the map
                    googleMap.moveCamera(cu);
                }
            });
        }
        else if(driverMarker!=null)
        {
            runOnUiThread(() ->
            {
                liveTrackingPresenter.plotPathRoute(driverMarker.getPosition(),pickUpMarker.getPosition());

                if(latLngBounds!=null)
                {
                    if(duration != null)
                        tv_all_tool_bar_title.setText(on_the_way_pick+" "+ duration +" "+mContext.getString(R.string.away));

                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(latLngBounds, padding);
                    // Move the map
                    googleMap.moveCamera(cu);
                }
            });
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void destLatLongBounds(LatLng driverLatLng)
    {
        runOnUiThread(() ->
        {
            if(driverLatLng!=null)
            {
                if(dropMarker != null)
                {
                    liveTrackingPresenter.plotPathRoute(driverLatLng,dropMarker.getPosition());

                    if(latLngBounds!=null)
                    {
                        if(duration != null)
                            tv_all_tool_bar_title.setText(on_the_way_drop+" "+ duration +" "+mContext.getString(R.string.away));

                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(latLngBounds, padding);
                        // Move the map
                        googleMap.moveCamera(cu);
                    }
                }
                else
                {
                    tv_all_tool_bar_title.setText(on_the_way_drop);
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(driverLatLng).zoom(16.00f).build();
                    googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            }
            else
            {
                tv_all_tool_bar_title.setText(on_the_way_drop);
                CameraPosition cameraPosition = new CameraPosition.Builder().target(pickUpMarker.getPosition()).zoom(16.00f).build();
                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });
    }

    @Override
    public void googlePathPlot(LatLongBounds latLongBounds)
    {
        if(googleMap != null) {
            if (confirmPathPlotLine != null)
                confirmPathPlotLine.remove();
            confirmPathPlotLine = googleMap.addPolyline(latLongBounds.getPolylineOptions());

            LatLng southWestLocation = new LatLng(Double.parseDouble(latLongBounds.getSouthwest().getLat()),
                    Double.parseDouble(latLongBounds.getSouthwest().getLng()));
            LatLng northEastLocation = new LatLng(Double.parseDouble(latLongBounds.getNortheast().getLat()),
                    Double.parseDouble(latLongBounds.getNortheast().getLng()));
            latLngBounds = new LatLngBounds(southWestLocation, northEastLocation);
            duration = latLongBounds.getDuration();
            // Obtain a movement description object
            // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(latLngBounds, padding);
            // Move the map
            googleMap.moveCamera(cu);
        }
    }

    /**
     * <h2>loadCarMapImage</h2>
     * used to load car map image
     */
    private void loadCarMapImage(double markerDimenWidth,double markerDimenHeight,LatLng driverLatLong,
                                 String driverMarkerImage)
    {
        Glide.with(mContext)
                .asBitmap()
                .load(driverMarkerImage)
                .listener(new RequestListener<Bitmap>()
                          {
                              @Override
                              public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Bitmap> target, boolean b) {
                                  return false;
                              }
                              @Override
                              public boolean onResourceReady(Bitmap bitmap, Object o, Target<Bitmap> target, DataSource dataSource, boolean b) {
                                  Utility.printLog(TAG+" live track loaded "+bitmap);
                                  if( bitmap!=null)
                                  {
                                      if(!bitmap.equals("") )
                                      {
                                          Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) markerDimenWidth,
                                                  (int) markerDimenHeight, false);
                                          driverMarker = googleMap.addMarker(new MarkerOptions().position(driverLatLong).
                                                  icon(BitmapDescriptorFactory.fromBitmap(scaledBitmap)));
                                      }
                                  }
                                  return false;
                              }
                          }
                ).submit();
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        Utility.printLog(TAG+" onMapReady ");
        this.googleMap = googleMap;
        liveTrackingPresenter.bookingDetailsAPI(bookingId);
    }

    @OnClick({R.id.iv_live_tracking_curr_location,R.id.rl_back_button,R.id.iv_back_button,
            R.id.tv_live_tracking_cancel,R.id.tv_live_tracking_share,R.id.tv_live_tracking_call,
            R.id.ll_address_drop_change,R.id.tv_drop_address,R.id.tv_live_tracking_chat})
    public void onClickEvent(View view)
    {
        switch (view.getId())
        {
            case R.id.iv_live_tracking_curr_location:
                liveTrackingPresenter.checkForLocationBounding();
                break;

            case R.id.rl_back_button:
            case R.id.iv_back_button:
                onBackPressed();
                break;

            case R.id.tv_live_tracking_cancel:
                liveTrackingPresenter.confirmCancelBookingAPI();
                break;

            case R.id.tv_live_tracking_share:
                liveTrackingPresenter.handleTrackingShare();
                break;

            case R.id.tv_live_tracking_call:
                liveTrackingPresenter.handleDriverCall();
                break;

            case R.id.ll_address_drop_change:
            case R.id.tv_drop_address:
                Intent intent = new Intent(this,AddressSelectionActivity.class);
                intent.putExtra("keyId", DROP_ADDRESS_REQUEST);
                intent.putExtra("key", "startActivityForResultHOME");
                intent.putExtra("comingFrom",DROP_ADDRESS_SCREEN);
                startActivityForResult(intent,DROP_ADDRESS_REQUEST);
                overridePendingTransition(R.anim.slide_in_up, R.anim.stay_still);
                break;

            case R.id.tv_live_tracking_chat:
                liveTrackingPresenter.checkForChat();
                break;
        }
    }

    @Override
    public void openChatScreen(String bid,String driverId,String driverName)
    {
        Intent intent1 = new Intent(this, ChattingActivity.class);
        intent1.putExtra("BID",bookingId);
        intent1.putExtra("DRIVER_ID",driverId);
        intent1.putExtra("DRIVER_NAME",driverName);
        startActivity(intent1);
    }

    @Override
    public void showAlertForAddressUpdate(String message)
    {
        tv_live_tracking_change.setText(getString(R.string.change));
        alerts.problemLoadingAlert(this,message);
    }

    @Override
    public void shareLink(String trackingLink)
    {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, trackingLink);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    @Override
    public void showPassengerCancelPopup(String message)
    {
        TextView tv_alert_yes =  userPromptWithTwoButtons.findViewById(R.id.tv_alert_yes);
        TextView tv_alert_title =  userPromptWithTwoButtons.findViewById(R.id.tv_alert_title);
        TextView tv_alert_body =  userPromptWithTwoButtons.findViewById(R.id.tv_alert_body);
        tv_alert_title.setText(cancel_booking);
        tv_alert_yes.setText(yes_alert);
        tv_alert_body.setText(message);
        tv_alert_yes.setOnClickListener(v ->
                liveTrackingPresenter.getCancellationReasons());
        userPromptWithTwoButtons.show();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        liveTrackingPresenter.handleBackState();
    }

    @Override
    public void showToast(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void moveCarIcon(LatLng driverLocation, float bearing)
    {
        utility.moveCarMarker(driverLocation,driverMarker);
    }

    @Override
    public void populateCancelReasonsDialog(JSONArray reasons)
    {
        userPromptWithTwoButtons.dismiss();
        cancelBookingReasonsDialog.populateWithDetails(reasons);
    }

    @Override
    public void enableCancelButton(String reason)
    {
        this.reason = reason;
        cancelBookingReasonsDialog.enableCancelButton();
    }

    @Override
    public void onclickOfConfirmCancel()
    {
        Utility.printLog(TAG+" reason selected "+reason);
        liveTrackingPresenter.cancelBookingAPI(reason);
    }

    @Override
    public void finishActivity()
    {
        finish();
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }

    @Override
    public void showDriverCancelDialog(String message)
    {
        Dialog driverCancellationDialog = alerts.userPromptWithOneButton(message,this);
        TextView tv_alert_ok =  driverCancellationDialog.findViewById(R.id.tv_alert_ok);
        tv_alert_ok.setOnClickListener(v ->
        {
            driverCancellationDialog.dismiss();
            finishActivity();
        });
        driverCancellationDialog.show();
    }

    @Override
    public void callDriver(String phoneNumber,boolean isEnable,String driverPicUrl)
    {
        TextView tv_alert_yes =  userPromptWithTwoButtons.findViewById(R.id.tv_alert_yes);
        TextView tv_alert_title =  userPromptWithTwoButtons.findViewById(R.id.tv_alert_title);
        TextView tv_alert_body =  userPromptWithTwoButtons.findViewById(R.id.tv_alert_body);
        tv_alert_title.setText(call);
        tv_alert_body.setText(call_driver);
        tv_alert_yes.setText(call_1);
        tv_alert_yes.setOnClickListener(v ->
        {
            userPromptWithTwoButtons.dismiss();
            if(isEnable)
            {
                Intent intent = new Intent(this, ClientActivity.class);
                intent.putExtra(PHONE_NUMBER, phoneNumber.trim());
                intent.putExtra(PHONE_IMAGE_URL, driverPicUrl);
                startActivity(intent);
            }
            else
            {
                String uri = "tel:" + phoneNumber.trim() ;
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                startActivity(intent);
            }
        });
        userPromptWithTwoButtons.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        Utility.printLog(TAG+" drop address update "+resultCode);
        if(data != null)
            liveTrackingPresenter.handleReturnIntent(requestCode,resultCode,data.getExtras());
        else
            liveTrackingPresenter.handleReturnIntent(requestCode,resultCode,null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LIVE_TRACKING_OPEN = false;
    }
}
