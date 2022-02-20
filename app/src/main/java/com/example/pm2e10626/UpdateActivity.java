package com.example.pm2e10626;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pm2e10626.tablas.Paises;
import com.example.pm2e10626.transacciones.Transacciones;

import java.util.ArrayList;

public class UpdateActivity extends AppCompatActivity {
    SQLiteConexion conexion;

    EditText pais,nombre,telefono,nota,id_update;

    Spinner comboPais;
    ArrayList<Paises> lista;
    ArrayList<String> ArregloPaises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        conexion = new SQLiteConexion(this, Transacciones.NameDateBase,null,1);
        Button btn = (Button)findViewById(R.id.btnActualizar);

        nombre = (EditText)findViewById(R.id.txtNombresUpdate);
        telefono = (EditText)findViewById(R.id.txtNumeroUpdate);
        nota = (EditText)findViewById(R.id.txtNotaUpdate);
        comboPais = (Spinner)findViewById(R.id.spPaisUpdate);



        String obtn= getIntent().getExtras().getString("id");
        id_update=(EditText)findViewById(R.id.id_update) ;

        id_update.setText(obtn);



        ObtenerListaPaises();
        ArrayAdapter adp = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,ArregloPaises);
        comboPais.setAdapter(adp);


        //a partir de la conexion de arriba , creamos una conexion escribible
        SQLiteDatabase db = conexion.getWritableDatabase();
        //
        //Parametros de configuracion de la sentncia
        String[] params ={id_update.getText().toString()};//parametro de la busqueda, id por el que lo vamos a buscar
        String[] fields ={Transacciones.nombre,

                Transacciones.idSpinner,
                Transacciones.telefono,
                Transacciones.nota
        };

        String wherecond = Transacciones.id + "=?";

        try {

            Cursor cdata =db.query(Transacciones.tablacontactos,fields,wherecond,params,null,null,null);
            cdata.moveToFirst();

            nombre.setText(cdata.getString(0));
            telefono.setText(cdata.getString(2));
            comboPais.setSelection(cdata.getInt(1));
            nota.setText(cdata.getString(3));



            Toast.makeText(getApplicationContext(),"Consulta realizada exitosamente",Toast.LENGTH_LONG).show();
        }catch(Exception e)
        {

            Toast.makeText(getApplicationContext(),"Elemento no encontrado",Toast.LENGTH_LONG).show();
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActualizarContacto();
            }
        });

    }

    private void ActualizarContacto() {

        SQLiteDatabase db = conexion.getWritableDatabase();
        String[] params = {id_update.getText().toString()};


        int posicion = (int) comboPais.getSelectedItemId();
        String pais = lista.get(posicion).getPais();
        String codigo = lista.get(posicion).getCodigo();

        ContentValues valores = new ContentValues();
        valores.put(Transacciones.nombre,nombre.getText().toString());
        valores.put(Transacciones.telefono,codigo+telefono.getText().toString());
        valores.put(Transacciones.nota,nota.getText().toString());
        valores.put(Transacciones.idSpinner,comboPais.getSelectedItemId());
        valores.put(Transacciones.pais,pais);
        db.update(Transacciones.tablacontactos,valores,Transacciones.id +"=?",params);
        Toast.makeText(getApplicationContext(),"DATO ACTUALIZADO EXITOSAMENTE",Toast.LENGTH_LONG).show();

        Intent pantalla = new Intent(getApplicationContext(),ConsultaActivity.class);
        startActivity(pantalla);



    }

    private void ObtenerListaPaises() {
        SQLiteDatabase db = conexion.getReadableDatabase();
        lista = new ArrayList<Paises>();
        Paises listPaises = null;
        Cursor cursor = db.rawQuery("SELECT * FROM " + Transacciones.tablapaises, null);

        while(cursor.moveToNext()){
            listPaises = new Paises();
            listPaises.setId(cursor.getInt(0));
            listPaises.setPais(cursor.getString(1));
            listPaises.setCodigo(cursor.getString(2));
            lista.add(listPaises);
        }
        fillCombo();
    }
    private void fillCombo() {
        ArregloPaises = new ArrayList<String>();
        for (int i = 0; i < lista.size(); i++){
            ArregloPaises.add(lista.get(i).getPais() + " - "
                    +lista.get(i).getCodigo());
        }
    }

    public void ClickActualizar(View view) {

    }
}