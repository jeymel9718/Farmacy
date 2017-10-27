package com.example.jeison.farmacy.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jeison.farmacy.Clases.Client;
import com.example.jeison.farmacy.Clases.Historial;
import com.example.jeison.farmacy.HistorialFragment;
import com.example.jeison.farmacy.R;
import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Jeison on 04/10/2017.
 */

public class HistorialAdapter extends RecyclerView.Adapter<HistorialAdapter.ViewHolder>{

    private List<Historial> mItems;
    private HistorialFragment.OnListener mListener;

    public HistorialAdapter(List<Historial> items, HistorialFragment.OnListener listener){
        this.mItems=items;
        this.mListener=listener;
    }

    public void delEnfermedad(Historial item){
        mItems.remove(item);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Historial item=mItems.get(position);
        holder.mItem=item;
        holder.mFecha.setText(item.mFecha);
        holder.mPadecimientos.setText(item.mPadecimientos);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonObject obj=new JsonObject();
                obj.addProperty("IdCedula", Client.getInstance().id);
                obj.addProperty("IdEnfermedad",holder.mItem.Idenfermedad);
                obj.addProperty("FechaEnfermedad",holder.mItem.mFecha);
                mListener.onListenerAction(holder.mItem,holder.mView);
                DelHistorial(obj.toString(),v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void DelHistorial(String datos, final View view){
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
        client.post(view.getContext(),"http://"+Client.getInstance().ip+":64698/api/EnfermedadxPersona/EliminarEnfxPer",entity,"application/json",new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(String response){

            }
            @Override
            public void onFailure(int statusCode, Throwable error,String content){
                Toast.makeText(view.getContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mFecha;
        public final TextView mPadecimientos;
        public final Button delete;
        public Historial mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mFecha = (TextView) view.findViewById(R.id.dirreccion);
            mPadecimientos = (TextView) view.findViewById(R.id.padecimientos);
            delete= (Button) view.findViewById(R.id.delete);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mFecha.getText() + "'";
        }
    }
}
