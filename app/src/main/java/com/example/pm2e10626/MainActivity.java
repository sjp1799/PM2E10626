package com.example.pm2e10626;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pm2e10626.tablas.Paises;
import com.example.pm2e10626.transacciones.Transacciones;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    SQLiteConexion conexion;
    EditText nombre,telefono,nota;
    Spinner comboPais;
    ArrayList<Paises> lista;
    ArrayList<String> ArregloPaises;
    /**/
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int PETICION_ACCESO_CAM = 100;
    ImageView objImagen;
    byte[] byteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*********/
        conexion = new SQLiteConexion(this, Transacciones.NameDateBase,null,1);
        Button btn = (Button)findViewById(R.id.btnGuardar);
        nombre = (EditText)findViewById(R.id.txtNombre);
        telefono = (EditText)findViewById(R.id.txtNumero);
        nota = (EditText)findViewById(R.id.txtNota);
        comboPais = (Spinner)findViewById(R.id.spPais);
        /*********/
        objImagen = (ImageView) findViewById(R.id.foto);
        byteArray = new byte[0];

        ObtenerListaPaises();
        ArrayAdapter adp = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,ArregloPaises);
        comboPais.setAdapter(adp);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgregarContacto();
            }
        });

        objImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permisos();

            }
        });
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

    private void AgregarContacto() {
        try {

            if (nombre.getText().toString().isEmpty() || telefono.getText().toString().isEmpty()
                    || nota.getText().toString().isEmpty()  ){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Alerta Examen");
                builder.setMessage("No Puede dejar campos Vacios");
                builder.setPositiveButton("Aceptar", null);

                AlertDialog dialog = builder.create();
                dialog.show();

            }else{
                //String pais = comboPais.getSelectedItem().toString();
                int posicion = (int) comboPais.getSelectedItemId();

                String pais = lista.get(posicion).getPais();
                String codigo = lista.get(posicion).getCodigo();

                SQLiteConexion conexion = new SQLiteConexion( this, Transacciones.NameDateBase, null, 1 );
                SQLiteDatabase db = conexion.getWritableDatabase();
                ContentValues valores = new ContentValues();

                valores.put(Transacciones.pais, pais);
                //valores.put(Transacciones.blobImagen, byteArray);
                valores.put(Transacciones.nombre, nombre.getText().toString());
                valores.put(Transacciones.telefono, codigo+telefono.getText().toString());
                valores.put(Transacciones.nota, nota.getText().toString());
                valores.put(Transacciones.idSpinner, posicion);


                Long resultado = db.insert(Transacciones.tablacontactos,Transacciones.id, valores);
                //Toast
                Toast.makeText(getApplicationContext(), "Registro ingresado: " + resultado.toString(),Toast.LENGTH_LONG).show();
                //byteArray = new byte[0];
                //objImagen.setImageResource(R.mipmap.ic_launcher_round);

                db.close();


                ClearScreen();
            }


        }catch (Exception ex){
            Toast.makeText(getApplicationContext(), "Ha ocurrido un error al guardar",Toast.LENGTH_LONG).show();
        }

    }

    private void ClearScreen() {
        comboPais.setSelection(0);
        nombre.setText("");
        telefono.setText("");
        nota.setText("");
    }

    private void permisos() {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{ Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, PETICION_ACCESO_CAM);
        }else{
            tomarFoto();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PETICION_ACCESO_CAM){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                tomarFoto();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Se necesitan permisos de acceso", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            getBytes(data);
        }
    }

    private void getBytes(Intent data){
        Bitmap photo = (Bitmap) data.getExtras().get("data");
        objImagen.setImageBitmap(photo);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byteArray = stream.toByteArray();
    }

    private void tomarFoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }





    public void ClickConsultarDatos(View view) {
        Intent intent = new Intent(this,ConsultaActivity.class );
        startActivity(intent);
    }

    public void clickAgregarPais(View view) {
        Intent intent = new Intent(this,PaisesActivity.class );
        startActivity(intent);
    }

    public  void  buscarI(View view)
    {

        Intent intent = new Intent(this,BuscarActivity.class );
        startActivity(intent);

    }

}