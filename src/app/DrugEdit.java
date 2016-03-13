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
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mypharmacy.dao.DatabaseHandler;
import com.mypharmacy.dao.DrugDAOImpl;
import com.mypharmacy.models.DrugModel;

import java.text.SimpleDateFormat;

/**
 * @author: Haythem Khiri
 */
public class DrugEdit extends ActionBarActivity {

    private DrugDAOImpl drugDAO;
    private DrugModel drug, updatedDrug;
    private DatabaseHandler dbHandler;

    private ActionBar actionBar;
    private EditText nameEditViewValue, descriptionEditViewValue, quantityEditViewValue, priceEditViewValue, dateOfExpirationEditViewValue;
    private Button editButton, resetButton;

    private long id;
    private String name, description, dateOfExpiration, quantityStr, priceStr;
    private int quantity;
    private float price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drug_edit);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        dbHandler = new DatabaseHandler(getApplicationContext(), null);
        drugDAO = new DrugDAOImpl(DrugEdit.this);
        updatedDrug = new DrugModel();
        id = this.getIntent().getExtras().getLong(dbHandler.DRUG_ID, 1L);
        drugDAO.connect();
        drug = drugDAO.getById(id);
        drugDAO.disconnect();
        updatedDrug.setId(id);

        nameEditViewValue = (EditText) findViewById(R.id.drug_name_edit);
        descriptionEditViewValue = (EditText) findViewById(R.id.drug_description_edit);
        quantityEditViewValue = (EditText) findViewById(R.id.drug_quantity_edit);
        priceEditViewValue = (EditText) findViewById(R.id.drug_price_edit);
        dateOfExpirationEditViewValue = (EditText) findViewById(R.id.drug_date_of_expiration_edit);

        nameEditViewValue.setText(drug.getName());
        descriptionEditViewValue.setText(drug.getDescription());
        quantityEditViewValue.setText(Integer.toString(drug.getQuantity()));
        priceEditViewValue.setText(Float.toString(drug.getPrice()));
        dateOfExpirationEditViewValue.setText(drug.getDateOfExpiration());

        editButton = (Button) findViewById(R.id.edit_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = nameEditViewValue.getText().toString().trim();
                description = descriptionEditViewValue.getText().toString().trim();
                quantityStr = quantityEditViewValue.getText().toString().trim();
                priceStr = priceEditViewValue.getText().toString().trim();
                quantity = Integer.parseInt(quantityStr);
                price = Float.parseFloat(priceStr);
                dateOfExpiration = dateOfExpirationEditViewValue.getText().toString().trim();
                if(name.matches("") || Integer.toString(quantity).matches("") || Float.toString(price).matches("")) {
                    Toast.makeText(getApplicationContext(), R.string.toast_update_submit_error, Toast.LENGTH_LONG).show();
                } else {
                    updatedDrug.setName(name);
                    updatedDrug.setDescription(description);
                    updatedDrug.setQuantity(quantity);
                    updatedDrug.setPrice(price);
                    updatedDrug.setDateOfLastPurchase(drug.getDateOfLastPurchase());
                    updatedDrug.setDateOfExpiration(dateOfExpiration);
                    if(drug.getQuantity() != updatedDrug.getQuantity()) {
                        updatedDrug.setDateOfLastPurchase(new SimpleDateFormat(getString(R.string.date_format)).format(System.currentTimeMillis()));
                    }
                    drugDAO.connect();
                    drugDAO.update(updatedDrug);
                    drugDAO.disconnect();
                    Toast.makeText(getApplicationContext(), R.string.toast_update_submit_success, Toast.LENGTH_LONG).show();
                    Intent goBack = new Intent(getApplicationContext(), DrugDetails.class);
                    goBack.putExtra(dbHandler.DRUG_ID, updatedDrug.getId());
                    startActivity(goBack);
                }
            }
        });

        resetButton = (Button) findViewById(R.id.reset_button);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameEditViewValue.getText().clear();
                descriptionEditViewValue.getText().clear();
                quantityEditViewValue.getText().clear();
                priceEditViewValue.getText().clear();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_drug_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.item_about:
                aboutMenuItem();
                break;
            case R.id.action_settings:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void aboutMenuItem() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.about_title))
                .setMessage(Html.fromHtml(getString(R.string.about_msg)))
                .setNeutralButton(getString(R.string.ok_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }
}
