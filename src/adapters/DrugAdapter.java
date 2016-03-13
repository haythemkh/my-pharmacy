/**
 * @author: Haythem Khiri
 * @project: My pharmacy Android App
 * @year: 2014
 * @license: MIT
 */
package com.mypharmacy.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mypharmacy.app.R;
import com.mypharmacy.models.DrugModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Haythem Khiri
 */
public class DrugAdapter extends ArrayAdapter {

    Context context;
    Resources resources;

    private List<DrugModel> drug;

    public DrugAdapter(Context context, ArrayList<DrugModel> listOfDrugs) {
        super(context, R.layout.activity_main, listOfDrugs);

        this.context = context;
        this.drug = listOfDrugs;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        resources = context.getResources();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_item, parent, false);

        TextView name = (TextView) convertView.findViewById(R.id.list_item_name);
        TextView quantity = (TextView) convertView.findViewById(R.id.list_item_quantity);
        TextView price = (TextView) convertView.findViewById(R.id.list_item_price);
        name.setText(drug.get(position).getName());
        quantity.setText(" (" + drug.get(position).getQuantity() + ")");

        String priceStr = Float.toString(drug.get(position).getPrice());
        int priceAfterDotsLength = priceStr.substring(priceStr.indexOf(".")+1, priceStr.length()-1).length();
        for(int i = 1; i < (3 - priceAfterDotsLength); i++) {
            priceStr += "0";
        }
        price.setText(priceStr);

        return convertView;
    }
}
