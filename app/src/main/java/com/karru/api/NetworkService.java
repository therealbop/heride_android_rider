package com.karru.api;

import com.karru.ConfigResponseModel;
import com.karru.authentication.UserDetailsDataModel;
import com.karru.booking_flow.address.model.FavAddressDataModel;
import com.karru.splash.first.LanguagesListModel;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * <h1>NetworkService</h1>
 * This class is used to provide the Observable<Response<ResponseBody>>
 * @author 3Embed
 * @since on 23-11-2017.
 */
public interface NetworkService
{
    /**********************************LoginActivity***************************************/
    @POST("/customer/signIn")
    @Headers( "Content-Type: application/json;charset=UTF-8")
    Observable<Response<ResponseBody>> loginAPI(@Header("lan") String languageCode,
                                                @Body UserDetailsDataModel userDetailsDataModel);

    /**************************************Common Activities***********************************************/

    @GET("/customer/appConfig/{deviceType}")
    Observable<Response<ConfigResponseModel>> getConfigurations(@Header("authorization") String authToken,
                                                                @Header("lan") String languageCode,
                                                                @Path("deviceType") int deviceType);//1-android, 2-ios

    @GET("/customer/drivers/{latitude}/{longitude}/{mqttTopic}/{api}/{serviceType}")
    Observable<Response<ResponseBody>> getDrivers(@Header("authorization") String authToken,
                                                  @Header("lan") String languageCode,
                                                  @Path("latitude") String latitude,
                                                  @Path("longitude") String longitude,
                                                  @Path("mqttTopic") String mqttTopic,
                                                  @Path("api") double apiType,
                                                  @Path("serviceType") double serviceType);//1-android, 2-ios

    /***************************************SignUpActivity******************************************/

    @POST("/customer/emailValidation")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> validateEmailAPI(@Header("lan") String languageCode,
                                                        @Field("email") String emailID);

    @POST("/customer/phoneValidation")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> validateMobileAPI(@Header("lan") String languageCode,
                                                         @Field("countryCode") String countryCode,
                                                         @Field("phone") String mobileNumber);

    @POST("/customer/referralCodeValidation")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> validateReferralCode(@Header("lan") String languageCode,
                                                            @Field("referralCode") String referralCode);

    @POST("/customer/signUp")
    Observable<Response<ResponseBody>> userSignUp(@Header("lan") String languageCode,
                                                  @Body UserDetailsDataModel userDetailsDataModel);

    /**************************************VerifyActivity*********************************************/

    @POST("/customer/resendOtp")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> getVerificationCode(@Header("lan") String languageCode,
                                                           @Field("userId") String userSid,
                                                           @Field("trigger") int triggerFrom);
    //1 - Register,2 - Forgot Password,3-change number, 4-login with phone OTP

    @POST("/customer/verifyPhoneNumber")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> verifyPhoneNumber(@Header("lan") String languageCode,
                                                         @Field("code") double OTP,
                                                         @Field("userId") String userSid);

    @POST("/customer/verifyVerificationCode")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> verifyOTPCode(@Header("lan") String languageCode,
                                                     @Field("code") double OTP,
                                                     @Field("userId") String userSid,
                                                     @Field("trigger") int triggerFrom); //2- forgotpassword , 3- change number

    /**************************************SupportActivity********************************************/

    @GET("/customer/support")
    Observable<Response<ResponseBody>> supportAPI(@Header("lan") String language,
                                                  @Header("authorization") String authToken);


    /*****************************************RateCardActivity**************************************/

    @GET("/customer/rateCard")
    Observable<Response<ResponseBody>> getRateCard(@Header("authorization") String authToken,
                                                   @Header("lan") String language);

    /******************************************BookingHistoryActivity***********************************/

    @GET("/customer/bookingHistory")
    Observable<Response<ResponseBody>> getBookingHistory(@Header("authorization") String authToken,
                                                         @Header("lan") String language);

