package com.example.jeison.farmacy.Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.jeison.farmacy.Adapters.SucursalesAdapter;
import com.example.jeison.farmacy.Clases.Client;
import com.example.jeison.farmacy.MedicamentosActivity;
import com.example.jeison.farmacy.R;
import com.example.jeison.farmacy.Clases.Sucursales;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link SucursalesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SucursalesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ListView mList;
    private ProgressBar progressBar;
    private SucursalesAdapter mLeadsAdapter;
    private ArrayList<Sucursales> mSucursales;

    public SucursalesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SucursalesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SucursalesFragment newInstance(String param1, String param2) {
        SucursalesFragment fragment = new SucursalesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSucursales=new ArrayList<Sucursales>();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sucursales, container, false);
        progressBar=(ProgressBar)root.findViewById(R.id.progressBar);

        // Instancia del ListView.
        mList = (ListView) root.findViewById(R.id.leads_list);

        // Inicializar el adaptador con la fuente de datos.
        GetSucursales();

        //Relacionando la lista con el adaptador


        // Eventos
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Sucursales currentLead = mLeadsAdapter.getItem(position);
                Intent medicinas=new Intent(getActivity(),MedicamentosActivity.class);
                medicinas.putExtra("Su_Name",currentLead.mName);
                medicinas.putExtra("Su_id",currentLead.mId);
                startActivity(medicinas);
            }
        });

        setHasOptionsMenu(true);
        return root;
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

            mList.setVisibility(show ? View.GONE : View.VISIBLE);
            mList.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mList.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            progressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            mList.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void GetSucursales(){
        showProgress(true);
        mSucursales.clear();
        RequestParams params=new RequestParams();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://"+ Client.getInstance().ip+":64698/api/Sucursal/GetAllSucursales",params,new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(String response){
                JsonParser parser = new JsonParser();
                JsonElement tradeElement = parser.parse(response);
                JsonArray sus=tradeElement.getAsJsonArray();
                for(int i=0;i<sus.size();++i){
                    JsonObject obj=sus.get(i).getAsJsonObject();
                    String location=obj.get("Provincia").getAsString()+", "+obj.get("Canton").getAsString()+", "+obj.get("Distrito").getAsString();
                    mSucursales.add(new Sucursales(obj.get("Nombre").getAsString(),location,
                            obj.get("DescripcionDireccion").getAsString(),
                            obj.get("IdSucursal").getAsString()));
                }
                showProgress(false);
                mLeadsAdapter = new SucursalesAdapter(getActivity(),mSucursales);
                mList.setAdapter(mLeadsAdapter);
            }

            @Override
            public void onFailure(int statusCode, Throwable error,String content){
                showProgress(false);
                Toast.makeText(getContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
            }
        });
    }

}
