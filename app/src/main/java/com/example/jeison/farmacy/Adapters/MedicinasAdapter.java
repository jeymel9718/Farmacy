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
import java.util.HashMap;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MedicinasAdapter extends RecyclerView.Adapter<MedicinasAdapter.ViewHolder> {

    private List<Medicinas> mValues;
    private final OnListFragmentInteractionListener mListener;
    public HashMap<String,Medicinas> mMapa=new HashMap<>();

    public MedicinasAdapter(ArrayList<Medicinas> items, OnListFragmentInteractionListener listener) {
        mValues=items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_medicinas, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Medicinas item=mValues.get(position);
        mMapa.put(item.mName,item);
        holder.mItem = item;
        holder.mName.setText(item.mName);
        holder.mPrice.setText("Precio:"+item.mPrice);
        holder.mCantidad.setText("Cantidad:"+item.mCantidad);
        mValues.get(position).mViewm=holder.mView;

        holder.mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked=holder.mAdd.isChecked();
                mListener.onListFragmentInteraction(holder.mItem,checked,holder.mView);
            }
        });
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem,false,null);
                }
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
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mName.getText() + "'";
        }
    }
}
