package com.example.pm2e10626;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pm2e10626.transacciones.Transacciones;

public class PaisesActivity extends AppCompatActivity {
    EditText pais,codigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paises);

        Button btn = (Button)findViewById(R.id.btnGuardarPais);
        pais = (EditText)findViewById(R.id.txtPais);
        codigo = (EditText)findViewById(R.id.txtCodigo);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgregarPais();
            }
        });

    }
    private void AgregarPais() {
        try {

            if (pais.getText().toString().isEmpty() || codigo.getText().toString().isEmpty()){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Alerta Examen");
                builder.setMessage("No Puede dejar campos Vacios");
                builder.setPositiveButton("Aceptar", null);

                AlertDialog dialog = builder.create();
                dialog.show();
            }else{
                SQLiteConexion conexion = new SQLiteConexion( this, Transacciones.NameDateBase, null, 1 );
                SQLiteDatabase db = conexion.getWritableDatabase();
                ContentValues valores = new ContentValues();
                valores.put(Transacciones.paisCatalogo, pais.getText().toString());
                valores.put(Transacciones.codigoCatalogo, codigo.getText().toString());

                Long resultado = db.insert(Transacciones.tablapaises,Transacciones.idPais, valores);

                Toast.makeText(getApplicationContext(), "Registro ingresado: " + resultado.toString(),Toast.LENGTH_LONG).show();
                db.close();

                ClearScreen();
                Inicio();
            }


        }catch (Exception ex){
            Toast.makeText(getApplicationContext(), "Ha ocurrido un error al guardar",Toast.LENGTH_LONG).show();
        }

    }

    private void ClearScreen() {
        pais.setText("");
        codigo.setText("");
    }
    public void Inicio(){
        Intent intent = new Intent(this,MainActivity.class );
        startActivity(intent);
    }
}