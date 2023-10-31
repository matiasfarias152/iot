package com.example.iot;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class ListaAmigos extends Fragment {

    private List<ListAmigos> elements;
    private RecyclerView recyclerView;
    private ListAmigosAdapter listAdapter;
    private FirebaseFirestore db;

    public ListaAmigos() {
        // Constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lista_amigos, container, false);
        recyclerView = rootView.findViewById(R.id.rvAmigos);

        db = FirebaseFirestore.getInstance();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        listAdapter = new ListAmigosAdapter(new ArrayList<>(), getContext());
        recyclerView.setAdapter(listAdapter);

        listAdapter.setOnItemClickListener(position -> eliminarAmigo(position));

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarListaAmigos();
    }

    private void cargarListaAmigos() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        String nombreUsuario = sharedPreferences.getString("username", "nombreUsuarioPorDefecto");

        if (!nombreUsuario.equals("nombreUsuarioPorDefecto")) {
            elements = new ArrayList<>();
            CollectionReference usuariosRef = db.collection("usuarios");

            usuariosRef.whereEqualTo("usuario", nombreUsuario)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            List<String> idAmigos = (List<String>) document.get("idamigos");

                            if (idAmigos != null) {
                                for (String amigo : idAmigos) {
                                    elements.add(new ListAmigos(amigo, "Estado de amigo"));
                                }
                                listAdapter.setItems(elements);
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure to get the list of friends
                    });
        }
    }

    private void eliminarAmigo(int position) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        String nombreUsuario = sharedPreferences.getString("username", "nombreUsuarioPorDefecto");

        if (!nombreUsuario.equals("nombreUsuarioPorDefecto")) {
            if (position >= 0 && position < elements.size()) {
                ListAmigos amigoAEliminar = elements.get(position);

                db.collection("usuarios")
                        .whereEqualTo("usuario", nombreUsuario)
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                List<String> idAmigos = (List<String>) document.get("idamigos");

                                if (idAmigos != null && idAmigos.contains(amigoAEliminar.getNombre())) {
                                    idAmigos.remove(amigoAEliminar.getNombre());

                                    document.getReference().update("idamigos", idAmigos)
                                            .addOnSuccessListener(aVoid -> {
                                                elements.remove(amigoAEliminar);
                                                listAdapter.setItems(elements);
                                                listAdapter.notifyDataSetChanged();

                                                // Verificar si la lista está vacía después de eliminar al amigo
                                                if (elements.isEmpty()) {
                                                    // Aquí puedes agregar una lógica adicional si la lista está vacía
                                                    // Por ejemplo, mostrar un mensaje de lista vacía o realizar alguna acción adicional.
                                                }
                                            })
                                            .addOnFailureListener(e -> {
                                                // Handle failure to delete the friend in Firestore
                                            });
                                }
                            }
                        })
                        .addOnFailureListener(e -> {
                            // Handle failure to get the list of friends from Firestore
                        });
            }
        }
    }
}
