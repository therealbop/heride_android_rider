package com.karru.data.source.local.sqlite;

import android.provider.BaseColumns;

/**
 * <h1>SQLitePersistenceContract</h1>
 * The contract used for the db to save the fav address locally.
 */
final class SQLitePersistenceContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private SQLitePersistenceContract() {}

    /* Inner class that defines the table contents */
    static abstract class FavAddressEntry implements BaseColumns
    {
        static final String FAV_DROP_ADRS_TABLE = "Fav_Drop_Adrs_Table";         // New Add Drop Address Table, created on 17/03/17
        static final String FAV_DROP_ADRS_KEY_ID = "Fav_Drop_Adrs_KeyId";        // Common column names
        static final String FAV_DROP_ADRS_NAME = "Fav_Drop_Adrs_Name";
        static final String FAV_DROP_ADRS_AREA = "Fav_Drop_Adrs_Area";
        static final String FAV_DROP_ADRS_LAT = "Fav_Drop_Adrs_Lat";
        static final String FAV_DROP_ADRS_LONG = "Fav_Drop_Adrs_Long";
    }

    /* Inner class that defines the table contents */
    static abstract class RecentAddressEntry implements BaseColumns
    {
        static final String DROP_ADDRESS_TABLE = "Drop_Address_Table";         // New Add Drop Address Table, created on 17/03/17
        static final String DROP_ADDRESS_KEY_ID = "Drop_Address_KeyId";        // Common column names
        static final String DROP_ADDRESS_AREA = "Drop_Address_Area";
        static final String DROP_ADDRESS_LAT = "Drop_Address_Latitude";
        static final String DROP_ADDRESS_LONG = "Drop_Address_Longitude";
    }

    /* Inner class that defines the table contents */
    static abstract class CardDetailsEntry implements BaseColumns
    {
        static final String CARD_DETAILS_TABLE = "Card_details_table";         // New Add Drop Address Table, created on 17/03/17
        static final String CARD_DETAILS_KEY_ID = "Card_details_KeyId";        // Common column names
        static final String CARD_DETAILS_LAST_DIGITS = "Card_details_card_last_digits";
        static final String CARD_DETAILS_BRAND = "Card_details_brand";
        static final String CARD_DETAILS_EXPIRY_MONTH = "Card_details_expiry_month";
        static final String CARD_DETAILS_EXPIRY_YEAR = "Card_details_expiry_year";
        static final String CARD_DETAILS_DEFAULT = "Card_details_default";
    }
}