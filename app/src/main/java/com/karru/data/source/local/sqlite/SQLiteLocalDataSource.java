package com.karru.data.source.local.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.karru.landing.payment.model.CardDetails;
import com.karru.booking_flow.address.model.AddressDataModel;
import com.karru.booking_flow.address.model.FavAddressDataModel;
import com.karru.utility.Utility;
import java.util.ArrayList;
import javax.inject.Inject;

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
import static com.karru.utility.Constants.RECENT_TYPE_LIST;

/**
 * <h1>SQLiteLocalDataSource</h1>
 * This method is used to do the address database operations
 * @author 3Embed
 * @since on 18-01-2018.
 */
public class SQLiteLocalDataSource implements SQLiteDataSource
{

    private static final String TAG = "SQLiteLocalDataSource";
    private SQLiteDbHelper sqLiteDbHelper;

    @Inject
    SQLiteLocalDataSource(SQLiteDbHelper addressDbHelper)
    {
        this.sqLiteDbHelper = addressDbHelper;
    }

    @Override
    public ArrayList<FavAddressDataModel> getFavAddresses()
    {
        Utility.printLog(TAG+ "getFavAddresses called() ");
        ArrayList<FavAddressDataModel> favAddressDataModels = new ArrayList<>();
        SQLiteDatabase db = sqLiteDbHelper.getReadableDatabase();

        String[] projection =
                {
                        FAV_DROP_ADRS_KEY_ID,
                        FAV_DROP_ADRS_NAME,
                        FAV_DROP_ADRS_AREA,
                        FAV_DROP_ADRS_LAT,
                        FAV_DROP_ADRS_LONG
                };

        Cursor mCursor = db.query(
                FAV_DROP_ADRS_TABLE, projection,
                null, null, null, null, null);

        if ((mCursor != null && mCursor.getCount() > 0) && mCursor.moveToFirst())
        {
            do
            {
                FavAddressDataModel favDropAdrsData = new FavAddressDataModel();
                favDropAdrsData.setAddressId(mCursor.getString(mCursor.getColumnIndexOrThrow(FAV_DROP_ADRS_KEY_ID)));
                favDropAdrsData.setName(mCursor.getString(mCursor.getColumnIndexOrThrow(FAV_DROP_ADRS_NAME)));
                favDropAdrsData.setAddress(mCursor.getString(mCursor.getColumnIndexOrThrow(FAV_DROP_ADRS_AREA)));
                favDropAdrsData.setLatitude(mCursor.getDouble(mCursor.getColumnIndexOrThrow(FAV_DROP_ADRS_LAT)));
                favDropAdrsData.setLongitude(mCursor.getDouble(mCursor.getColumnIndexOrThrow(FAV_DROP_ADRS_LONG)));
                favAddressDataModels.add(favDropAdrsData);
            }
            while (mCursor.moveToNext());
            mCursor.close();
        }

        db.close();
        Utility.printLog(TAG+ "getFavAddresses size(): " + favAddressDataModels.size());
        return favAddressDataModels;
    }

    @Override
    public long insertFavAddress(FavAddressDataModel favAddressDataModel)
    {
        Utility.printLog(TAG+ "insertFavDropAdrssData favDropAdrsData: "+ favAddressDataModel.toString());
        SQLiteDatabase db = sqLiteDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FAV_DROP_ADRS_KEY_ID, favAddressDataModel.getAddressId());
        contentValues.put(FAV_DROP_ADRS_NAME, favAddressDataModel.getName());
        contentValues.put(FAV_DROP_ADRS_AREA, favAddressDataModel.getAddress());
        contentValues.put(FAV_DROP_ADRS_LAT, String.valueOf(favAddressDataModel.getLatitude()));
        contentValues.put(FAV_DROP_ADRS_LONG, String.valueOf(favAddressDataModel.getLongitude()));

