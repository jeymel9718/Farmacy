package com.example.jeison.farmacy;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jeison.farmacy.Adapters.ConfigAdapter;
import com.example.jeison.farmacy.Adapters.TerminarAdapter;
import com.example.jeison.farmacy.Clases.Client;
import com.example.jeison.farmacy.Clases.Medicinas;
import com.google.gson.Gson;
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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A login screen that offers login via email/password.
 */
public class PedidosConfigActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    private JsonParser mParser=new JsonParser();
    private String Peidoid=null;
    private AlertDialog.Builder builder;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // UI references.
    private TextView mSucursalView;
    private EditText mDateView;
    private EditText mTelefonoView;
    private View mProgressView;
    private View mLoginFormView;
    private RecyclerView recyclerView;
    private ConfigAdapter madapter;
    private OnListener mListener;
    private String SuId;
    private String fecha;
    private ImageView mRecetaImg;
    private String Receta;
    private Button SelectReceta;
    private ArrayList<Medicinas> medicinases;
    private String SucursalName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos_config);


        // Set up the login form.
        mListener=new OnListener();
        medicinases=new ArrayList<Medicinas>();
        builder=new AlertDialog.Builder(this);
        SucursalName=getIntent().getStringExtra("Su_name");
        SuId=getIntent().getStringExtra("Su_id");
        fecha=getIntent().getStringExtra("Fecha");
        Peidoid=getIntent().getStringExtra("PedidoId");
        Receta=getIntent().getStringExtra("Imagen");
        String medicinas=getIntent().getExtras().getString("medicinas");
        stringtoArr(medicinas);
        mDateView=(EditText) findViewById(R.id.dirreccion);
        mDateView.setText(fecha);
        mRecetaImg=(ImageView) findViewById(R.id.imagen);
        mRecetaImg.setImageBitmap(decodeBase64(Receta));
        mSucursalView= (TextView) findViewById(R.id.Sucursal);
        mSucursalView.setText("Sucursal de recojo:"+SucursalName);
        mTelefonoView=(EditText) findViewById(R.id.telefono);
        mTelefonoView.setText(Client.getInstance().Telefono);

        madapter=new ConfigAdapter(this.medicinases,mListener);
        recyclerView= (RecyclerView) findViewById(R.id.rec_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        recyclerView.setAdapter(madapter);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pedidos, menu);
        return true;
    }

    public void stringtoArr(String arr){
        JsonParser parser = new JsonParser();
        JsonElement tradeElement = parser.parse(arr);
        JsonArray sus=tradeElement.getAsJsonArray();
        for(int i=0;i<sus.size();++i){
            JsonObject obj=sus.get(i).getAsJsonObject();
            medicinases.add(new Medicinas(obj.get("Nombre").getAsString(),obj.get("Precio").getAsString(),
                    obj.get("Cantidad").getAsString(),obj.get("IdMedicamento").getAsString()));
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mTelefonoView.setError(null);
        mDateView.setError(null);

        // Store values at the time of the login attempt.
        String telefono = mTelefonoView.getText().toString();
        String fecha = mDateView.getText().toString();
        String sucursal=mSucursalView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(telefono)) {
            mTelefonoView.setError(getString(R.string.error_field_required));
            focusView = mTelefonoView;
            cancel = true;
        }else if(!isTelefonoValid(telefono)){
            mTelefonoView.setError(getString(R.string.error_incorrect_phone));
            focusView = mTelefonoView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(fecha)) {
            mDateView.setError(getString(R.string.error_field_required));
            focusView = mDateView;
            cancel = true;
        } else if (!isDateValid(fecha)) {
            mDateView.setError(getString(R.string.error_incorrect_date));
            focusView = mDateView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
        }
    }

    private boolean isDateValid(String date){
        return date.contains("/");
    }
    private boolean isTelefonoValid(String telefono){
        Pattern pat = Pattern.compile("[0-9]+");
        Matcher match=pat.matcher(telefono);
        return match.matches();
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id=item.getItemId();
        if(id==android.R.id.home){
            this.finish();
        }else if(id==R.id.action_done){
            showProgress(true);
            JsonObject obj=new JsonObject();
            obj.addProperty("FechaRecojo",mDateView.getText().toString());
            obj.addProperty("IdPedido",Peidoid);
            obj.addProperty("RecetaImg",Receta);
            UpdatePedido(obj.toString());
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

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
    public void UpdatePedido(String datos){
        AsyncHttpClient client = new AsyncHttpClient();
        ByteArrayEntity entity = null;
        try {
            entity = new ByteArrayEntity(datos.getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        client.post(getApplicationContext(),"http://"+Client.getInstance().ip+":64698/api/Pedido/UpdatePedido",entity,"application/json",new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(String response){
                UpdateMedicinas();
            }
            @Override
            public void onFailure(int statusCode, Throwable error,String content){
                showProgress(false);
                Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
            }
        });

    }
    public void updatemedicina(String datos){
        AsyncHttpClient client = new AsyncHttpClient();
        ByteArrayEntity entity = null;
        try {
            entity = new ByteArrayEntity(datos.getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        client.post(getApplicationContext(),"http://"+Client.getInstance().ip+":64698/api/PedidoxMedicamento/UpdatePedidoxMedicamento",entity,"application/json",new AsyncHttpResponseHandler(){
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

    public void UpdateMedicinas(){
        List<Medicinas> items=madapter.getmMedicinas();
        for(int i=0;i<items.size();++i){
            Medicinas item=items.get(i);
            JsonObject obj=new JsonObject();
            obj.addProperty("IdPedido",Peidoid);
            obj.addProperty("IdMedicamento",item.ID);
            obj.addProperty("Cantidad",item.mCantidad);
            updatemedicina(obj.toString());
        }
        showProgress(false);
        builder.setTitle("Modificacion de Pedido").setMessage("Modificacion de Pedido exitosa");
        builder.setPositiveButton("Finalizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
            }
        });
        builder.show();
    }


    public class OnListener{
        public void onListenerAction(Medicinas item,View view){
            recyclerView.removeView(view);
            madapter.delMedicina(item);
        }
    }
}

