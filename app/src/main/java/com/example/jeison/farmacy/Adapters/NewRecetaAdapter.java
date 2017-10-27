package com.example.jeison.farmacy.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.jeison.farmacy.Clases.Medicinas;
import com.example.jeison.farmacy.CrearReceta;
import com.example.jeison.farmacy.R;

import java.util.List;

/**
 * Created by Jeison on 15/10/2017.
 */

public class NewRecetaAdapter extends RecyclerView.Adapter<NewRecetaAdapter.ViewHolder> {

    public List<Medicinas> mValues;
    public CrearReceta.OnListener mListener;

    public NewRecetaAdapter(List<Medicinas> items, CrearReceta.OnListener listener){
        this.mValues=items;
        this.mListener=listener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recetas_item, parent, false);
        return new NewRecetaAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem=mValues.get(position);
        holder.mName.setText(mValues.get(position).mName);
        holder.mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked=holder.mAdd.isChecked();
                mListener.onListenerAction(holder.mItem,checked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public final View mView;
        public final TextView mName;
        public final CheckBox mAdd;
        public Medicinas mItem;
        public ViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
            mName=(TextView) itemView.findViewById(R.id.name);
            mAdd=(CheckBox) itemView.findViewById(R.id.check_add);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mName.getText() + "'";
        }
    }
}
