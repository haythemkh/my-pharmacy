/**
 * @author: Haythem Khiri
 * @project: My pharmacy Android App
 * @year: 2014
 * @license: MIT
 */
package com.mypharmacy.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mypharmacy.models.DrugModel;

import java.util.ArrayList;

/**
 * @author: Haythem Khiri
 */
public class DrugDAOImpl implements DrugDAO {

    private SQLiteDatabase db;
    private DatabaseHandler dbHandler;
    private static final String SQL_STAR = "id, name, description, quantity, price, date_of_last_purchase, date_of_expiration";

    Context context;

    public DrugDAOImpl(Context context) {
        this.context = context;
        dbHandler = new DatabaseHandler(context, null);
    }

    @Override
    public void connect() {
        db = dbHandler.getWritableDatabase();
    }

    @Override
    public void disconnect() {
        db.close();
    }

    @Override
    public ArrayList<DrugModel> getAll() {
        ArrayList<DrugModel> drugs = new ArrayList<DrugModel>();
        DrugModel drug;

        String query = "SELECT " + SQL_STAR + " FROM " + dbHandler.TABLE_NAME_DRUG + " ORDER BY " + dbHandler.DRUG_NAME + " COLLATE NOCASE";
        Cursor c = db.rawQuery(query, null);
        try{
            if(c.moveToFirst()) {
                do {
                    drug = new DrugModel();
                    drug.setId(Integer.parseInt(c.getString(0)));
                    drug.setName(c.getString(1));
                    drug.setDescription(c.getString(2));
                    drug.setQuantity(Integer.parseInt(c.getString(3)));
                    drug.setPrice(Float.parseFloat(c.getString(4)));
                    drug.setDateOfLastPurchase(c.getString(5));
                    drug.setDateOfExpiration(c.getString(6));

                    drugs.add(drug);
                } while(c.moveToNext());
            }
        } catch(Exception e) {}

        return drugs;
    }

    @Override
    public DrugModel getById(long id) {
        Cursor c;
        String query = "SELECT " + SQL_STAR + " FROM drug WHERE id=?";

        c = db.rawQuery(query, new String[] { Long.toString(id) });
        if(c.moveToFirst() && c.getCount() != 0) {
            return new DrugModel(c.getLong(0), c.getString(1), c.getString(2), c.getInt(3), c.getFloat(4), c.getString(5), c.getString(6));
        }
        return null;
    }

    @Override
    public long save(DrugModel obj) {
        dbHandler.getReadableDatabase();
        ContentValues v = new ContentValues();

        v.put(dbHandler.DRUG_NAME, obj.getName());
        v.put(dbHandler.DRUG_DESCRIPTION, obj.getDescription());
        v.put(dbHandler.DRUG_QUANTITY, obj.getQuantity());
        v.put(dbHandler.DRUG_PRICE, obj.getPrice());
        v.put(dbHandler.DRUG_DATE_OF_LAST_PURCHASE, obj.getDateOfLastPurchase());
        v.put(dbHandler.DRUG_DATE_OF_EXPIRATION, obj.getDateOfExpiration());

        long drugId = db.insert(dbHandler.TABLE_NAME_DRUG, null, v);
        this.disconnect();

        return drugId;
    }

    @Override
    public void update(DrugModel obj) {
        String whereClause = "id=?";
        String[] whereArgs = new String[] { obj.getId() + "" };
        ContentValues v = new ContentValues();

        v.put(dbHandler.DRUG_NAME, obj.getName());
        v.put(dbHandler.DRUG_DESCRIPTION, obj.getDescription());
        v.put(dbHandler.DRUG_QUANTITY, obj.getQuantity());
        v.put(dbHandler.DRUG_PRICE, obj.getPrice());
        v.put(dbHandler.DRUG_DATE_OF_LAST_PURCHASE, obj.getDateOfLastPurchase());
        v.put(dbHandler.DRUG_DATE_OF_EXPIRATION, obj.getDateOfExpiration());

        db.update(dbHandler.TABLE_NAME_DRUG, v, whereClause, whereArgs);
    }

    @Override
    public DrugModel delete(DrugModel obj) {
        db.delete(dbHandler.TABLE_NAME_DRUG, "id=" + obj.getId(), null);
        return obj;
    }
}
