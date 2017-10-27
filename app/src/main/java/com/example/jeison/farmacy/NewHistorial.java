package com.example.jeison.farmacy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jeison.farmacy.Adapters.EnfermedadAdapter;
import com.example.jeison.farmacy.Clases.Client;
import com.example.jeison.farmacy.Clases.Enfermedad;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class NewHistorial extends AppCompatActivity {

    public ArrayList<Enfermedad> enfermedads=new ArrayList<Enfermedad>();
    private OnListener mListener=new OnListener();
    private RecyclerView recyclerView;
    private EditText mDate;
    private ArrayList<Enfermedad> postenfer=new ArrayList<Enfermedad>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_historial);

        recyclerView=(RecyclerView) findViewById(R.id.lista);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        GetEnfermedades();
        mDate=(EditText) findViewById(R.id.dirreccion);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pedidos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id=item.getItemId();
        if(id==android.R.id.home){
            this.finish();
        }else if(id==R.id.action_done){
           postEnfermedades();
        }
        return super.onOptionsItemSelected(item);
    }

    public void postEnfermedades(){
        for(int i=0;i<postenfer.size();++i){
            String id=postenfer.get(i).Id;
            JsonObject obj=new JsonObject();
            obj.addProperty("IdCedula", Client.getInstance().id);
            obj.addProperty("IdEnfermedad",id);
            obj.addProperty("FechaEnfermedad",mDate.getText().toString());
            postEnfermedad(obj.toString());
        }
    }

    public void postEnfermedad(String datos){
        AsyncHttpClient client = new AsyncHttpClient();
        Log.i("Json",datos);
        ByteArrayEntity entity = null;
        try {
            entity = new ByteArrayEntity(datos.getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.d("Entity",entity.toString());
        client.post(getApplicationContext(),"http://"+Client.getInstance().ip+":64698/api/EnfermedadxPersona/PostEnfermedadxPersona",entity,"application/json",new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(String response){

            }
            @Override
            public void onFailure(int statusCode, Throwable error,String content){
                Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
            }
        });

    }
    public void GetEnfermedades(){
        //showProgress(true);
        RequestParams params=new RequestParams();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://"+Client.getInstance().ip+":64698/api/Enfermedad/GetAllEnfermedades",params,new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(String response){
                JsonParser parser = new JsonParser();
                JsonElement tradeElement = parser.parse(response);
                JsonArray sus=tradeElement.getAsJsonArray();
                for(int i=0;i<sus.size();++i){
                    JsonObject obj=sus.get(i).getAsJsonObject();
                    enfermedads.add(new Enfermedad(obj.get("IdEnfermedad").getAsString(),obj.get("Nombre").getAsString()));
                }
                recyclerView.setAdapter(new EnfermedadAdapter(enfermedads,mListener));



            }

            @Override
            public void onFailure(int statusCode, Throwable error,String content){
                //showProgress(false);
                Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
            }
        });
    }

    public class OnListener{
        public void onListenerAction(Enfermedad item,boolean checked){
            if(checked) {
                postenfer.add(item);
                Log.i("myTag", "Estoy aqui");
            }
        }
    }


}
