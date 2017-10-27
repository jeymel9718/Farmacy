package com.example.jeison.farmacy;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeison on 02/10/2017.
 */

public class ListPedidosAdapter extends RecyclerView.Adapter<ListPedidosAdapter.ViewHolder> {
    private List<Pedidos> mPedidos;
    private final PedidosFragment.OnListener mListener;

    public ListPedidosAdapter(ArrayList<Pedidos> pedidos, PedidosFragment.OnListener listener){
        this.mPedidos=pedidos;
        this.mListener=listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_pedidos, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListPedidosAdapter.ViewHolder holder, int position) {
        Pedidos item=mPedidos.get(position);
        holder.mItem=item;
        holder.mNumero.setText("Pedido#:"+item.numPedido);
        holder.mSucursal.setText("Sucursal de recojo:"+item.Sucursal);
        holder.mFecha.setText(item.Dater);
        holder.mDireccion.setText(item.Direccion);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onListenerAction(holder.mItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPedidos.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNumero;
        public final TextView mSucursal;
        public final TextView mFecha;
        public final TextView mDireccion;
        public Pedidos mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNumero=(TextView) view.findViewById(R.id.numero);
            mSucursal=(TextView) view.findViewById(R.id.sucursal);
            mDireccion=(TextView) view.findViewById(R.id.dirreccion);
            mFecha=(TextView) view.findViewById(R.id.fecha);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNumero.getText() + "'";
        }
    }
}
