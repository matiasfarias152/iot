package com.example.iot;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.List;


public class ListaAmigos extends Fragment {

    private List<ListAmigos> elements;
    private RecyclerView recyclerView;
    private ListAmigosAdapter listAdapter;

    public ListaAmigos(){

    }

    public void init(){
        elements = new ArrayList<>();
        elements.add(new ListAmigos("Pedro", "IOT"));
        elements.add(new ListAmigos("Matias", "Holaa"));
        elements.add(new ListAmigos("Pancho", "A"));
        elements.add(new ListAmigos("Gabriel", "Apple"));
        elements.add(new ListAmigos("Luis", "profe"));
        elements.add(new ListAmigos("Alejandro", "Buenos dias"));
        elements.add(new ListAmigos("Juan", "aburrido"));
        elements.add(new ListAmigos("Francisca", "zzz"));
        elements.add(new ListAmigos("Victor", "Informes"));

        listAdapter = new ListAmigosAdapter(elements,getContext());
        recyclerView.setAdapter(listAdapter);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lista_amigos, container, false);
        recyclerView = rootView.findViewById(R.id.listAmigos);


        // Configura el RecyclerView
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicializa y configura el adaptador
        init();

        return rootView;
    }
}