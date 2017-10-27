package com.example.jeison.farmacy.Clases;

/**
 * Created by Jeison on 26/09/2017.
 */

public class Sucursales {
    public String mName;
    public String mId;
    public String mLocation;
    public String mAddress;

    public Sucursales(String name,String location,String Address,String id){
        mName=name;
        mLocation=location;
        mAddress=Address;
        mId=id;
    }

    public String toString(){
        return "Sucursal{Name:"+mName+",Location:"+mLocation+",Address:"+mAddress+"}";
    }
}
