package com.example.iot;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class HolderMensaje extends RecyclerView.ViewHolder{

    private TextView nombrechat;
    private TextView mensaje;
    private TextView hora;
    private CircleImageView fotoMensaje;
    public HolderMensaje(@NonNull View itemView) {
        super(itemView);

        nombrechat = (TextView) itemView.findViewById(R.id.nombreMensaje);
        mensaje = (TextView) itemView.findViewById(R.id.mensajeMensaje);
        hora = (TextView) itemView.findViewById(R.id.horaMensaje);
        fotoMensaje = (CircleImageView) itemView.findViewById(R.id.fotoPerfilMensaje);
    }

    public TextView getNombrechat() {
        return nombrechat;
    }

    public void setNombrechat(TextView nombrechat) {
        this.nombrechat = nombrechat;
    }

    public TextView getMensaje() {
        return mensaje;
    }

    public void setMensaje(TextView mensaje) {
        this.mensaje = mensaje;
    }

    public TextView getHora() {
        return hora;
    }

    public void setHora(TextView hora) {
        this.hora = hora;
    }

    public CircleImageView getFotoMensaje() {
        return fotoMensaje;
    }

    public void setFotoMensaje(CircleImageView fotoMensaje) {
        this.fotoMensaje = fotoMensaje;
    }
}
