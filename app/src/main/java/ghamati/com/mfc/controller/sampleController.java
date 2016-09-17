package ghamati.com.mfc.controller;

import java.util.List;

import ghamati.com.mfc.database.samplet;
import ghamati.com.mfc.observer.DataObserver;

/**
 * Created by khashayar on 9/16/16.
 */
public class sampleController extends CoreController<samplet> {


    @Override
    public void response(samplet data) {

    }

    @Override
    public void notifyObservers(List<DataObserver> observers) {

    }

    @Override
    public String setRoute() {
        return null;
    }

    @Override
    public boolean hasDataBase() {
        return false;
    }
}
