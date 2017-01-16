package com.jefrienalvizures.tonechord.bean;

/**
 * Created by Jefrien on 18/12/2016.
 */
public class Usuario {
    private int id = 0;
    private String name;
    private String email;
    private String password;
    private String imagen="";

    public String toJson(){
        String json = "{" +
                "\"id\":\""+this.id+"\"," +
                "\"name\":\""+this.name+"\"," +
                "\"email\":\""+this.email+"\"" +
                "\"imagen\":\""+this.imagen+"\"" +
                "}";

        return json;
    }

    public String editToJson(){
        String json = "{" +
                "\"id\":\""+this.id+"\"," +
                "\"name\":\""+this.name+"\"," +
                "\"email\":\""+this.email+"\"" +
                "}";

        return json;
    }

    public Usuario() {
    }

    public Usuario(int id, String name, String email, String password, String imagen) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.imagen = imagen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