    /**********************************************PaymentActivity***********************/

    @HTTP(method = "DELETE", path = "/customer/card", hasBody = true)
    @FormUrlEncoded
    Observable<Response<ResponseBody>> deleteCard(@Header("authorization") String authToken,
                                                  @Header("lan") String language,
                                                  @Field("cardId") String cardId);

    /**********************************************AddCardActivity***********************/

    @POST("/customer/card")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> addCard(@Header("authorization") String language,
                                               @Header("lan") String languageCode,
                                               @Field("email") String email,
                                               @Field("cardToken") String cardToken);

    /****************************************ProfileActivity****************************************/

    @PATCH("/customer/profile")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> updateProfilePic(@Header("authorization") String authToken,
                                                        @Header("lan") String languageCode,
                                                        @Field("profilePic") String profilePicUrl);

    @POST("/customer/signOut")
    Observable<Response<ResponseBody>> logOut(@Header("authorization") String authToken,
                                              @Header("lan") String language);

    /********************************** MainActivity **************************************/

    @GET("/customer/address")
    Observable<Response<ResponseBody>> getFavAddresses(@Header("authorization") String authToken,
                                                       @Header("lan") String languageCode);


    /********************************** HOMEFragment **************************************/
    @POST("/customer/address")
    Observable<Response<ResponseBody>> addFavAddressAPI(@Header("authorization") String language,
                                                        @Header("lan") String languageCode,
                                                        @Body FavAddressDataModel favAddressDataModel);

    @DELETE("/customer/address/{id}")
    Observable<Response<ResponseBody>> deleteFavAddress(@Header("authorization") String language,
                                                        @Header("lan") String languageCode,
                                                        @Path("id") String addressId);

    @GET("/customer/lastDue")
    Observable<Response<ResponseBody>> getOutstandingBalance(@Header("authorization") String language,
                                                             @Header("lan") String languageCode);


    @GET("/customer/preferences/{cityId}/{typeId}/{serviceType}")
    Observable<Response<ResponseBody>> getDriverPreferences(@Header("authorization") String language,
                                                            @Header("lan") String languageCode,
                                                            @Path("cityId") String cityId,
                                                            @Path("typeId") String typeId,
                                                            @Path("serviceType") int serviceType);
    @GET
    Observable<Response<ResponseBody>> getETAMatrix(@Url String url);

    @POST("/customer/fareEstimate")
    @FormUrlEncoded
    Observable<Response<ResponseBody>>  fareEstimation(@Header("authorization") String authToken,
                                                       @Header("lan") String languageCode,
                                                       @Field("vehicleTypeId") String vehicleId,
                                                       @Field("pickupLatLong") String latLong,
                                                       @Field("dropLatLong") String dropLatLong,
                                                       @Field("bookingType") int bookingType,
                                                       @Field("pickup") String pickAddress,
                                                       @Field("drop") String drop,
                                                       @Field("instituteUserId") String instituteUserId,
                                                       @Field("promoCode") String promoCode,
                                                       @Field("paymentMethod") int paymentMethod,
                                                       @Field("payByWallet") int payByWallet,
                                                       @Field("bookingPreference") String bookingPreference);

    @POST("/customer/promoCodeValidation")
    @FormUrlEncoded
    Observable<Response<ResponseBody>>  validatePromo(@Header("authorization") String authToken,
                                                      @Header("lan") String languageCode,
                                                      @Field("latitude") double latitude,
                                                      @Field("longitude") double longitude,
                                                      @Field("amount") double amount,
                                                      @Field("vehicleTypeId") String vehicleTypeId,
                                                      @Field("paymentMethod") int paymentMethod,
                                                      @Field("payByWallet") int payByWallet,
                                                      @Field("couponCode") String couponCode);

    /*******************************ForGotPasswordActivity*******************************************/

