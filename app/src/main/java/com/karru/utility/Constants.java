package com.karru.utility;

import com.heride.rider.BuildConfig;

/**
 * <h1>Constants</h1>
 * <p>
 *     Class to initialize all the required global variables constants
 * </p>
 * @since 23/5/15.
 */
public class Constants
{
 public static final String AMAZON_PROFILE_PATH="https://herideapp.s3.us-east-1.amazonaws.com/Customer/ProfilePictures/";
 public static final String AMAZON_PROFILE_FOLDER="Customer/ProfilePictures/";
 public static final String PARENT_FOLDER="com.heride.rider";

 public static boolean switchFlag = false;
 public static boolean cardFlag = false;
 public static boolean bookingFlag = false;
 public static final int DROP_ID=150;
 public static final int PICK_ID=151;
 public static final int COMPANY_ADDR_ID=152;

 /**
  * flag for order toast
  */
 public static  boolean showToast=false;
 public static final String APP_VERSION = BuildConfig.VERSION_NAME;
 public static final String DEVICE_MODEL = android.os.Build.MODEL;
 public static final String DEVICE_MAKER = android.os.Build.MANUFACTURER;
 public static final int DEVICE_TYPE = 1; //1-android, 2-ios
 public static final int CAMERA_PIC = 101, GALLERY_PIC = 102, CROP_IMAGE = 103;
 public static final int RC_SIGN_IN = 7;
 public static boolean  isToUpdateAlertVisible = false;
 public static boolean  IS_APP_BACKGROUND = false;

 /*Google places API*/
 public static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
 public static final String TYPE_AUTOCOMPLETE = "/autocomplete";
 public static final String OUT_JSON = "/json";

 /*Drop Address screen*/
 public static final int RECENT_TYPE_LIST =1,FAV_TYPE_LIST = 2,SPECIAL_TYPE =3;

 /*ImagesListAdapter*/
 public static final int ITEM_ROW = 0;
 public static final int FOOTER_ROW = 1;

 /*Booking History fragments*/
 public static final String BOOKING_HISTORY = "booking_history_list";
 public static final String BOOKING_HISTORY_TYPE = "booking_history_type";
 public static final int FROM_LIVE_TRACKING = 1;
 public static final int FROM_HISTORY = 2;

 /*Invoice Activity */
 public static final String INVOICE_DATA = "invoice_data";

 /*AddShipmentActivity*/
 public static final int PICK_CONTACT = 105;

 /*AddCardActivity */
 public static final int REQUEST_CODE_PERMISSION_MULTIPLE = 127;
 public static final int ADD_CARD_REQUEST = 100;
 public static final int CHANGE_CARD_REQUEST = 200;
 public static final int PROMO_CODE_REQUEST = 123;
 public static final String LAST_DIGITS = "last_digits";
 public static final String CARD_TOKEN = "card_token";
 public static final String CARD_BRAND = "card_brand";

 /*SignUpActivity*/
 public final static String NAME = "name";
 public final static String PHONE = "phone";
 public final static String EMAIL = "email";
 public final static String PICTURE = "picture";
 public final static String LOGIN_TYPE = "login_type";
 public final static String SOCIAL_MEDIA_ID = "ent_socialMedia_id";
 public final static String REFERRAL_CODE = "ent_referral_code";
 public final static String OTP_TIMER = "otp_timer";
 public final static String PASSWORD = "ent_password";
 public final static String COUNTRY_CODE = "ent_country_code";
 public final static String COMING_FROM = "comingFrom";
 public final static String MOBILE = "ent_mobile";
 public final static String WEB_LINK = "Link";
 public final static String SCREEN_TITLE = "Title";
 public final static String TERMS_LINK = "terms_link";
 public final static String PRIVACY_LINK = "privacy_link";

 /*Splash*/
 public final static int NORMAL_LOGIN = 1;
 public final static int PARTNER_LOGIN = 2;

 /*HomepageFragment*/
 public static final long UI_ANIMATION_DELAY = 300;

 /*Location Manager*/
 public static final int REQUEST_CHECK_SETTINGS = 1123;

 public static final double RESPONSE_IN_API = 1 ;
 public static final double RESPONSE_IN_MQTT = 0 ;

