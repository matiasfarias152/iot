package com.example.iot;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistroComunidad extends Fragment {

    private EditText campo1;
    private EditText campo2;
    private EditText campo3;
    private Button botonRegistrar;

    private FirebaseFirestore db;

    public RegistroComunidad() {
        // Required empty public constructor
    }

    public static RegistroComunidad newInstance(String param1, String param2) {
        RegistroComunidad fragment = new RegistroComunidad();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registro_comunidad, container, false);

        campo1 = view.findViewById(R.id.registroNombrecom);
        campo2 = view.findViewById(R.id.registroTipoactividadcom);
        campo3 = view.findViewById(R.id.registroDescripcioncom);
        botonRegistrar = view.findViewById(R.id.botonComunidadRegistrar);

        db = FirebaseFirestore.getInstance();

        botonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarComunidad();
            }
        });

        return view;
    }

    private void registrarComunidad() {
        String valorCampo1 = campo1.getText().toString().trim();
        String valorCampo2 = campo2.getText().toString().trim();
        String valorCampo3 = campo3.getText().toString().trim();

        if (valorCampo1.isEmpty() || valorCampo2.isEmpty() || valorCampo3.isEmpty()) {
            // Mostrar un mensaje de error o notificar al usuario que los campos están vacíos
            Toast.makeText(getContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            // Si los campos no están vacíos, proceder con el registro
            List<String> usuarios = new ArrayList<>();

            Map<String, Object> comunidad = new HashMap<>();
            comunidad.put("nombrecomunidad", valorCampo1);
            comunidad.put("tipoactividadcomunidad", valorCampo2);
            comunidad.put("descripcioncomunidad", valorCampo3);
            comunidad.put("idusuarios", usuarios);

            db.collection("comunidades")
                    .add(comunidad)
                    .addOnSuccessListener(documentReference -> {
                        campo1.setText("");
                        campo2.setText("");
                        campo3.setText("");

                        ListaComunidades fragmentPrincipal = new ListaComunidades(); // Reemplaza con el nombre de tu Fragment principal
                        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                        transaction.replace(R.id.contenedor, fragmentPrincipal); // Reemplaza "R.id.contenedor_fragment" con el ID de tu contenedor de fragmentos
                        transaction.commit();
                    })
                    .addOnFailureListener(e -> {
                        // Manejar el error al registrar la actividad
                    });
        }
    }
}