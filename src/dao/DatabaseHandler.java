/**
 * @author: Haythem Khiri
 * @project: My pharmacy Android App
 * @year: 2014
 * @license: MIT
 */
package com.mypharmacy.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author: Haythem Khiri
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // Database's specifications
    private static final int DATABASE_VERSION = 5;
    private static final String DATABASE_NAME = "mypharmacy.db";

    // DrugModel table's specifications
    public static final String TABLE_NAME_DRUG = "drug";
    public static final String DRUG_ID = "id";
    public static final String DRUG_NAME = "name";
    public static final String DRUG_DESCRIPTION = "description";
    public static final String DRUG_QUANTITY = "quantity";
    public static final String DRUG_PRICE = "price";
    public static final String DRUG_DATE_OF_LAST_PURCHASE = "date_of_last_purchase";
    public static final String DRUG_DATE_OF_EXPIRATION = "date_of_expiration";

    // Queries
    private static final String DROP_DRUG_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME_DRUG;
    private static final String CREATE_DRUG_TABLE = "CREATE TABLE " + TABLE_NAME_DRUG + " (" +
                                    DRUG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                                    DRUG_NAME + " TEXT NOT NULL, " +
                                    DRUG_DESCRIPTION + " TEXT, " +
                                    DRUG_QUANTITY + " INTEGER NOT NULL, " +
                                    DRUG_PRICE + " REAL NOT NULL, " +
                                    DRUG_DATE_OF_LAST_PURCHASE + " TEXT NOT NULL, " +
                                    DRUG_DATE_OF_EXPIRATION + " TEXT NOT NULL )";

    public DatabaseHandler(Context context, CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DRUG_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {
        db.execSQL(DROP_DRUG_TABLE);
        onCreate(db);
    }
}
