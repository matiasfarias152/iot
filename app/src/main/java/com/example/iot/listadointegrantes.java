package com.example.iot;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class listadointegrantes extends Fragment {

    private RecyclerView recyclerView;
    private listaintegrantesadapter listAdapter;
    private List<String> userList;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public listadointegrantes() {
        // Required empty public constructor
    }

    public static listadointegrantes newInstance(String param1, String param2) {
        listadointegrantes fragment = new listadointegrantes();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_listadointegrantes, container, false);

        recyclerView = rootView.findViewById(R.id.listIntegrantes);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (getArguments() != null) {
            userList = getArguments().getStringArrayList("idUsuarios");
        }

        if (userList != null && !userList.isEmpty()) {
            listAdapter = new listaintegrantesadapter(userList, getContext());
            recyclerView.setAdapter(listAdapter);
        }

        return rootView;
    }
}