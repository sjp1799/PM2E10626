package com.example.pm2e10626.tablas;

public class Contactos {

    private Integer id;
    private String blobImagen;
    private String pais;
    private String nombre;
    private String telefono;
    private String nota;
    private Integer idSpinner;

    public Contactos(){}
    public Contactos(Integer id,String blobImagen, String nombre, String telefono, String nota,Integer idSpinner) {
        this.id = id;
        this.blobImagen = blobImagen;
        this.nombre = nombre;
        this.telefono = telefono;
        this.nota = nota;
        this.idSpinner = idSpinner;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public  String getBlobImagen(){return  blobImagen;}
    public void setBlobImagen(String blobImagen){this.blobImagen = blobImagen;}

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }
    public Integer getIdSpinner() {
        return idSpinner;
    }

    public void setIdSpinner(Integer idSpinner) {
        this.idSpinner = idSpinner;
    }

}
