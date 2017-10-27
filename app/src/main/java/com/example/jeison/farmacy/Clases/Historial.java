package com.example.jeison.farmacy.Clases;

/**
 * Created by Jeison on 04/10/2017.
 */

public class Historial {
    public String mFecha;
    public String mPadecimientos;
    public String Idenfermedad;

    public Historial(String fecha,String padecimientos,String idenfermedad){
        this.mFecha=fecha;
        this.mPadecimientos=padecimientos;
        this.Idenfermedad=idenfermedad;
    }

    public String toString(){
        return "{\"mFecha\":\""+this.mFecha+"\",\"mPadecimientos\":\""+mPadecimientos+"\"}";
    }
}
