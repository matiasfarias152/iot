package com.example.iot;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

public class listaintegrantesadapter extends RecyclerView.Adapter<listaintegrantesadapter.ViewHolder> {

    private List<String> mData;
    private LayoutInflater mInflater;
    private Context context;

    public listaintegrantesadapter(List<String> itemList, Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.listaintegrantes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String userName = mData.get(position);
        holder.nombre.setText(userName);

        holder.btnAnadirAmigo.setOnClickListener(v -> {
            String usuarioClickeado = mData.get(position);

            // Obtener el usuario logeado desde SharedPreferences
            SharedPreferences sharedPreferences = context.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
            String nombreUsuarioActual = sharedPreferences.getString("username", "nombreUsuarioPorDefecto");

            if (!nombreUsuarioActual.equals("nombreUsuarioPorDefecto")) {
                // Verificar y agregar al usuario clickeado como amigo del usuario logeado
                agregarAmigo(nombreUsuarioActual, usuarioClickeado);
            } else {
                Toast.makeText(context, "Usuario no logeado", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void agregarAmigo(String usuarioActual, String usuarioClickeado) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usuariosRef = db.collection("usuarios");

        usuariosRef.whereEqualTo("usuario", usuarioActual)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        List<String> idAmigos = (List<String>) document.get("idamigos");

                        if (idAmigos != null && idAmigos.contains(usuarioClickeado)) {
                            // El usuario clickeado ya está en la lista de amigos
                            Toast.makeText(context, "El usuario ya está en la lista de amigos", Toast.LENGTH_SHORT).show();
                        } else {
                            // Agregar al usuario clickeado como amigo del usuario actual
                            usuariosRef.document(document.getId()).update("idamigos", FieldValue.arrayUnion(usuarioClickeado))
                                    .addOnSuccessListener(aVoid -> {
                                        // Éxito al agregar al usuario clickeado como amigo
                                        Toast.makeText(context, "Se añadió a " + usuarioClickeado + " como amigo", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        // Error al agregar al usuario clickeado como amigo
                                        Toast.makeText(context, "Error al añadir amigo", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    }
                });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre;
        Button btnAnadirAmigo;

        ViewHolder(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombreintegranteTextView);
            btnAnadirAmigo = itemView.findViewById(R.id.btnAnadiramigo);
        }
    }
}
