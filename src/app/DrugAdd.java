/**
 * @author: Haythem Khiri
 * @project: My pharmacy Android App
 * @year: 2014
 * @license: MIT
 */
package com.mypharmacy.app;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mypharmacy.dao.DrugDAOImpl;
import com.mypharmacy.models.DrugModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author: Haythem Khiri
 */
public class DrugAdd extends ActionBarActivity {

    private EditText nameEditText, descriptionEditText, quantityEditText, priceEditText;
    private TextView dateOfExpirationTextView, mDateDisplay;;
    private Button addButton, resetButton, mPickDate;
    private ActionBar actionBar;

    private String name, description, dateOfExpiration;
    private int quantity, mYear, mMonth, mDay;;
    private float price;

    static final int DATE_DIALOG_ID = 0;

    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    updateDisplay();
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drug_add);

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        mDateDisplay = (TextView) findViewById(R.id.drug_date_of_expiration);
        mPickDate = (Button) findViewById(R.id.datePickerButton);

        mPickDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });

        updateDisplay();

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        addButton = (Button) findViewById(R.id.new_button);
        resetButton = (Button) findViewById(R.id.reset_button);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameEditText = (EditText) findViewById(R.id.drug_name);
                descriptionEditText = (EditText) findViewById(R.id.drug_description);
                quantityEditText = (EditText) findViewById(R.id.drug_quantity);
                priceEditText = (EditText) findViewById(R.id.drug_price);
                dateOfExpirationTextView = (TextView) findViewById(R.id.drug_date_of_expiration);

                name = nameEditText.getText().toString().trim();
                description = descriptionEditText.getText().toString().trim();
                quantity = Integer.parseInt(quantityEditText.getText().toString().trim());
                price = Float.parseFloat(priceEditText.getText().toString().trim());
                try {
                    String dateToFormat = dateOfExpirationTextView.getText().toString().trim();
                    DateFormat formatter = new SimpleDateFormat(getString(R.string.datepicker_format));
                    Date date = formatter.parse(dateToFormat);
                    SimpleDateFormat newFormat = new SimpleDateFormat(getString(R.string.date_format));
                    dateOfExpiration = newFormat.format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if(name.matches("") || Integer.toString(quantity).matches("") || Float.toString(price).matches("") || dateOfExpiration.matches("")) {
                    Toast.makeText(getApplicationContext(), R.string.toast_new_submit_error, Toast.LENGTH_LONG).show();
                } else {
                    DrugModel drug = new DrugModel(name, description, quantity, price, dateOfExpiration);
                    DrugDAOImpl drugDAO = new DrugDAOImpl(DrugAdd.this);
                    drugDAO.connect();
                    drugDAO.save(drug);
                    drugDAO.disconnect();
                    Toast.makeText(getApplicationContext(), R.string.toast_new_submit_success, Toast.LENGTH_LONG).show();
                    Intent goBack = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(goBack);
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameEditText = (EditText) findViewById(R.id.drug_name);
                descriptionEditText = (EditText) findViewById(R.id.drug_description);
                quantityEditText= (EditText) findViewById(R.id.drug_quantity_edit);
                priceEditText = (EditText) findViewById(R.id.drug_price_edit);
                nameEditText.getText().clear();
                descriptionEditText.getText().clear();
                quantityEditText.getText().clear();
                priceEditText.getText().clear();
            }
        });
    }

    private void updateDisplay() {
        String mDayPrefix = "", mMonthPrefix = "";
        if(mDay < 10) mDayPrefix = "0";
        if(mMonth < 10) mMonthPrefix = "0";
        this.mDateDisplay.setText(
                new StringBuilder()
                        .append(mDayPrefix + mDay).append("/")
                        .append(mMonthPrefix + (mMonth + 1)).append("/")
                        .append(mYear).append(""));
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                        mDateSetListener,
                        mYear, mMonth, mDay);
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
