package com.sistemservicesonline.oss.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sistemservicesonline.oss.R;
import com.sistemservicesonline.oss.appcode.Estudio;
import com.sistemservicesonline.oss.appcode.ExperienciaLaboral;

import java.util.List;

public class EstudioAdapter extends RecyclerView.Adapter<EstudioAdapter.ViewHolder> {

    private EstudioAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick (int position, String codigo);
    }

    public void setOnItemClickListener (EstudioAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView TextViewCodigo, TextViewTitulo, TextViewDescripcion, TextViewPeriodo;

        public ViewHolder(View itemView, final EstudioAdapter.OnItemClickListener listener) {
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

    private List<Estudio> estudioList;

    public EstudioAdapter(List<Estudio> estudioList) {
        this.estudioList = estudioList;
    }

    @Override
    public EstudioAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.items_datosusuario, viewGroup, false);
        EstudioAdapter.ViewHolder viewHolder = new EstudioAdapter.ViewHolder(view, mListener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.TextViewCodigo.setText(estudioList.get(i).getCodigo());
        viewHolder.TextViewTitulo.setText(estudioList.get(i).getInstitucion());
        viewHolder.TextViewDescripcion.setText(estudioList.get(i).getDescripcion());
        viewHolder.TextViewPeriodo.setText(estudioList.get(i).getFechaInicial() + " - " + estudioList.get(i).getFechaFinal());
    }

    @Override
    public int getItemCount() {
        return estudioList.size();
    }
}
