package com.example.pm2e10626;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pm2e10626.tablas.Contactos;
import com.example.pm2e10626.transacciones.Transacciones;

import java.util.ArrayList;

public class ConsultaActivity extends AppCompatActivity {
    SQLiteConexion conexion;
    String numeroTelefonoGlobal, n, nombreGlobal;

    int informacion, posicion = -1;
    ListView listacontactos;
    ArrayList<Contactos> lista;
    ArrayList<String> ArregloContactos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);

        conexion = new SQLiteConexion(this, Transacciones.NameDateBase, null, 1);
        listacontactos = (ListView) findViewById(R.id.listaContactos);
        Button btnCompartir = (Button)findViewById(R.id.btnCompartir);



        ObtenerListaContactos();
        ArrayAdapter adp = new ArrayAdapter(this, android.R.layout.simple_list_item_activated_1, ArregloContactos);
        listacontactos.setAdapter(adp);

        listacontactos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                posicion = position;
                informacion = lista.get(position).getId();
                numeroTelefonoGlobal = lista.get(position).getTelefono();
                nombreGlobal = lista.get(position).getNombre();

                n = Integer.toString(informacion);
                Toast.makeText(getApplicationContext(), n, Toast.LENGTH_LONG).show();
            }
        });

        btnCompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Compartir();
            }
        });

    }

    private void ObtenerListaContactos() {
        SQLiteDatabase db = conexion.getReadableDatabase();
        Contactos listContactos = null;
        lista = new ArrayList<Contactos>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + Transacciones.tablacontactos, null);

        while (cursor.moveToNext()) {
            listContactos = new Contactos();
            listContactos.setId(cursor.getInt(0));
            listContactos.setPais(cursor.getString(1));
            listContactos.setNombre(cursor.getString(2));
            listContactos.setTelefono(cursor.getString(3));
            listContactos.setNota(cursor.getString(4));
            listContactos.setIdSpinner(cursor.getInt(5));

            lista.add(listContactos);
        }
        fillList();
    }

    private void fillList() {
        ArregloContactos = new ArrayList<String>();
        for (int i = 0; i < lista.size(); i++) {
            ArregloContactos.add(lista.get(i).getId() + " - "
                    + lista.get(i).getNombre() + " - "
                    + lista.get(i).getTelefono());
        }
    }

    public void Llamar() {
        String phone = numeroTelefonoGlobal;
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "+" + phone, null));
        startActivity(intent);
    }

    public void clickCall(View view) {
        if (posicion != -1) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            Llamar();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("¿Llamar a " + nombreGlobal + "?").setPositiveButton("Sí", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        } else {
            Toast.makeText(getApplicationContext(), "Debe seleccionar un registro", Toast.LENGTH_LONG).show();
        }
        //Toast.makeText(getApplicationContext(), "Debe seleccionar un registro" ,Toast.LENGTH_LONG).show();
    }

    private void Eliminar() {
        SQLiteDatabase db = conexion.getWritableDatabase();
        db.execSQL("DELETE FROM Contactos WHERE id='" + informacion + "'");
        Toast.makeText(getApplicationContext(), "DATO ELIMINADO", Toast.LENGTH_LONG).show();
    }

    private void Actualizar() {
        Intent pantalla = new Intent(getApplicationContext(), UpdateActivity.class);
        pantalla.putExtra("id", +informacion + "");
        startActivity(pantalla);
    }

    public void clickELiminar(View view) {
        if (posicion != -1) {

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            Eliminar();
                            ObtenerListaContactos();
                            ArrayAdapter adp = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_activated_1, ArregloContactos);
                            listacontactos.setAdapter(adp);
                            posicion = -1;
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("¿Esta seguro?").setPositiveButton("Sí", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        } else {
            Toast.makeText(getApplicationContext(), "Debe seleccionar un registro", Toast.LENGTH_LONG).show();
        }
    }

    public void clickActualizar(View view) {
        if (posicion != -1) {
            Actualizar();
        } else {
            Toast.makeText(getApplicationContext(), "Debe seleccionar un registro", Toast.LENGTH_LONG).show();
        }
    }

    public void clickInicio(View view) {
        Intent pantalla = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(pantalla);
    }
    public void Compartir (){
        if (posicion != -1) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            CompartirClick();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("¿Compartir el contacto de " + nombreGlobal + "?").setPositiveButton("Sí", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        } else {
            Toast.makeText(getApplicationContext(), "Debe seleccionar un registro", Toast.LENGTH_LONG).show();
        }
    }

    public void CompartirClick(){
        Intent intent = new Intent (Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,"Nombre"+ " "+ nombreGlobal +" "+"\n" +"Telefono"+" "+ numeroTelefonoGlobal);
        Intent eleccion = Intent.createChooser(intent,"Compartir usando");
        startActivity(eleccion);
    }
}

