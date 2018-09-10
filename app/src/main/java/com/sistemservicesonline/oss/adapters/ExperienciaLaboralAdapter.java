package com.sistemservicesonline.oss.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sistemservicesonline.oss.R;
import com.sistemservicesonline.oss.appcode.ExperienciaLaboral;

import java.util.List;

public class ExperienciaLaboralAdapter extends RecyclerView.Adapter<ExperienciaLaboralAdapter.ViewHolder> {

    private ExperienciaLaboralAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick (int position, String codigo);
    }

    public void setOnItemClickListener (ExperienciaLaboralAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView TextViewCodigo, TextViewTitulo, TextViewDescripcion, TextViewPeriodo;

        public ViewHolder(View itemView, final ExperienciaLaboralAdapter.OnItemClickListener listener) {
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

    private List<ExperienciaLaboral>  experienciaLaboralList;

    public ExperienciaLaboralAdapter(List<ExperienciaLaboral> experienciaLaboralList) {
        this.experienciaLaboralList = experienciaLaboralList;
    }

    @Override
    public ExperienciaLaboralAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.items_datosusuario, viewGroup, false);
        ExperienciaLaboralAdapter.ViewHolder viewHolder = new ExperienciaLaboralAdapter.ViewHolder(view, mListener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.TextViewCodigo.setText(experienciaLaboralList.get(i).getCodigo());
        viewHolder.TextViewTitulo.setText(experienciaLaboralList.get(i).getCargo());
        viewHolder.TextViewDescripcion.setText(experienciaLaboralList.get(i).getDescripcion());
        viewHolder.TextViewPeriodo.setText(experienciaLaboralList.get(i).getFechaInicial() + " - " + experienciaLaboralList.get(i).getFechaFinal());
    }

    @Override
    public int getItemCount() {
        return experienciaLaboralList.size();
    }
}
