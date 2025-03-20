package com.ogsbreakout.etapa7.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String home(){
        return "Home";
    }
    @GetMapping("/Home")
    public String home2(){
        return "Home";
    }

    @GetMapping("/Login")
    public String login(){
        return "Login";
    }
    
    @GetMapping("/NovaConta")
    public String novaConta(){
        return "NovaConta";
    }
    
    @GetMapping("/Catalogo")
    public String catalogo(){
        return "Catalogo";
    }
    
    @GetMapping("/EsqueciSenha")
    public String esqueciSenha(){
        return "EsqueciSenha";
    }
    
    @GetMapping("/RecuperarConta")
    public String recuperarConta(){
        return "RecuperarConta";
    }
    
    @GetMapping("/MeusJogos")
    public String meusJogos(){
        return "MeusJogos";
    }
    
    @GetMapping("/Conta")
    public String conta(){
        return "Conta";
    }
    
    @GetMapping("/NovaSenha")
    public String novaSenha(){
        return "NovaSenha";
    }
    
    @GetMapping("/BarraNav")
    public String barraNav(){
        return "BarraNav";
    }
}