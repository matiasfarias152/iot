package com.example.iot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class Principal1 extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal1);

        //Obtener fragmento principal
        FragmentManager fragmentManager = getSupportFragmentManager();

        //Transacción
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Listadodeactividades listadoact = new Listadodeactividades();
        transaction.replace(R.id.contenedor, listadoact);

        transaction.commit();



        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);




        /* Incorporamos el menú lateral */
        NavigationView nav = (NavigationView) findViewById(R.id.nav);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //Recuperar la opcion del menu
                int id = item.getItemId(); //Recuperar el id
                if (id==R.id.opcion1){
                    registroactividad p = new registroactividad();
                    getSupportFragmentManager().beginTransaction().replace(R.id.contenedor,p).commit();

                }
                else if(id==R.id.opcion2){
                    invitaractividad ia = new invitaractividad();
                    getSupportFragmentManager().beginTransaction().replace(R.id.contenedor,ia).commit();

                }
                else if(id==R.id.opcion3){
                    RegistroComunidad rc = new RegistroComunidad();
                    getSupportFragmentManager().beginTransaction().replace(R.id.contenedor,rc).commit();

                }
                else if(id==R.id.opcion4){
                    ListaAmigos la = new ListaAmigos();
                    getSupportFragmentManager().beginTransaction().replace(R.id.contenedor,la).commit();
                }
                else if (id==R.id.opcion5){
                   Listadodeactividades lact = new Listadodeactividades();
                   getSupportFragmentManager().beginTransaction().replace(R.id.contenedor,lact).commit();
                }
                else if (id==R.id.opcion6){
                    ListaComunidades lc = new ListaComunidades();
                    getSupportFragmentManager().beginTransaction().replace(R.id.contenedor,lc).commit();
                }
                return false;
            }
        });
        DrawerLayout dl = (DrawerLayout) findViewById(R.id.principal);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                dl,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close

        );

        dl.addDrawerListener(toggle);
        toggle.syncState();

        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dl.isDrawerOpen(GravityCompat.START)){
                    dl.closeDrawer(GravityCompat.START);
                }
                else{
                    dl.openDrawer((int)Gravity.START);
                }
            }
        });
    }


}