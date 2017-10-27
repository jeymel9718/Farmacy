package com.example.jeison.farmacy.Adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jeison.farmacy.Clases.Client;
import com.example.jeison.farmacy.Clases.Medicinas;
import com.example.jeison.farmacy.Clases.Recetas;
import com.example.jeison.farmacy.Interfaces.OnListFragmentInteractionListener;
import com.example.jeison.farmacy.R;
import com.example.jeison.farmacy.dummy.DummyContent.DummyItem;
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

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class RecetasAdapter extends RecyclerView.Adapter<RecetasAdapter.ViewHolder> {

    private final List<Recetas> mValues;
    private final OnListFragmentInteractionListener mListener;
    private ArrayList<Medicinas> medicinases=new ArrayList<>();

    public RecetasAdapter(List<Recetas> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    public List<Recetas> getmValue(){
        return this.mValues;
    }

    public void delMedicina(Recetas item){
        mValues.remove(item);
        notifyDataSetChanged();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_recetas, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).mId);
        holder.mContentView.setText(mValues.get(position).mContent);
        holder.mImageView.setImageBitmap(decodeBase64(mValues.get(position).mImagen));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetMedicinas(holder.mItem.mId,holder.mItem.mImagen);
            }
        });
        holder.mBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonObject obj=new JsonObject();
                obj.addProperty("IdReceta",holder.mItem.mId);
                DelReceta(obj.toString(),v,holder);
            }
        });
    }
    public void GetMedicinas(String id,final String img){
        RequestParams params=new RequestParams();
        params.put("id",id);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://"+ Client.getInstance().ip+":64698/api/MedicamentoxReceta/GetMedicamentosxReceta",params,new AsyncHttpResponseHandler(){
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
                mListener.onRecetaSelected(medicinases,img);



            }

            @Override
            public void onFailure(int statusCode, Throwable error,String content){
                //showProgress(false);
                //Toast.makeText(, "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
            }
        });
    }
    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public void DelReceta(String datos, final View view, final ViewHolder viewHolder){
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
        client.post(view.getContext(),"http://"+ Client.getInstance().ip+":64698/api/Receta/DeleteReceta",entity,"application/json",new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(String response){
                mListener.onBorrarReceta(viewHolder.mItem,viewHolder.mView);
            }
            @Override
            public void onFailure(int statusCode, Throwable error,String content){
                Toast.makeText(view.getContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final ImageView mImageView;
        public final Button mBorrar;
        public Recetas mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            mImageView = (ImageView) view.findViewById(R.id.ic_receta);
            mBorrar = (Button) view.findViewById(R.id.delete);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
