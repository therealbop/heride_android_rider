package com.karru.dagger;

import com.karru.ads.AdvertiseModule;
import com.karru.ads.AdvertiseUtilModule;
import com.karru.authentication.change_password.ChangePassUtilModule;
import com.karru.authentication.change_password.ChangePasswordActivity;
import com.karru.authentication.change_password.ChangePasswordActivityDaggerModule;
import com.karru.authentication.forgot_password.ForgotPassUtilModule;
import com.karru.authentication.forgot_password.ForgotPasswordActivity;
import com.karru.authentication.forgot_password.ForgotPasswordDaggerModule;
import com.karru.authentication.login.LoginActivity;
import com.karru.authentication.login.LoginDaggerModule;
import com.karru.authentication.login.LoginUtilsModule;
import com.karru.authentication.signup.SignUpActivity;
import com.karru.authentication.signup.SignUpDaggerModule;
import com.karru.authentication.signup.SignUpUtilModule;
import com.karru.authentication.verification.VerifyOTPActivity;
import com.karru.authentication.verification.VerifyOTPDaggerModule;
import com.karru.authentication.verification.VerifyOtpUtilModule;
import com.karru.booking_flow.address.AddressSelectModule;
import com.karru.booking_flow.address.AddressSelectionUtilModule;
import com.karru.booking_flow.address.view.AddressSelectionActivity;
import com.karru.booking_flow.invoice.InvoiceModule;
import com.karru.booking_flow.invoice.InvoiceUtilModule;
import com.karru.booking_flow.invoice.view.InvoiceActivity;
import com.karru.booking_flow.location_from_map.LocationFromMapActivity;
import com.karru.booking_flow.location_from_map.LocationFromMapDaggerModule;
import com.karru.booking_flow.location_from_map.LocationMapDaggerUtilModule;
import com.karru.booking_flow.ride.live_tracking.view.LiveTrackingActivity;
import com.karru.booking_flow.ride.live_tracking.LiveTrackingModule;
import com.karru.booking_flow.ride.live_tracking.LiveTrackingUtilModule;
import com.karru.booking_flow.ride.live_tracking.mqttChat.ChatUtilModule;
import com.karru.booking_flow.ride.live_tracking.mqttChat.ChattingActivity;
import com.karru.booking_flow.ride.live_tracking.mqttChat.ChattingModule;
import com.karru.booking_flow.ride.request.RequestingActivity;
import com.karru.booking_flow.ride.request.RequestingModule;
import com.karru.booking_flow.ride.request.RequestingUtilModule;
import com.karru.booking_flow.scheduled_booking.ScheduleBookingModule;
import com.karru.booking_flow.scheduled_booking.ScheduleUtilModule;
import com.karru.booking_flow.scheduled_booking.ScheduledBookingActivity;
import com.karru.help_center.ZendeskHelpActivity;
import com.karru.help_center.ZendeskModule;
import com.karru.help_center.ZendeskUtilModule;
import com.karru.help_center.zendesk_ticket_details.HelpIndexAdapterModule;
import com.karru.help_center.zendesk_ticket_details.HelpTicketDetailsModule;
import com.karru.help_center.zendesk_ticket_details.view.HelpIndexTicketDetailsActivity;
import com.karru.landing.MainActivity;
import com.karru.landing.MainActivityDaggerModule;
import com.karru.landing.about.AboutActivity;
import com.karru.landing.about.AboutDaggerModule;
import com.karru.landing.about.AboutUtilModule;
import com.karru.landing.add_card.AddCardActivity;
import com.karru.landing.add_card.AddCardActivityModule;
import com.karru.landing.add_card.AddCardDaggerUtilModule;
import com.karru.landing.card_details.CardDetailsActivity;
import com.karru.landing.card_details.CardDetailsActivityDaggerModule;
import com.karru.landing.card_details.CardDetailsUtilModule;
import com.karru.landing.corporate.CorporateProfileDaggerModule;
import com.karru.landing.corporate.CorporateProfileModule;
import com.karru.landing.corporate.add_corporate.AddCorporateProfileAccountActivity;
import com.karru.landing.corporate.add_corporate.AddCorporateProfileAccountDaggerModule;
import com.karru.landing.corporate.add_corporate.AddCorporateProfileUtilModule;
import com.karru.landing.corporate.view.CorporateProfileActivity;
import com.karru.landing.emergency_contact.EmergencyContactActivity;
import com.karru.landing.emergency_contact.dagger_module.EmergencyContactDaggerModule;
import com.karru.landing.favorite.FavoriteDriverDaggerModule;
import com.karru.landing.favorite.FavoriteDriverUtilModule;
import com.karru.landing.favorite.view.FavoriteDriversActivity;
import com.karru.landing.history.BookingHistoryDaggerModule;
import com.karru.landing.history.HistoryDaggerUtilModule;
import com.karru.landing.history.history_details.view.HistoryDetailsActivity;
import com.karru.landing.history.history_details.HistoryDetailsModule;
import com.karru.landing.history.history_details.HistoryUtilModule;
import com.karru.landing.history.view.BookingHistoryActivity;
import com.karru.landing.home.promo_code.PromoCodeActivity;
import com.karru.landing.home.promo_code.PromoCodeDaggerModule;
import com.karru.landing.invite.InviteActivity;
import com.karru.landing.invite.InviteActivityDaggerModule;
import com.karru.landing.invite.InviteUtilModule;
import com.karru.landing.live_chat.LiveChatActivity;
import com.karru.landing.my_vehicles.MyVehiclesActivity;
import com.karru.landing.my_vehicles.MyVehiclesDaggerModule;
import com.karru.landing.my_vehicles.MyVehiclesUtilModule;
import com.karru.landing.my_vehicles.add_new_vehicle.AddNewVehicleActivity;
import com.karru.landing.my_vehicles.add_new_vehicle.AddNewVehicleDaggerModule;
import com.karru.landing.my_vehicles.add_new_vehicle.AddNewVehicleUtilModule;
import com.karru.landing.payment.PaymentActivity;
import com.karru.landing.payment.PaymentDaggerModule;
import com.karru.landing.payment.PaymentDaggerUtilModule;
import com.karru.landing.profile.ProfileActivity;
import com.karru.landing.profile.ProfileDaggerModule;
import com.karru.landing.profile.ProfileDaggerUtilModule;
import com.karru.landing.profile.edit_email.EditEmailActivity;
import com.karru.landing.profile.edit_email.EditEmailActivityDaggermodule;
import com.karru.landing.profile.edit_email.EditEmailUtilModule;
import com.karru.landing.profile.edit_name.EditNameActivity;
import com.karru.landing.profile.edit_name.EditNameActivityDaggerModule;
import com.karru.landing.profile.edit_name.EditNameUtilModule;
import com.karru.landing.profile.edit_phone_number.EditPhoneNumberActivity;
import com.karru.landing.profile.edit_phone_number.EditPhoneNumberDaggermodule;
import com.karru.landing.profile.edit_phone_number.EditPhoneUtilModule;
import com.karru.landing.rate.RateCardActivity;
import com.karru.landing.rate.RateCardActivityDaggerModule;
import com.karru.landing.rate.RateCardUtilModule;
import com.karru.landing.support.SupportActivity;
import com.karru.landing.support.SupportDaggerModule;
import com.karru.landing.support.SupportDaggerUtilModule;
import com.karru.landing.wallet.WalletActivity;
import com.karru.landing.wallet.WalletActivityDaggerModule;
import com.karru.landing.wallet.WalletUtilModule;
import com.karru.landing.wallet.wallet_transaction.WalletTransactionDaggerModule;
import com.karru.landing.wallet.wallet_transaction.WalletTransactionUtilModule;
import com.karru.landing.wallet.wallet_transaction.view.WalletTransactionActivity;
import com.karru.managers.location.LocationModule;
import com.karru.network.NetworkReachableActivity;
import com.karru.network.NetworkReachableModule;
import com.karru.network.NetworkReachableUtilModule;
import com.karru.rental.RentCarModule;
import com.karru.rental.RentalUtilModule;
import com.karru.rental.view.RentalActivity;
import com.karru.rental.view.RentalFareActivity;
import com.karru.splash.first.SplashActivity;
import com.karru.splash.first.SplashDaggerModule;
import com.karru.splash.first.SplashUtilModule;
import com.karru.splash.second.SecondSplashActivity;
import com.karru.splash.second.SecondSplashDaggerModule;
import com.karru.splash.second.SecondSplashUtilModule;
import com.karru.twilio_call.ClientActivity;
import com.karru.twilio_call.ClientActivityDaggerModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * We want Dagger.Android to create a Subcomponent which has a parent Component of whichever module ActivityBindingModule is on,
 * in our case that will be AppComponent. The beautiful part about this setup is that you never need to tell AppComponent that it is going to have all these subcomponents
 * nor do you need to tell these subcomponents that AppComponent exists.
 * We are also telling Dagger.Android that this generated SubComponent needs to include the specified modules and be aware of a scope annotation @ActivityScoped
 * When Dagger.Android annotation processor runs it will create 4 subComponents for us.
 */
