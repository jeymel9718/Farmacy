package com.example.jeison.farmacy.Interfaces;

import android.view.View;

import com.example.jeison.farmacy.Clases.Medicinas;
import com.example.jeison.farmacy.Clases.Recetas;

import java.util.ArrayList;

/**
 * Created by Jeison on 14/10/2017.
 */

public interface OnListFragmentInteractionListener {
    // TODO: Update argument type and name
    void onListFragmentInteraction(Medicinas item, boolean checked, View view);
    void onPedidoFragmentInteration(Medicinas item,boolean checked,View view);
    void onTerminarPedidoInteration(String PedidoList);
    void onBorrarReceta(Recetas item,View view);
    void onRecetaSelected(ArrayList<Medicinas> medicinases,String imagen);
}