    @POST("/customer/forgotPassword")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> forgotPassword(@Header("lan") String languageCode,
                                                      @Field("emailOrPhone") String eMail,
                                                      @Field("countryCode") String countryCode,
                                                      @Field("type") int type);

    /*******************************AddressActivity*******************************************/

    @GET("/customer/hotelFavouriteAddress/{search}")
    Observable<Response<ResponseBody>> fetchHotelFavAddress(@Header("authorization") String authToken,
                                                            @Header("lan") String languageCode,
                                                            @Path("search") String search);

    /****************************EditPhoneNumberActivity************************************************/

    @POST("/customer/phoneValidation")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> verifyPhoneNumber(@Header("lan") String language,
                                                         @Field("countryCode") String countryCode,
                                                         @Field("phone") String phoneNuber);

    @PATCH("/customer/phoneNumber")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> changePhoneNumber(@Header("authorization") String authorization,
                                                         @Header("lan") String language,
                                                         @Field("countryCode") String countryCode,
                                                         @Field("phone") String phoneNumber);

    /***************************EditNameActivity*****************************************************/
    @PATCH("/customer/profile")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> updateProfileEditName(@Header("authorization") String authToken,
                                                             @Header("lan") String language,
                                                             @Field("firstName") String name);

    /*************************EditEmailActivity************************************************************/

    @POST("/customer/emailValidation")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> verifyEmail(@Header("lan") String language,
                                                   @Field("email") String Emial);

    @PATCH("/customer/email")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> changeEmailAPI(@Header("authorization") String authorization,
                                                      @Header("lan") String language,
                                                      @Field("email") String email);

    /***************************ChangePasswordActivity******************************************************/

    @PATCH("/customer/password")
    @FormUrlEncoded
    Observable<Response<ResponseBody>>  changePassword(@Header("lan") String language,
                                                       @Field("password") String password,
                                                       @Field("userId") String userId);

    @PATCH("/customer/password/me")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> getProfileCheckPassword(@Header("authorization") String authToken,
                                                               @Header("lan") String language,
                                                               @Field("oldPassword") String oldPassword,
                                                               @Field("newPassword") String newPassword);

    /*****************************RequestingActivity********************************************************/

    @POST("/customer/book")
    @FormUrlEncoded
    Observable<Response<ResponseBody>>  liveBooking(@Header("authorization") String authToken,
                                                    @Header("lan") String languageCode,
                                                    @Field("vehicleTypeId") String vehicleId,
                                                    @Field("paymentType") int paymentType,
                                                    @Field("payByWallet") int payByWallet,
                                                    @Field("bookingType") int bookingType,
                                                    @Field("deviceId") String deviceId,
                                                    @Field("deviceType") int deviceType,
                                                    @Field("bookingDate") String bookingDate,
                                                    @Field("deviceTime") String deviceTime,
                                                    @Field("amount") String amount,
                                                    @Field("timeFare") String timeFare,
                                                    @Field("distFare") String distFare,
                                                    @Field("distance") String distance,
                                                    @Field("duration") String duration,
                                                    @Field("pickupAddress") String pickupAddress,
                                                    @Field("pickupLongitude") String pickupLongitude,
                                                    @Field("pickupLatitude") String pickupLatitude ,
                                                    @Field("dropAddress") String dropAddress,
                                                    @Field("dropLongitude") String dropLongitude,
                                                    @Field("dropLatitude") String dropLatitude,
                                                    @Field("cardId") String cardToken,
                                                    @Field("cardType") String cardType,
                                                    @Field("estimateId") String estimateId,
                                                    @Field("areaZoneId") String areaZoneId,
                                                    @Field("areaZoneTitle") String areaZoneTitle,
                                                    @Field("areaPickupId") String areaPickupId,
                                                    @Field("areaPickupTitle") String areaPickupTitle,
                                                    @Field("customerName") String customerName,
                                                    @Field("customerNumber") String customerNumber,
                                                    @Field("numOfPassanger") String numOfPassanger,
                                                    @Field("bookingPreference") String bookingPreference,
                                                    @Field("isSomeOneElseBooking") int isSomeOneElseBooking,
                                                    @Field("instituteUserId") String instituteUserId,
                                                    @Field("favoriteDriverId") String favoriteDriverId ,
                                                    @Field("serviceType") int serviceType,
                                                    @Field("promoCode") String promoCode,
                                                    @Field("userVehicleId") String userVehicleId);


