package com.example.jeison.farmacy;

import com.example.jeison.farmacy.Clases.Medicinas;

import java.util.List;

/**
 * Created by Jeison on 02/10/2017.
 */

public class Pedidos {
    public List<Medicinas> medicamentos;
    public String mRecetaimg;
    public String numPedido;
    public String Sucursal;
    public String Dater;
    public String Direccion;

    public Pedidos(String numpedido,String sucursal,String drecojo,String direccion,String receta){
        this.numPedido=numpedido;
        this.Sucursal=sucursal;
        this.Dater=drecojo;
        this.Direccion=direccion;
        this.mRecetaimg=receta;
    }
}
