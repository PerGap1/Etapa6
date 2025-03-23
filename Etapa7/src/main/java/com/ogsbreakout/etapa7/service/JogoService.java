package com.ogsbreakout.etapa7.service;

import com.ogsbreakout.etapa7.model.Jogo;
import com.ogsbreakout.etapa7.model.Usuario;
import com.ogsbreakout.etapa7.model.UsuarioJogo;
import com.ogsbreakout.etapa7.repository.JogoRepository;
import com.ogsbreakout.etapa7.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JogoService {

    @Autowired
    private JogoRepository jogoRepository;
    
    @Autowired
    private UsuarioJogoService usuarioJogoService;

    @Autowired
    UsuarioRepository usuarioRepository;

    //Preenche a tabela com "não possui"
    public Jogo criarJogo(Jogo jogo) {
        List<Usuario> usuarios = usuarioRepository.findAll();
            
        for(Usuario usuario : usuarios){
            usuarioJogoService.perder(usuario, jogo);
        }
        
        return jogoRepository.save(jogo);
    }

    //Processa para garantir o preenchimento correto das posses
    public List<Jogo> listarJogo() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<Jogo> jogos = jogoRepository.findAll();
            
        for(Usuario usuario : usuarios){
            for (Jogo jogo : jogos){
                usuarioJogoService.perder(usuario, jogo);
            }
        }
        
        return jogos;
    }
    
    public Jogo buscarJogoPorId(int id) {
        return jogoRepository.findById(id).orElse(null);
    }
    
    public List<Jogo> listarJogoPossui(HttpSession session) {
        //Processamento para evitar erros
        List<Usuario> u = usuarioRepository.findAll();
        List<Jogo> j = jogoRepository.findAll();
        
        for(Usuario usuario : u){
            for (Jogo jogo : j){
                usuarioJogoService.perder(usuario, jogo);
            }
        }
        
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) {
            return null; 
        }
        
        List<Jogo> listaJogos = jogoRepository.findAll();
        List<Jogo> jogosPossui = new ArrayList<>();
        
        for(Jogo jogo : listaJogos){
            if (verificarPossuiBoolean(usuario, jogo)){
                jogosPossui.add(jogo);
            }
        }
        return jogosPossui;
    }
    
    public Jogo atualizarJogo(Integer id, Jogo jogoAtual) { 
        Jogo jogo = buscarJogoPorId(id); 
        
        jogo.setTitulo(jogoAtual.getTitulo()); 
        jogo.setCategoria(jogoAtual.getCategoria()); 
        jogo.setValor(jogoAtual.getValor()); 
        jogo.setLancamento(jogoAtual.getLancamento()); 
        jogo.setCriador(jogoAtual.getCriador()); 
        jogo.setDescricao(jogoAtual.getDescricao()); 
        jogo.setClassificacao(jogoAtual.getClassificacao()); 
        
        jogoRepository.save(jogo);
        
        return jogo; 
    } 

    public boolean verificarPossuiBoolean(Usuario usuario, Jogo jogo) {
        return usuarioJogoService.possuiJogo(usuario, jogo);
    }
    
    public String verificarPossui(Usuario usuario, Jogo jogo) {
        for (UsuarioJogo usuarioJogo : jogo.getUsuarioJogos()) {
            if (usuarioJogo.getUsuario().equals(usuario)) {
                return usuarioJogo.getPossui()? "Sim" : "Não";
            }
        }
        return "Não";
    }

    public void deletarJogo(int id) {
        jogoRepository.deleteById(id);
    }
    
    public List<Jogo> findByTituloContainingPossue(String filtro, HttpSession session){
        //Processamento para evitar erros
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<Jogo> jogos = jogoRepository.findAll();
        
        for(Usuario usuario : usuarios){
            for (Jogo jogo : jogos){
                usuarioJogoService.perder(usuario, jogo);
            }
        }
        
        List<Jogo> listaInicial = jogoRepository.findByTituloContaining(filtro);
        List<Jogo> listaJogos = new ArrayList<>();
        
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) {
            return null; 
        }
        
        for(Jogo jogo : listaInicial){
            if (usuarioJogoService.possuiJogo(usuario, jogo)){
                listaJogos.add(jogo);
            }
        }
        
        return listaJogos;
    }
    
    //Processa para garantir o preenchimento correto das posses
    public List<Jogo> listarPorFiltro(String filtro){
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<Jogo> jogos = jogoRepository.findAll();
        
        for(Usuario usuario : usuarios){
            for (Jogo jogo : jogos){
                usuarioJogoService.perder(usuario, jogo);
            }
        }
        
        List<Jogo> listaJogos = jogoRepository.findByTituloContaining(filtro);
        
        return listaJogos;
    }
}
