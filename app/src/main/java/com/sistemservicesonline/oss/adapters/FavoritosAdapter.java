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

import java.util.ArrayList;
import java.util.List;

public class FavoritosAdapter extends RecyclerView.Adapter<FavoritosAdapter.ViewHolder> implements Filterable {
    private FavoritosAdapter.OnItemClickListener mListener;
    private List<Favorito> favoritoList;
    private List<Favorito> favoritoListFull;

    public interface OnItemClickListener {
        void onItemClick (int position, String codigo);
    }

    public void setOnItemClickListener (FavoritosAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView TextViewToken, TextViewNombreUsuario, TextViewPerfilProfesional;
        private RatingBar RatingBarCalificacion;

        public ViewHolder(View itemView, final FavoritosAdapter.OnItemClickListener listener) {
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

    public FavoritosAdapter(List<Favorito> favoritoList) {
        this.favoritoList = favoritoList;
        favoritoListFull = new ArrayList<>(favoritoList);
    }

    @Override
    public FavoritosAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.items_usuarios, viewGroup, false);
        FavoritosAdapter.ViewHolder viewHolder = new FavoritosAdapter.ViewHolder(view, mListener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritosAdapter.ViewHolder viewHolder, int i) {
        viewHolder.TextViewToken.setText(favoritoList.get(i).getCodigoUsuarioFavorito());
        viewHolder.TextViewNombreUsuario.setText(favoritoList.get(i).getPrimerNombre() + " " + favoritoList.get(i).getSegundoNombre() + " " + favoritoList.get(i).getPrimerApellido() + " " + favoritoList.get(i).getSegundoApellido());
        viewHolder.TextViewPerfilProfesional.setText(favoritoList.get(i).getPerfilProfesional());
        viewHolder.RatingBarCalificacion.setRating(favoritoList.get(i).getCalificacion());
    }

    @Override
    public int getItemCount() {
        return favoritoList.size();
    }

    @Override
    public Filter getFilter() {
        return ListFavoritos;
    }

    private Filter ListFavoritos = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Favorito> ListFavoritosFiltrada = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                ListFavoritosFiltrada.addAll(favoritoListFull);
            } else {
                String TextoFiltro = charSequence.toString().toLowerCase().trim();
                for (Favorito favorito : favoritoListFull) {
                    String sNombreUsuario = !favorito.getSegundoNombre().equals("") ? favorito.getPrimerNombre() + " " + favorito.getSegundoNombre() + " " + favorito.getPrimerApellido() + " " + favorito.getSegundoApellido() : favorito.getPrimerNombre() + " " + favorito.getPrimerApellido() + " " + favorito.getSegundoApellido();
                    if (sNombreUsuario.toLowerCase().contains(TextoFiltro)) {
                        ListFavoritosFiltrada.add(favorito);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = ListFavoritosFiltrada;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            favoritoList.clear();
            favoritoList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };
}