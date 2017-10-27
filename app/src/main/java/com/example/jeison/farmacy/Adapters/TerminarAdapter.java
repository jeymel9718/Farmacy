package com.example.jeison.farmacy.Adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jeison.farmacy.Clases.Medicinas;
import com.example.jeison.farmacy.FinalizarPedidosActivity;
import com.example.jeison.farmacy.R;

import java.util.List;

/**
 * Created by Jeison on 03/10/2017.
 */

public class TerminarAdapter extends RecyclerView.Adapter<TerminarAdapter.ViewHolder> {

    private List<Medicinas> mMedicinas;
    private final FinalizarPedidosActivity.OnListener mListener;

    public TerminarAdapter(List<Medicinas> medicinases, FinalizarPedidosActivity.OnListener listener){
        this.mMedicinas=medicinases;
        this.mListener=listener;
    }

    public void delMedicina(Medicinas item){
        mMedicinas.remove(item);
        notifyDataSetChanged();
    }

    public List<Medicinas> getmMedicinas() {
        return mMedicinas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pedidos_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Medicinas item = mMedicinas.get(position);
        holder.mItem = item;
        holder.mItem.mCantidad="1";
        holder.mName.setText(item.mName);
        holder.mPrice.setText("Precio:"+item.mPrice);
        holder.mTotal.setText("Total:"+item.mPrice);
        holder.mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onListenerAction(holder.mItem, holder.mView);
            }
        });
        holder.mCantidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("Cantidad",s.toString());
                mMedicinas.get(position).mCantidad=s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                Medicinas item=mMedicinas.get(position);
                holder.mTotal.setText("Total:"+multi(item.mCantidad,item.mPrice));
            }
        });
    }
    @Override
    public int getItemCount() {
        return mMedicinas.size();
    }

    public String multi(String a,String B){
        if(!a.equals("") && !B.equals("")){
            int result=Integer.parseInt(a)*Integer.parseInt(B);
            return Integer.toString(result);
        }else {
            return null;
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mName;
        public final TextView mPrice;
        public final EditText mCantidad;
        public final Button mAdd;
        public final TextView mTotal;
        public Medicinas mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mName = (TextView) view.findViewById(R.id.name);
            mPrice = (TextView) view.findViewById(R.id.price);
            mAdd= (Button) view.findViewById(R.id.check_add);
            mTotal= (TextView) view.findViewById(R.id.total);
            mCantidad= (EditText) view.findViewById(R.id.cantidad);
            mCantidad.setText("1");
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mName.getText() + "'";
        }
    }
}
