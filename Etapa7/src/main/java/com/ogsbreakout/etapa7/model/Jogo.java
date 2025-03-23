package com.ogsbreakout.etapa7.model;

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
@Table(name = "Jogo")
public class Jogo {    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NotNull(message = "Título não informado")
    private String titulo;
    
    @NotBlank(message = "Categoria não informada")
    private String categoria;
    
    @Min(0)
    @NotNull(message = "Valor do jogo não informado")
    private Integer valor;
    
    @NotNull(message = "Lançamento do jogo não informado")
    private LocalDateTime lancamento;
    
    @NotBlank(message = "Criador não informado")
    private String criador;
    
    @NotBlank(message = "Descrição não informada")
    private String descricao;
    
    @Min(0)
    @Max(20)
    @NotNull(message = "Classificação etária não informada")
    private Integer classificacao;
    
    @OneToMany(mappedBy = "jogo")
    private List<UsuarioJogo> usuarioJogos;
    
    //Get e set
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public Integer getValor() { return valor; }
    public void setValor(Integer valor) { this.valor = valor; }

    public LocalDateTime getLancamento() { return lancamento; }
    public void setLancamento(LocalDateTime lancamento) { this.lancamento = lancamento; }

    public String getCriador() { return criador; }
    public void setCriador(String criador) { this.criador = criador; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public Integer getClassificacao() { return classificacao; }
    public void setClassificacao(Integer classificacao) { this.classificacao = classificacao; }

    public List<UsuarioJogo> getUsuarioJogos() { return usuarioJogos; }
    public void setUsuarioJogos(List<UsuarioJogo> usuarioJogos) { this.usuarioJogos = usuarioJogos; }
}