package com.example.iot;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListComunidadesAdapter extends RecyclerView.Adapter<ListComunidadesAdapter.ViewHolder> {

    private List<ListComunidades> mData;
    private LayoutInflater mInflater;
    private Context context;

    public ListComunidadesAdapter(List<ListComunidades> itemList, Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.listacomunidades, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(mData.get(position));
        int adapterPosition = holder.getAdapterPosition();

        holder.entrarchatButton.setOnClickListener(view -> {
            String nombreComunidad = mData.get(position).getNombrecomunidad();
            String nombreUsuario = obtenerNombreUsuario(); // Obtener el nombre del usuario actual

            redirigirOUnirse(nombreComunidad, nombreUsuario);
        });

        holder.eliminarButton.setOnClickListener(view -> {
            String nombreComunidad = mData.get(position).getNombrecomunidad();
            eliminarComunidad(nombreComunidad,position);

        });

        holder.entrarlistado.setOnClickListener(view -> {
            // Aquí redirige al fragmento deseado
            String nombreComunidad = mData.get(position).getNombrecomunidad();
            irAListadoIntegrantes(nombreComunidad);
        });
    }
    private String obtenerNombreUsuario() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("username", "DEFAULT_USERNAME_SI_NO_HAY_VALOR");
    }

    private void irAListadoIntegrantes(String nombreComunidad) {
        // Obtener la lista de idusuarios correspondiente a la comunidad seleccionada
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("comunidades")
                .whereEqualTo("nombrecomunidad", nombreComunidad)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            List<String> idUsuarios = (List<String>) document.get("idusuarios");

                            // Crear e iniciar el fragmento con los datos obtenidos
                            Fragment listadoIntegrantesFragment = new listadointegrantes();
                            Bundle args = new Bundle();
                            args.putStringArrayList("idUsuarios", new ArrayList<>(idUsuarios));
                            listadoIntegrantesFragment.setArguments(args);

                            FragmentTransaction transaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.contenedor, listadoIntegrantesFragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }
                    }
                });
    }

    private void redirigirOUnirse(String nombreComunidad, String nombreUsuario) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("comunidades")
                .whereEqualTo("nombrecomunidad", nombreComunidad)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            List<String> idUsuarios = (List<String>) document.get("idusuarios");
                            if (idUsuarios != null && idUsuarios.contains(nombreUsuario)) {
                                redirigirAChat(nombreComunidad);
                            } else {
                                unirseAComunidad(document.getId(), nombreComunidad, nombreUsuario);
                            }
                        }
                    }
                });
    }

    private void unirseAComunidad(String comunidadId, String nombreComunidad, String nombreUsuario) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("comunidades")
                .document(comunidadId)
                .update("idusuarios", FieldValue.arrayUnion(nombreUsuario))
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Te has unido a la comunidad " + nombreComunidad, Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error al unirse a la comunidad " + nombreComunidad, Toast.LENGTH_SHORT).show();
                });
    }

    private void redirigirAChat(String nombreComunidad) {
        Fragment chatFragment = chatcomunidades.newInstance(nombreComunidad);
        FragmentTransaction transaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contenedor, chatFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void eliminarComunidad(String nombreComunidad, int position) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("comunidades")
                .whereEqualTo("nombrecomunidad", nombreComunidad)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        document.getReference().delete()
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(context, "Comunidad eliminada", Toast.LENGTH_SHORT).show();

                                    // Verificar si la posición a eliminar es válida
                                    if (position != RecyclerView.NO_POSITION && position < mData.size()) {
                                        mData.remove(position);
                                        notifyItemRemoved(position);
                                    }

                                    // Actualizar el conjunto de datos una sola vez después de completar la eliminación
                                    setItems(mData);
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(context, "Error al eliminar la comunidad", Toast.LENGTH_SHORT).show();
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error en la consulta", Toast.LENGTH_SHORT).show();
                });
    }
    public void setItems(List<ListComunidades> items) {
        mData = items;
        notifyDataSetChanged(); // Actualizar la vista con los nuevos datos
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iconImage;
        TextView nombrecomunidad, tipoactividadcomunidad, descripcioncomunidad;
        View entrarchatButton; // Botón "EntrarChat"
        View eliminarButton; // Botón "Eliminar"

        View entrarlistado; //Botón "Entrar listado"

        ViewHolder(View itemView) {
            super(itemView);
            iconImage = itemView.findViewById(R.id.IconImageViewCom);
            nombrecomunidad = itemView.findViewById(R.id.nombrecomunidadTextView);
            tipoactividadcomunidad = itemView.findViewById(R.id.tipocomunidadTextView);
            descripcioncomunidad = itemView.findViewById(R.id.descripcioncomunidadTextView);
            entrarchatButton = itemView.findViewById(R.id.btnEntrarChat);
            eliminarButton = itemView.findViewById(R.id.btnEliminarComunidad);
            entrarlistado = itemView.findViewById(R.id.btnListaintegrantes);
        }

        void bindData(final ListComunidades item) {
            nombrecomunidad.setText(item.getNombrecomunidad());
            tipoactividadcomunidad.setText(item.getTipoactividadcomunidad());
            descripcioncomunidad.setText(item.getDescripcioncomunidad());
        }
    }
}
