/**
 * @author: Haythem Khiri
 * @project: My pharmacy Android App
 * @year: 2014
 * @license: MIT
 */
package com.mypharmacy.models;


import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: Haythem Khiri
 */
public class DrugModel {

    private long id;
    private String name;
    private String description;
    private int quantity;
    private float price;
    private String dateOfLastPurchase;
    private String dateOfExpiration;

    public DrugModel() {}

    public DrugModel(String name, String description, int quantity, float price, String dateOfExpiration) {
        this.setName(name);
        this.setDescription(description);
        this.setPrice(price);
        this.setQuantity(quantity);
        this.setDateOfLastPurchase(new SimpleDateFormat("dd MMM yyyy").format(new Date()));
        this.setDateOfExpiration(dateOfExpiration);
    }

    public DrugModel(String name, String description, int quantity, float price, String dateOfLastPurchase, String dateOfExpiration) {
        this.setName(name);
        this.setDescription(description);
        this.setPrice(price);
        this.setQuantity(quantity);
        this.setDateOfLastPurchase(dateOfLastPurchase);
        this.setDateOfExpiration(dateOfExpiration);
    }

    public DrugModel(long id, String name, String description, int quantity, float price, String dateOfExpiration) {
        this(name, description, quantity, price, dateOfExpiration);
        this.id = id;
    }

    public DrugModel(long id, String name, String description, int quantity, float price, String dateOfLastPurchase, String dateOfExpiration) {
        this(name, description, quantity, price, dateOfLastPurchase);
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if(description != null && !description.matches("")) {
            this.description = description;
        } else {
            this.description = "";
        }
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDateOfLastPurchase() {
        return dateOfLastPurchase;
    }

    public void setDateOfLastPurchase(String dateOfLastPurchase) {
        this.dateOfLastPurchase = dateOfLastPurchase;
    }

    public String getDateOfExpiration() {
        return dateOfExpiration;
    }

    public void setDateOfExpiration(String dateOfExpiration) {
        this.dateOfExpiration = dateOfExpiration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DrugModel drug = (DrugModel) o;

        if (!name.equals(drug.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return this.name + " (" + this.quantity + ") : " +
               this.dateOfLastPurchase;
    }
}
