package com.example.pm2e10626;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pm2e10626.transacciones.Transacciones;

public class BuscarActivity extends AppCompatActivity {

    SQLiteConexion conexion;
    EditText id, nombres, telefono, nota;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar);



        conexion = new SQLiteConexion(this, Transacciones.NameDateBase, null, 1);



        Button btnconsulta = (Button) findViewById(R.id.btnbuscar);
        Button btnbinicio =  (Button) findViewById(R.id.btninicio);

        id = (EditText) findViewById(R.id.txtcid);
        nombres = (EditText) findViewById(R.id.txtbnombre);
        telefono = (EditText) findViewById(R.id.txtbtelefono);
        nota = (EditText) findViewById(R.id.txtbnota);

        btnconsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Buscar();

            }
        });



        btnbinicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void Buscar() {
        SQLiteDatabase db = conexion.getWritableDatabase();
        String[] params = {id.getText().toString()};
        String[] fields = {Transacciones.nombre,
                Transacciones.telefono,
                Transacciones.nota};
        String wherecond = Transacciones.id + "=?";


        try {

            Cursor cdata = db.query(Transacciones.tablacontactos, fields, wherecond, params, null, null, null);

            cdata.moveToFirst();


            nombres.setText(cdata.getString(0));

            telefono.setText(cdata.getString(1));

            nota.setText(cdata.getString(2));




            Toast.makeText(getApplicationContext(), "Consultado con exito", Toast.LENGTH_LONG).show();

        } catch (Exception ex) {
            ClearScreen();
            Toast.makeText(getApplicationContext(), "Elemento no encontrado", Toast.LENGTH_LONG).show();

        }
    }

    private void ClearScreen() {
    }


}
