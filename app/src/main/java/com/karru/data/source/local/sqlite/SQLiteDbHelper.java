package com.karru.data.source.local.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.karru.utility.Utility;

import static com.karru.data.source.local.sqlite.SQLitePersistenceContract.CardDetailsEntry.CARD_DETAILS_BRAND;
import static com.karru.data.source.local.sqlite.SQLitePersistenceContract.CardDetailsEntry.CARD_DETAILS_DEFAULT;
import static com.karru.data.source.local.sqlite.SQLitePersistenceContract.CardDetailsEntry.CARD_DETAILS_EXPIRY_MONTH;
import static com.karru.data.source.local.sqlite.SQLitePersistenceContract.CardDetailsEntry.CARD_DETAILS_EXPIRY_YEAR;
import static com.karru.data.source.local.sqlite.SQLitePersistenceContract.CardDetailsEntry.CARD_DETAILS_KEY_ID;
import static com.karru.data.source.local.sqlite.SQLitePersistenceContract.CardDetailsEntry.CARD_DETAILS_LAST_DIGITS;
import static com.karru.data.source.local.sqlite.SQLitePersistenceContract.CardDetailsEntry.CARD_DETAILS_TABLE;
import static com.karru.data.source.local.sqlite.SQLitePersistenceContract.FavAddressEntry.FAV_DROP_ADRS_AREA;
import static com.karru.data.source.local.sqlite.SQLitePersistenceContract.FavAddressEntry.FAV_DROP_ADRS_KEY_ID;
import static com.karru.data.source.local.sqlite.SQLitePersistenceContract.FavAddressEntry.FAV_DROP_ADRS_LAT;
import static com.karru.data.source.local.sqlite.SQLitePersistenceContract.FavAddressEntry.FAV_DROP_ADRS_LONG;
import static com.karru.data.source.local.sqlite.SQLitePersistenceContract.FavAddressEntry.FAV_DROP_ADRS_NAME;
import static com.karru.data.source.local.sqlite.SQLitePersistenceContract.FavAddressEntry.FAV_DROP_ADRS_TABLE;
import static com.karru.data.source.local.sqlite.SQLitePersistenceContract.RecentAddressEntry.DROP_ADDRESS_AREA;
import static com.karru.data.source.local.sqlite.SQLitePersistenceContract.RecentAddressEntry.DROP_ADDRESS_KEY_ID;
import static com.karru.data.source.local.sqlite.SQLitePersistenceContract.RecentAddressEntry.DROP_ADDRESS_LAT;
import static com.karru.data.source.local.sqlite.SQLitePersistenceContract.RecentAddressEntry.DROP_ADDRESS_LONG;
import static com.karru.data.source.local.sqlite.SQLitePersistenceContract.RecentAddressEntry.DROP_ADDRESS_TABLE;

/**
 * <h1>SQLiteDbHelper</h1>
 * This class is used for defining database and creating columns
 * @version v1.0
 */
public class SQLiteDbHelper extends SQLiteOpenHelper
{
    private static final String TAG = "DatabaseHelper";            // Logcat tag
    private static final int DATABASE_VERSION = 1;                // Database Version
    private static final String TEXT_TYPE = " VARCHAR";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES_FOR_FAV = "CREATE TABLE IF NOT EXISTS " +
            FAV_DROP_ADRS_TABLE + "(" + FAV_DROP_ADRS_KEY_ID + TEXT_TYPE + COMMA_SEP +
            FAV_DROP_ADRS_NAME+ TEXT_TYPE + COMMA_SEP + FAV_DROP_ADRS_AREA + TEXT_TYPE + COMMA_SEP +
            FAV_DROP_ADRS_LAT + TEXT_TYPE + COMMA_SEP + FAV_DROP_ADRS_LONG + TEXT_TYPE + ")";

    private static final String CREATE_DROP_ADDRESS_TABLE = "CREATE TABLE " +
            DROP_ADDRESS_TABLE + "(" + DROP_ADDRESS_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            DROP_ADDRESS_AREA + TEXT_TYPE + COMMA_SEP + DROP_ADDRESS_LAT + TEXT_TYPE + COMMA_SEP +
            DROP_ADDRESS_LONG + TEXT_TYPE+ ")";

    private static final String CREATE_CARD_DETAILS_TABLE = "CREATE TABLE IF NOT EXISTS " +
            CARD_DETAILS_TABLE + "(" + CARD_DETAILS_KEY_ID + TEXT_TYPE + COMMA_SEP +
            CARD_DETAILS_LAST_DIGITS+ TEXT_TYPE + COMMA_SEP + CARD_DETAILS_BRAND + TEXT_TYPE + COMMA_SEP +
            CARD_DETAILS_EXPIRY_MONTH + TEXT_TYPE + COMMA_SEP + CARD_DETAILS_EXPIRY_YEAR + TEXT_TYPE +
            COMMA_SEP + CARD_DETAILS_DEFAULT + INTEGER_TYPE+")";

    /**
     * <h2>SQLiteDbHelper</h2>
     * This is constructor of this class
     * @param mContext  context from which this class is called
     */
    public SQLiteDbHelper(Context mContext)
    {
        super(mContext, mContext.getPackageName(), null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Utility.printLog(TAG+"onCreate database ");
        createFavAddressTable(db);
        createCardDetailsTable(db);
        db.execSQL(CREATE_DROP_ADDRESS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + SQLitePersistenceContract.FavAddressEntry.FAV_DROP_ADRS_TABLE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
        onUpgrade(db, 1, 2);
    }

    /**
     * <h2>createFavAddressTable</h2>
     * This method is used to create the fav address table
     * @param db database object
     */
    void createFavAddressTable(SQLiteDatabase db)
    {
        db.execSQL("DROP TABLE IF EXISTS " + FAV_DROP_ADRS_TABLE);
        db.execSQL(SQL_CREATE_ENTRIES_FOR_FAV);
    }

    /**
     * <h2>createCardDetailsTable</h2>
     * This method is used to create the card details table
     * @param db database object
     */
    void createCardDetailsTable(SQLiteDatabase db)
    {
        db.execSQL("DROP TABLE IF EXISTS " + CARD_DETAILS_TABLE);
        db.execSQL(CREATE_CARD_DETAILS_TABLE);
    }
}
