package com.example.jeison.farmacy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jeison.farmacy.Clases.Client;
import com.example.jeison.farmacy.Fragments.SucursalesFragment;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SucursalesFragment sucursalesFragment;
    private PedidosFragment pedidosFragment;
    private HistorialFragment historialFragment;
    private boolean first_fragment=true;
    private TextView name;
    private DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View nview=navigationView.getHeaderView(0);
        name=(TextView) nview.findViewById(R.id.nombreUs);

        GetUser();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        sucursalesFragment=new SucursalesFragment();
        pedidosFragment=new PedidosFragment();
        historialFragment=new HistorialFragment();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_pedidos) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if(first_fragment) {
                transaction.add(R.id.frame_container, pedidosFragment).commit();
                first_fragment=false;
            }else{
                transaction.replace(R.id.frame_container,pedidosFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        } else if (id == R.id.nav_sucursales) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if(first_fragment) {
                transaction.add(R.id.frame_container, sucursalesFragment).commit();
                first_fragment=false;
            }else{
                transaction.replace(R.id.frame_container,sucursalesFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        }  else if (id == R.id.nav_history) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if(first_fragment) {
                transaction.add(R.id.frame_container, historialFragment).commit();
                first_fragment=false;
            }else{
                transaction.replace(R.id.frame_container,historialFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        } else if (id == R.id.nav_acount) {
            Intent acount=new Intent(getApplicationContext(),Acount.class);
            startActivity(acount);
        } else if (id == R.id.nav_exit) {
            Intent loggin=new Intent(getApplicationContext(),LoginActivity.class);
            this.finish();
            startActivity(loggin);
        }else{
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void GetUser(){
        //showProgress(true);
        RequestParams params=new RequestParams();
        params.put("id", Client.getInstance().id);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://"+Client.getInstance().ip+":64698/api/Persona/GetPersona",params,new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(String response){
                JsonParser parser = new JsonParser();
                JsonElement tradeElement = parser.parse(response);
                JsonObject sus=tradeElement.getAsJsonObject();
                Client.getInstance().Telefono=sus.get("Telefono").getAsString();
                Client.getInstance().Name=sus.get("Nombre").getAsString();
                Client.getInstance().Apellido1=sus.get("Apellido1").getAsString();
                Client.getInstance().Apellido2=sus.get("Apellido2").getAsString();
                Client.getInstance().Provincia=sus.get("Provincia").getAsString();
                Client.getInstance().Canton=sus.get("Canton").getAsString();
                Client.getInstance().Distrito=sus.get("Distrito").getAsString();
                Client.getInstance().Direccion=sus.get("DescripcionDireccion").getAsString();
                Client.getInstance().Fecha=sus.get("FechaNacimiento").getAsString();

                name.setText(Client.getInstance().Name+" "+Client.getInstance().Apellido1+" "+Client.getInstance().Apellido2);

            }

            @Override
            public void onFailure(int statusCode, Throwable error,String content){
                //showProgress(false);
                Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
            }
        });
    }
}
