package com.example.jeison.farmacy;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.jeison.farmacy.Adapters.NewRecetaAdapter;
import com.example.jeison.farmacy.Adapters.RecetasAdapter;
import com.example.jeison.farmacy.Clases.Client;
import com.example.jeison.farmacy.Clases.Medicinas;
import com.example.jeison.farmacy.Clases.Recetas;
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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class CrearReceta extends AppCompatActivity {

    private ProgressBar mProgressView;
    private LinearLayout mLinearView;
    private RecyclerView recyclerView;
    private NewRecetaAdapter adapter;
    private String RecetaId;
    private ImageView RecetaImg;
    private Button SelButton;
    private String Receta=null;
    private final int GALERY=200;
    private AlertDialog.Builder builder;
    private ArrayList<Medicinas> toAdd=new ArrayList<Medicinas>();
    private OnListener mListener=new OnListener();
    public ArrayList<Medicinas> medicinases=new ArrayList<Medicinas>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_receta);

        builder=new AlertDialog.Builder(this);
        RecetaImg=(ImageView) findViewById(R.id.img);
        SelButton=(Button) findViewById(R.id.sel_img);
        SelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] options={"Tomar Foto","Elejir de Galeria","Cancelar"};
                builder.setTitle("Seleccionar fuente").setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(options[which]=="Tomar Foto"){

                        }else if(options[which]=="Elejir de Galeria"){
                            Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            startActivityForResult(intent.createChooser(intent,"Seleciona app de imagen"),GALERY);
                        } else if (options[which]=="Cancelar") {
                            dialog.cancel();
                        }
                    }
                });
                builder.show();
            }
        });
        mProgressView=(ProgressBar) findViewById(R.id.progressBar);
        mLinearView=(LinearLayout) findViewById(R.id.imagen);
        recyclerView=(RecyclerView) findViewById(R.id.lista);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        GetAllMedicinas();

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALERY && resultCode==RESULT_OK){
            Uri path=data.getData();
            try {
                final InputStream imageStream = getContentResolver().openInputStream(path);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                Receta=encodeImage(selectedImage);
                RecetaImg.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public String encodeImage(Bitmap bm){
        String endodeString=null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG,100,baos);
        byte[] b = baos.toByteArray();
        endodeString= Base64.encodeToString(b, Base64.DEFAULT);
        return endodeString;
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
            showProgress(true);
            JsonObject pedido=new JsonObject();
            pedido.addProperty("IdCedula",Client.getInstance().id);
            pedido.addProperty("RecetaImage",Receta);
            PostReceta(pedido.toString());
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLinearView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLinearView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLinearView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLinearView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
    public void PostReceta(String datos){
        AsyncHttpClient client = new AsyncHttpClient();
        ByteArrayEntity entity = null;
        try {
            entity = new ByteArrayEntity(datos.getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        client.post(getApplicationContext(),"http://"+Client.getInstance().ip+":64698/api/Receta/PostReceta",entity,"application/json",new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(String response){
                GetRecetaId();
            }
            @Override
            public void onFailure(int statusCode, Throwable error,String content){
                showProgress(false);
                Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void GetRecetaId(){
        RequestParams params=new RequestParams();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://"+Client.getInstance().ip+":64698/api/Receta/GetLastId",params,new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(String response){
                RecetaId=response;
                PostMedicinas();
            }

            @Override
            public void onFailure(int statusCode, Throwable error,String content){
                showProgress(false);
                Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void PostMedicinas(){
        for(int i=0;i<toAdd.size();++i){
            Medicinas item=toAdd.get(i);
            JsonObject medicinaxpedido=new JsonObject();
            medicinaxpedido.addProperty("IdPedido",RecetaId);
            medicinaxpedido.addProperty("IdMedicamento",item.ID);
            medicinaxpedido.addProperty("Cantidad",item.mCantidad);
            PostMedicina(medicinaxpedido.toString());
        }
        showProgress(false);
        builder.setTitle("Creacion de Receta");
        builder.setMessage("Creacion de Receta Exitosa");
        builder.setPositiveButton("Finalizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
            }
        });
        builder.show();
    }
    public void PostMedicina(String datos){
        AsyncHttpClient client = new AsyncHttpClient();
        ByteArrayEntity entity = null;
        try {
            entity = new ByteArrayEntity(datos.getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        client.post(getApplicationContext(),"http://"+Client.getInstance().ip+":64698/api/MedicamentoxReceta/PostMedicamentoxReceta",entity,"application/json",new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(String response){

            }
            @Override
            public void onFailure(int statusCode, Throwable error,String content){
                showProgress(false);
                Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void GetAllMedicinas(){
        showProgress(true);
        RequestParams params=new RequestParams();
        params.put("id", Client.getInstance().id);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://"+ Client.getInstance().ip+":64698/api/Medicamento/GetAllMedicamentos",params,new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(String response){
                JsonParser parser = new JsonParser();
                JsonElement tradeElement = parser.parse(response);
                JsonArray sus=tradeElement.getAsJsonArray();
                for(int i=0;i<sus.size();++i){
                    JsonObject obj=sus.get(i).getAsJsonObject();
                    medicinases.add(new Medicinas(obj.get("Nombre").getAsString(),"0","1",
                            obj.get("IdMedicamento").getAsString()));
                }
                adapter=new NewRecetaAdapter(medicinases,mListener);
                recyclerView.setAdapter(adapter);
                showProgress(false);
            }

            @Override
            public void onFailure(int statusCode, Throwable error,String content){
                Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
            }
        });
    }

    public class OnListener{
        public void onListenerAction(Medicinas item,boolean checked){
            if(checked){
                toAdd.add(item);
            }
        }
    }
}
