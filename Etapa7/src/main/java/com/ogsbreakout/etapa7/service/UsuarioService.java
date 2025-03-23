package com.ogsbreakout.etapa7.service;

import com.ogsbreakout.etapa7.model.Jogo;
import com.ogsbreakout.etapa7.model.Usuario;
import com.ogsbreakout.etapa7.repository.JogoRepository;
import com.ogsbreakout.etapa7.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private UsuarioJogoService usuarioJogoService;
    
    @Autowired
    JogoRepository jogoRepository;

    //Preenche a tabela com "não possui"
    public Usuario criarUsuario(Usuario usuario) {
        List<Jogo> jogos = jogoRepository.findAll();
            
        for(Jogo jogo : jogos){
            usuarioJogoService.perder(usuario, jogo);
        }
        
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarUsuario() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<Jogo> jogos = jogoRepository.findAll();
            
        for(Usuario usuario : usuarios){
            for (Jogo jogo : jogos){
                usuarioJogoService.perder(usuario, jogo);
            }
        }
        
        return usuarios;
    }

    public Usuario buscarUsuarioPorId(int id) {
        return usuarioRepository.findById(id).orElse(null);
    }
    
    public Usuario atualizarUsuario(Integer id, Usuario usuarioAtual) { 
        Usuario usuario = buscarUsuarioPorId(id); 
        
        usuario.setNome(usuarioAtual.getNome()); 
        usuario.setSenha(usuarioAtual.getSenha()); 
        usuario.setEmail(usuarioAtual.getEmail()); 
        usuario.setApelido(usuarioAtual.getApelido()); 
        usuario.setIdade(usuarioAtual.getIdade()); 
        usuario.setAcesso(usuarioAtual.getAcesso());
        
        usuarioRepository.save(usuario);
        
        return usuario; 
    } 

    public void deletarUsuario(int id) {
        usuarioRepository.deleteById(id);
    }
    
    //Processa para garantir o preenchimento correto das posses
    public List<Usuario> listarPorFiltro(String filtro){
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<Jogo> jogos = jogoRepository.findAll();
        
        for(Usuario usuario : usuarios){
            for (Jogo jogo : jogos){
                usuarioJogoService.perder(usuario, jogo);
            }
        }
        
        List<Usuario> listaUsuarios = usuarioRepository.findByNomeContaining(filtro);
        
        return listaUsuarios;
    }
}
