package com.example.jeison.farmacy;

import com.example.jeison.farmacy.Clases.Medicinas;

import java.util.ArrayList;

/**
 * Created by Jeison on 30/09/2017.
 */

public class MedicinasProvider {
    private static MedicinasProvider provider = new MedicinasProvider();
    public ArrayList<Medicinas> Items=new ArrayList<Medicinas>();

    public MedicinasProvider(){

    }

    public static MedicinasProvider getInstance() {
        return provider;
    }
}

