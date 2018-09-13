package com.sistemservicesonline.oss.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.sistemservicesonline.oss.R;
import com.sistemservicesonline.oss.appcode.Comentario;
import com.sistemservicesonline.oss.appcode.Estudio;

import java.util.List;

public class ComentariosAdapter extends RecyclerView.Adapter<ComentariosAdapter.ViewHolder> {

    private ComentariosAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick (int position, String codigo);
    }

    public void setOnItemClickListener (ComentariosAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView TextViewCodigo, TextViewNombreUsuario, TextViewDescripcion;
        private RatingBar RatingBarCalificacion;

        public ViewHolder(View itemView, final ComentariosAdapter.OnItemClickListener listener) {
            super(itemView);
            TextViewCodigo = itemView.findViewById(R.id.TextViewCodigo);
            TextViewNombreUsuario = itemView.findViewById(R.id.TextViewNombreUsuario);
            RatingBarCalificacion = itemView.findViewById(R.id.RatingBarCalificacion);
            TextViewDescripcion = itemView.findViewById(R.id.TextViewDescripcion);

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

    private List<Comentario> comentarioList;

    public ComentariosAdapter(List<Comentario> comentarioList) {
        this.comentarioList = comentarioList;
    }

    @Override
    public ComentariosAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.items_comentarios, viewGroup, false);
        ComentariosAdapter.ViewHolder viewHolder = new ComentariosAdapter.ViewHolder(view, mListener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ComentariosAdapter.ViewHolder viewHolder, int i) {
        viewHolder.TextViewCodigo.setText(comentarioList.get(i).getCodigo());
        viewHolder.TextViewNombreUsuario.setText(comentarioList.get(i).getNombreResponsable());
        viewHolder.RatingBarCalificacion.setRating(comentarioList.get(i).getCalificacion());
        viewHolder.TextViewDescripcion.setText(comentarioList.get(i).getDescripcion());
    }

    @Override
    public int getItemCount() {
        return comentarioList.size();
    }
}
