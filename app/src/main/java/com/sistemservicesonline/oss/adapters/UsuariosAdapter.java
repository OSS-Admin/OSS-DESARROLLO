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
import com.sistemservicesonline.oss.appcode.Favorito;
import com.sistemservicesonline.oss.appcode.Usuario;

import java.util.ArrayList;
import java.util.List;

public class UsuariosAdapter extends RecyclerView.Adapter<UsuariosAdapter.ViewHolder> implements Filterable {
    private UsuariosAdapter.OnItemClickListener mListener;
    private List<Usuario> usuarioList;
    private List<Usuario> usuarioListFull;

    public interface OnItemClickListener {
        void onItemClick (int position, String codigo);
    }

    public void setOnItemClickListener (UsuariosAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView TextViewToken, TextViewNombreUsuario, TextViewPerfilProfesional;
        private RatingBar RatingBarCalificacion;


        public ViewHolder(View itemView, final UsuariosAdapter.OnItemClickListener listener) {
            super(itemView);
            TextViewToken = itemView.findViewById(R.id.TextViewToken);
            TextViewNombreUsuario = itemView.findViewById(R.id.TextViewNombreUsuario);
            TextViewPerfilProfesional = itemView.findViewById(R.id.TextViewPerfilProfesional);
            RatingBarCalificacion = itemView.findViewById(R.id.RatingBarCalificacion);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View v){
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position, (String) TextViewToken.getText());
                        }
                    }
                }
            });
        }
    }

    public UsuariosAdapter(List<Usuario> usuarioList) {
        this.usuarioList = usuarioList;
        usuarioListFull = new ArrayList<>(usuarioList);
    }

    @Override
    public UsuariosAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.items_usuarios, viewGroup, false);
        UsuariosAdapter.ViewHolder viewHolder = new UsuariosAdapter.ViewHolder(view, mListener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UsuariosAdapter.ViewHolder viewHolder, int i) {
        viewHolder.TextViewToken.setText(usuarioList.get(i).getCodigoUsuario());
        viewHolder.TextViewNombreUsuario.setText(usuarioList.get(i).getPrimerNombre() + " " + usuarioList.get(i).getSegundoNombre() + " " + usuarioList.get(i).getPrimerApellido() + " " + usuarioList.get(i).getSegundoApellido());
        viewHolder.TextViewPerfilProfesional.setText(usuarioList.get(i).getPerfilProfesional());
        viewHolder.RatingBarCalificacion.setRating(usuarioList.get(i).getCalificacion());
    }

    @Override
    public int getItemCount() {
        return usuarioList.size();
    }

    @Override
    public Filter getFilter() {
        return ListUsuarios;
    }

    private Filter ListUsuarios = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Usuario> ListUsuariosFiltrada = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                ListUsuariosFiltrada.addAll(usuarioListFull);
            } else {
                String TextoFiltro = charSequence.toString().toLowerCase().trim();
                for (Usuario usuario : usuarioListFull) {
                    String sNombreUsuario = !usuario.getSegundoNombre().equals("") ? usuario.getPrimerNombre() + " " + usuario.getSegundoNombre() + " " + usuario.getPrimerApellido() + " " + usuario.getSegundoApellido() : usuario.getPrimerNombre() + " " + usuario.getPrimerApellido() + " " + usuario.getSegundoApellido();
                    if (sNombreUsuario.toLowerCase().contains(TextoFiltro)) {
                        ListUsuariosFiltrada.add(usuario);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = ListUsuariosFiltrada;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            usuarioList.clear();
            usuarioList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };
}
