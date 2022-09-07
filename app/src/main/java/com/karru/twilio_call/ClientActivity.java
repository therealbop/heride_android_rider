package com.karru.twilio_call;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.heride.rider.R;
import com.karru.util.AppPermissionsRunTime;
import com.karru.utility.Utility;
import com.twilio.voice.Call;
import com.twilio.voice.CallException;
import com.twilio.voice.ConnectOptions;
import com.twilio.voice.RegistrationException;
import com.twilio.voice.RegistrationListener;
import com.twilio.voice.Voice;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.karru.utility.Constants.PERMISSION_DENIED;
import static com.karru.utility.Constants.PERMISSION_GRANTED;
import static com.karru.utility.Constants.PHONE_FROM;
import static com.karru.utility.Constants.PHONE_IMAGE_URL;
import static com.karru.utility.Constants.PHONE_NUMBER;
import static com.karru.utility.Constants.PHONE_TO;

/**
 * <h1>ClientActivity</h1>
 * used to open the twiilio calling activity
 */
public class ClientActivity extends DaggerAppCompatActivity implements RegistrationListener
        , ClientActivityContract.ClientView {
    private static final String TAG = "ClientActivity";
    @Inject
    ClientActivityContract.ClientPresenter clientPresenter;
    @Inject
    AppPermissionsRunTime appPermissionsRunTime;

    @BindView(R.id.callee_image_iv)
    ImageView callee_image_iv;
    @BindView(R.id.mute_action_fab)
    ImageView muteActionFab;
    @BindView(R.id.speaker_action_fab)
    ImageView speakerActionFab;
    @BindView(R.id.hangup_action_fab)
    ImageView hangupActionFab;
    @BindView(R.id.chronometer)
    Chronometer chronometer;
    @BindView(R.id.call_layout)
    View callView;
    @BindString(R.string.micro_phone_permission)
    String micro_phone_permission;
    @BindString(R.string.failed_to_init_call)
    String failed_to_init_call;
    @BindString(R.string.device_error)
    String device_error;
    @BindString(R.string.no_device)
    String no_device;
    @BindString(R.string.bad_gateway)
    String bad_gateway;

    private boolean muteMicrophone;
    private boolean speakerPhone;
    private String mob;
    private static final int MIC_PERMISSION_REQUEST_CODE = 1;
    private AudioManager audioManager;
    private int savedAudioMode = AudioManager.MODE_INVALID;
    private String imageUrl;
    private Call activeCall;
    Call.Listener callListener = callListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twilio_client);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null) {
            mob = intent.getStringExtra(PHONE_NUMBER);
            imageUrl = intent.getStringExtra(PHONE_IMAGE_URL);
        }

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.optionalCircleCrop();
        if (imageUrl != null && !imageUrl.equals(""))
            Glide.with(ClientActivity.this)
                    .load(imageUrl)
                    .apply(requestOptions)
                    .into(callee_image_iv);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        if (appPermissionsRunTime.checkIfPermissionNeeded()) {
            if (appPermissionsRunTime.checkIfPermissionGrant(Manifest.permission.RECORD_AUDIO, this))
                initializeTwilioClientSDK();
            else {
                String[] strings = {Manifest.permission.RECORD_AUDIO};
                appPermissionsRunTime.requestForPermission(strings, this, MIC_PERMISSION_REQUEST_CODE);
            }
        } else
            initializeTwilioClientSDK();

        setCallAction();
    }


    @Override
    public void showToast() {
        Toast.makeText(this, bad_gateway, Toast.LENGTH_LONG).show();
        onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case MIC_PERMISSION_REQUEST_CODE:
                Utility.printLog(TAG + " onRequestPermissionsResult " + requestCode);
                int status = appPermissionsRunTime.getPermissionStatusCall(this,
                        Manifest.permission.RECORD_AUDIO, true);
                Utility.printLog(TAG + " onRequestPermissionsResult " + status);
                switch (status) {
                    case PERMISSION_GRANTED:
                        initializeTwilioClientSDK();
                        break;

                    case PERMISSION_DENIED:
                        onBackPressed();
                        break;
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        disconnect();
        resetUI();
        finish();
    }

    /**
     * <h>Initialize Twilio</h>
     * <p>Initializing the Twilio Client SDK</p>
     */
    private void initializeTwilioClientSDK() {
        /*if (!Twilio.isInitialized()) {
            Twilio.initialize(getApplicationContext(), new Twilio.InitListener() {
                @Override
                public void onInitialized() {
                    Twilio.setLogLevel(Log.DEBUG);
                    clientPresenter.getCapabilityToken(mob);
                }

                @Override
                public void onError(Exception e)
                {
                    Utility.printLog(TAG+" onError "+e);
                    Toast.makeText(ClientActivity.this, failed_to_init_call, Toast.LENGTH_LONG).show();
                }
            });
        } else*/
        clientPresenter.getCapabilityToken(mob);
    }


    @Override
    public void createDevice(String capabilityToken) {
        try {
            connect(mob, capabilityToken);
        } catch (Exception e) {
            Toast.makeText(ClientActivity.this, device_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        clientPresenter.checkRTLConversion();
    }

    /*
     * Receive intent for incoming call from Twilio Client Service
     * Android will only call Activity.onNewIntent() if `android:launchMode` is set to `singleTop`.
     */
    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    /**
     * <h>Connect to Call</h>
     * <p>Create an outgoing connection</p>
     *
     * @param contact contact number to make outgoing call
     */
    private void connect(String contact, String token) {

        Map<String, String> params = new HashMap<>();
        params.put(PHONE_TO, contact);
        ConnectOptions connectOptions = new ConnectOptions.Builder(token)
                .params(params)
                .build();
        activeCall = Voice.connect(ClientActivity.this, connectOptions, callListener);
        setCallUI();
    }

    /**
     * <H>Disconnect the call</H>
     * <p>Disconnecting an active connection</p>
     */
    private void disconnect() {
        if (activeCall != null) {
            activeCall.disconnect();
            activeCall = null;
        }
    }

    /**
     * <h>Set call action</h>
     * <P>The initial state when there is no active connection</P>
     */
    private void setCallAction() {
        hangupActionFab.setOnClickListener(hangupActionFabClickListener());
        muteActionFab.setOnClickListener(muteMicrophoneFabClickListener());
        speakerActionFab.setOnClickListener(toggleSpeakerPhoneFabClickListener());
    }


    /**
     * <h>Ui Reset method</h>
     * <p>this method is usig to Reset UI elements</p>
     */
    private void resetUI() {
        muteMicrophone = false;
        speakerPhone = false;
        muteActionFab.setImageDrawable(ContextCompat.getDrawable(ClientActivity.this, R.drawable.ic_mic_green_24px));
        speakerActionFab.setImageDrawable(ContextCompat.getDrawable(ClientActivity.this, R.drawable.ic_speaker_off_black_24dp));
        setAudioFocus(false);
        audioManager.setSpeakerphoneOn(speakerPhone);
        chronometer.stop();
    }

    /**
     * <h>Set Call Ui</h>
     * <P>The UI state when there is an active connection</P>
     */
    private void setCallUI() {
        hangupActionFab.setVisibility(View.VISIBLE);
        callView.setVisibility(View.VISIBLE);
        chronometer.setVisibility(View.VISIBLE);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }

    private View.OnClickListener muteMicrophoneFabClickListener() {
        return v -> {
            muteMicrophone = !muteMicrophone;
            if (activeCall != null) {
                activeCall.mute(muteMicrophone);
            }

            if (muteMicrophone) {
                muteActionFab.setImageDrawable(ContextCompat.getDrawable(ClientActivity.this, R.drawable.ic_mic_off_red_24px));
            } else {
                muteActionFab.setImageDrawable(ContextCompat.getDrawable(ClientActivity.this, R.drawable.ic_mic_green_24px));
            }
        };
    }

    private View.OnClickListener toggleSpeakerPhoneFabClickListener() {
        return v ->
        {
            speakerPhone = !speakerPhone;
            setAudioFocus(true);
            audioManager.setSpeakerphoneOn(speakerPhone);
            if (speakerPhone) {
                speakerActionFab.setImageDrawable(ContextCompat.getDrawable(ClientActivity.this, R.drawable.ic_speaker_on_black_24dp));
            } else {
                speakerActionFab.setImageDrawable(ContextCompat.getDrawable(ClientActivity.this, R.drawable.ic_speaker_off_black_24dp));
            }
        };
    }

    private View.OnClickListener hangupActionFabClickListener() {
        return v ->
        {
            disconnect();
            resetUI();
            finish();
        };
    }


    @Override
    protected void onPause() {
        super.onPause();
        clientPresenter.dispose();
    }

    /**
     * <h>Setting AudioFocus</h>
     * <p> this methos is using to set Audio Mode</p>
     *
     * @param setFocus wheather to focus or not
     */
    private void setAudioFocus(boolean setFocus) {
        if (audioManager != null) {
            if (setFocus) {
                savedAudioMode = audioManager.getMode();
                audioManager.requestAudioFocus(null, AudioManager.STREAM_VOICE_CALL,
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            } else {
                audioManager.setMode(savedAudioMode);
                audioManager.abandonAudioFocus(null);
            }
        }
    }

    @Override
    public void onRegistered(@NonNull String accessToken, @NonNull String fcmToken) {
        Log.d(TAG, "onRegistered: ");
    }

    @Override
    public void onError(@NonNull RegistrationException registrationException, @NonNull String accessToken, @NonNull String fcmToken) {
        Log.d(TAG, "onError: ");

    }

    private Call.Listener callListener() {
        return new Call.Listener() {
            /*
             * This callback is emitted once before the Call.Listener.onConnected() callback when
             * the callee is being alerted of a Call. The behavior of this callback is determined by
             * the answerOnBridge flag provided in the Dial verb of your TwiML application
             * associated with this client. If the answerOnBridge flag is false, which is the
             * default, the Call.Listener.onConnected() callback will be emitted immediately after
             * Call.Listener.onRinging(). If the answerOnBridge flag is true, this will cause the
             * call to emit the onConnected callback only after the call is answered.
             * See answeronbridge for more details on how to use it with the Dial TwiML verb. If the
             * twiML response contains a Say verb, then the call will emit the
             * Call.Listener.onConnected callback immediately after Call.Listener.onRinging() is
             * raised, irrespective of the value of answerOnBridge being set to true or false
             */
            @Override
            public void onRinging(@NonNull Call call) {
                Log.d(TAG, "Ringing");
            }

            @Override
            public void onConnectFailure(@NonNull Call call, @NonNull CallException error) {
                setAudioFocus(false);
                Log.d(TAG, "Connect failure" + error.getLocalizedMessage() + " " + error.getMessage());
                resetUI();
            }

            @Override
            public void onConnected(@NonNull Call call) {
                setAudioFocus(true);
//                applyFabState(inputSwitchFab, fileAndMicAudioDevice.isMusicPlaying());
                Log.d(TAG, "Connected " + call.getSid());
                activeCall = call;
            }

            @Override
            public void onReconnecting(@NonNull Call call, @NonNull CallException callException) {
                Log.d(TAG, "onReconnecting");
            }

            @Override
            public void onReconnected(@NonNull Call call) {
                Log.d(TAG, "onReconnected");
            }

            @Override
            public void onDisconnected(@NonNull Call call, CallException error) {
                setAudioFocus(false);
                /*if (activeCall == call) {
                    activeCall = null;
                } else if (activeCall != null && call != null) {
                    if (activeCall == call) {
                        activeCall = null;
                        Completable.complete()
                                .delay(10, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .doOnComplete(() -> {
                                    resetUI();
                                    finish();
                                })
                                .subscribe();
                    }
                }*/
                resetUI();
                finish();
            }
        };
    }
}