        long id = db.insert(FAV_DROP_ADRS_TABLE, null, contentValues);
        Utility.printLog(TAG+ "insertFavDropAdrssData id: "+id);
        db.close();
        return id;
    }

    @Override
    public void insertAllFavAddresses(ArrayList<FavAddressDataModel> favAddressDataModels)
    {
        SQLiteDatabase db = sqLiteDbHelper.getWritableDatabase();
        sqLiteDbHelper.createFavAddressTable(db);

        for(FavAddressDataModel favAddressDataModel: favAddressDataModels)
        {
            Utility.printLog(TAG+ "insertAllFavAddresses size: "+favAddressDataModel.getAddressId()
                    +" "+favAddressDataModel.get_id());
            ContentValues contentValues = new ContentValues();
            contentValues.put(FAV_DROP_ADRS_KEY_ID, favAddressDataModel.get_id());
            contentValues.put(FAV_DROP_ADRS_NAME, favAddressDataModel.getName());
            contentValues.put(FAV_DROP_ADRS_AREA, favAddressDataModel.getAddress());
            contentValues.put(FAV_DROP_ADRS_LAT, String.valueOf(favAddressDataModel.getLatitude()));
            contentValues.put(FAV_DROP_ADRS_LONG, String.valueOf(favAddressDataModel.getLongitude()));

            long id = db.insert(FAV_DROP_ADRS_TABLE, null, contentValues);
            Utility.printLog(TAG+" insertFavDropAdrssData id: " + id);
        }
    }

    @Override
    public ArrayList<AddressDataModel> extractAllNonFavAddresses()
    {
        ArrayList<AddressDataModel> addressDataModels = new ArrayList<>();
        SQLiteDatabase db = sqLiteDbHelper.getReadableDatabase();
        String[] projection =
                {
                        DROP_ADDRESS_KEY_ID,
                        DROP_ADDRESS_AREA,
                        DROP_ADDRESS_LAT,
                        DROP_ADDRESS_LONG
                };

        Cursor mCursor = db.query(
                DROP_ADDRESS_TABLE, projection,
                null, null, null, null, null);

        if ((mCursor != null && mCursor.getCount() > 0) && mCursor.moveToFirst())
        {
            do {
                AddressDataModel addressDataModel = new AddressDataModel();
                addressDataModel.setAddressId(mCursor.getInt(mCursor.getColumnIndex(DROP_ADDRESS_KEY_ID)));
                addressDataModel.setAddress(mCursor.getString(mCursor.getColumnIndex(DROP_ADDRESS_AREA)));
                addressDataModel.setLat(mCursor.getString(mCursor.getColumnIndex(DROP_ADDRESS_LAT)));
                addressDataModel.setLng(mCursor.getString(mCursor.getColumnIndex(DROP_ADDRESS_LONG)));
                addressDataModel.setType(RECENT_TYPE_LIST);
                Utility.printLog("DB extractFrmAdrsTable: latitude: " + addressDataModel.getLat() + " ,logitude: " + addressDataModel.getLng()+" ,address: " + addressDataModel.getAddress()+" ,addressid: " + addressDataModel.getAddressId());
                addressDataModels.add(addressDataModel);
            } while (mCursor.moveToNext());
            mCursor.close();
        }
        Utility.printLog("DB extractAllFrmAdrsTable rcntlyVisitedAL.size(): " + addressDataModels.size());
        return addressDataModels;
    }

    @Override
    public long insertNonFavAddressData(String area, String lat, String log)
    {
        SQLiteDatabase db = sqLiteDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DROP_ADDRESS_AREA, area);
        contentValues.put(DROP_ADDRESS_LAT, lat);
        contentValues.put(DROP_ADDRESS_LONG, log);
        long id = db.insert(DROP_ADDRESS_TABLE, null, contentValues);
        Utility.printLog(TAG+" INSERTED ROW NUMBER IS :InsertDropData: " + id );
        return id;
    }

    @Override
    public void deleteFavAddress(String id)
    {
        Utility.printLog(TAG+ "deleteFavDropAdrs id: "+id);
        SQLiteDatabase db = sqLiteDbHelper.getReadableDatabase();
        db.delete(FAV_DROP_ADRS_TABLE, FAV_DROP_ADRS_KEY_ID + "=? ", new String[]{id});
    }

    @Override
    public void deleteRecentAddressTable()
    {
        SQLiteDatabase db = sqLiteDbHelper.getWritableDatabase();
        db.delete(DROP_ADDRESS_TABLE,null,null);
        Utility.printLog("DataBase Is CLeared" );
    }

    @Override
    public void deleteCardDetailsTable()
    {
        SQLiteDatabase db = sqLiteDbHelper.getWritableDatabase();
        db.delete(CARD_DETAILS_TABLE,null,null);
        Utility.printLog("DataBase Is CLeared" );
    }

    @Override
    public void insertAllCardsStored(ArrayList<CardDetails> cardDetailsDataModels)
    {
        SQLiteDatabase db = sqLiteDbHelper.getWritableDatabase();
        sqLiteDbHelper.createCardDetailsTable(db);

        Utility.printLog(TAG+ " CARD insert all card details size: "+cardDetailsDataModels.size());
        for(CardDetails cardDetailsDataModel: cardDetailsDataModels)
        {
            Utility.printLog(TAG+ " CARD insert all card id "+cardDetailsDataModel.getId());
            int isDefaultCard = 0;

            if(cardDetailsDataModel.getDefault())
                isDefaultCard = 1;

            ContentValues contentValues = new ContentValues();
            contentValues.put(CARD_DETAILS_KEY_ID, cardDetailsDataModel.getId());
            contentValues.put(CARD_DETAILS_LAST_DIGITS, cardDetailsDataModel.getLast4());
            contentValues.put(CARD_DETAILS_BRAND, cardDetailsDataModel.getBrand());
            contentValues.put(CARD_DETAILS_EXPIRY_MONTH, cardDetailsDataModel.getExpMonth());
            contentValues.put(CARD_DETAILS_EXPIRY_YEAR, cardDetailsDataModel.getExpYear());
            contentValues.put(CARD_DETAILS_DEFAULT, isDefaultCard);

            long id = db.insert(CARD_DETAILS_TABLE, null, contentValues);
            Utility.printLog(TAG+" CARD insert card details id: " + id);
        }
    }

    @Override
    public ArrayList<CardDetails> extractAllCardDetailsStored()
    {
        ArrayList<CardDetails> cardDetailsDataModels = new ArrayList<>();
        SQLiteDatabase db = sqLiteDbHelper.getReadableDatabase();

        String[] projection =
                {
                        CARD_DETAILS_KEY_ID,
                        CARD_DETAILS_LAST_DIGITS,
                        CARD_DETAILS_BRAND,
                        CARD_DETAILS_EXPIRY_MONTH,
                        CARD_DETAILS_EXPIRY_YEAR,
                        CARD_DETAILS_DEFAULT
                };

        Cursor mCursor = db.query(
                CARD_DETAILS_TABLE, projection,
                null, null, null, null, null);

        if ((mCursor != null && mCursor.getCount() > 0) && mCursor.moveToFirst())
        {
            do
            {
                boolean defaultCard = false;
                if(mCursor.getInt(mCursor.getColumnIndexOrThrow(CARD_DETAILS_DEFAULT)) == 1)
                    defaultCard = true;

                CardDetails cardDetailsDataModel = new CardDetails();
                cardDetailsDataModel.setId(mCursor.getString(mCursor.getColumnIndexOrThrow(CARD_DETAILS_KEY_ID)));
                cardDetailsDataModel.setLast4(mCursor.getString(mCursor.getColumnIndexOrThrow(CARD_DETAILS_LAST_DIGITS)));
                cardDetailsDataModel.setBrand(mCursor.getString(mCursor.getColumnIndexOrThrow(CARD_DETAILS_BRAND)));
                cardDetailsDataModel.setExpMonth(mCursor.getString(mCursor.getColumnIndexOrThrow(CARD_DETAILS_EXPIRY_MONTH)));
                cardDetailsDataModel.setExpYear(mCursor.getString(mCursor.getColumnIndexOrThrow(CARD_DETAILS_EXPIRY_YEAR)));
                cardDetailsDataModel.setDefault(defaultCard);
                cardDetailsDataModels.add(cardDetailsDataModel);
            }
            while (mCursor.moveToNext());
            mCursor.close();
        }

        db.close();
        Utility.printLog(TAG + " extractAllCardDetailsStored size(): " + cardDetailsDataModels.size());
        return cardDetailsDataModels;
    }

    @Override
    public long insertCard(CardDetails cardDetailsDataModel)
    {
        Utility.printLog(TAG+ " insert card data "+ cardDetailsDataModel.toString());
        SQLiteDatabase db = sqLiteDbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(CARD_DETAILS_KEY_ID, cardDetailsDataModel.getId());
        contentValues.put(CARD_DETAILS_LAST_DIGITS, cardDetailsDataModel.getLast4());
        contentValues.put(CARD_DETAILS_BRAND, cardDetailsDataModel.getBrand());
        contentValues.put(CARD_DETAILS_EXPIRY_MONTH, cardDetailsDataModel.getExpMonth());
        contentValues.put(CARD_DETAILS_EXPIRY_YEAR, cardDetailsDataModel.getExpYear());
        contentValues.put(CARD_DETAILS_DEFAULT, cardDetailsDataModel.getDefault());

        long id = db.insert(CARD_DETAILS_TABLE, null, contentValues);
        Utility.printLog(TAG+ "insertFavDropAdrssData id: "+id);
        db.close();
        return id;
    }

    @Override
    public void deleteCard(String id)
    {
        Utility.printLog(TAG+ " deleteCard id: "+id);
        SQLiteDatabase db = sqLiteDbHelper.getReadableDatabase();
        int i = db.delete(CARD_DETAILS_TABLE, CARD_DETAILS_KEY_ID + "=? ", new String[]{id});
        Utility.printLog(TAG+ " deleteCard deleted : "+i);
    }

    @Override
    public void updateCardDetails(String id,boolean isDefault)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CARD_DETAILS_KEY_ID, id);
        contentValues.put(CARD_DETAILS_DEFAULT, isDefault);

        SQLiteDatabase db = sqLiteDbHelper.getWritableDatabase();
        db.update(CARD_DETAILS_TABLE, contentValues, CARD_DETAILS_KEY_ID+ "=? ", new String[]{id});
    }
}
