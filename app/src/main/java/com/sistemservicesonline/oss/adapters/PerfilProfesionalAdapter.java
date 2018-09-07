package com.sistemservicesonline.oss.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sistemservicesonline.oss.R;
import com.sistemservicesonline.oss.appcode.PerfilProfesional;

import java.util.List;

public class PerfilProfesionalAdapter extends RecyclerView.Adapter<PerfilProfesionalAdapter.ViewHolder> {

    private PerfilProfesionalAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick (int position, String codigo);
    }

    public void setOnItemClickListener (PerfilProfesionalAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView TextViewCodigo, TextViewTitulo, TextViewDescripcion, TextViewPeriodo;

        public ViewHolder(View itemView, final PerfilProfesionalAdapter.OnItemClickListener listener) {
            super(itemView);
            TextViewCodigo = itemView.findViewById(R.id.TextViewCodigo);
            TextViewTitulo = itemView.findViewById(R.id.TextViewTitulo);
            TextViewDescripcion = itemView.findViewById(R.id.TextViewDescripcion);
            TextViewPeriodo = itemView.findViewById(R.id.TextViewPeriodo);

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

    private List<PerfilProfesional> perfilProfesionalList;

    public PerfilProfesionalAdapter(List<PerfilProfesional> perfilProfesionalList) {
        this.perfilProfesionalList = perfilProfesionalList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.items_datosusuario, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view, mListener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.TextViewCodigo.setText(perfilProfesionalList.get(i).getCodigo());
        viewHolder.TextViewTitulo.setText(perfilProfesionalList.get(i).getTitulo());
        viewHolder.TextViewDescripcion.setText(perfilProfesionalList.get(i).getDescripcion());
        viewHolder.TextViewPeriodo.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return perfilProfesionalList.size();
    }
}
