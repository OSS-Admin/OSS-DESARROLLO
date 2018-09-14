package com.sistemservicesonline.oss.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sistemservicesonline.oss.R;
import com.sistemservicesonline.oss.appcode.Favorito;

import java.util.List;

public class FavoritosAdapter extends RecyclerView.Adapter<FavoritosAdapter.ViewHolder> {
    private FavoritosAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick (int position, String codigo);
    }

    public void setOnItemClickListener (FavoritosAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView TextViewToken, TextViewNombreUsuario, TextViewPerfilProfesional;

        public ViewHolder(View itemView, final FavoritosAdapter.OnItemClickListener listener) {
            super(itemView);
            TextViewToken = itemView.findViewById(R.id.TextViewToken);
            TextViewNombreUsuario = itemView.findViewById(R.id.TextViewNombreUsuario);
            TextViewPerfilProfesional = itemView.findViewById(R.id.TextViewPerfilProfesional);

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

    private List<Favorito> favoritoList;

    public FavoritosAdapter(List<Favorito> favoritoList) {
        this.favoritoList = favoritoList;
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
    }

    @Override
    public int getItemCount() {
        return favoritoList.size();
    }
}