 /*VerifyOTP Activity*/
 public static final int FORGOT_PASS_TYPE = 2 ;  //2- forgotpassword
 public static final int EDIT_PHONE_NUMBER = 3 ; //3- edit phone number

 /*PaymentActivity*/
 public static final String NUMBER = "NUM";
 public static final String EXPIRE = "EXP";
 public static final String ID = "ID";
 public static final String SNAME = "NAM";
 public static final String DEFAULT = "DFLT";
 public static final int CARD_DETAILS_REQUEST = 125;
 public static final String IS_FROM_BOOKING = "is_from_booking";

 /*HomepageFragment*/
 public static final int NOW_BOOKING_TYPE = 1;
 public static final int LATER_BOOKING_TYPE = 2;

 /*Drop address*/
 public static final int DROP_ADDRESS_REQUEST = 30;
 public static final String DROP_ADDRESS_SCREEN = "drop";
 public static final String DROP_LAT = "drop_lat";
 public static final String DROP_LONG = "drop_lng";
 public static final String DROP_ADDRESS = "drop_addr";
 public static final String VEHICLE_DETAIL = "vehicel_details";

 /*Requesting Activity*/
 public static final String VEHICLE_ID = "vehicle_id";
 public static final String PAYMENT_TYPE = "payment_type";
 public static final String BOOKING_TYPE = "booking_type";
 public static final String PICK_ADDRESS = "pick_address";
 public static final String PICK_LAT = "pick_lat";
 public static final String PICK_LONG = "pick_long";
 public static final String AMOUNT = "amount";
 public static final String TIME_FARE = "timeFare";
 public static final String DISTANCE_FARE = "distFare";
 public static final String DISTANCE = "distance";
 public static final String DURATION = "duration";
 public static final String ESTIMATE_ID = "esitmate_id";
 public static final String VEHICLE_IMAGE = "vehicle_image";
 public static final String VEHICLE_NAME = "vehicle_name";
 public static final String BOOKING_DATE = "booking_date";
 public static final String BOOKING_TIME = "booking_time";
 public static final String CLEAR_STACK = "clear_stack";
 public static final String PICK_ZONE_ID = "pick_zone_id";
 public static final String PICK_ZONE_TITLE = "pick_zone_title";
 public static final String PICK_GATE_ID = "pick_gate_id";
 public static final String PICK_GATE_TITLE = "pick_gate_title";
 public static final String SOMEONE_NAME = "someone_name";
 public static final String SOMEONE_NUMBER = "someone_number";
 public static final String VEHICLE_CAPACITY = "vehicle_capacity";
 public static final String SOMEONE_ELSE_BOOKING = "someone_else_booking";
 public static final String LIVE_CHAT_NAME = "live_chat_name";
 public static final String PAYMENT_OPTION = "payment_option";
 public static final String INSTITUTE_ID = "institute_id";
 public static final String FAV_DRIVERS = "fav_drivers";
 public static final String DRIVER_PREF = "driver_pref";
 public static final String PROMO_CODE = "promocode";
 public static final String IS_TOWING_ENABLE = "is_towing_enable";
 public static final String IS_PARTNER_TYPE = "isPartnerUser";
 public static final String HOTEL_USER_TYPE = "hotelUserType";
 public static final String GUEST_NAME = "guestName";
 public static final String GUEST_COUNRY_CODE = "guestCountryCode";
 public static final String GUEST_NUMBER= "guestNumber";
 public static final String GUEST_ROOM_NUMBER= "guestRoomNumber";
 public static final String HOTEL_USER_ID= "hotel_user_id";
 public static final String PACKAGE_ID= "package_id";
 public static final int REQUESTING_SCREEN = 35;
 public static final String REQUESTING_DATA = "requestingData";
 public static final int REQUESTING_BUSY_DRIVERS = 50;
 public static final int SHOW_HOTEL = 70;
 public static final int IGNORED = 59;
 public static final int READ_CONTACTS_PERMISSIONS_REQUEST = 55;

 /*PAYMENT TYPE */
 public static final int CASH = 2;
 public static final int CARD = 1;
 public static final int BOTH = 3;
 public static final int PAY_BY_WALLET = 1;
 public static final int DONT_PAY_BY_WALLET = 0;

