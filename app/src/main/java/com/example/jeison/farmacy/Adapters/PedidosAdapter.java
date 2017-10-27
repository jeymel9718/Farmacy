package com.example.jeison.farmacy.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.jeison.farmacy.Clases.Medicinas;
import com.example.jeison.farmacy.Interfaces.OnListFragmentInteractionListener;
import com.example.jeison.farmacy.R;
import com.example.jeison.farmacy.dummy.DummyContent.DummyItem;

import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class PedidosAdapter extends RecyclerView.Adapter<PedidosAdapter.ViewHolder> {

    private  ArrayList<Medicinas> mValues;
    private final OnListFragmentInteractionListener mListener;
    private int currentPosition=0;

    public PedidosAdapter(OnListFragmentInteractionListener listener) {
        mValues = new ArrayList<Medicinas>();
        mListener = listener;
    }

    public boolean Contains(Medicinas item){
        return mValues.contains(item);
    }

    public void addMedicina(Medicinas item){
        mValues.add(item);
        notifyDataSetChanged();
    }
    public void delMedicina(Medicinas item){
        mValues.remove(item);
        notifyDataSetChanged();
    }

    public ArrayList<Medicinas> getItems(){
        return this.mValues;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_medicinas, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mName.setText(mValues.get(position).mName);
        holder.mPrice.setText("Precio:"+mValues.get(position).mPrice);
        holder.mCantidad.setText("Cantidad:"+mValues.get(position).mCantidad);

        holder.mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked=holder.mAdd.isChecked();
                mListener.onPedidoFragmentInteration(holder.mItem,checked,holder.mView);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mName;
        public final TextView mPrice;
        public final TextView mCantidad;
        public final CheckBox mAdd;
        public Medicinas mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mName = (TextView) view.findViewById(R.id.name);
            mPrice = (TextView) view.findViewById(R.id.price);
            mCantidad= (TextView) view.findViewById(R.id.cantidad);
            mAdd= (CheckBox) view.findViewById(R.id.check_add);
            mAdd.setChecked(true);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mName.getText() + "'";
        }
    }
}
