package com.example.pm2e10626.transacciones;

public class Transacciones {


    public static final String tablacontactos = "contactos";
    public static final String tablapaises = "paises";


    public static final String id = "id";
    public static final String blobImagen = "blobImagen";
    public static final String pais = "pais";
    public static final String nombre = "nombre";
    public static final String telefono = "telefono";
    public static final String nota = "nota";
    public static final String idSpinner = "idSpinner";


    public static final String idPais = "idPais";
    public static final String paisCatalogo = "paisCatalogo";
    public static final String codigoCatalogo = "codigoCatalogo";



    public static final String CreateTableContactos = "CREATE TABLE contactos ( id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,blobImagen BLOB,pais TEXT, nombre TEXT, telefono INTEGER,"+
            "nota TEXT,idSpinner INTEGER)";


    public static final String CreateTablePaises = "CREATE TABLE paises ( idPais INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,paisCatalogo TEXT, codigoCatalogo INTEGER)";

    public static final String DropTableContactos = "DROP TABLE IF EXISTS contactos";
    public static final String DropTablePaises = "DROP TABLE IF EXISTS paises";


    public static final String NameDateBase = "DBExamen1";
}
