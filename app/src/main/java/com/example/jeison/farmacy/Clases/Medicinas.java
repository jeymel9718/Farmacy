package com.example.jeison.farmacy.Clases;

import android.view.View;

/**
 * Created by Jeison on 30/09/2017.
 */

public class Medicinas {
    public String mName;
    public String mPrice;
    public String mCantidad;
    public View mViewm;
    public String ID;

    public Medicinas(String name,String price,String cantidad,String id){
        this.mName=name;
        this.mCantidad=cantidad;
        this.mPrice=price;
        this.ID=id;
        mViewm=null;
    }

    public String toString(){
        return "{\"mName\":\""+mName+"\",\"mPrice\":\""+mPrice+"\",\"mCantidad\":\""+mCantidad+"\",\"ID\":\""+ID+"\"}";
    }
}
