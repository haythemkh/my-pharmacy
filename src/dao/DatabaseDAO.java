/**
 * @author: Haythem Khiri
 * @project: My pharmacy Android App
 * @year: 2014
 * @license: MIT
 */
package com.mypharmacy.dao;

import com.mypharmacy.models.DrugModel;

import java.util.ArrayList;

/**
 * @author: Haythem Khiri
 */
public interface DatabaseDAO<T> {

    public void connect();
    public void disconnect();

    public ArrayList<DrugModel> getAll();
    public T getById(long id);
    public long save(T obj);
    public void update(T obj);
    public T delete(T obj);
}