 /*LiveTracking*/
 public static final String BOOKING_ID="booking_id";

 /*Invoice Activity*/
 public static boolean IS_INVOICE_OPEN = false;
 public static boolean IS_CHAT_OPEN = false;

 /*Dagger */
 public static final String SPLASH="splash_activity";
 public static final String LOGIN="login_activity";
 public static final String VERIFY="verify_activity";
 public static final String FORGOT_PASS="forgot_pass_activity";
 public static final String SECOND_SPLASH="second_splash_activity";
 public static final String REGISTER="register_activity";
 public static final String LIVE="live_tracking";
 public static final String HOME_FRAG="home_fragment";
 public static final String PROMO_ACTIVITY="promo_activity";
 public static final String INVOICE="invoice_activity";
 public static final String MAIN="main_activity";
 public static final String ABOUT="about_activity";
 public static final String INVITE="invite_activity";
 public static final String MY_VEHICLES="my_vehicles_activity";
 public static final String CORPORATE_PROFILE="CorporateProfile";
 public static final String ADD_NEW_VEHICLE="add_new_vehicle";
 public static final String FAVORITE_DRIVER="favorite_driver";
 public static final String FAVORITE_ONLINE_DRIVER="favorite_online_driver";
 public static final String FAVORITE_OFFLINE_DRIVER="favorite_offline_driver";
 public static final String CORPORATE_ADD_PROFILE="ADdCorporateProfile";
 public static final String REQUEST="request_activity";
 public static final String HISTORY_DETAILS="history_details_activity";
 public static final String SCHEDULE="schedule_activity";
 public static final String ADS="advertise_activity";
 public static final String EMERGENCY="emergency_activity";
 public static final String SUPPORT="support_activity";
 public static final String PAYMENT="payment_activity";
 public static final String HISTORY="history_activity";
 public static final String PROFILE="profile_activity";
 public static final String EDIT_NAME="edit_name_activity";
 public static final String EDIT_PHONE="edit_phone_activity";
 public static final String EDIT_EMAIL="edit_email_activity";
 public static final String CHANGE_PASS="change_pass_activity";
 public static final String RATE_CARD="rate_card_activity";
 public static final String ADDRESS_SELECTION="address_selection_activity";
 public static final String ADD_CARD="add_card_activity";
 public static final String CARD_DETAILS="card_details_activity";
 public static final String NETWORK="network_activity";
 public static final String LOCATION_FROM_MAP="location_map_activity";
 public static final String WALLET="wallet_activity";
 public static final String WALLET_TRANSACTION="wallet_transaction_activity";
 public static final String CHAT="chat_activity";
 public static final String ADVERTISE_DETAILS="adv_details";

 public static String GMT_CURRENT_LAT = "0";
 public static String GMT_CURRENT_LNG = "0";

 /*Schedule booking*/
 public static final int SCHEDULE_CODE= 56;
 public static final int CANCELLED_BOOKING= 5;
 public static final int BACK_PRESSED= 80;
 public static final int DRIVER_WIDTH= 35;
 public static final int DRIVER_HEIGHT= 50;
 public static final int PROFILE_CODE= 30;

 public static boolean LIVE_TRACKING_OPEN=false;
 public static boolean NETWORK_SCREEN_OPEN=false;

 /*Permissions */
 public static final int PERMISSION_REQUEST = 1;
 public static final int LOCATION_PERMISSION_CODE = 4;
 public static final int WRITE_STORAGE_PERMISSION_CODE = 8;
 public static final int PERMISSION_GRANTED = 40;
 public static final int PERMISSION_DENIED = 41;
 public static final int PERMISSION_BLOCKED = 42;
 public static final int OREO_PERMISSION = 78;
 /*Twillio*/
 public static final String PHONE_NUMBER="phonenumber";
 public static final String PHONE_IMAGE_URL="ImageUrl";
 public static final String PHONE_TO="to";
 public static final String PHONE_FROM="From";
 public static final String LANGUAGE="language";

 /*Advertise*/
 //1-view 2-ignore
 public static final int VIEWED = 1;
 public static final int IGNORE = 2;
 public static final int ADD_VEHICLE = 45;
}
