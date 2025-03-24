package com.ogsbreakout.etapa7.controller;

import com.ogsbreakout.etapa7.model.Jogo;
import com.ogsbreakout.etapa7.model.ListaCodigo;
import com.ogsbreakout.etapa7.model.Usuario;
import com.ogsbreakout.etapa7.model.UsuarioJogo;
import com.ogsbreakout.etapa7.service.JogoService;
import com.ogsbreakout.etapa7.service.UsuarioJogoService;
import com.ogsbreakout.etapa7.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
    @Autowired
    JogoService jogoService;

    @Autowired
    UsuarioService usuarioService;
    
    @Autowired
    UsuarioJogoService usuarioJogoService;

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
    
    //Esqueci senha
    @GetMapping("/EsqueciSenha")
    public String esqueciSenha(){
        return "EsqueciSenha";
    }
    
    @PostMapping("/BuscarEmail")
    public String buscarEmail(@RequestParam (name = "email") String email, Model model){
        List<Usuario> listaUsuarios = usuarioService.listarUsuario();
        
        for(Usuario usuario : listaUsuarios){
            if (usuario.getEmail().equals(email)){
                String codigoGerado = String.valueOf(gerarCodigo());

                ListaCodigo.novoCodigo(email, codigoGerado);
                System.out.println(codigoGerado);

                model.addAttribute("email", email);
                return "RecuperarConta";
            }
        }
        
        model.addAttribute("erroEmail", "Email não encontrado");
        return "EsqueciSenha";
    }
    
    @PostMapping("/VerificarCodigo")
    public String verificarCodigo (@RequestParam (name = "codigo") String codigo, @RequestParam (name = "email") String email, Model model, HttpSession session){
        if (ListaCodigo.verificarCodigo(email, codigo)){
            for (Usuario usuario : usuarioService.listarUsuario()){
                if (usuario.getEmail().equals(email)){
                    session.setAttribute("usuarioLogado", usuario);
                    return "NovaSenha";
                }
            }
            
            model.addAttribute("erroEmail", "Email não encontrado");
            return "EsqueciSenha";
        }
        
        model.addAttribute("email", email);
        model.addAttribute("erroCodigo", "Código incorreto");
        return "RecuperarConta";
    }
    
    //GerarCodigo
    public String gerarCodigo() {
        Random random = new Random();
        int codigo = 100000 + random.nextInt(900000);
        
        return String.valueOf(codigo);
    }
    
    //Recuperar conta
    @GetMapping("/RecuperarConta")
    public String recuperarConta(Model model, @RequestParam(name = "email") String email){
        model.addAttribute("email", email);
        model.addAttribute("erroEmail", "Email não enviado");
        return "RecuperarConta";
    }
    
    @PostMapping("/NovaSenha")
    public String novaSenha(HttpSession session, @RequestParam(name = "senha") String senha){
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) {
            return "redirect:/Login"; 
        }
        
        usuario.setSenha(senha);
        usuarioService.atualizarUsuario(usuario.getId(), usuario);
        
        return "redirect:/Catalogo";
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
            List<Jogo> jogos = jogoService.listarPorFiltro(filtro);
            List<UsuarioJogo> usuarioJogos = usuarioJogoService.listarUsuarioJogoContendo(filtro);

            model.addAttribute("listaUsuarioJogos", usuarioJogos);
            model.addAttribute("listarJogos", jogos);
            model.addAttribute("filtro", filtro);
        }
        
        return "Catalogo";
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
            
            for (Jogo jogo : jogos){
                System.out.println(jogo.getTitulo());
            }
            
            model.addAttribute("listarJogos", jogos);
            model.addAttribute("filtro", filtro);
            return "MeusJogos";
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
        //Atualizar
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
        //Admin
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
    @GetMapping("/AtualizarJogo")
    public String atualizarJogo(Model model, HttpSession session){
        //Atualizar
        System.out.println("here");
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) {
            return "redirect:/Login"; 
        }
        
        return "InformarIdJogo";
    }
    
    @GetMapping("/AtualizarJogo/{id}")
    public String atualizarJogo(Model model, HttpSession session, @PathVariable(value = "id", name = "id") Integer id){
        //Admin
        Usuario acesso = (Usuario) session.getAttribute("usuarioLogado");
        if (acesso == null) {
            return "redirect:/Login"; 
        }
        if (!acesso.getAcesso().equals("admin")){
            return "redirect:/";
        }
        
        Jogo jogo = jogoService.buscarJogoPorId(id);
        
        model.addAttribute("jogoAntigo", jogo);
        model.addAttribute("jogoNovo", jogo);
        return "AtualizarJogo";
    }
    
    //Administrador
    @GetMapping("/Administrador")
    public String administrador(@RequestParam(name = "filtro", required = false) String filtroUsuario, 
    @RequestParam(name = "filtro", required = false) String filtroJogo, Model model, HttpSession session, @RequestParam(name = "erroAcesso", required = false) boolean erroAcesso){

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
            usuarios = usuarioService.listarPorFiltro(filtroUsuario);
            model.addAttribute("filtroUsuario", filtroUsuario);
            System.out.println("filtro usuário");
        }
        
        //Filtro Jogos
        if (filtroJogo == null || filtroJogo.equals("")){
            jogos = jogoService.listarJogo();
        }
        else{
            jogos = jogoService.listarPorFiltro(filtroJogo);
            model.addAttribute("filtroJogo", filtroJogo);
        }
        
        if (erroAcesso){
            model.addAttribute("erroAcesso", "Você não pode mudar o acesso de um administrador");
        }
        
        model.addAttribute("listarJogos", jogos);
        model.addAttribute("listarUsuarios", usuarios);
        
        return "Administrador";
    }
    
    @GetMapping("/InformarIdJogo")
    public String informarIdJogo(HttpSession session, Model model){
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) {
            return "redirect:/Login"; 
        }
        
        if (usuario.getAcesso().equals("user")){
            model.addAttribute("erroAcesso", "Você não tem acesso a essa função");
            return "Conta";
        }
        
        return "InformarIdJogo";
    }
}