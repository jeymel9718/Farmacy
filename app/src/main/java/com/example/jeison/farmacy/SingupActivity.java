package com.example.jeison.farmacy;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jeison.farmacy.Clases.Client;
import com.google.gson.JsonObject;
import com.loopj.android.http.*;

import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A login screen that offers login via email/password.
 */
public class SingupActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

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
    private EditText mPasswordView;
    private EditText mCedulaView;
    private EditText mNombreView;
    private EditText mApellido1;
    private EditText mApellido2;
    private EditText mDate;
    private EditText mTelefono;
    private EditText mCanton;
    private EditText mProvincia;
    private EditText mDistrito;
    private EditText mAddress;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);
        // Set up the login form.
        mCedulaView= (EditText) findViewById(R.id.cedula);
        mApellido1= (EditText) findViewById(R.id.apellido1);
        mApellido2=(EditText) findViewById(R.id.apellido2);
        mDate=(EditText) findViewById(R.id.fecha);
        mNombreView= (EditText) findViewById(R.id.nombre);
        mTelefono= (EditText) findViewById(R.id.telefono);
        mProvincia= (EditText) findViewById(R.id.provincia);
        mCanton= (EditText) findViewById(R.id.canton);
        mDistrito= (EditText) findViewById(R.id.distrito);
        mAddress= (EditText) findViewById(R.id.dirreccion);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

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

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        JsonObject persona=new JsonObject();

        // Reset errors.
        mPasswordView.setError(null);
        mCedulaView.setError(null);
        mNombreView.setError(null);
        mDate.setError(null);
        mTelefono.setError(null);
        mProvincia.setError(null);
        mCanton.setError(null);
        mDistrito.setError(null);
        mTelefono.setError(null);
        mAddress.setError(null);

        // Store values at the time of the login attempt.
        String password = mPasswordView.getText().toString();
        String name = mNombreView.getText().toString();
        String apellido1 = mApellido1.getText().toString();
        String apellido2 = mApellido2.getText().toString();
        String date = mDate.getText().toString();
        String cedula = mCedulaView.getText().toString();
        String telefono=mTelefono.getText().toString();
        String canton=mCanton.getText().toString();
        String distrito=mDistrito.getText().toString();
        String provincia=mProvincia.getText().toString();
        String address=mAddress.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        //cedula
        if (TextUtils.isEmpty(cedula)) {
            mCedulaView.setError(getString(R.string.error_field_required));
            focusView = mCedulaView;
            cancel = true;
        } else if (!isCedulaValid(cedula)) {
            mCedulaView.setError(getString(R.string.error_invalid_cedula));
            focusView = mCedulaView;
            cancel = true;
        }
        //validacion de dirrecciones
        if(TextUtils.isEmpty(canton) || TextUtils.isEmpty(provincia) || TextUtils.isEmpty(distrito)){
            mProvincia.setError(getString(R.string.error_field_required));
            focusView=mProvincia;
            cancel=true;
        }
        if(TextUtils.isEmpty(address)){
            mAddress.setError(getString(R.string.error_field_required));
            focusView=mAddress;
            cancel=true;
        }

        //validacion telefono
        if(TextUtils.isEmpty(telefono)){
            mTelefono.setError(getString(R.string.error_field_required));
            focusView=mTelefono;
            cancel=true;
        }else if(!isTelefonoValid(telefono)){
            mTelefono.setError(getString(R.string.error_incorrect_phone));
            focusView=mTelefono;
            cancel=true;
        }

        //validacion nombre y apellidos
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(apellido1) || TextUtils.isEmpty(apellido2)) {
            mNombreView.setError(getString(R.string.error_field_required));
            focusView = mNombreView;
            cancel = true;
        } else if (!isNameValid(name) || !(isNameValid(apellido1)) || !isNameValid(apellido2)) {
            mNombreView.setError(getString(R.string.error_invalid_name));
            focusView = mNombreView;
            cancel = true;
        }

        //validacion fecha
        if (TextUtils.isEmpty(date)) {
            mDate.setError(getString(R.string.error_field_required));
            focusView = mDate;
            cancel = true;
        } else if (!isDateValid(date)) {
            mDate.setError(getString(R.string.error_incorrect_date));
            focusView = mDate;
            cancel = true;
        }

        //validacion de address


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            persona.addProperty("IdCedula",cedula);
            persona.addProperty("Nombre",name);
            persona.addProperty("Apellido1",apellido1);
            persona.addProperty("Apellido2",apellido2);
            persona.addProperty("Contraseña",password);
            persona.addProperty("Telefono",telefono);
            persona.addProperty("Provincia",provincia);
            persona.addProperty("Canton",canton);
            persona.addProperty("Distrito",distrito);
            persona.addProperty("DescripcionDireccion",address);
            persona.addProperty("FechaNacimiento",date);
            showProgress(true);
            RegistroUser(persona.toString());

        }
    }
    private boolean isTelefonoValid(String telefono){
        Pattern par =Pattern.compile("[0-9]{8,8}");
        Matcher matcher=par.matcher(telefono);
        return matcher.matches();
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }
    private boolean isCedulaValid(String cedula) {
        //TODO: Replace this with your own logic
        return cedula.length() > 8;
    }
    private boolean isDateValid(String date){
        return date.contains("-");
    }
    private boolean isNameValid(String name){
        Pattern pat = Pattern.compile("^[A-Z][a-z]+");
        Matcher match=pat.matcher(name);
        return match.matches();
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

    public void RegistroUser(String datos){
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
        client.post(getApplicationContext(),"http://"+ Client.getInstance().ip+":64698/api/Persona/PostPersona",entity,"application/json",new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(String response){
                showProgress(false);
                finish();
            }
            @Override
            public void onFailure(int statusCode, Throwable error,String content){
                showProgress(false);
                Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
            }
        });

    }

}

