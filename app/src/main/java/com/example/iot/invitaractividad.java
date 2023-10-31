package com.example.iot;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class invitaractividad extends Fragment {

    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String USERNAME = "username";

    public invitaractividad() {
        // Required empty public constructor
    }

    public static invitaractividad newInstance(String param1, String param2) {
        invitaractividad fragment = new invitaractividad();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invitaractividad, container, false);

        Button btnUnirseActividad = view.findViewById(R.id.btnUnirseActividad);
        EditText codigoinvitarTextView = view.findViewById(R.id.codigoinvitarTextView);

        btnUnirseActividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codigoIngresado = codigoinvitarTextView.getText().toString();

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference actividadesRef = db.collection("actividades");

                actividadesRef.whereEqualTo("codigo", codigoIngresado).get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot snapshots) {
                                if (!snapshots.isEmpty()) {
                                    for (QueryDocumentSnapshot snapshot : snapshots) {
                                        String actividadId = snapshot.getId();
                                        verificarYAgregarUsuario(actividadId);
                                        return;
                                    }
                                    mostrarMensaje("Código de actividad inválido");
                                } else {
                                    mostrarMensaje("Código de actividad inválido");
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                mostrarMensaje("Error al buscar la actividad");
                            }
                        });
            }
        });

        return view;
    }

    private void verificarYAgregarUsuario(String actividadId) {
        String usuarioId = sharedPreferences.getString(USERNAME, "");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference actividadRef = db.collection("actividades").document(actividadId);

        actividadRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                List<String> idUsuarios = (List<String>) snapshot.get("idusuarios");

                if (idUsuarios != null && idUsuarios.contains(usuarioId)) {
                    mostrarMensaje("Ya estás unido a esta actividad");
                } else {
                    agregarUsuarioALaListaDeParticipantes(actividadId);
                    mostrarMensaje("Te has unido a la actividad exitosamente");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mostrarMensaje("Error al verificar la actividad");
            }
        });
    }

    private void agregarUsuarioALaListaDeParticipantes(String actividadId) {
        String usuarioId = sharedPreferences.getString(USERNAME, "");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference listaParticipantesRef = db.collection("actividades").document(actividadId);

        // Añadir al usuario actual a la lista de participantes
        listaParticipantesRef.update("idusuarios", FieldValue.arrayUnion(usuarioId))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void mostrarMensaje(String mensaje) {
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show();
    }
}