    @PUT("/customer/cancelBookingBeforeAccept")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> cancelBookingBeforeAccept(@Header("authorization") String authToken,
                                                                 @Header("lan") String languageCode,
                                                                 @Field("bookingId") String bookingId,
                                                                 @Field("reason") String reason);

    /**************************************LiveTrackingActivity*************************************/

    @GET("/customer/bookingDetails/{bookingId}/{callFrom}")
    Observable<Response<ResponseBody>> bookingDetails(@Header("authorization") String authToken,
                                                      @Header("lan") String languageCode,
                                                      @Path("bookingId") String bookingId,
                                                      @Path("callFrom") int calledFrom);

    @GET("/customer/cancellationFees/{bookingId}")
    Observable<Response<ResponseBody>> cancelFeesAPI(@Header("authorization") String authToken,
                                                     @Header("lan") String languageCode,
                                                     @Path("bookingId") String bookingId);

    @GET("/customer/cancellationReasonRide")
    Observable<Response<ResponseBody>> cancelReasons(@Header("authorization") String authToken,
                                                     @Header("lan") String languageCode);

    @PUT("/customer/cancelBookingAfterAccept")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> cancelBookingAPI(@Header("authorization") String authToken,
                                                        @Header("lan") String languageCode,
                                                        @Field("bookingId") String bookingId,
                                                        @Field("reason") String reason);

    @POST("/customer/dropLocation")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> updateDropAddress(@Header("authorization") String authToken,
                                                         @Header("lan") String languageCode,
                                                         @Field("bookingId") String bookingId,
                                                         @Field("dropLatitude") String dropLatitude,
                                                         @Field("dropLongitude") String dropLongitude,
                                                         @Field("dropAddress") String dropAddress);

    /*********************************CardDetailsACtivity*************************************************/
    @PATCH("/customer/card")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> makeDefaultCard(@Header("authorization") String language,
                                                       @Header("lan") String languageCode,
                                                       @Field("cardId") String cardId);


    /********************************* Invoice Activity *************************************************/

    @GET("/customer/ratingTextRide")
    Observable<Response<ResponseBody>> driverFeedbackDetails(@Header("authorization") String language,
                                                             @Header("lan") String languageCode);

    @POST("/customer/reviewAndRating")
    @FormUrlEncoded
    Observable<Response<ResponseBody>>  updateReview(@Header("authorization") String authToken,
                                                     @Header("lan") String languageCode,
                                                     @Field("bookingId") String bookingId,
                                                     @Field("rating") double rating,
                                                     @Field("review") StringBuilder review);

    @POST("/customer/bookingTip")
    @FormUrlEncoded
    Observable<Response<ResponseBody>>  updateTip(@Header("authorization") String authToken,
                                                  @Header("lan") String languageCode,
                                                  @Field("bookingId") String bookingId,
                                                  @Field("tip") double rating);

    @POST("/customer/favouriteDriver")
    @FormUrlEncoded
    Observable<Response<ResponseBody>>  addDriverToFav(@Header("authorization") String authToken,
                                                       @Header("lan") String languageCode,
                                                       @Field("driverId") String driverId);

    @HTTP(method = "DELETE", path = "/customer/favouriteDriver/{driverId}", hasBody = true)
    Observable<Response<ResponseBody>> deleteDriverFromFav(@Header("authorization") String authToken,
                                                           @Header("lan") String language,
                                                           @Path("driverId") String driverId);


