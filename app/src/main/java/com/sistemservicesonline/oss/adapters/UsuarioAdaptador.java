package com.sistemservicesonline.oss.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.sistemservicesonline.oss.appcode.Usuario;
import com.sistemservicesonline.oss.R;

import java.util.List;

public class UsuarioAdaptador extends RecyclerView.Adapter<UsuarioAdaptador.ViewHolder> {

    private UsuarioAdaptador.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick (int position);
    }

    public void setOnItemClickListener (UsuarioAdaptador.OnItemClickListener listener) {
        mListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private EditText EditTextTitulo, EditTextDescripcion;

        public ViewHolder(View itemView, final UsuarioAdaptador.OnItemClickListener listener) {
            super(itemView);
            EditTextTitulo = itemView.findViewById(R.id.EditTextCargoTitulo);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View v){
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public List<Usuario> LstUsuarios;

    public UsuarioAdaptador(List<Usuario> lstUsuarios) {
        LstUsuarios = lstUsuarios;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_usuarios, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view, mListener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        //viewHolder.TextViewNombreUsuario.setText(LstUsuarios.get(i).getNombreCompleto());
    }

    @Override
    public int getItemCount() {
        return LstUsuarios.size();
    }
}
