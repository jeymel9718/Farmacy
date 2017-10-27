package com.example.jeison.farmacy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.jeison.farmacy.Fragments.SucursalesFragment;

public class SucursalesActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sucursales);



        SucursalesFragment leadsFragment = (SucursalesFragment)
                getSupportFragmentManager().findFragmentById(R.id.sucursales_container);

        if (leadsFragment == null) {
            leadsFragment = SucursalesFragment.newInstance("param1","param2");
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.sucursales_container, leadsFragment)
                    .commit();
        }

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id=item.getItemId();
        if(id==android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
