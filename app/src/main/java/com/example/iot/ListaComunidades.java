package com.example.iot;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class ListaComunidades extends Fragment {

    private List<ListComunidades> elements;
    private RecyclerView recyclerView;
    private ListComunidadesAdapter listAdapter;

    public ListaComunidades(){

    }

    public void init(){
        elements = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference comunidadesRef = db.collection("comunidades");

        comunidadesRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()){
                    for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        ListComunidades comunidad = documentSnapshot.toObject(ListComunidades.class);
                        elements.add(comunidad);
                    }
                    listAdapter = new ListComunidadesAdapter(elements,getContext());

                    recyclerView.setAdapter(listAdapter);
                    listAdapter.setItems(elements);

                }else{
                    //No se encontraron datos
                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_lista_comunidades, container, false);
        recyclerView = rootView.findViewById(R.id.listComunidades);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        init();
        return rootView;
    }

}

