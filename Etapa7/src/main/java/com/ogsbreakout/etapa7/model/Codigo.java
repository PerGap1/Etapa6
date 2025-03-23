package com.ogsbreakout.etapa7.model;

public class Codigo {
    private String email;
    private String codigo;
    
    //Construtor
    public Codigo(String email, String codigo){
        this.email = email;
        this.codigo = codigo;
    }
    
    //Get e set
    public void setEmail(String email){this.email = email;}
    public String getEmail(){return email;}
    
    public void setCodigo(String codigo){this.codigo = codigo;}
    public String getCodigo(){return codigo;}
    
    public boolean conferirCodigo(String email, String codigo){
        return (this.email.equals(email) || this.codigo.equals(codigo));
    }
}
