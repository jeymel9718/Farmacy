package com.example.jeison.farmacy;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.jeison.farmacy.Clases.Client;
import com.example.jeison.farmacy.Clases.Medicinas;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;

public class PedidosFragment extends Fragment {

    public OnListener mListener;
    public RecyclerView mPedidos;
    private Context mContext;
    private ArrayList<Pedidos> pedidoses=new ArrayList<Pedidos>();
    private ArrayList<Medicinas> lista=new ArrayList<Medicinas>();
    public PedidosFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListener=new OnListener();



    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_pedidos, container, false);
        mContext=view.getContext();
        mPedidos= (RecyclerView) view.findViewById(R.id.recycler);
        mPedidos.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        GetPedidos();

        return view;
    }

    public void GetPedidos(){
        //showProgress(true);
        pedidoses.clear();
        RequestParams params=new RequestParams();
        params.put("id", Client.getInstance().id);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://"+Client.getInstance().ip+":64698/api/Pedido/GetPedidos",params,new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(String response){
                JsonParser parser = new JsonParser();
                JsonElement tradeElement = parser.parse(response);
                JsonArray sus=tradeElement.getAsJsonArray();
                for(int i=0;i<sus.size();++i){
                    JsonObject obj=sus.get(i).getAsJsonObject();
                    String direccion=obj.get("Provincia").getAsString()+", "+obj.get("Canton").getAsString()+", "+obj.get("Distrito").getAsString();
                    pedidoses.add(new Pedidos(obj.get("IdPedido").getAsString(),
                            obj.get("NombreSucursal").getAsString(),obj.get("FechaRecojo").getAsString(),direccion
                            ,obj.get("RecetaImg").getAsString()));
                }
                //showProgress(false);
                mPedidos.setAdapter(new ListPedidosAdapter(pedidoses,mListener));

            }

            @Override
            public void onFailure(int statusCode, Throwable error,String content){
                //showProgress(false);
                Toast.makeText(mContext, "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
            }
        });
    }

    public class OnListener{
        public void onListenerAction(final Pedidos item){
            lista.clear();
            final String sucursal=item.Sucursal;
            final Intent intent=new Intent(mContext,PedidosConfigActivity.class);
            RequestParams params=new RequestParams();
            params.put("id",item.numPedido);
            AsyncHttpClient client = new AsyncHttpClient();
            client.get("http://"+Client.getInstance().ip+":64698/api/PedidoxMedicamento/GetMedicamentosxPedido",params,new AsyncHttpResponseHandler(){
                @Override
                public void onSuccess(String response){

                    //showProgress(false);
                    intent.putExtra("medicinas",response);
                    intent.putExtra("Su_name",sucursal);
                    intent.putExtra("Su_id","4");
                    intent.putExtra("Fecha",item.Dater);
                    intent.putExtra("PedidoId",item.numPedido);
                    intent.putExtra("Imagen",item.mRecetaimg);
                    startActivity(intent);

                }

                @Override
                public void onFailure(int statusCode, Throwable error,String content){
                    //showProgress(false);
                    Toast.makeText(mContext, "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            });
            Log.i("Data",lista.toString());

        }
    }
}
