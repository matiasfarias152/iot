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
        View view = mInflater.inflate(R.layout.listaactividades, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListadoActividadesAdapter.ViewHolder holder, final int position) {
        holder.bindData(mData.get(position));

        // Actualizar el TextView 'codigo' con el valor obtenido de la lista de elementos
        holder.codigo.setText(mData.get(position).getCodigo());

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
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
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
        TextView lugar, fecha, hora, nombreactividad, tipo, codigo, cantidad;
        View eliminarButton; // Botón "Eliminar"

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
            eliminarButton = itemView.findViewById(R.id.btnEliminarActividad); // Asegúrate de que el ID sea correcto
        }

        void bindData(final ListadoActividades item) {
            lugar.setText(item.getLugar());
            fecha.setText(item.getFecha());
            hora.setText(item.getHora());
            tipo.setText(item.getTipoactividad());
            nombreactividad.setText(item.getNombreactividad());
            cantidad.setText(String.valueOf(item.getCantidad()));

            Log.d("TipoValor", "Valor del tipo: " + item.getTipoactividad());
        }
    }
}

