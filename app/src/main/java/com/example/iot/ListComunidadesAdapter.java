package com.example.iot;

import android.content.Context;
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
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

public class ListComunidadesAdapter extends RecyclerView.Adapter<ListComunidadesAdapter.ViewHolder> {

    private List<ListComunidades> mData;
    private LayoutInflater mInflater;
    private Context context;

    public ListComunidadesAdapter(List<ListComunidades> itemList, Context context){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
    }

    @Override
    public int getItemCount() { return mData.size(); }

    @Override
    public ListComunidadesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.listacomunidades, null);
        return new ListComunidadesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListComunidadesAdapter.ViewHolder holder, final int position){
        holder.bindData(mData.get(position));

        // Configuración del OnClickListener para el botón "Eliminar"
        holder.eliminarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    // Obtener el nombre de la comunidad del elemento a eliminar
                    String nombreComunidadABorrar = mData.get(adapterPosition).getNombrecomunidad();

                    // Eliminar el elemento de Firestore
                    eliminarComunidad(nombreComunidadABorrar);

                    // Eliminar el elemento del RecyclerView
                    mData.remove(adapterPosition);
                    notifyItemRemoved(adapterPosition);
                }
            }
        });

        // Configuración del OnClickListener para el botón "Entrar al Chat"
        holder.entrarchatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Realiza la transición al fragmento deseado aquí, por ejemplo:
                chatcomunidades fragment = new chatcomunidades(); // Reemplaza "ChatFragment" con tu fragmento
                FragmentTransaction transaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedor, fragment); // Reemplaza "R.id.fragmentContainer" con el ID de tu contenedor de fragmentos
                transaction.addToBackStack(null); // Opcional: Agregar la transacción a la pila de retroceso
                transaction.commit();
            }
        });
    }



    public void setItems(List<ListComunidades> items) {
        mData = items;
    }

    private void eliminarComunidad(String nombreComunidad) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("comunidades")
                .whereEqualTo("nombrecomunidad", nombreComunidad)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (com.google.firebase.firestore.QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        document.getReference().delete()
                                .addOnSuccessListener(aVoid -> {
                                    // Documento eliminado exitosamente
                                    Toast.makeText(context, "Comunidad eliminada", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    // Error al eliminar el documento
                                    Toast.makeText(context, "Error al eliminar la comunidad", Toast.LENGTH_SHORT).show();
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
        TextView nombrecomunidad, tipoactividadcomunidad, descripcioncomunidad;
        View eliminarButton; // Botón "Eliminar"

        View entrarchatButton; // Botón "EntrarChat"



        ViewHolder(View itemView) {
            super(itemView);
            iconImage = itemView.findViewById(R.id.IconImageViewCom);
            nombrecomunidad = itemView.findViewById(R.id.nombrecomunidadTextView);
            tipoactividadcomunidad = itemView.findViewById(R.id.tipocomunidadTextView);
            descripcioncomunidad = itemView.findViewById(R.id.descripcioncomunidadTextView);
            eliminarButton = itemView.findViewById(R.id.btnEliminarComunidad); // Asegúrate de que el ID sea correcto
            entrarchatButton = itemView.findViewById(R.id.btnEntrarChat);
        }

        void bindData(final ListComunidades item) {
            nombrecomunidad.setText(item.getNombrecomunidad());
            tipoactividadcomunidad.setText(item.getTipoactividadcomunidad());
            descripcioncomunidad.setText(item.getDescripcioncomunidad());
        }
    }
}



