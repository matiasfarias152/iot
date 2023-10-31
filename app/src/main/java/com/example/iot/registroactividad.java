package com.example.iot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class registroactividad extends Fragment {

    private EditText campo1;
    private EditText campo2;
    private EditText campo3;
    private EditText campo4;
    private EditText campo5;
    private Button botonRegistrar;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registroactividad, container, false);

        campo1 = view.findViewById(R.id.registrotipoactividad);
        campo2 = view.findViewById(R.id.registrofecha);
        campo3 = view.findViewById(R.id.registrohora);
        campo4 = view.findViewById(R.id.registrolugar);
        campo5 = view.findViewById(R.id.registronombreactividad);
        botonRegistrar = view.findViewById(R.id.btnRegistrarActividad);

        db = FirebaseFirestore.getInstance();

        botonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarActividad();
            }
        });

        return view;
    }

    private void registrarActividad() {
        String valorCampo1 = campo1.getText().toString().trim();
        String valorCampo2 = campo2.getText().toString().trim();
        String valorCampo3 = campo3.getText().toString().trim();
        String valorCampo4 = campo4.getText().toString().trim();
        String valorCampo5 = campo5.getText().toString().trim();

        if (valorCampo1.isEmpty() || valorCampo2.isEmpty() || valorCampo3.isEmpty() || valorCampo4.isEmpty() || valorCampo5.isEmpty()) {
            Toast.makeText(getContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            Map<String, Object> actividad = new HashMap<>();
            List<String> usuarios = new ArrayList<>();

            actividad.put("tipoactividad", valorCampo1);
            actividad.put("fecha", valorCampo2);
            actividad.put("hora", valorCampo3);
            actividad.put("lugar", valorCampo4);
            actividad.put("nombreactividad", valorCampo5);
            actividad.put("idusuarios", usuarios);

            db.collection("actividades")
                    .add(actividad)
                    .addOnSuccessListener(documentReference -> {
                        String nuevoDocumentoId = documentReference.getId();
                        String codigo = nuevoDocumentoId.substring(0, 6);

                        Map<String, Object> actividadConCodigo = new HashMap<>(actividad);
                        actividadConCodigo.put("codigo", codigo);

                        db.collection("actividades")
                                .document(documentReference.getId())
                                .set(actividadConCodigo)
                                .addOnSuccessListener(aVoid -> {
                                    campo1.setText("");
                                    campo2.setText("");
                                    campo3.setText("");
                                    campo4.setText("");
                                    campo5.setText("");

                                    Listadodeactividades fragmentPrincipal = new Listadodeactividades();
                                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                                    transaction.replace(R.id.contenedor, fragmentPrincipal);
                                    transaction.commit();
                                })
                                .addOnFailureListener(e -> {
                                    // Manejar el error al registrar la actividad
                                });
                    })
                    .addOnFailureListener(e -> {
                        // Manejar el error al registrar la actividad
                    });
        }
    }
}


