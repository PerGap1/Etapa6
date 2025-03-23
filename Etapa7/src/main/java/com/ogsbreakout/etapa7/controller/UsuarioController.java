package com.ogsbreakout.etapa7.controller;

import com.ogsbreakout.etapa7.model.Usuario;
import com.ogsbreakout.etapa7.repository.UsuarioRepository;
import com.ogsbreakout.etapa7.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/Usuario")
public class UsuarioController {
    
    @Autowired
    UsuarioService usuarioService;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/")
    public String viewHomePage() {
        return "home";
    }
    
    @PostMapping("/Entrar")
    public String entrarComUsuario(@RequestParam(name = "nome") String nome, @RequestParam(name = "senha") String senha, Model model, HttpSession session) {
        Usuario usuario = usuarioRepository.findByNomeAndSenha(nome, senha);
        if (usuario != null) {
            session.setAttribute("usuarioLogado", usuario);
            return "redirect:/Catalogo";
        } 
        else {
            model.addAttribute("erroAutenticacao", "Usuário ou senha incorretos");
            model.addAttribute("nome", nome);
            model.addAttribute("senha", senha);
            return "Login";
        }
    }
    
    @PostMapping("/CriarConta")
    public String criarConta(@Valid @ModelAttribute Usuario usuario, BindingResult result, Model model){
        
        if (result.hasErrors()) {
            if (usuario.getIdade() == null || usuario.getIdade() < 12){
                model.addAttribute("erroIdade", "Você precisa ter mais de 12 anos");
            }
            model.addAttribute("erroInvalidade", "Seus dados estão incorretos");
            model.addAttribute("usuario", usuario);
            return "NovaConta";
        }
        
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            model.addAttribute("erroEmail", "Esse email já está cadastrado");
        } else if (usuarioRepository.existsByNome(usuario.getNome())) {
            model.addAttribute("erroNome", "Nome de usuário indisponível");
        } else if (usuarioRepository.existsByApelido(usuario.getApelido())) {
            model.addAttribute("erroApelido", "Apelido indisponível");
        } else {
            usuarioRepository.save(usuario);
            
            return "redirect:/Catalogo";
        }
        
        model.addAttribute("usuario", usuario);
        return "NovaConta"; 
    }
    
    //Cuidar para diferença entre atualizar usuário e criar usuário

    @GetMapping("/Deletar/{id}")
    public String deletarUsuario(@PathVariable(value = "id") Integer id, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) {
            return "redirect:/Login"; 
        }
        if (!usuario.getAcesso().equals("admin")){
            return "redirect:/";
        }
        
        usuarioService.deletarUsuario(id);
        return "redirect:/Administrador"; 
    }
    
    @PostMapping("/AtualizarConta")
    public String atualizarConta(@Valid @ModelAttribute Usuario usuarioNovo, BindingResult result, Model model, HttpSession session){
        Usuario usuarioAntigo = (Usuario) session.getAttribute("usuarioLogado");
        if (usuarioAntigo == null) {
            return "redirect:/Login"; 
        }
        
        if (result.hasErrors()) {
            if (usuarioNovo.getIdade() == null || usuarioNovo.getIdade() < 12){
                model.addAttribute("erroIdade", "Você precisa ter mais de 12 anos");
            }
            model.addAttribute("erroInvalidade", "Seus dados estão incorretos");
            model.addAttribute("usuarioNovo", usuarioNovo);
            model.addAttribute("usuarioAntigo", usuarioAntigo);
            
            return "AtualizarConta";
        }

        if (usuarioRepository.existsByEmail(usuarioNovo.getEmail()) && !usuarioNovo.getEmail().equals(usuarioAntigo.getEmail())) {
            model.addAttribute("erroEmail", "Esse email já está cadastrado");
        } 
        else if (usuarioRepository.existsByNome(usuarioNovo.getNome()) && !usuarioNovo.getNome().equals(usuarioAntigo.getNome())) {
            model.addAttribute("erroNome", "Nome de usuário indisponível");
        } 
        else if (usuarioRepository.existsByApelido(usuarioNovo.getApelido()) && !usuarioNovo.getApelido().equals(usuarioAntigo.getApelido())) {
            model.addAttribute("erroApelido", "Apelido indisponível");
        } 
        else {
            usuarioService.atualizarUsuario(usuarioAntigo.getId(), usuarioNovo);
            
            return "Conta";
        }
        
        model.addAttribute("usuarioNovo", usuarioNovo);
        model.addAttribute("usuarioAntigo", usuarioAntigo);
        
        return "AtualizarConta"; 
    }
}
