/**
 * @author: Haythem Khiri
 * @project: My pharmacy Android App
 * @year: 2014
 * @license: MIT
 */
package com.mypharmacy.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.mypharmacy.dao.DatabaseHandler;
import com.mypharmacy.dao.DrugDAOImpl;
import com.mypharmacy.models.DrugModel;

/**
 * @author: Haythem Khiri
 */
public class DrugDetails extends ActionBarActivity {

    private long id;
    private DrugModel drug;
    private DrugDAOImpl drugDAO;
    private DatabaseHandler dbHandler;

    private ActionBar actionBar;
    private TextView nameTextViewValue, descriptionTextViewValue, quantityTextViewValue, priceTextViewValue, dateOfExpirationTextViewValue,
            dateOfLastPurchaseTextViewValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drug_details);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        dbHandler = new DatabaseHandler(getApplicationContext(), null);
        id = this.getIntent().getExtras().getLong(dbHandler.DRUG_ID, 1L);
        drugDAO = new DrugDAOImpl(DrugDetails.this);
        drugDAO.connect();
        drug = drugDAO.getById(id);
        drugDAO.disconnect();
        nameTextViewValue = (TextView) findViewById(R.id.drug_name_value);
        quantityTextViewValue = (TextView) findViewById(R.id.drug_quantity_value);
        priceTextViewValue = (TextView) findViewById(R.id.drug_price_value);
        dateOfExpirationTextViewValue = (TextView) findViewById(R.id.drug_date_of_expiration_value);
        dateOfLastPurchaseTextViewValue = (TextView) findViewById(R.id.drug_date_value);
        descriptionTextViewValue = (TextView) findViewById(R.id.drug_description_value);

        nameTextViewValue.setText(drug.getName());
        quantityTextViewValue.setText(Integer.toString(drug.getQuantity()));
        String priceStr = Float.toString(drug.getPrice());
        int priceAfterDotsLength = priceStr.substring(priceStr.indexOf(".")+1, priceStr.length()-1).length();
        for(int i = 1; i < (3 - priceAfterDotsLength); i++) {
            priceStr += "0";
        }
        priceTextViewValue.setText(priceStr);
        dateOfExpirationTextViewValue.setText(drug.getDateOfExpiration());
        dateOfLastPurchaseTextViewValue.setText(drug.getDateOfLastPurchase().toString());

        if(drug.getDescription().matches("")) {
            descriptionTextViewValue.setText("none");
            descriptionTextViewValue.setTypeface(null, Typeface.ITALIC);
        } else {
            descriptionTextViewValue.setText(drug.getDescription().toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_drug_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.item_edit:
                updateMenuItem();
                break;
            case R.id.item_delete:
                deleteMenuItem();
                break;
            case R.id.item_about:
                aboutMenuItem();
                break;
            case R.id.action_settings:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateMenuItem() {
        Intent editDrugIntent = new Intent(getApplicationContext(), DrugEdit.class);
        editDrugIntent.putExtra(dbHandler.DRUG_ID, drug.getId());
        startActivity(editDrugIntent);
    }

    public void deleteMenuItem() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.confirm_remove_title))
                .setMessage(getString(R.string.confirm_remove_msg))
                .setNeutralButton(getString(R.string.yes_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        drugDAO.connect();
                        drugDAO.delete(drug);
                        drugDAO.disconnect();
                        Toast.makeText(getApplicationContext(), R.string.toast_delete_submit_success, Toast.LENGTH_LONG).show();
                        Intent goBack = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(goBack);
                    }
                })
                .setPositiveButton(getString(R.string.no_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    public void aboutMenuItem() {
        new AlertDialog.Builder(getApplicationContext())
                .setTitle(getString(R.string.about_title))
                .setMessage(Html.fromHtml(getString(R.string.about_msg)))
                .setNeutralButton(getString(R.string.ok_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }
}
