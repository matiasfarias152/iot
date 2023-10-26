package com.example.iot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void login(View v) {
        EditText campousuario = this.findViewById(R.id.usuario);
        String usuario = campousuario.getText().toString();
        EditText campocontrasenia = this.findViewById(R.id.contrasenia);
        String contrasenia = campocontrasenia.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usuariosRef = db.collection("usuarios");

        usuariosRef.whereEqualTo("usuario", usuario).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String contraseniaEnFirestore = document.getString("contrasena");
                            if (contraseniaEnFirestore != null && contraseniaEnFirestore.equals(contrasenia)) {
                                Intent i = new Intent(this, Principal1.class);
                                startActivity(i);
                            } else {
                                Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al realizar la consulta", Toast.LENGTH_SHORT).show();
                });
    }
    public void crearCuenta(View v){
            Intent i = new Intent(this,Registrarcuenta.class);
            startActivity(i);
        }


}