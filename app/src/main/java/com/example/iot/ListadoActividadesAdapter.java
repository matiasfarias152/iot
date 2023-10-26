package com.example.iot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

public class ListadoActividadesAdapter extends RecyclerView.Adapter<ListadoActividadesAdapter.ViewHolder> {
    private List<ListadoActividades> mData;
    private LayoutInflater mInflater;
    private Context context;

    public ListadoActividadesAdapter(List<ListadoActividades> itemList, Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public ListadoActividadesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.listaactividades, null);
        return new ListadoActividadesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListadoActividadesAdapter.ViewHolder holder, final int position) {
        holder.bindData(mData.get(position));

        // Configuración del OnClickListener para el botón "Eliminar"
        holder.eliminarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    // Obtener el nombre de la actividad del elemento a eliminar
                    String nombreActividadABorrar = mData.get(adapterPosition).getNombreactividad();

                    // Eliminar la actividad de Firestore basada en el nombre de la actividad
                    eliminarActividad(nombreActividadABorrar);

                    // Eliminar el elemento del RecyclerView
                    mData.remove(adapterPosition);
                    notifyItemRemoved(adapterPosition);
                }
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
                    for (com.google.firebase.firestore.QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        document.getReference().delete()
                                .addOnSuccessListener(aVoid -> {
                                    // Documento eliminado exitosamente
                                    Toast.makeText(context, "Actividad eliminada", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    // Error al eliminar el documento
                                    Toast.makeText(context, "Error al eliminar la actividad", Toast.LENGTH_SHORT).show();
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    // Error en la consulta
                    Toast.makeText(context, "Error en la consulta", Toast.LENGTH_SHORT).show();
                });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iconImage;
        TextView lugar, fecha, hora, nombreactividad,tipo;
        View eliminarButton; // Botón "Eliminar"

        ViewHolder(View itemView) {
            super(itemView);
            iconImage = itemView.findViewById(R.id.IconImageView);
            lugar = itemView.findViewById(R.id.lugarTextView);
            fecha = itemView.findViewById(R.id.fechaTextView);
            hora = itemView.findViewById(R.id.horaTextView);
            tipo = itemView.findViewById(R.id.tipoTextView);
            nombreactividad = itemView.findViewById(R.id.nombreactividadTextView);
            eliminarButton = itemView.findViewById(R.id.btnEliminarActividad); // Asegúrate de que el ID sea correcto
        }

        void bindData(final ListadoActividades item) {
            lugar.setText(item.getLugar());
            fecha.setText(item.getFecha());
            hora.setText(item.getHora());
            tipo.setText(item.getTipo());
            nombreactividad.setText(item.getNombreactividad());
        }
    }
}
