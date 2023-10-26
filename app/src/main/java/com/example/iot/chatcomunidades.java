package com.example.iot;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link chatcomunidades#newInstance} factory method to
 * create an instance of this fragment.
 */
public class chatcomunidades extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

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

    public static chatcomunidades newInstance(String param1, String param2) {
        chatcomunidades fragment = new chatcomunidades();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chatcomunidades, container, false);

        fotoPerfil = (CircleImageView) view.findViewById(R.id.fotoPerfil);
        nombrechat = (TextView) view.findViewById(R.id.nombrechat);
        rvMensajes = (RecyclerView) view.findViewById(R.id.rvMensajes);
        txtMensaje = (EditText) view.findViewById(R.id.txtMensaje);
        btnEnviarMensaje = (Button) view.findViewById(R.id.btnEnviarMensaje);

        db = FirebaseFirestore.getInstance();
        mensajesCollection = db.collection("chat"); // Colección de mensajes

        adapter = new AdapterMensajes(getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        rvMensajes.setLayoutManager(layoutManager);
        rvMensajes.setAdapter(adapter);

        btnEnviarMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mensajeTexto = txtMensaje.getText().toString();
                String nombreUsuario = nombrechat.getText().toString();
                String hora = "00:00"; // Cambia esto para obtener la hora real
                agregarMensajeFirestore(mensajeTexto, nombreUsuario, hora);
                txtMensaje.setText("");
            }
        });

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                setScrollbar();
            }
        });

        mensajesCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("Firestore", "Error al escuchar cambios en la colección de mensajes", e);
                    return;
                }

                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    Mensaje mensaje = document.toObject(Mensaje.class);
                    adapter.addMensaje(mensaje);
                }
                setScrollbar();
            }
        });

        return view;
    }

    private void agregarMensajeFirestore(String mensajeTexto, String nombreUsuario, String hora) {
        Mensaje mensaje = new Mensaje(mensajeTexto, nombreUsuario, "", "1", hora);

        mensajesCollection.add(mensaje)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("Firestore", "Mensaje agregado con ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Firestore", "Error al agregar mensaje", e);
                    }
                });
    }

    private void setScrollbar() {
        rvMensajes.scrollToPosition(adapter.getItemCount() - 1);
    }
}