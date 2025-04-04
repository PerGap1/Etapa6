package com.ogsbreakout.etapa7.controller;

import com.ogsbreakout.etapa7.model.Jogo;
import com.ogsbreakout.etapa7.model.Usuario;
import com.ogsbreakout.etapa7.repository.JogoRepository;
import com.ogsbreakout.etapa7.service.JogoService;
import com.ogsbreakout.etapa7.service.UsuarioJogoService;
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

@Controller
@RequestMapping("/Jogo")
public class JogoController {
    
    @Autowired
    JogoService jogoService;
    
    @Autowired
    private JogoRepository jogoRepository;
    
    @Autowired
    private UsuarioJogoService usuarioJogoService;

    @GetMapping("/")
    public String viewHomePage(Model model) {
        return "home";
    }
    
    @GetMapping("/Detalhes/{id}")
    public String detalhes(Model model, @PathVariable("id") int id, HttpSession session){
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) {
            return "redirect:/Login"; 
        }
        
        Jogo jogo = jogoService.buscarJogoPorId(id);
        
        model.addAttribute("jogo", jogoService.buscarJogoPorId(id));
        model.addAttribute("possui", usuarioJogoService.possuiJogo(usuario, jogo));
        return "VerJogo";
    }
    
    @PostMapping("/Comprar/{id}")
    public String comprarJogo(@PathVariable("id") int id, Model model, HttpSession session){
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) {
            return "redirect:/Login"; 
        }
        
        Jogo jogo = jogoService.buscarJogoPorId(id);
        model.addAttribute("jogo", jogoService.buscarJogoPorId(id));
        model.addAttribute("possui", usuarioJogoService.possuiJogo(usuario, jogo));
        
        boolean possuiJogo = usuarioJogoService.possuiJogo(usuario, jogo);
        if (possuiJogo) {
            model.addAttribute("erro", "Você já possui este jogo!");
            return "VerJogo";
        }
        
        usuarioJogoService.comprar(usuario, jogo);
        return "VerJogo";
    }

        
    @PostMapping("/CriarJogo")
    public String criarjogo(@Valid @ModelAttribute Jogo jogoNovo, Model model, BindingResult result, HttpSession session){
        if (!result.hasErrors()){
            if (!jogoRepository.existsByTitulo(jogoNovo.getTitulo())){
                jogoService.criarJogo(jogoNovo);

                Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
                usuarioJogoService.comprar(usuario, jogoNovo);
                
                model.addAttribute("sucessoOperacao", "Jogo criado!");
                return "/Conta";
            }
            else{
                model.addAttribute("erroTitulo", "Jogo já cadastrado");
            }
        }
        else{
            model.addAttribute("erroInvalidade", "Preencha corretamente os campos");
        }

        model.addAttribute("titulo", jogoNovo.getTitulo());
        model.addAttribute("categoria", jogoNovo.getCategoria());
        model.addAttribute("valor", jogoNovo.getValor());
        model.addAttribute("lancamento", jogoNovo.getLancamento());
        model.addAttribute("criador", jogoNovo.getCriador());
        model.addAttribute("descricao", jogoNovo.getDescricao());
        model.addAttribute("classificacao", jogoNovo.getClassificacao());

        return "CriadorJogos"; 
    }
    
    @PostMapping("/AtualizarJogo")
    public String atualizarJogo(@Valid @ModelAttribute Jogo jogoNovo, BindingResult result, Model model, HttpSession session){
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) {
            return "redirect:/Login"; 
        }
        
        if (result.hasErrors()){
            model.addAttribute("erroInvalidade", "Preencha corretamente os campos");
        }

        Jogo jogoAntigo = jogoService.buscarJogoPorId(jogoNovo.getId());

        if (jogoRepository.existsByTitulo(jogoNovo.getTitulo()) && !jogoNovo.getTitulo().equals(jogoAntigo.getTitulo())){
            model.addAttribute("erroTitulo", "Esse título está indisponível");
        }
        else{
            jogoService.atualizarJogo(jogoNovo.getId(), jogoNovo);

            model.addAttribute("sucessoOperacao", "Jogo atualizado!");
            return "Conta";
        }
        
        model.addAttribute(jogoNovo);

        return "AtualizarJogo";  
    }
    
    @GetMapping("/Deletar/{id}")
    public String deletarJogo(@PathVariable(value = "id") Integer id, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) {
            return "redirect:/Login"; 
        }
        if (!usuario.getAcesso().equals("admin")){
            return "redirect:/";
        }
        
        jogoService.deletarJogo(id);
        return "redirect:/Administrador"; 
    }
}
