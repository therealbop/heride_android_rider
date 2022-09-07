package com.karru.data.source.local.sqlite;

import com.karru.landing.payment.model.CardDetails;
import com.karru.booking_flow.address.model.AddressDataModel;
import com.karru.booking_flow.address.model.FavAddressDataModel;

import java.util.ArrayList;

/**
 * <h1>SQLiteDataSource</h1>
 * @author by 3Embed
 * @since on 18-01-2018.
 * Main entry point for accessing tasks data.
 * <p>
 * For simplicity, only getTasks() and getTask() have callbacks. Consider adding callbacks to other
 * methods to inform the user of network/database errors or successful operations.
 * For example, when a new task is created, it's synchronously stored in cache but usually every
 * operation on database or network should be executed in a different thread.
 */
public interface SQLiteDataSource
{
    /**
     * <h2><getFavAddresses</h2>
     * extract all favorite address stored
     * @return list of fav address
     */
    ArrayList<FavAddressDataModel> getFavAddresses();

    /**
     * Only store newly added fav address.
     * @param favAddressDataModel favAddressDataModel: to be inserted into database
     * @return fav address data model id
     */
    long insertFavAddress(FavAddressDataModel favAddressDataModel);

    /**
     * <h2>insertAllFavAddresses</h2>
     * This method is used for storing all fav address
     * @param favAddressDataModels list of fav address
     */
    void insertAllFavAddresses(ArrayList<FavAddressDataModel> favAddressDataModels);

    /**
     * <h2>extractAllNonFavAddresses</h2>
     * extract all recent addresses stored
     * @return list of address
     */
    ArrayList<AddressDataModel> extractAllNonFavAddresses();

    /**
     * Only store the recent address name and lat-longs
     * @param area drop address area
     * @param lat drop address latitude
     * @param log drop address longitude
     * @return returns the database id
     */
    long insertNonFavAddressData(String area, String lat, String log) ;

    /**
     * <h2>deleteFavAddress</h2>
     * This method is used to delete the fav address from database
     * @param id id of address to be deleted
     */
    void deleteFavAddress(String id);

    /**
     * <h2>deleteRecentAddressTable</h2>
     * This method is used to delete the recent address table
     */
    void deleteRecentAddressTable();

    /**
     * <h2>deleteCardDetailsTable</h2>
     * This method is used to delete the stored card details table
     */
    void deleteCardDetailsTable();

    /**
     * <h2>insertAllCardsStored</h2>
     * This method is used for storing all cards stored
     * @param cardDetailsDataModels list of cards stored
     */
    void insertAllCardsStored(ArrayList<CardDetails> cardDetailsDataModels);

    /**
     * <h2><extractAllCardDetailsStored</h2>
     * extract all card details stored in data base
     * @return list of cards stored
     */
    ArrayList<CardDetails> extractAllCardDetailsStored();

    /**
     * <h2>insertCard</h2>
     * Only store newly added card details.
     * @param cardDetailsDataModel cardDetailsDataModel: to be inserted into database
     * @return card address data model id
     */
    long insertCard(CardDetails cardDetailsDataModel);

    /**
     * <h2>deleteCard</h2>
     * Only delete the card detail
     * @param id id of card to be deleted
     */
    void deleteCard(String id);

    /**
     * <h2>updateCardDetails</h2>
     * used to update the card with default card
     * @param id id of card
     */
    void updateCardDetails(String id,boolean isDefault);
}