@Module
interface  ActivityBindingModule
{
    @ActivityScoped
    @ContributesAndroidInjector(modules = {SplashDaggerModule.class, LocationModule.class, SplashUtilModule.class})
    SplashActivity provideSplashActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {SecondSplashDaggerModule.class,SecondSplashUtilModule.class})
    SecondSplashActivity provideSecondSplashActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {LoginDaggerModule.class , LoginUtilsModule.class})
    LoginActivity provideLoginActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {SignUpDaggerModule.class, SignUpUtilModule.class})
    SignUpActivity provideSignUpActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {VerifyOtpUtilModule.class, VerifyOTPDaggerModule.class})
    VerifyOTPActivity provideVerifyOTPActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = MainActivityDaggerModule.class)
    MainActivity provideMainActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {ForgotPasswordDaggerModule.class, ForgotPassUtilModule.class})
    ForgotPasswordActivity provideForgotPasswordActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {AboutDaggerModule.class, AboutUtilModule.class})
    AboutActivity provideAboutActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {RateCardActivityDaggerModule.class, RateCardUtilModule.class})
    RateCardActivity provideRateCardActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {PaymentDaggerModule.class, PaymentDaggerUtilModule.class})
    PaymentActivity paymentActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {BookingHistoryDaggerModule.class, HistoryDaggerUtilModule.class})
    BookingHistoryActivity bookingHistoryActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {ProfileDaggerUtilModule.class, ProfileDaggerModule.class})
    ProfileActivity profileActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {InviteActivityDaggerModule.class,InviteUtilModule.class})
    InviteActivity provideInviteActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {SupportDaggerModule.class, SupportDaggerUtilModule.class})
    SupportActivity provideSupportActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {EditNameActivityDaggerModule.class, EditNameUtilModule.class})
    EditNameActivity provideEditNameActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {EditPhoneUtilModule.class,EditPhoneNumberDaggermodule.class})
    EditPhoneNumberActivity provideEdiPhoneNumberActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {EditEmailUtilModule.class,EditEmailActivityDaggermodule.class})
    EditEmailActivity provideEditEmailActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {ChangePassUtilModule.class,ChangePasswordActivityDaggerModule.class})
    ChangePasswordActivity provideChangePasswordActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {AddressSelectionUtilModule.class,AddressSelectModule.class})
    AddressSelectionActivity provideAddressSelectionActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {RequestingModule.class,RequestingUtilModule.class})
    RequestingActivity provideRequestingActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {AddCardDaggerUtilModule.class,AddCardActivityModule.class})
    AddCardActivity provideAddCardActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {CardDetailsUtilModule.class,CardDetailsActivityDaggerModule.class})
    CardDetailsActivity provideDeleteCardActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {LiveTrackingModule.class, LiveTrackingUtilModule.class})
    LiveTrackingActivity provideLiveTrackingActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {LocationFromMapDaggerModule.class,LocationMapDaggerUtilModule.class})
    LocationFromMapActivity provideLocationFromMap();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {InvoiceModule.class, InvoiceUtilModule.class})
    InvoiceActivity provideInvoiceActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {HistoryDetailsModule.class,HistoryUtilModule.class})
    HistoryDetailsActivity provideHistoryDetailsActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {ScheduleBookingModule.class, ScheduleUtilModule.class})
    ScheduledBookingActivity provideScheduledBookingActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = EmergencyContactDaggerModule.class)
    EmergencyContactActivity provideEmergencyContact();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {WalletUtilModule.class,WalletActivityDaggerModule.class})
    WalletActivity provideWalletActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {WalletTransactionDaggerModule.class,WalletTransactionUtilModule.class})
    WalletTransactionActivity provideWalletTransactionActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    LiveChatActivity liveChatActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {CorporateProfileDaggerModule.class,CorporateProfileModule.class})
    CorporateProfileActivity corporateProfileActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {AddCorporateProfileAccountDaggerModule.class,
            AddCorporateProfileUtilModule.class})
    AddCorporateProfileAccountActivity addCorporateProfileAccountActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {ClientActivityDaggerModule.class})
    ClientActivity clientActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {ChattingModule.class, ChatUtilModule.class})
    ChattingActivity chattingActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {FavoriteDriverDaggerModule.class, FavoriteDriverUtilModule.class})
    FavoriteDriversActivity favoriteDriversActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {AdvertiseModule.class, AdvertiseUtilModule.class})
    com.karru.ads.AdvertiseActivity advertiseDialog();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {PromoCodeDaggerModule.class})
    PromoCodeActivity promoCodeActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {NetworkReachableModule.class, NetworkReachableUtilModule.class})
    NetworkReachableActivity networkAvailableActivity();


    @ActivityScoped
    @ContributesAndroidInjector(modules = {ZendeskModule.class, ZendeskUtilModule.class})
    ZendeskHelpActivity provideZendeskHelp();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {HelpTicketDetailsModule.class, HelpIndexAdapterModule.class})
    HelpIndexTicketDetailsActivity provideHelpIndexDetails();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {MyVehiclesDaggerModule.class, MyVehiclesUtilModule.class})
    MyVehiclesActivity myVehiclesActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {AddNewVehicleDaggerModule.class, AddNewVehicleUtilModule.class})
    AddNewVehicleActivity addNewVehicleActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {RentCarModule.class, RentalUtilModule.class})
    RentalActivity rentCarActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {RentCarModule.class, RentalUtilModule.class})
    RentalFareActivity rentalFareActivity();
}
