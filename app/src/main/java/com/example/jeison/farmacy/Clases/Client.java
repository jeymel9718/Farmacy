package com.example.jeison.farmacy.Clases;

/**
 * Created by Jeison on 09/10/2017.
 */

public class Client {
    public static Client cliente=new Client();
    public String id;
    public String ip;
    public String Name;
    public String Telefono;
    public String Apellido1;
    public String Apellido2;
    public String Canton;
    public String Provincia;
    public String Distrito;
    public String Direccion;
    public String Fecha;

    public Client(){
    }

    public void setName(String name){
        this.Name=name;
    }
    public void setId(String id){
        this.id=id;
    }
    public void setTelefono(String telefono){
        this.Telefono=telefono;
    }

    public static Client getInstance() {
        return cliente;
    }
}
