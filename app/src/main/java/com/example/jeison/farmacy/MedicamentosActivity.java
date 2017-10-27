package com.example.jeison.farmacy;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.jeison.farmacy.Adapters.MedicinasAdapter;
import com.example.jeison.farmacy.Clases.Medicinas;
import com.example.jeison.farmacy.Clases.Recetas;
import com.example.jeison.farmacy.Fragments.MedicinasFragment;
import com.example.jeison.farmacy.Fragments.RecetasFragment;
import com.example.jeison.farmacy.Interfaces.OnListFragmentInteractionListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MedicamentosActivity extends AppCompatActivity implements OnListFragmentInteractionListener {

    private MedicinasFragment medicinasFragment;
    private RecetasFragment recetasFragment;
    private String SucursalName;
    private String SucursalId;
    private String Recetaimg=null;
    private FrameLayout container;
    private boolean backstate=true;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicamentos);
        container=(FrameLayout) findViewById(R.id.frame_container);
        medicinasFragment=new MedicinasFragment();
        recetasFragment=new RecetasFragment();
        SucursalName = getIntent().getStringExtra("Su_Name");
        SucursalId=getIntent().getStringExtra("Su_id");

        builder=new AlertDialog.Builder(this);
        builder.setTitle("Medicamentos que no se pudieron añadir").setMessage("Los siguiente medicamentos no se pudieron añadir");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        Bundle args=new Bundle();
        args.putString("ID",SucursalId);
        medicinasFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame_container, medicinasFragment).commit();



        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.meidcamentos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id=item.getItemId();
        if(id==android.R.id.home){
            if(backstate){
                this.finish();
            }else{
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,medicinasFragment)
                        .addToBackStack(null).commit();
                backstate=true;
            }

        }else if (id==R.id.new_recipe){
            Intent intent=new Intent(this,CrearReceta.class);
            startActivity(intent);
        }else if (id==R.id.add_recipe){
            backstate=false;
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,recetasFragment).
                    addToBackStack(null).commit();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onListFragmentInteraction(Medicinas item, boolean checked, View view) {
        if(checked){
            medicinasFragment.mPedidos.addMedicina(item);
        }
    }

    @Override
    public void onPedidoFragmentInteration(Medicinas item, boolean checked,View view) {
        if(!checked){
            CheckBox check=(CheckBox) item.mViewm.findViewById(R.id.check_add);
            check.setChecked(false);
            medicinasFragment.pedidos.removeView(view);
            medicinasFragment.mPedidos.delMedicina(item);
        }
    }

    @Override
    public void onTerminarPedidoInteration(String PedidoList) {
        Intent intent=new Intent(getApplicationContext(),FinalizarPedidosActivity.class);
        intent.putExtra("medicinas",PedidoList);
        intent.putExtra("Su_name",SucursalName);
        intent.putExtra("Su_id",SucursalId);
        intent.putExtra("Imagen",Recetaimg);
        Log.i("Information","hola estoy aqui");
        startActivity(intent);
    }

    @Override
    public void onBorrarReceta(Recetas item, View view) {
        recetasFragment.recyclerView.removeView(view);
        recetasFragment.Arecetas.delMedicina(item);
    }

    @Override
    public void onRecetaSelected(ArrayList<Medicinas> medicinases,String imagen) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,medicinasFragment)
                .addToBackStack(null).commit();
        Recetaimg=imagen;
        ArrayList<String> nohay=new ArrayList<>();
        HashMap<String,Medicinas> map=medicinasFragment.Amedicinas.mMapa;
        for(int i=0;i<medicinases.size();++i) {
            Medicinas item = medicinases.get(i);
            if (map.containsKey(item.mName)) {
                Medicinas item2=map.get(item.mName);
                if(!item2.mCantidad.equals("0") && !medicinasFragment.mPedidos.Contains(item2)) {
                    medicinasFragment.mPedidos.addMedicina(map.get(item.mName));
                }
            } else {
                nohay.add(item.mName);
            }
        }
        CharSequence[] cs = nohay.toArray(new CharSequence[nohay.size()]);
        builder.setItems(cs, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }
}
