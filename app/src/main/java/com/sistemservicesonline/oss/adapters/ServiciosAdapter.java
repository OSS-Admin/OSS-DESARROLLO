package com.sistemservicesonline.oss.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RatingBar;
import android.widget.TextView;

import com.sistemservicesonline.oss.R;
import com.sistemservicesonline.oss.appcode.Servicio;
import com.sistemservicesonline.oss.appcode.Usuario;

import java.util.ArrayList;
import java.util.List;

public class ServiciosAdapter extends RecyclerView.Adapter<ServiciosAdapter.ViewHolder> implements Filterable {
    private ServiciosAdapter.OnItemClickListener mListener;
    private List<Servicio> servicioList;
    private List<Servicio> servicioListFull;

    public interface OnItemClickListener {
        void onItemClick(int position, String codigo);
    }

    public void setOnItemClickListener (ServiciosAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView TextViewCodigo, TextViewNombreUsuario, TextViewDescripcion, TextViewFecha, TextViewEstado;

        public ViewHolder(View itemView, final ServiciosAdapter.OnItemClickListener listener) {
            super(itemView);
            TextViewCodigo = itemView.findViewById(R.id.TextViewCodigo);
            TextViewNombreUsuario = itemView.findViewById(R.id.TextViewNombreUsuario);
            TextViewDescripcion = itemView.findViewById(R.id.TextViewDescripcion);
            TextViewFecha = itemView.findViewById(R.id.TextViewFecha);
            TextViewEstado = itemView.findViewById(R.id.TextViewEstado);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View v){
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position, (String) TextViewCodigo.getText());
                        }
                    }
                }
            });
        }
    }

    public ServiciosAdapter(List<Servicio> servicioList) {
        this.servicioList = servicioList;
        servicioListFull = new ArrayList<>(servicioList);
    }

    @Override
    public ServiciosAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.items_servicios, viewGroup, false);
        ServiciosAdapter.ViewHolder viewHolder = new ServiciosAdapter.ViewHolder(view, mListener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ServiciosAdapter.ViewHolder viewHolder, int i) {
        viewHolder.TextViewCodigo.setText(servicioList.get(i).getCodigo());
        viewHolder.TextViewNombreUsuario.setText(servicioList.get(i).getNombreUsuario());
        viewHolder.TextViewDescripcion.setText(servicioList.get(i).getDescripcion());
        viewHolder.TextViewFecha.setText(servicioList.get(i).getFechaServicio());
        viewHolder.TextViewEstado.setText(servicioList.get(i).getEstado());
    }

    @Override
    public int getItemCount() {
        return servicioList.size();
    }

    @Override
    public Filter getFilter() {
        return ListServicio;
    }

    private Filter ListServicio = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Servicio> ListUsuariosFiltrada = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                ListUsuariosFiltrada.addAll(servicioListFull);
            } else {
                String TextoFiltro = charSequence.toString().toLowerCase().trim();
                for (Servicio servicio : servicioListFull) {
                    if (servicio.getNombreUsuario().toLowerCase().contains(TextoFiltro) || servicio.getDescripcion().toLowerCase().contains(TextoFiltro)) {
                        ListUsuariosFiltrada.add(servicio);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = ListUsuariosFiltrada;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            servicioList.clear();
            servicioList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };
}
