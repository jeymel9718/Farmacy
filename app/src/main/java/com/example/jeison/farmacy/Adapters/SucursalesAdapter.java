package com.example.jeison.farmacy.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.jeison.farmacy.R;
import com.example.jeison.farmacy.Clases.Sucursales;

import java.util.List;

/**
 * Created by Jeison on 28/09/2017.
 */

public class SucursalesAdapter extends ArrayAdapter<Sucursales> {

    public SucursalesAdapter(Context context, List<Sucursales> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Obtener inflater.
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ViewHolder holder;

        // ¿Ya se infló este view?
        if (null == convertView) {
            //Si no existe, entonces inflarlo con image_list_view.xml
            convertView = inflater.inflate(
                    R.layout.list_item_sucursal,
                    parent,
                    false);

            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.su_name);
            holder.location = (TextView) convertView.findViewById(R.id.su_location);
            holder.address = (TextView) convertView.findViewById(R.id.su_addres);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Lead actual.
        Sucursales lead = getItem(position);

        // Setup.
        holder.name.setText(lead.mName);
        holder.location.setText(lead.mLocation);
        holder.address.setText(lead.mAddress);
        //Glide.with(getContext()).load(lead.getImage()).into(holder.avatar);

        return convertView;
    }

    static class ViewHolder {
        TextView name;
        TextView location;
        TextView address;
    }
}
