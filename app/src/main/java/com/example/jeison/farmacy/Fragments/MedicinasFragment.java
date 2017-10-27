package com.example.jeison.farmacy.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.jeison.farmacy.Adapters.MedicinasAdapter;
import com.example.jeison.farmacy.Adapters.PedidosAdapter;
import com.example.jeison.farmacy.Clases.Client;
import com.example.jeison.farmacy.Clases.Medicinas;
import com.example.jeison.farmacy.Interfaces.OnListFragmentInteractionListener;
import com.example.jeison.farmacy.R;
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
public class MedicinasFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 3;
    private String id;
    private OnListFragmentInteractionListener mListener;
    public PedidosAdapter mPedidos;
    public RecyclerView pedidos;
    public RecyclerView medicamentos;
    public MedicinasAdapter Amedicinas;
    private Context context;
    private ArrayList<Medicinas> medicinases=new ArrayList<Medicinas>();
    public Button endPedido;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MedicinasFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MedicinasFragment newInstance(int columnCount) {
        MedicinasFragment fragment = new MedicinasFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            id=getArguments().getString("ID");
            mPedidos=new PedidosAdapter(mListener);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medicinas_list, container, false);
        endPedido=(Button) view.findViewById(R.id.button);
        endPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("myTag",mPedidos.getItems().toString());
                mListener.onTerminarPedidoInteration(mPedidos.getItems().toString());

            }
        });
        // Set the adapter
        context = view.getContext();
        medicamentos = (RecyclerView) view.findViewById(R.id.list);
        medicamentos.setLayoutManager(new GridLayoutManager(context, mColumnCount));

        GetMedicinas();
        pedidos= (RecyclerView) view.findViewById(R.id.list2);
        pedidos.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
        pedidos.setAdapter(mPedidos);

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

    public void GetMedicinas(){
        //showProgress(true);
        RequestParams params=new RequestParams();
        params.put("id",id);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://"+ Client.getInstance().ip+":64698/api/MedicamentoxSucursal/GetMedicamentoxSucursal",params,new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(String response){
                JsonParser parser = new JsonParser();
                JsonElement tradeElement = parser.parse(response);
                JsonArray sus=tradeElement.getAsJsonArray();
                for(int i=0;i<sus.size();++i){
                    JsonObject obj=sus.get(i).getAsJsonObject();
                    medicinases.add(new Medicinas(obj.get("Nombre").getAsString(),obj.get("Precio").getAsString(),
                            obj.get("Cantidad").getAsString(),obj.get("IdMedicamento").getAsString()));
                }
                //showProgress(false);
                Amedicinas=new MedicinasAdapter(medicinases,mListener);
                medicamentos.setAdapter(Amedicinas);


            }

            @Override
            public void onFailure(int statusCode, Throwable error,String content){
                //showProgress(false);
                Toast.makeText(context, "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
            }
        });
    }

}
