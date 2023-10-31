package com.example.iot;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.List;

public class ListadoActividadesAdapter extends RecyclerView.Adapter<ListadoActividadesAdapter.ViewHolder> {
    private List<ListadoActividades> mData;
    private LayoutInflater mInflater;
    private Context context;

    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String USERNAME = "username";

    public ListadoActividadesAdapter(List<ListadoActividades> itemList, Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;

        sharedPreferences = context.getSharedPreferences(SHARED_PREFS,Context.MODE_PRIVATE);
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public ListadoActividadesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.listaactividades, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListadoActividadesAdapter.ViewHolder holder, final int position) {
        holder.bindData(mData.get(position));

        holder.cantidad.setText("0");

        holder.codigo.setText(mData.get(position).getCodigo());

        actualizarCantidadUsuarios(holder.cantidad, mData.get(position).getNombreactividad());

        holder.eliminarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    String nombreActividadABorrar = mData.get(adapterPosition).getNombreactividad();
                    eliminarActividad(nombreActividadABorrar);
                    mData.remove(adapterPosition);
                    notifyItemRemoved(adapterPosition);
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("¿Deseas unirte a la actividad?")
                        .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                String userId = sharedPreferences.getString(USERNAME, "");

                                agregarUsuarioALaActividad(userId, mData.get(position).getNombreactividad());
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // No hacer nada o mostrar un mensaje de cancelación
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


    }

    public void setItems(List<ListadoActividades> items) {
        mData = items;
        notifyDataSetChanged();
    }



    private void eliminarActividad(String nombreActividad) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("actividades")
                .whereEqualTo("nombreactividad", nombreActividad)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        document.getReference().delete()
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(context, "Actividad eliminada", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(context, "Error al eliminar la actividad", Toast.LENGTH_SHORT).show();
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error en la consulta", Toast.LENGTH_SHORT).show();
                });
    }

    private void agregarUsuarioALaActividad(String userId, String nombreActividad) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("actividades")
                .whereEqualTo("nombreactividad", nombreActividad)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        DocumentReference docRef = document.getReference();
                        docRef.update("idusuarios", FieldValue.arrayUnion(userId))
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(context, "Te has unido a la actividad", Toast.LENGTH_SHORT).show();

                                    setItems(mData);


                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(context, "Error al unirse a la actividad", Toast.LENGTH_SHORT).show();
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error en la consulta", Toast.LENGTH_SHORT).show();
                });
    }
    private void actualizarCantidadUsuarios(TextView cantidadTextView, String nombreActividad) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("actividades")
                .whereEqualTo("nombreactividad", nombreActividad)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        // Obtener la lista de usuarios y actualizar el TextView "cantidad"
                        List<String> usuarios = (List<String>) document.get("idusuarios");
                        if (usuarios != null) {
                            int cantidadUsuarios = usuarios.size();
                            cantidadTextView.setText(String.valueOf(cantidadUsuarios));
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    // Manejar errores
                });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iconImage;
        TextView lugar, fecha, hora, nombreactividad, tipo, codigo, cantidad;
        View eliminarButton;

        ViewHolder(View itemView) {
            super(itemView);
            iconImage = itemView.findViewById(R.id.IconImageView);
            lugar = itemView.findViewById(R.id.lugarTextView);
            fecha = itemView.findViewById(R.id.fechaTextView);
            hora = itemView.findViewById(R.id.horaTextView);
            tipo = itemView.findViewById(R.id.tipoTextViewActividades);
            codigo = itemView.findViewById(R.id.codigoTextView);
            cantidad = itemView.findViewById(R.id.cantidadTextView);
            nombreactividad = itemView.findViewById(R.id.nombreactividadTextView);
            eliminarButton = itemView.findViewById(R.id.btnEliminarActividad);
        }

        void bindData(final ListadoActividades item) {
            lugar.setText(item.getLugar());
            fecha.setText(item.getFecha());
            hora.setText(item.getHora());
            tipo.setText(item.getTipoactividad());
            nombreactividad.setText(item.getNombreactividad());
            cantidad.setText(String.valueOf(item.getCantidad()));
        }
    }
}



