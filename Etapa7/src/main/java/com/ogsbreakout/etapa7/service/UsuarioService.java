package com.ogsbreakout.etapa7.service;

import com.ogsbreakout.etapa7.model.Usuario;
import com.ogsbreakout.etapa7.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario criarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarUsuario() {
        return usuarioRepository.findAll();
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
}
