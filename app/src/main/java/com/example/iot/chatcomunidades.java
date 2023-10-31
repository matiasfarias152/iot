package com.example.iot;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import de.hdodenhof.circleimageview.CircleImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class chatcomunidades extends Fragment {

    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String USERNAME = "username";
    private TextView nombreusuariochat;
    private CircleImageView fotoPerfil;
    private TextView nombrechat;
    private RecyclerView rvMensajes;
    private EditText txtMensaje;
    private Button btnEnviarMensaje;
    private AdapterMensajes adapter;
    private FirebaseFirestore db;
    private CollectionReference mensajesCollection;

    public chatcomunidades() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chatcomunidades, container, false);
        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String nombreUsuario = sharedPreferences.getString(USERNAME, "");

        Bundle args = getArguments();
        String nombreComunidad = args != null ? args.getString("nombreComunidad") : "";

        fotoPerfil = view.findViewById(R.id.fotoPerfil);
        nombrechat = view.findViewById(R.id.nombrechat);
        rvMensajes = view.findViewById(R.id.rvMensajes);
        txtMensaje = view.findViewById(R.id.txtMensaje);
        btnEnviarMensaje = view.findViewById(R.id.btnEnviarMensaje);

        nombrechat.setText(nombreComunidad);

        db = FirebaseFirestore.getInstance();
        CollectionReference chatCollection = db.collection("chat");

        if (!nombreComunidad.isEmpty()) {
            DocumentReference comunidadRef = chatCollection.document(nombreComunidad);
            mensajesCollection = comunidadRef.collection("mensajes");

            adapter = new AdapterMensajes(getContext());
            LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
            rvMensajes.setLayoutManager(layoutManager);
            rvMensajes.setAdapter(adapter);

            btnEnviarMensaje.setOnClickListener(v -> {
                String mensajeTexto = txtMensaje.getText().toString();
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
                String hora = dateFormat.format(calendar.getTime()); // Cambia esto para obtener la hora real
                agregarMensajeFirestore(mensajeTexto, nombreUsuario, hora, nombreComunidad);
                txtMensaje.setText("");
            });

            adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    super.onItemRangeInserted(positionStart, itemCount);
                    setScrollbar();
                }
            });

            mensajesCollection.addSnapshotListener((queryDocumentSnapshots, e) -> {
                if (e != null) {
                    Log.e("Firestore", "Error al escuchar cambios en la colección de mensajes", e);
                    return;
                }

                for (QueryDocumentSnapshot document : Objects.requireNonNull(queryDocumentSnapshots)) {
                    Mensaje mensaje = document.toObject(Mensaje.class);
                    String mensajeId = document.getId();

                    mensaje.setIdDocumento(mensajeId);

                    if(!adapter.containsMensajeWithId(mensajeId)){
                        adapter.addMensaje(mensaje);
                    }

                }
                setScrollbar();
            });
        } else {
            Log.e("Firestore", "Nombre de comunidad no válido");
        }

        return view;
    }

    private void agregarMensajeFirestore(String mensajeTexto, String nombreUsuario, String hora, String nombreComunidad) {
        Mensaje mensaje = new Mensaje(mensajeTexto, nombreUsuario, "", "1", hora, nombreComunidad);

        mensajesCollection.add(mensaje)
                .addOnSuccessListener(documentReference -> Log.d("Firestore", "Mensaje agregado con ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.e("Firestore", "Error al agregar mensaje", e));
    }

    public static chatcomunidades newInstance(String nombreComunidad) {
        chatcomunidades fragment = new chatcomunidades();
        Bundle args = new Bundle();
        args.putString("nombreComunidad", nombreComunidad);
        fragment.setArguments(args);
        return fragment;
    }

    private void setScrollbar() {
        rvMensajes.scrollToPosition(adapter.getItemCount() - 1);
    }
}
