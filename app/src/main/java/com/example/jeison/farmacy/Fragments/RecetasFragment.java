package com.example.jeison.farmacy.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.jeison.farmacy.Adapters.MedicinasAdapter;
import com.example.jeison.farmacy.Adapters.RecetasAdapter;
import com.example.jeison.farmacy.Clases.Client;
import com.example.jeison.farmacy.Clases.Medicinas;
import com.example.jeison.farmacy.Clases.Recetas;
import com.example.jeison.farmacy.Interfaces.OnListFragmentInteractionListener;
import com.example.jeison.farmacy.R;
import com.example.jeison.farmacy.dummy.DummyContent;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class RecetasFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private ArrayList<Recetas> recetases=new ArrayList<Recetas>();
    private OnListFragmentInteractionListener mListener;
    public RecetasAdapter Arecetas;
    public RecyclerView recyclerView;
    private Context context;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecetasFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static RecetasFragment newInstance(int columnCount) {
        RecetasFragment fragment = new RecetasFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recetas_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                GetRecetas();
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void GetRecetas(){
        recetases.clear();
        RequestParams params=new RequestParams();
        params.put("id",Client.getInstance().id);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://"+ Client.getInstance().ip+":64698/api/Receta/GetRecetasId",params,new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(String response){
                JsonParser parser = new JsonParser();
                JsonElement tradeElement = parser.parse(response);
                JsonArray sus=tradeElement.getAsJsonArray();
                for(int i=0;i<sus.size();++i){
                    JsonObject obj=sus.get(i).getAsJsonObject();
                    recetases.add(new Recetas(obj.get("IdReceta").getAsString(),"",obj.get("RecetaImage").getAsString()));
                }
                Arecetas=new RecetasAdapter(recetases,mListener);
                recyclerView.setAdapter(Arecetas);


            }

            @Override
            public void onFailure(int statusCode, Throwable error,String content){
                Toast.makeText(context, "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
            }
        });
    }
}
