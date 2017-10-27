package com.example.jeison.farmacy;

import com.example.jeison.farmacy.Clases.Sucursales;

import java.util.ArrayList;

/**
 * Created by Jeison on 26/09/2017.
 */

public class SucursalesProvider {

    private static SucursalesProvider provider = new SucursalesProvider();
    public ArrayList<Sucursales> Items=new ArrayList<Sucursales>();

    public SucursalesProvider(){

    }

    public static SucursalesProvider getInstance() {
        return provider;
    }
}
