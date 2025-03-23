package com.ogsbreakout.etapa7.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "UsuarioJogo")
public class UsuarioJogo {    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "jogo_id") 
    private Jogo jogo;


    @NotNull(message = "O campo possue não foi preenchido corretamente")
    private Boolean possui = false;
    
    //Get e set
    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}

    public Usuario getUsuario() {return usuario;}
    public void setUsuario(Usuario usuario) {this.usuario = usuario;}

    public Jogo getJogo() {return jogo;}
    public void setJogo(Jogo jogo) {this.jogo = jogo;}
    
    public Boolean getPossui() {return possui;}
    public void setPossui(Boolean possui) {this.possui = possui;}
}
