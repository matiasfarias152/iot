package com.example.iot;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Registrarcuenta extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarcuenta);
    }

    public void registrarusuario(View v) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        EditText campousuarioregistro = this.findViewById(R.id.usuarioregistro);
        String usuarioregistro = campousuarioregistro.getText().toString();

        EditText campocontraseniaregistro = this.findViewById(R.id.contraseniaregistro);
        String contraseniaregistro = campocontraseniaregistro.getText().toString();

        EditText camporeingresecontrasenia = this.findViewById(R.id.reingresarcontrasenia);
        String reingresarcontrasenia = camporeingresecontrasenia.getText().toString();

        EditText campocorreoregistro = this.findViewById(R.id.correoregistro);
        String correoregistro = campocorreoregistro.getText().toString();

        if (usuarioregistro.isEmpty() || contraseniaregistro.isEmpty() || reingresarcontrasenia.isEmpty() || correoregistro.isEmpty()) {
            Toast.makeText(this, "Todos los campos deben estar llenos", Toast.LENGTH_SHORT).show();
        } else if (!contraseniaregistro.equals(reingresarcontrasenia)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
        } else {
            Map<String, Object> userData = new HashMap<>();
            userData.put("usuario", usuarioregistro);
            userData.put("correo", correoregistro);
            userData.put("contrasena", contraseniaregistro);
            userData.put("idamigos", new ArrayList<String>()); //Lista vacia para los amigos

            db.collection("usuarios").document(usuarioregistro).set(userData).addOnSuccessListener(aVoid -> {
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                Toast.makeText(this, "No se pudo registrar el usuario: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }
    }
}