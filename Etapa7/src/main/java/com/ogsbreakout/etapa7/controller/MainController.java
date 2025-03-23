package com.ogsbreakout.etapa7.controller;

import com.ogsbreakout.etapa7.model.Jogo;
import com.ogsbreakout.etapa7.model.Usuario;
import com.ogsbreakout.etapa7.model.UsuarioJogo;
import com.ogsbreakout.etapa7.repository.JogoRepository;
import com.ogsbreakout.etapa7.repository.UsuarioJogoRepository;
import com.ogsbreakout.etapa7.repository.UsuarioRepository;
import com.ogsbreakout.etapa7.service.JogoService;
import com.ogsbreakout.etapa7.service.UsuarioJogoService;
import com.ogsbreakout.etapa7.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
    @Autowired
    JogoService jogoService;
    
    @Autowired
    JogoRepository jogoRepository;
    
    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    UsuarioService usuarioService;
    
    @Autowired
    UsuarioJogoService usuarioJogoService;
    
    @Autowired
    UsuarioJogoRepository usuarioJogoRepository;
    
    //Home
    @GetMapping("/")
    public String home(HttpSession session){
        session.invalidate();
        return "Home";
    }
    @GetMapping("/Home")
    public String home2(HttpSession session){
        session.invalidate();
        return "Home";
    }

    //Login
    @GetMapping("/Login")
    public String login(){
        return "Login";
    }
    
    //Nova conta
    @GetMapping("/NovaConta")
    public String novaConta(Model model){
        model.addAttribute(new Usuario());
        return "NovaConta";
    }
    
    //Catálogo
    @GetMapping("/Catalogo")
    public String catalogo(@RequestParam(name = "filtro", required = false) String filtro, Model model, HttpSession session){
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        
        if (usuario == null) {
            return "redirect:/Login"; 
        }
        
        if (filtro == null || filtro.equals("")){
            List<Jogo> jogos = jogoService.listarJogo();
            List<UsuarioJogo> usuarioJogos = new ArrayList<>();
            
            for(Jogo jogo : jogos){
                UsuarioJogo usuarioJogo = usuarioJogoService.perder(usuario, jogo);
                usuarioJogos.add(usuarioJogo);
            }

            model.addAttribute("listaUsuarioJogos", usuarioJogos);
            model.addAttribute("listarJogos", jogos);
        }
        else{
            List<Jogo> jogos = jogoRepository.findByTituloContaining(filtro);
            List<UsuarioJogo> usuarioJogos = usuarioJogoService.listarUsuarioJogoContendo(filtro);

            model.addAttribute("listaUsuarioJogos", usuarioJogos);
            model.addAttribute("listarJogos", jogos);
            model.addAttribute("filtro", filtro);
        }
        
        return "Catalogo";
    }
    
    //Esqueci senha
    @GetMapping("/EsqueciSenha")
    public String esqueciSenha(){
        return "EsqueciSenha";
    }
    
    //Recuperar conta
    @GetMapping("/RecuperarConta")
    public String recuperarConta(){
        return "RecuperarConta";
    }
    
    //Meus jogos
    @GetMapping("/MeusJogos")
    public String meusJogos(@RequestParam(name = "filtro", required = false) String filtro, Model model, HttpSession session){
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        
        if (usuario == null) {
            return "redirect:/Login"; 
        }
        
        if (filtro == null || filtro.equals("")){
            List<Jogo> jogos = jogoService.listarJogoPossui(session);

            model.addAttribute("listarJogos", jogos);
            return "MeusJogos";
        }
        else{
            List<Jogo> jogos = jogoService.findByTituloContainingPossue(filtro, session);
            
            model.addAttribute("listarJogos", jogos);
            model.addAttribute("filtro", filtro);
            return "Catalogo";
        }
    }
    
    //Conta
    @GetMapping("/Conta")
    public String conta(Model model, HttpSession session){
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) {
            return "redirect:/Login"; 
        }
        
        model.addAttribute("nome", usuario.getNome());
        
        return "Conta";
    }
    
    //Nova senha
    @GetMapping("/NovaSenha")
    public String novaSenha(){
        return "NovaSenha";
    }
    
    //Não implantado
    @GetMapping("/BarraNav")
    public String barraNav(){
        return "BarraNav";
    }
    
    //Criar jogo
    @GetMapping("/CriadorJogos")
    public String criadorJogos(HttpSession session, Model model){
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) {
            return "redirect:/Login"; 
        }
        
        if (usuario.getAcesso().equals("user")){
            model.addAttribute("erroAcesso", "Você não tem acesso a essa função");
            return "/Conta";
        }
        
        model.addAttribute("jogo", new Jogo());
        return "CriadorJogos";
    }
    
    //Atualizar conta
    @GetMapping("/AtualizarConta")
    public String atualizarConta(Model model, HttpSession session){
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) {
            return "redirect:/Login"; 
        }
        
        model.addAttribute("usuarioAntigo", usuario);
        model.addAttribute("usuarioNovo", usuario);
        return "AtualizarConta";
    }
    
    @GetMapping("/AtualizarConta/{id}")
    public String atualizarConta(Model model, HttpSession session, @PathVariable(value = "id") Integer id){
        Usuario acesso = (Usuario) session.getAttribute("usuarioLogado");
        if (acesso == null) {
            return "redirect:/Login"; 
        }
        if (!acesso.getAcesso().equals("admin")){
            return "redirect:/";
        }
        
        Usuario usuario = usuarioService.buscarUsuarioPorId(id);
        
        model.addAttribute("usuarioAntigo", usuario);
        model.addAttribute("usuarioNovo", usuario);
        return "AtualizarConta";
    }

    //Atualizar jogo
    @GetMapping("/AtualizarJogo/{id}")
    public String atualizarJogo(Model model, HttpSession session, @PathVariable(value = "id") Integer id){
        Usuario acesso = (Usuario) session.getAttribute("usuarioLogado");
        if (acesso == null) {
            return "redirect:/Login"; 
        }
        if (acesso.getAcesso().equals("user")){
            model.addAttribute("erroAcesso", "Você não tem acesso a essa função");
            return "Conta";
        }
        
        Jogo jogo = jogoService.buscarJogoPorId(id);
        
        model.addAttribute("jogo", jogo);

        return "AtualizarJogo";
    }
    
    //Administrador
    @GetMapping("/Administrador")
    public String administrador(@RequestParam(name = "filtro", required = false) String filtroUsuario, 
    @RequestParam(name = "filtro", required = false) String filtroJogo, Model model, HttpSession session){
        
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) {
            return "redirect:/Login"; 
        }
        if (!usuario.getAcesso().equals("admin")){
            model.addAttribute("erroAcesso", "Você não tem acesso a essa função");
            return "Conta";
        }
        
        List<Usuario> usuarios;
        List<Jogo> jogos;
        
        //Filtro Usuarios
        if (filtroUsuario == null || filtroUsuario.equals("")){
            usuarios = usuarioService.listarUsuario();
        }
        else{
            usuarios = usuarioRepository.findByNomeContaining(filtroUsuario);
            model.addAttribute("filtroUsuario", filtroUsuario);
            System.out.println("filtro usuário");
        }
        
        //Filtro Jogos
        if (filtroJogo == null || filtroJogo.equals("")){
            jogos = jogoService.listarJogo();
        }
        else{
            jogos = jogoRepository.findByTituloContaining(filtroJogo);
            model.addAttribute("filtroJogo", filtroJogo);
        }
        
        model.addAttribute("listarJogos", jogos);
        model.addAttribute("listarUsuarios", usuarios);
        
        return "Administrador";
    }
}