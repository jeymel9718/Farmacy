package com.example.jeison.farmacy.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.jeison.farmacy.Clases.Enfermedad;
import com.example.jeison.farmacy.NewHistorial;
import com.example.jeison.farmacy.R;

import java.util.List;

/**
 * Created by Jeison on 10/10/2017.
 */

public class EnfermedadAdapter extends RecyclerView.Adapter<EnfermedadAdapter.ViewHolder>{
    private List<Enfermedad> mMedicinas;
    private final NewHistorial.OnListener mListener;

    public EnfermedadAdapter(List<Enfermedad> medicinases,NewHistorial.OnListener listener){
        this.mMedicinas=medicinases;
        this.mListener=listener;
    }

    @Override
    public EnfermedadAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.enfermedad_item, parent, false);
        return new EnfermedadAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final EnfermedadAdapter.ViewHolder holder, int position) {
        Enfermedad item = mMedicinas.get(position);
        holder.mItem = item;
        holder.mName.setText(item.name);

        holder.mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check=holder.mAdd.isChecked();
                mListener.onListenerAction(holder.mItem,check);
            }
        });
    }
    @Override
    public int getItemCount() {
        return mMedicinas.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mName;
        public final CheckBox mAdd;
        public Enfermedad mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mName = (TextView) view.findViewById(R.id.name);
            mAdd= (CheckBox) view.findViewById(R.id.add_enfermedad);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mName.getText() + "'";
        }
    }
}
