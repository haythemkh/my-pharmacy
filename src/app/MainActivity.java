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
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.mypharmacy.adapters.DrugAdapter;
import com.mypharmacy.dao.DatabaseHandler;
import com.mypharmacy.dao.DrugDAOImpl;
import com.mypharmacy.models.DrugModel;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author: Haythem Khiri
 */
public class MainActivity extends ActionBarActivity {

    private DrugDAOImpl drugDAO;
    private DrugAdapter drugAdapter;
    private DatabaseHandler dbHandler;

    private ListView searchListView;
    private EditText searchEditText;
    private Timer timer;

    private ArrayList<DrugModel> listOfDrugs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.activity_list_of_drugs);

        dbHandler = new DatabaseHandler(getApplicationContext(), null);
        listOfDrugs = new ArrayList<DrugModel>();
        searchListView = (ListView) findViewById(R.id.list_of_drugs);
        searchEditText = (EditText) findViewById(R.id.search_drugs_edittext);
        drugDAO = new DrugDAOImpl(MainActivity.this);
        drugAdapter = new DrugAdapter(MainActivity.this, listOfDrugs);
        timer = new Timer();

        drugDAO.connect();
        listOfDrugs = drugDAO.getAll();
        searchListView.setAdapter(drugAdapter);
        drugAdapter.notifyDataSetInvalidated();
        drugAdapter.setNotifyOnChange(true);
        drugDAO.disconnect();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    String str = searchEditText.getText().toString();
                    if (str.length() == 0) {
                        timerMethod();
                    }
                } catch (Exception e) {}
            }
        }, 0, 3000);

        searchListView = (ListView) this.findViewById(R.id.list_of_drugs);
        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                long id = listOfDrugs.get(i).getId();

                Intent detailsDrugIntent = new Intent(MainActivity.this, DrugDetails.class);
                detailsDrugIntent.putExtra(dbHandler.DRUG_ID, id);

                startActivity(detailsDrugIntent);
            }
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                ArrayList<DrugModel> arrayTemplist = new ArrayList<DrugModel>();

                if(charSequence.length() == 0 || charSequence == null) {
                    arrayTemplist.addAll(listOfDrugs);
                    listOfDrugs.clear();
                    listOfDrugs.addAll(arrayTemplist);
                    timerMethod();
                } else {
                    for(int j = 0; j < listOfDrugs.size(); j++) {
                        if(listOfDrugs.get(j).getName().toLowerCase()
                            .contains(charSequence.toString().toLowerCase())) {
                            arrayTemplist.add(listOfDrugs.get(j));
                        }
                        listOfDrugs.clear();
                        listOfDrugs.addAll(arrayTemplist);
                    }
                    drugAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void timerMethod() {
        this.runOnUiThread(Timer_Tick);
    }

    private Runnable Timer_Tick = new Runnable() {
        @Override
        public void run() {
            searchListView = (ListView) findViewById(R.id.list_of_drugs);

            drugDAO = new DrugDAOImpl(MainActivity.this);
            drugDAO.connect();
            listOfDrugs = drugDAO.getAll();
            drugAdapter.notifyDataSetChanged();
            drugAdapter.notifyDataSetInvalidated();
            drugAdapter = new DrugAdapter(MainActivity.this, listOfDrugs);
            searchListView.setAdapter(drugAdapter);
            drugDAO.disconnect();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.item_add:
                addMenuItem();
                break;
            case R.id.item_about:
                aboutMenuItem();
                break;
            case R.id.action_settings:
                return true;
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

    public void addMenuItem() {
        startActivity(new Intent(getApplicationContext(), DrugAdd.class));
    }

}