    /*********************************** EmergencyContact *******************************************/

    @POST("/customer/emergencyContact")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> saveEmergencyContact(@Header("authorization") String language,
                                                            @Header("lan") String languageCode,
                                                            @Field("contactName") String contactName,
                                                            @Field("contactNumber") String contactNumber);

    @GET("/customer/emergencyContact")
    Observable<Response<ResponseBody>> getSavedEmergencyContact(@Header("authorization") String language,
                                                                @Header("lan") String languageCode);

    @HTTP(method = "DELETE", path = "/customer/emergencyContact/{id}", hasBody = true)
    Observable<Response<ResponseBody>> deleteContact(@Header("authorization") String authToken,
                                                     @Header("lan") String language,
                                                     @Path("id") String id);

    @PATCH("/customer/emergencyContactStatus/{id}/{status}")
    Observable<Response<ResponseBody>> updateContactStatus(@Header("authorization") String authToken,
                                                           @Header("lan") String language,
                                                           @Path("id") String id,
                                                           @Path("status") int status);  //1-Active,0-DeActive

    /**************************************WalletTransactionActivity****************************/

    @GET("/customer/walletTransaction/{pageIndex}")
    Observable<Response<ResponseBody>> getWalletTransaction(@Header("authorization") String authToken,
                                                            @Header("lan") String language,
                                                            @Path("pageIndex") String pageIndex);

    /**************************************WalletActivity****************************/
    @POST("/customer/rechargeWallet")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> rechargeWallet(@Header("authorization") String authToken,
                                                      @Header("lan") String language,
                                                      @Field("cardId") String cardId,
                                                      @Field("amount") String amount);

    /*********************************Corporate Profile Activity ****************** */

    @GET("/customer/institute/user/accounts")
    Observable<Response<ResponseBody>> getCorporateProfiles(@Header("authorization") String authToken,
                                                            @Header("lan") String language);

    /*********************************AddCorporateProfileActivity ****************** */

    @POST("/customer/institute/user/officialEmailVerification")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> addCorporateProfile(@Header("authorization") String authToken,
                                                           @Header("lan") String language,
                                                           @Field("email") String email);

    /*********************************Favorites ****************** */

    @GET("/customer/favouriteDriver")
    Observable<Response<ResponseBody>> getFavorites(@Header("authorization") String authToken,
                                                    @Header("lan") String language);

    /*********************************SPlash ****************** */

    @GET("/customer/languages")
    Observable<Response<LanguagesListModel>> getLanguages();

    /************************************Chat***********************/
    @POST("message")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> message(@Header("language") String language,
                                               @Header("authorization") String authorization,
                                               @Field("type") String type,
                                               @Field("timestamp") String timestamp,
                                               @Field("content") String content,
                                               @Field("fromID") String fromID,
                                               @Field("bid") String bid,
                                               @Field("targetId") String targetId);

    @GET("chatHistory/{bookingId}/{pageNo}")
    Observable<Response<ResponseBody>> chatHistory(@Header("language") String language,
                                                   @Header("authorization") String authorization,
                                                   @Path("bookingId") String bid,
                                                   @Path("pageNo") String page);

    /********************************************INVITE**********************************/

    @GET("/customer/referralCode")
    Observable<Response<ResponseBody>> fetchReferral(@Header("authorization") String authorization,
                                                     @Header("language") String language);

    /********************************************MY VEHICLES**********************************/

    @GET("/customer/vehicleDeatils")
    Observable<Response<ResponseBody>> fetchMyVehicles(@Header("authorization") String authorization,
                                                       @Header("language") String language);

    @HTTP(method = "DELETE", path = "/customer/vehicleDelete", hasBody = true)
    @FormUrlEncoded
    Observable<Response<ResponseBody>> deleteVehicle(@Header("authorization") String authToken,
                                                     @Header("lan") String language,
                                                     @Field("vehicleId") String vehicleId);

