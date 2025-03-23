package com.ogsbreakout.etapa7.service;

import com.ogsbreakout.etapa7.model.Usuario;
import com.ogsbreakout.etapa7.model.Jogo;
import com.ogsbreakout.etapa7.model.UsuarioJogo;
import com.ogsbreakout.etapa7.repository.UsuarioJogoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioJogoService {

    @Autowired
    private UsuarioJogoRepository usuarioJogoRepository;

    public boolean possuiJogo(Usuario usuario, Jogo jogo) {
        
        UsuarioJogo usuarioJogo = usuarioJogoRepository.findByUsuarioAndJogo(usuario, jogo);
        
        if (usuarioJogo != null){
            return usuarioJogo.getPossui();
        }
        return false;
        
        
        //Optional<UsuarioJogo> usuarioJogo = usuarioJogoRepository.findByUsuarioAndJogo(usuario, jogo);
        //return usuarioJogo.map(UsuarioJogo::getPossui).orElse(false);
    }    
    
    public void comprar(Usuario usuario, Jogo jogo){
        UsuarioJogo usuarioJogo = usuarioJogoRepository.findByUsuarioAndJogo(usuario, jogo);
        
        if (usuarioJogo == null) {
            usuarioJogo = new UsuarioJogo();
            usuarioJogo.setUsuario(usuario);
            usuarioJogo.setJogo(jogo);
        }

        usuarioJogo.setPossui(true);  
        usuarioJogoRepository.save(usuarioJogo);
    }
    
    public UsuarioJogo perder(Usuario usuario, Jogo jogo){
        UsuarioJogo usuarioJogo = usuarioJogoRepository.findByUsuarioAndJogo(usuario, jogo);
        
        if (usuarioJogo == null) {
            usuarioJogo = new UsuarioJogo();
            usuarioJogo.setUsuario(usuario);
            usuarioJogo.setJogo(jogo);
        }
        
        if (usuario.getAcesso().equals("admin")){
            usuarioJogo.setPossui(true); 
        }
        else{
            usuarioJogo.setPossui(false); 
        }

        usuarioJogoRepository.save(usuarioJogo);
        return usuarioJogo;
    }
    
    public List<UsuarioJogo> listarUsuarioJogo() {
        return usuarioJogoRepository.findAll();
    }
    
    public List<UsuarioJogo> listarUsuarioJogoContendo(String filtro) {
        List<UsuarioJogo> lista = usuarioJogoRepository.findAll();
        List<UsuarioJogo> listaUsuarioJogo = usuarioJogoRepository.findAll();
             
        for (UsuarioJogo usuarioJogo : lista){
            if (usuarioJogo.getUsuario().getNome().equals(filtro)){
                listaUsuarioJogo.add(usuarioJogo);
            }
        }
        
        return listaUsuarioJogo;
    }
}
