package com.example.iot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Listadodeactividades extends Fragment {

    private List<ListadoActividades> elements;
    private RecyclerView recyclerView;
    private ListadoActividadesAdapter listAdapter;

    public Listadodeactividades() {
        // Constructor vacío
    }

    public void init() {
        elements = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference actividadesRef = db.collection("actividades");

        actividadesRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (!queryDocumentSnapshots.isEmpty()) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    ListadoActividades actividad = documentSnapshot.toObject(ListadoActividades.class);
                    elements.add(actividad);
                }

                for (ListadoActividades actividad : elements) {
                    obtenerCodigoDesdeFirebase(actividad);
                }

                listAdapter = new ListadoActividadesAdapter(elements, getContext());
                recyclerView.setAdapter(listAdapter);
                listAdapter.setItems(elements);
            } else {
                // No se encontraron datos
            }
        });
    }

    private void obtenerCodigoDesdeFirebase(ListadoActividades actividad) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("actividades").document(actividad.getNombreactividad());

        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String codigo = documentSnapshot.getString("codigo");
                actividad.setCodigo(codigo);
                listAdapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(e -> {
            // Error al obtener el documento
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_listadodeactividades, container, false);
        recyclerView = rootView.findViewById(R.id.listRecyclerView);

        // Configura el RecyclerView
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicializa y configura el adaptador
        init();

        // Agrega el botón flotante
        FloatingActionButton fab = rootView.findViewById(R.id.btnCrearnuevaactividad);

        fab.setOnClickListener(view -> {
            Fragment nuevoFragmento = new registroactividad();
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.contenedor, nuevoFragmento);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return rootView;
    }
}