    /**************************************NewVehicle******************************************/
    @GET("/customer/getYears")
    Observable<Response<ResponseBody>> getYearData(@Header("authorization") String authorization,
                                                   @Header("lan") String lan);

    @POST("/customer/getMake")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> getMakeData(@Header("authorization") String authorization,
                                                   @Header("lan") String lan,
                                                   @Field("year") String year);

    @POST("/customer/getModel")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> getModelData(@Header("authorization") String authorization,
                                                    @Header("lan") String lan,
                                                    @Field("year") String year,
                                                    @Field("makeId") String makeId);

    @POST("/customer/vehicle")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> saveVehicleDetails(@Header("authorization") String authorization,
                                                          @Header("lan") String lan,
                                                          @Field("vehicleYear") String vehicleYear,
                                                          @Field("vehicleMake") String vehicleMake,
                                                          @Field("vehicleModel") String vehicleModel,
                                                          @Field("vehicleColor") String vehicleColor);

    /********************************************ZENDESK**********************************/

    //Zendesk API
    @GET("zendesk/user/ticket/{emailId}")
    Observable<Response<ResponseBody>> onToGetZendeskTicket(@Header("authorization") String authorization,
                                                            @Header("language") String language,
                                                            @Path("emailId") String emailId);


    @GET("zendesk/ticket/history/{id}")
    Observable<Response<ResponseBody>> onToGetZendeskHistory(@Header("authorization") String authorization,
                                                             @Header("language") String language,
                                                             @Path("id") String id);


    @POST("zendesk/ticket")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> createTicket(@Header("authorization") String authorization,
                                                    @Header("language") String language,
                                                    @Field("subject") String subject,
                                                    @Field("body") String body,
                                                    @Field("status") String status,
                                                    @Field("priority") String priority,
                                                    @Field("type") String type,
                                                    @Field("requester_id") String requester_id);

    @PUT("zendesk/ticket/comments")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> commentOnTicket(@Header("authorization") String authorization,
                                                       @Header("language") String language,
                                                       @Field("id") String id,
                                                       @Field("body") String body,
                                                       @Field("author_id") String author_id);

    /*************************************PromoCode********************************************/

    @GET("/customer/promoCode/{lat}/{long}")
    Observable<Response<ResponseBody>> getPromoCodeData(@Header("authorization") String authorization,
                                                        @Header("lan") String lan,
                                                        @Path("lat") double latitude,
                                                        @Path("long") double longitude);

    /************************************HelpCenter********************************************/

    @GET("/customer/helpText/{status}")
    Observable<Response<ResponseBody>> getHelpData(@Header("authorization") String authorization,
                                                   @Header("lan") String lan,
                                                   @Path("status") double status);

    /*************************************Advertise********************************************/

    @PATCH("/utility/notification")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> updateNotificationStatus(@Header("authorization") String language,
                                                                @Header("lan") String languageCode,
                                                                @Field("status") int status,
                                                                @Field("messageId") String messageId);

    /**************************************RentalActivity*************************************/

    @GET("/customer/getPackages/{pickLat}/{pickLong}")
    Observable<Response<ResponseBody>> getPackages(@Header("authorization") String authToken,
                                                   @Header("lan") String languageCode,
                                                   @Path("pickLat") String pickLat,
                                                   @Path("pickLong") String pickLong);

    @GET("/customer/getRentalVehicleTypes/{pickLat}/{pickLong}/{packageId}/{bookingType}")
    Observable<Response<ResponseBody>> getRentalVehicleTypes(@Header("authorization") String authToken,
                                                             @Header("lan") String languageCode,
                                                             @Path("pickLat") String pickLat,
                                                             @Path("pickLong") String pickLong,
                                                             @Path("packageId") String packageId,
                                                             @Path("bookingType") int bookingType);
}


