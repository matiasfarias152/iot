package com.example.iot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
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

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Consume el evento táctil para evitar que se propague más allá del Fragment
                return true;
            }
        });

        return view;
    }

    private void registrarActividad() {
        String valorCampo1 = campo1.getText().toString();
        String valorCampo2 = campo2.getText().toString();
        String valorCampo3 = campo3.getText().toString();
        String valorCampo4 = campo4.getText().toString();
        String valorCampo5 = campo5.getText().toString();

        // Crear un mapa con los datos a registrar
        Map<String, Object> actividad = new HashMap<>();
        actividad.put("tipoactividad", valorCampo1);
        actividad.put("fecha", valorCampo2);
        actividad.put("hora", valorCampo3);
        actividad.put("lugar", valorCampo4);
        actividad.put("nombreactividad",valorCampo5);

        // Agregar la actividad a Firestore
        db.collection("actividades")
                .add(actividad)
                .addOnSuccessListener(documentReference -> {
                    // Éxito al registrar la actividad
                    campo1.setText("");
                    campo2.setText("");
                    campo3.setText("");
                    campo4.setText("");
                    campo5.setText("");

                    Listadodeactividades fragmentPrincipal = new Listadodeactividades (); // Reemplaza con el nombre de tu Fragment principal
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    transaction.replace(R.id.contenedor, fragmentPrincipal); // Reemplaza "R.id.contenedor_fragment" con el ID de tu contenedor de fragmentos
                    transaction.commit();
                })
                .addOnFailureListener(e -> {
                    // Error al registrar la actividad
                });
    }


}