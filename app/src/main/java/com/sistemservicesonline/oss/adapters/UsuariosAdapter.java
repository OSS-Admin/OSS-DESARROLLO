package com.sistemservicesonline.oss.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sistemservicesonline.oss.R;
import com.sistemservicesonline.oss.appcode.Usuario;

import java.util.List;

public class UsuariosAdapter extends RecyclerView.Adapter<UsuariosAdapter.ViewHolder> {
    private UsuariosAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick (int position, String codigo);
    }

    public void setOnItemClickListener (UsuariosAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView TextViewToken, TextViewNombreUsuario, TextViewPerfilProfesional;

        public ViewHolder(View itemView, final UsuariosAdapter.OnItemClickListener listener) {
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

    private List<Usuario> usuarioList;

    public UsuariosAdapter(List<Usuario> usuarioList) {
        this.usuarioList = usuarioList;
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
    }

    @Override
    public int getItemCount() {
        return usuarioList.size();
    }
}
