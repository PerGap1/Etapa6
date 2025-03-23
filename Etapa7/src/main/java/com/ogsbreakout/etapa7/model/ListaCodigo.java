package com.ogsbreakout.etapa7.model;

import java.util.ArrayList;
import java.util.List;

public class ListaCodigo {
    private static List<Codigo> listaCodigos = new ArrayList<>();
    
    public static void novoCodigo (String email, String valorCodigo){
        listaCodigos.add(new Codigo(email, valorCodigo));
    }
    
    public static boolean verificarCodigo(String email, String valorCodigo){
        for (Codigo codigo : listaCodigos){            
            if (codigo.getCodigo().equals(valorCodigo) && codigo.getEmail().equals(email)){
                return true;
            }
        }
        
        return false;
    }
}
