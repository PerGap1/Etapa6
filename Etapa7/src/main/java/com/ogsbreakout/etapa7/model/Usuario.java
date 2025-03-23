package com.ogsbreakout.etapa7.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "Usuario") //ERRO EM POTENCIAL COM ID E ID_USUARIO    
public class Usuario {    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NotNull(message = "Nome de usuário não informado")
    @Column(unique = true)
    private String nome;
    
    @NotBlank(message = "Senha do usuário não informada")
    private String senha;
    
    @NotBlank(message = "Email do usuário não informado")
    @Column(unique = true)
    private String email;
    
    @NotBlank(message = "Apelido do usuário não informado")
    @Column(unique = true)
    private String apelido;
    
    @Min(12)
    @Max(99)
    @NotNull(message = "Você deve ter no mínimo 12 anos para entrar")
    private Integer idade;
    
    private LocalDateTime dataEntrada = LocalDateTime.now();
    
    private String acesso = "user";
    
    @OneToMany(mappedBy = "usuario")
    private List<UsuarioJogo> usuarioJogos;
    
    //Get e set
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getApelido() { return apelido; }
    public void setApelido(String apelido) { this.apelido = apelido; }

    public Integer getIdade() { return idade; }
    public void setIdade(Integer idade) { this.idade = idade; }

    public LocalDateTime getDataEntrada() { return dataEntrada; }
    public void setDataEntrada(LocalDateTime dataEntrada) { this.dataEntrada = dataEntrada; }
    
    public String getAcesso() { return acesso; }
    public void setAcesso(String acesso) { this.acesso = acesso; }
    
    public List<UsuarioJogo> getUsuarioJogos() {return usuarioJogos;}
    public void setUsuarioJogos(List<UsuarioJogo> usuarioJogos) {this.usuarioJogos = usuarioJogos;}
}

